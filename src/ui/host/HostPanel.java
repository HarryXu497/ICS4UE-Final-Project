package ui.host;

import function.Procedure;
import client.ClientConnection;
import server.HostServer;
import ui.components.CustomButton;
import ui.components.CustomLabel;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HostPanel extends JPanel {
    private final Procedure onStart;
    private final HostServer host;
    private final CustomLabel connectionsLabel;
    private final CustomButton startButton;

    public HostPanel(HostServer host, Procedure onStart) {
        this.onStart = onStart;
        this.host = host;

        // Layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Components
        CustomLabel ipLabel = new CustomLabel("IP: ", this.host.getInetAddress().getHostAddress());
        CustomLabel portLabel = new CustomLabel("Port: ", Integer.toString(this.host.getPort()));
        this.connectionsLabel = new CustomLabel("Connections: ", Integer.toString(0));

        this.startButton = new CustomButton("Start");
        this.startButton.addMouseListener(new OnStartListener());

        // Alignment
        ipLabel.setAlignmentX(0.5F);
        portLabel.setAlignmentX(0.5F);
        this.connectionsLabel.setAlignmentX(0.5F);
        this.startButton.setAlignmentX(0.5F);

        this.add(Box.createVerticalGlue());
        this.add(ipLabel);
        this.add(portLabel);
        this.add(this.connectionsLabel);
        this.add(this.startButton);
        this.add(Box.createRigidArea(new Dimension(0, 25)));
        this.add(Box.createVerticalGlue());

        this.host.onConnect(this::updateConnections);
        this.host.onDisconnect(this::updateConnections);
    }

    private void updateConnections(ClientConnection client) {
        this.connectionsLabel.setContent(Integer.toString(this.host.getNumConnections()));
    }

    private class OnStartListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            onStart.execute();
        }
    }
}
