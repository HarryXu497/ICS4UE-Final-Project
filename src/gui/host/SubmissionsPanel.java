package gui.host;

import client.ClientConnection;
import function.Procedure;
import server.HostServer;
import gui.Const;
import gui.components.CustomButton;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * Host panel displaying list of user program submissions
 * @author Harry Xu
 * @version 1.0 - December 30th 2023
 */
class SubmissionsPanel extends JPanel {
    private final HostServer host;

    private final List<ClientConnection> clients;
    private final JList<String> submissions;

    /**
     * Constructs a {@link SubmissionsPanel}
     * @param host the host server
     * @param onGameStart a callback function to call when the host starts the game
     */
    public SubmissionsPanel(HostServer host, Procedure onGameStart) {
        this.host = host;
        this.clients = new ArrayList<>();

        // Layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Components
        JLabel header = new JLabel("Submissions");
        header.setFont(new Font(Const.DEFAULT_FONT, Font.BOLD, 24));
        header.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.submissions = new JList<>();
        this.submissions.getSelectionModel().addListSelectionListener(new ViewCodeListener(this::syncData));
        this.submissions.setFont(new Font(Const.MONOSPACE_FONT, Font.PLAIN, 16));
        this.submissions.setVisibleRowCount(20);
        this.submissions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(this.submissions);

        JButton startGameButton = new CustomButton("Start Game");
        startGameButton.addActionListener((e) -> onGameStart.execute());

        // Add components
        this.add(centerComponent(header));
        this.add(scrollPane);
        this.add(centerComponent(startGameButton));

        // Attaching listeners
        this.host.onSubmit(this::syncData);
    }

    /**
     * syncData
     * Synchronizes the table display with the server
     */
    public void syncData() {
        Set<ClientConnection> connections = this.host.getConnections();

        // Filter and sort submitted programs
        this.clients.clear();

        for (ClientConnection connection : connections) {
            if (connection.hasSubmitted()) {
                this.clients.add(connection);
            }
        }

        this.clients.sort(Comparator.comparing(ClientConnection::getSubmissionTime));

        // Update display
        Vector<String> listData = new Vector<>();

        for (ClientConnection connection : this.clients) {
            listData.add(connection.getName() + ".java");
        }

        this.submissions.setListData(listData);
    }

    /**
     * centerComponent
     * Centers the component horizontally in a vertical box layout
     * by wrapping it with a {@link JPanel} with {@link BoxLayout},
     * and inserting {@link Box#createVerticalGlue()} on both sides.
     * @param component the component to wrap
     * @return a {@link JPanel} containing the centered {@code component}
     */
    private static JPanel centerComponent(JComponent component) {
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(Box.createHorizontalGlue());
        panel.add(component);
        panel.add(Box.createHorizontalGlue());

        return panel;
    }

    /**
     * Opens {@link CodeViewFrame} when a list item is selected.
     * @author Harry Xu
     * @version 1.0 - December 30th 2023
     */
    private class ViewCodeListener implements ListSelectionListener {
        private final Procedure onDelete;

        /**
         * Constructs a {@link ViewCodeListener}.
         * @param onDelete a callback which is called when the host attempts to delete a submission
         */
        public ViewCodeListener(Procedure onDelete) {
            this.onDelete = onDelete;
        }

        /**
         * Called whenever the value of the selection changes.
         * @param e the {@link ListSelectionEvent} which contains event information
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) {
                return;
            }

            // Open 'CodeViewFrame'
            int submissionIndex = e.getFirstIndex();

            if ((submissionIndex < 0) || (submissionIndex >= clients.size())) {
                return;
            }

            ClientConnection client = clients.get(submissionIndex);
            CodeViewFrame codeViewFrame = new CodeViewFrame(client, this.onDelete);
            codeViewFrame.setVisible(true);
        }
    }
}
