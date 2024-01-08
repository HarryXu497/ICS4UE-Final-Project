package ui.client;

import function.ConnectConsumer;
import ui.Const;
import ui.components.CustomButton;
import ui.components.FormControls;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class JoinPanel extends JPanel {

    private final ConnectConsumer onJoin;
    private final JTextField ipField;
    private final JTextField portField;
    private final JTextField nameField;
    private final JLabel errorMessage;
    private final CustomButton submitButton;

    public JoinPanel(ConnectConsumer onJoin) {
        this.onJoin = onJoin;

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        // Creating Components
        this.errorMessage = new JLabel();
        this.errorMessage.setForeground(Color.RED);
        this.errorMessage.setVisible(false);
        this.errorMessage.setFont(new Font(Const.DEFAULT_FONT, Font.PLAIN, 12));

        JLabel label = new JLabel("Join Game: ");
        label.setFont(new Font(Const.DEFAULT_FONT, Font.BOLD, 24));

        String[] labels = { "IP: ", "Port: ", "Name: " };
        FormControls formControls = new FormControls(labels);

        this.ipField = formControls.getInputComponent(labels[0]);
        this.portField = formControls.getInputComponent(labels[1]);
        this.nameField = formControls.getInputComponent(labels[2]);

        // Submit Button
        this.submitButton = new CustomButton("Join");
        this.submitButton.addMouseListener(new SubmitListener());


        // Alignment
        label.setAlignmentX(0.5F);
        this.errorMessage.setAlignmentX(0.5F);
        this.submitButton.setAlignmentX(0.5F);

        // Adding to panel
        this.add(Box.createVerticalGlue());
        this.add(label);
        this.add(Box.createRigidArea(new Dimension(0, 5)));
        this.add(formControls);
        this.add(Box.createRigidArea(new Dimension(0, 5)));
        this.add(this.errorMessage);
        this.add(Box.createRigidArea(new Dimension(0, 5)));
        this.add(this.submitButton);
        this.add(Box.createRigidArea(new Dimension(0, 25)));
        this.add(Box.createVerticalGlue());
    }

    private void setErrorMessage(String message) {
        errorMessage.setText(message);
        errorMessage.setVisible(message.length() != 0);
    }

    private class SubmitListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (!submitButton.isEnabled()) {
                return;
            }

            submitButton.setEnabled(false);
            String ip = ipField.getText();
            int port;
            String name = nameField.getText().trim();

            try {
                port = Integer.parseInt(portField.getText());
            } catch (NumberFormatException ex) {
                setErrorMessage("Invalid Port");
                submitButton.setEnabled(true);
                return;
            }

            if (name.length() == 0) {
                setErrorMessage("Invalid Name");
                submitButton.setEnabled(true);
                return;
            }

            ConnectThread connectThread = new ConnectThread(ip, port, name);
            connectThread.start();
        }
    }

    private class ConnectThread extends Thread {
        private final String ip;
        private final int port;
        private final String name;

        public ConnectThread(String ip, int port, String name) {
            this.ip = ip;
            this.port = port;
            this.name = name;
        }

        @Override
        public void run() {
            submitButton.setEnabled(true);

            try {
                onJoin.connect(this.ip, this.port, this.name);
            } catch (UnknownHostException e) {
                setErrorMessage("Unknown host IP address");
            } catch (ConnectException e) {
                e.printStackTrace();
                setErrorMessage("Connection refused");
            } catch (SocketException e) {
              setErrorMessage("Network is unreachable");
            } catch (IllegalArgumentException e) {
                setErrorMessage(e.getMessage());
            } catch (IOException e) {
                setErrorMessage("Something went wrong");
            }
        }
    }
}