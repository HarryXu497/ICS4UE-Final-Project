package gui.client;

import client.ClientConnection;
import function.Procedure;
import server.ServerCode;
import gui.Const;
import gui.components.MultiScreenFrame;

import javax.swing.JFrame;
import java.awt.event.WindowEvent;
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
    private final MultiScreenFrame frame;

    private ClientConnection client;
    private InputStreamReader input;
    private OutputStreamWriter output;

    /**
     * Constructs a {@link ClientConnection}
     */
    public ClientApplication() {
        // Frame initialization
        this.frame = new MultiScreenFrame("Client", new JoinPanel(this::connectSocket));
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(Const.FRAME_WIDTH, Const.FRAME_HEIGHT);
        this.frame.getContentPane().setBackground(Const.PRIMARY_COLOR);
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
        this.frame.switchScreen(new WaitingPanel());

        // Waits for the host to start the game
        NextScreenThread waitingThread = new NextScreenThread(this::endWaiting);
        waitingThread.start();
    }

    /**
     * endWaiting
     * Transitions to the code submission screen.
     */
    private void endWaiting() {
        WaitingPanel currentPanel = (WaitingPanel) this.frame.getCurrentScreen();
        currentPanel.stopTimer();

        this.frame.switchScreen(new CodePanel(this.client, this::sendProgram));

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
                System.out.println("Error in underlying TCP protocol.");
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
				System.out.println("An error occured while reading from the client");
            }
        }
    }
}