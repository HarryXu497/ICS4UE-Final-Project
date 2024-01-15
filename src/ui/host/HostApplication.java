package ui.host;

import client.ClientConnection;
import game.GamePanel;
import server.HostServer;
import server.ServerState;
import ui.Const;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class HostApplication {
    private final HostServer server;

    private final JFrame frame;
    private final HostPanel hostPanel;
    private SubmissionsPanel submissionsPanel;
    private GamePanel gamePanel;

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

    public HostApplication() throws IOException {
        this.server = new HostServer();

        this.frame = new JFrame("Host");
        this.hostPanel = new HostPanel(this.server, this::switchScreen);

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(Const.FRAME_WIDTH, Const.FRAME_HEIGHT);
        this.frame.getContentPane().setBackground(Const.PRIMARY_COLOR);
        this.frame.setResizable(true);
        this.frame.add(this.hostPanel, BorderLayout.CENTER);
    }

    public void run() {
        this.frame.setVisible(true);
        this.server.serve();
    }

    public void switchScreen() {
        this.server.setState(ServerState.CORRESPONDING);

        this.submissionsPanel = new SubmissionsPanel(this.server, this::startGame);
        this.frame.remove(this.hostPanel);
        this.frame.add(this.submissionsPanel, BorderLayout.CENTER);
        this.frame.revalidate();
        this.frame.repaint();
    }

    public void startGame() {
        this.server.setState(ServerState.CLOSED);

        Set<ClientConnection> submittedClients = this.server.getConnections()
                .stream()
                .filter(ClientConnection::hasSubmitted)
                .collect(Collectors.toSet());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Close server
        try {
            this.close();
        } catch (IOException e) {
            System.out.println("An error occurred while trying to close the server ");
        }

        this.gamePanel = new GamePanel(submittedClients, screenSize, this::onGameWin);
        this.frame.remove(this.submissionsPanel);
        this.frame.setSize(screenSize);
        this.frame.add(this.gamePanel, BorderLayout.CENTER);
        this.frame.revalidate();
        this.frame.repaint();

        this.gamePanel.start();
    }

    public void onGameWin(String winner) {
        this.frame.remove(this.gamePanel);
        this.frame.setSize(new Dimension(Const.FRAME_WIDTH, Const.FRAME_HEIGHT));
        this.frame.add(new WinPanel(winner), BorderLayout.CENTER);
        this.frame.revalidate();
        this.frame.repaint();
    }

    public void close() throws IOException {
        this.server.close();
    }
}
