package ui.host;

import client.ClientConnection;
import function.Procedure;
import server.HostServer;
import ui.Const;
import ui.components.CustomButton;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.function.Consumer;

public class SubmissionsPanel extends JPanel {
    private final HostServer server;
    private final Procedure onGameStart;

    private final List<ClientConnection> clients;
    private final JList<String> submissions;

    public SubmissionsPanel(HostServer server, Procedure onGameStart) {
        this.server = server;
        this.onGameStart = onGameStart;

        this.server.onSubmit(this::updateData);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.clients = new ArrayList<>();

        JLabel header = new JLabel("Submissions");
        header.setFont(new Font(Const.DEFAULT_FONT, Font.BOLD, 24));
        header.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        header.setAlignmentX(0.5f);

        this.submissions = new JList<>();
        this.submissions.getSelectionModel().addListSelectionListener(new ViewCodeListener(this::updateData));
        this.submissions.setFont(new Font(Const.MONOSPACE_FONT, Font.PLAIN, 16));
        this.submissions.setVisibleRowCount(20);
        this.submissions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(this.submissions);

        JButton startGameButton = new CustomButton("Start Game");
        startGameButton.addActionListener(new GameStartListener());

        this.add(header, BorderLayout.PAGE_START);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(startGameButton, BorderLayout.PAGE_END);
    }

    public void updateData() {
        Set<ClientConnection> connections = this.server.getConnections();

        this.clients.clear();

        Vector<String> listData = new Vector<>();

        for (ClientConnection connection : connections) {
            if (connection.hasSubmitted()) {
                this.clients.add(connection);
            }
        }

        this.clients.sort(Comparator.comparing(ClientConnection::getSubmissionTime));

        for (ClientConnection connection : this.clients) {
            listData.add(connection.getName() + ".java");
        }

        this.submissions.setListData(listData);
    }

    private class ViewCodeListener implements ListSelectionListener {

        private final Procedure onDelete;

        public ViewCodeListener(Procedure onDelete) {
            this.onDelete = onDelete;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int submissionIndex = e.getFirstIndex();
            ClientConnection client = clients.get(submissionIndex);
            CodeViewFrame codeViewFrame = new CodeViewFrame(client, this.onDelete);
            codeViewFrame.setVisible(true);
        }
    }

    private class GameStartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            onGameStart.execute();
        }
    }
}
