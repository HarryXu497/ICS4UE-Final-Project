package gui;

import gui.client.ClientApplication;
import gui.components.CustomButton;
import gui.host.HostApplication;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

/**
 * Application for joining or hosting a game
 * @author Harry Xu
 * @version 1.0 - January 17th 2024
 */
public class Application {
    private static final int APPLICATION_WIDTH = 280;
    private static final int APPLICATION_HEIGHT = 180;

    private final JFrame frame;

    static {
        // Load fonts
        try {
            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            graphicsEnvironment.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/OpenSans.ttf")));
            graphicsEnvironment.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/JetBrainsMono.ttf")));
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructs an {@link Application}.
     */
    public Application() {
        // Frame
        this.frame = new JFrame("Application");

        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
        this.frame.getContentPane().setBackground(Const.PRIMARY_COLOR);
        this.frame.setResizable(true);

        // Components
        JPanel startPanel = new JPanel();
        startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.Y_AXIS));

        CustomButton clientButton = new CustomButton("Join a Game", Font.BOLD, 24);
        CustomButton hostButton = new CustomButton("Host a Game", Font.BOLD, 24);

        clientButton.setFocusable(false);
        hostButton.setFocusable(false);

        clientButton.addActionListener(new OnClientClick());
        hostButton.addActionListener(new OnHostClick());

        clientButton.setMaximumSize(hostButton.getMaximumSize());

        clientButton.setAlignmentX(0.5F);
        hostButton.setAlignmentX(0.5F);

        // Adding components
        startPanel.add(Box.createVerticalGlue());
        startPanel.add(clientButton);
        startPanel.add(Box.createVerticalStrut(10));
        startPanel.add(hostButton);
        startPanel.add(Box.createVerticalGlue());

        this.frame.add(startPanel);
    }

    /**
     * run
     * Runs the host GUI and server application
     */
    public void run() {
        this.frame.setVisible(true);
    }

    /**
     * Runs the {@link ClientApplication} when the corresponding button is clicked.
     * @author Harry Xu
     * @version 1.0 - January 17th 2024
     */
    private class OnClientClick implements ActionListener {
        /**
         * Invoked when an action occurs.
         * @param e the {@link ActionEvent} object containing event information
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            ClientApplication clientApplication = new ClientApplication();
            clientApplication.run();

            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }

    /**
     * Runs the {@link HostApplication} when the corresponding button is clicked.
     * @author Harry Xu
     * @version 1.0 - January 17th 2024
     */
    private class OnHostClick implements ActionListener {
        /**
         * Invoked when an action occurs.
         * @param e the {@link ActionEvent} object containing event information
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            HostApplication host;

            try {
                host = new HostApplication();
            } catch (IOException ex) {
                System.out.println("Unable to open host server.");
                return;
            }

            host.run();

            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }
}
