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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Client panel for joining a hosted game.
 * @author Harry Xu
 * @version 1.0 - December 24th 2023
 */
public class JoinPanel extends JPanel {
    private final ConnectConsumer onJoin;
    private final JTextField ipField;
    private final JTextField portField;
    private final JTextField nameField;
    private final JLabel errorMessage;
    private final CustomButton submitButton;

    /**
     * Constructs a {@link JoinPanel} with a connection callback.
     * @param onJoin the callback function to call when the client
     *               attempts to connect to the host
     */
    JoinPanel(ConnectConsumer onJoin) {
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
        this.submitButton.addActionListener(new SubmitListener());

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

    /**
     * setErrorMessage
     * Sets the text of the {@link JoinPanel#errorMessage} label.
     * @param message the message to set the label text to
     */
    private void setErrorMessage(String message) {
        this.errorMessage.setText(message);
        this.errorMessage.setVisible(message.length() != 0);
    }

    /**
     * Listens to submit button events.
     * @author Harry Xu
     * @version 1.0 - December 24th 2023
     */
    private class SubmitListener implements ActionListener {
        /**
         * actionPerformed
         * Called when the submit button is pressed.
         * @param e the {@link ActionEvent} object.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!submitButton.isEnabled()) {
                return;
            }

            // Get data from fields
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

            if (!isValidName(name)) {
                setErrorMessage("Invalid Name");
                submitButton.setEnabled(true);
                return;
            }

            // Attempt to connect to server on background thread
            ConnectThread connectThread = new ConnectThread(ip, port, name);
            connectThread.start();
        }
    }

    /**
     * isValidName
     * Validates a client's name.
     * @param name the name to validate
     * @return if the name is valid
     */
    private static boolean isValidName(String name) {
        if (name.length() == 0) {
            return false;
        }

        if (!Character.isUpperCase(name.charAt(0))) {
            return false;
        }

        for (int i = 1; i < name.length(); i++) {
            char currentChar = name.charAt(i);

            if (!isValidCharacter(currentChar)) {
                return false;
            }
        }

        return true;
    }

    /**
     * isValidCharacter
     * Validates a single character in the client's name.
     * @param c the char to validate
     * @return if the char is valid
     */
    private static boolean isValidCharacter(char c) {
        return (Character.isAlphabetic(c)) ||
                (Character.isDigit(c)) ||
                (c == '_') ||
                (c == '$');
    }

    /**
     * Attempts to connect the client with a {@link server.HostServer}.
     * @author Harry Xu
     * @version 1.0 - December 24th 2023
     */
    private class ConnectThread extends Thread {
        private final String ip;
        private final int port;
        private final String name;

        /**
         * Constructs a {@link ConnectThread}.
         * @param ip the ip of the host
         * @param port the port of the host socket
         * @param name the name of the client
         */
        public ConnectThread(String ip, int port, String name) {
            this.ip = ip;
            this.port = port;
            this.name = name;
        }

        /**
         * run
         * Runs the thread.
         */
        @Override
        public void run() {
            submitButton.setEnabled(true);

            try {
                onJoin.connect(this.ip, this.port, this.name);
            } catch (UnknownHostException e) {
                setErrorMessage("Unknown host IP address");
            } catch (ConnectException e) {
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