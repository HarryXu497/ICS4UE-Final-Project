package ui.client;

import client.ClientConnection;
import function.Procedure;
import server.ServerCode;
import ui.Const;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

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

        NextScreenThread waitingThread = new NextScreenThread(this::endWaiting);
        waitingThread.start();
    }

    private void endWaiting() {
        this.codePanel = new CodePanel(this.client, this::sendProgram);
        this.waitingPanel.stopTimer();
        this.frame.remove(this.waitingPanel);
        this.frame.add(this.codePanel, BorderLayout.CENTER);
        this.frame.revalidate();
        this.frame.repaint();

        // Start thread to waiting for close message
        NextScreenThread closeScreenThread = new NextScreenThread(this::closeFrame);
        closeScreenThread.start();
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

    private void closeFrame() {
        this.frame.dispatchEvent(new WindowEvent(this.frame, WindowEvent.WINDOW_CLOSING));

        try {
            this.close();
        } catch (IOException e) {
            System.out.println("Failed to close client socket.");
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

    private class NextScreenThread extends Thread {

        private final Procedure onNextScreen;

        public NextScreenThread(Procedure onNextScreen) {
            this.onNextScreen = onNextScreen;
        }

        @Override
        public void run() {
            try {
                client.getSocket().setSoTimeout(0);
            } catch (SocketException e) {
                e.printStackTrace();
            }

            try {
                while (true) {
                    int c = input.read();

                    if (c == ServerCode.NEXT_SCREEN.ordinal()) {
                        this.onNextScreen.execute();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}