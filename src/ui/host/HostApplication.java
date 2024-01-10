package ui.host;

import server.HostServer;
import server.ServerState;
import ui.Const;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

public class HostApplication {
    private final HostServer server;

    private final JFrame frame;
    private final HostPanel hostPanel;
    private SubmissionsPanel submissionsPanel;

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

        this.submissionsPanel = new SubmissionsPanel(this.server);
        this.frame.remove(this.hostPanel);
        this.frame.add(this.submissionsPanel, BorderLayout.CENTER);
        this.frame.revalidate();
        this.frame.repaint();
    }

    public void close() throws IOException {
        this.server.close();
    }
}
