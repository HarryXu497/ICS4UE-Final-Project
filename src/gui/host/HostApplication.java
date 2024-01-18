package gui.host;

import client.ClientConnection;
import game.GamePanel;
import server.HostServer;
import server.ServerCode;
import server.ServerState;
import gui.Const;
import gui.components.MultiScreenFrame;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Host GUI application for hosting a game
 * @author Harry Xu
 * @version 1.0 - December 22rd 2023
 */
public class HostApplication {
    private final HostServer server;

    private final MultiScreenFrame frame;

    /**
     * Constructs a {@link HostApplication}.
     * @throws IOException if an I/O error occurs while trying to open the host server
     */
    public HostApplication() throws IOException {
        this.server = new HostServer();

        this.frame = new MultiScreenFrame("Host", new HostPanel(this.server, this::switchScreen));

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(Const.FRAME_WIDTH, Const.FRAME_HEIGHT);
        this.frame.getContentPane().setBackground(Const.PRIMARY_COLOR);
        this.frame.setResizable(true);new HostPanel(this.server, this::switchScreen);
    }

    /**
     * run
     * Runs the host GUI and server application
     */
    public void run() {
        this.frame.setVisible(true);
        this.server.serve();
    }

    /**
     * switchScreen
     * Switches the displayed panel to the {@link SubmissionsPanel}
     * (i.e. opens program submissions).
     */
    public void switchScreen() {
        this.server.setState(ServerState.CORRESPONDING);

        this.frame.switchScreen(new SubmissionsPanel(this.server, this::startGame));
    }

    /**
     * startGame
     * Switches the displayed panel to the {@link GamePanel}
     * (i.e. starts the game simulation).
     */
    public void startGame() {
        this.server.setState(ServerState.CLOSED);

        // Get submitted programs
        Set<ClientConnection> submittedClients = this.server.getConnections()
                .stream()
                .filter(ClientConnection::hasSubmitted)
                .collect(Collectors.toSet());

        // Close server
        this.server.broadcast(ServerCode.NEXT_SCREEN);

        try {
            this.close();
        } catch (IOException e) {
            System.out.println("An error occurred while trying to close the server ");
        }

        // Switch to 'GamePanel'
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        GamePanel gamePanel = new GamePanel(submittedClients, screenSize, this::onGameWin);
        this.frame.setSize(screenSize);
        this.frame.switchScreen(gamePanel);

        gamePanel.start();
    }

    /**
     * onGameWin
     * Switches the displayed panel to the {@link WinPanel},
     * which displays the rankings.
     * @param standings the ordered list of standings
     */
    public void onGameWin(List<String> standings) {
        this.frame.setSize(new Dimension(Const.FRAME_WIDTH, Const.FRAME_HEIGHT));
        this.frame.switchScreen(new WinPanel(standings));
    }

    /**
     * close
     * Closes the host server.
     * @throws IOException if an I/O error occurs when closing the socket
     */
    public void close() throws IOException {
        this.server.close();
    }
}
