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

/**
 * Client GUI application for connecting to the host
 * @author Harry Xu
 * @version 1.0 - December 23rd 2023
 */
public class ClientApplication {
    private final JFrame frame;
    private final JoinPanel joinPanel;
    private WaitingPanel waitingPanel;
    private ClientConnection client;
    private InputStreamReader input;
    private OutputStreamWriter output;

    static {
        // Load fonts
        try {
            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            graphicsEnvironment.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/OpenSans.ttf")));
            graphicsEnvironment.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/JetBrainsMono.ttf")));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructs a {@link ClientConnection}
     */
    public ClientApplication() {
        // Frame initialization
        this.frame = new JFrame("Client");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(Const.FRAME_WIDTH, Const.FRAME_HEIGHT);
        this.frame.getContentPane().setBackground(Const.PRIMARY_COLOR);

        this.joinPanel = new JoinPanel(this::connectSocket);
        this.frame.add(this.joinPanel, BorderLayout.CENTER);
    }

    /**
     * run
     * Runs the application
     */
    public void run() {
        this.frame.setVisible(true);
    }

    /**
     * connectSocket
     * Attempts to connect to the {@link server.HostServer} socket.
     * @param ip the host ip address
     * @param port the host server port
     * @param name the name of the client
     * @throws IOException if an I/O error occurs while attempting to connect to socket
     */
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

        // Waits for the host to start the game
        NextScreenThread waitingThread = new NextScreenThread(this::endWaiting);
        waitingThread.start();
    }

    /**
     * endWaiting
     * Transitions to the code submission screen.
     */
    private void endWaiting() {
        this.waitingPanel.stopTimer();

        CodePanel codePanel = new CodePanel(this.client, this::sendProgram);
        this.frame.remove(this.waitingPanel);
        this.frame.add(codePanel, BorderLayout.CENTER);
        this.frame.revalidate();
        this.frame.repaint();

        // Start thread to waiting for close message
        NextScreenThread closeScreenThread = new NextScreenThread(this::closeFrame);
        closeScreenThread.start();
    }

    /**
     * sendProgram
     * Write the client's program to the host socket.
     * @param program the program source code
     * @throws IOException if an I/O error occurs when writing to the host socket
     */
    private void sendProgram(String program) throws IOException {
        this.output.write(program);
        this.output.write(ServerCode.SUBMISSION_FINISHED.ordinal());
        this.output.flush();
    }

    /**
     * closeFrame
     * Closes the client application and GUI.
     */
    private void closeFrame() {
        this.frame.dispatchEvent(new WindowEvent(this.frame, WindowEvent.WINDOW_CLOSING));

        try {
            this.close();
        } catch (IOException e) {
            System.out.println("Failed to close client socket.");
        }
    }

    /**
     * close
     * Closes the socket and its input streams.
     * @throws IOException if an I/O error occurs while attempting to close the socket
     */
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

    /**
     * Thread that waits for the {@link ServerCode#NEXT_SCREEN} message
     * from the server and calls the callback function when received.
     * @author Harry Xu
     * @version 1.0 - December 23rd 2023
     */
    private class NextScreenThread extends Thread {
        private final Procedure onNextScreen;

        /**
         * Constructs a {@link NextScreenThread} with a callback
         * to call when the {@link ServerCode#NEXT_SCREEN} message is received
         * @param onNextScreen the function which is called when the
         *                      {@link ServerCode#NEXT_SCREEN} message is received
         */
        public NextScreenThread(Procedure onNextScreen) {
            this.onNextScreen = onNextScreen;
        }

        /**
         * run
         * Runs the thread.
         */
        @Override
        public void run() {
            try {
                client.getSocket().setSoTimeout(0);
            } catch (SocketException e) {
                e.printStackTrace();
            }

            try {
                boolean nextScreenReceived = false;

                while (!nextScreenReceived) {
                    int c = input.read();

                    if (c == ServerCode.NEXT_SCREEN.ordinal()) {
                        this.onNextScreen.execute();
                        nextScreenReceived = true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}