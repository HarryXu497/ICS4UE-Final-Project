package ui.client;

import client.ClientConnection;
import server.ServerCode;
import ui.Const;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientApplication {
    private final JFrame frame;
    private final JoinPanel joinPanel;
    private WaitingPanel waitingPanel;
    private CodePanel codePanel;

    private ClientConnection client;
    private InputStreamReader input;
    private OutputStreamWriter output;

    static {
        try {
            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            graphicsEnvironment.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/OpenSans.ttf")));
            graphicsEnvironment.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/JetBrainsMono.ttf")));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public ClientApplication() {
        this.frame = new JFrame("Client");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(Const.FRAME_WIDTH, Const.FRAME_HEIGHT);
        this.frame.getContentPane().setBackground(Const.PRIMARY_COLOR);
        this.joinPanel = new JoinPanel(this::connectSocket);
        this.frame.add(this.joinPanel, BorderLayout.CENTER);
    }

    public void run() {
        this.frame.setVisible(true);
    }

    private void connectSocket(String ip, int port, String name) throws IOException {
        try {
            this.client = new ClientConnection(new Socket(ip, port));
        } catch (IllegalArgumentException e) {
            // Custom error message
            throw new IllegalArgumentException("Port " + port + " out of range");
        }

        // Access I/O streams
        Socket clientSocket = this.client.getSocket();;
        clientSocket.setSoTimeout(2000);
        this.input = new InputStreamReader(clientSocket.getInputStream());
        this.output = new OutputStreamWriter(clientSocket.getOutputStream());

        // Attempt to write name to host
        this.output.write(name);
        this.output.write('\n');
        this.output.flush();

        int c = this.input.read();

        if (c == ServerCode.DISCONNECT.ordinal()) {
            this.close();
            throw new IllegalArgumentException("Name '" + name + "' taken");
        }

        this.client.setName(name);

        // Valid name -> next panel
        this.waitingPanel = new WaitingPanel();
        this.frame.remove(this.joinPanel);
        this.frame.add(this.waitingPanel, BorderLayout.CENTER);
        this.frame.revalidate();
        this.frame.repaint();

        WaitingThread waitingThread = new WaitingThread();
        waitingThread.start();
    }

    private void endWaiting() {
        this.codePanel = new CodePanel(this.client, this::sendProgram);
        this.waitingPanel.stopTimer();
        this.frame.remove(this.waitingPanel);
        this.frame.add(this.codePanel, BorderLayout.CENTER);
        this.frame.revalidate();
        this.frame.repaint();
    }

    private void sendProgram(String program) {
        try {
            output.write(program);
            output.write(ServerCode.SUBMISSION_FINISHED.ordinal());
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() throws IOException {
        if (this.client == null) {
            return;
        }

        if (this.input != null) {
            this.input.close();
        }

        if (this.output != null) {
            this.output.close();
        }

        synchronized (this.client) {
            this.client.close();
        }
    }

    private class WaitingThread extends Thread {
        @Override
        public void run() {
            try {
                int c = input.read();

                while (c != -1) {
                    c = input.read();

                    if (c == ServerCode.NEXT_SCREEN.ordinal()) {
                        endWaiting();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}