package gui.host;

import function.Procedure;
import server.HostServer;
import gui.components.CustomButton;
import gui.components.CustomLabel;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Host panel displaying information for joining the game
 * @author Harry Xu
 * @version 1.0 - December 24th 2023
 */
class HostPanel extends JPanel {
    private final Procedure onStart;
    private final HostServer host;
    private final CustomLabel connectionsLabel;

    /**
     * Constructs a {@link HostPanel}.
     * @param host the host server
     * @param onStart a callback function to call when the host opens submissions
     */
    public HostPanel(HostServer host, Procedure onStart) {
        this.onStart = onStart;
        this.host = host;

        // Layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Components
        CustomLabel ipLabel = new CustomLabel("IP: ", this.host.getInetAddress().getHostAddress());
        CustomLabel portLabel = new CustomLabel("Port: ", Integer.toString(this.host.getPort()));
        this.connectionsLabel = new CustomLabel("Connections: ", Integer.toString(0));

        CustomButton startButton = new CustomButton("Start");
        startButton.addActionListener(new OnStartListener());

        // Alignment
        ipLabel.setAlignmentX(0.5F);
        portLabel.setAlignmentX(0.5F);
        this.connectionsLabel.setAlignmentX(0.5F);
        startButton.setAlignmentX(0.5F);

        // Adding to panel
        this.add(Box.createVerticalGlue());
        this.add(ipLabel);
        this.add(portLabel);
        this.add(this.connectionsLabel);
        this.add(startButton);
        this.add(Box.createRigidArea(new Dimension(0, 25)));
        this.add(Box.createVerticalGlue());

        // Attaching server listeners
        this.host.onConnect(this::updateConnections);
        this.host.onDisconnect(this::updateConnections);
    }

    /**
     * updateConnections
     * Updates the text of the {@link HostPanel#connectionsLabel}
     * to match the number of host server connections.
     */
    private void updateConnections() {
        this.connectionsLabel.setContent(Integer.toString(this.host.getNumConnections()));
    }

    /**
     * Executes the {@link HostPanel#onStart} callback when the
     * start button is pressed.
     * @author Harry Xu
     * @version 1.0 - December 24th 2023
     */
    private class OnStartListener implements ActionListener {
        /**
         * actionPerformed
         * Invoked when the button is pressed.
         * @param e the {@link ActionEvent} object
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            onStart.execute();
        }
    }
}
