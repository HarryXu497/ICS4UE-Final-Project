package ui.host;

import client.ClientConnection;
import server.HostServer;
import ui.Const;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class SubmissionsPanel extends JPanel {
    private final HostServer server;
    private final List<ClientConnection> clients;
    private final JList<String> submissions;

    public SubmissionsPanel(HostServer server) {
        this.server = server;
        this.server.onSubmit(this::updateData);

        this.clients = new ArrayList<>();

        this.submissions = new JList<>();
        this.submissions.addMouseListener(new ViewCodeListener());
        this.submissions.setFont(new Font(Const.MONOSPACE_FONT, Font.PLAIN, 16));

        JLabel header = new JLabel("Submissions");
        header.setFont(new Font(Const.DEFAULT_FONT, Font.BOLD, 24));

        this.add(header, BorderLayout.PAGE_START);
        this.add(this.submissions, BorderLayout.CENTER);
    }

    public void updateData(ClientConnection client) {
        Set<ClientConnection> connections = this.server.getConnections();

        this.clients.clear();
        this.clients.addAll(connections);
        this.clients.sort(Comparator.comparing(ClientConnection::getSubmissionTime));

        Vector<String> listData = new Vector<>();

        for (ClientConnection connection : connections) {
            if (connection.hasSubmitted()) {
                listData.add(connection.getName() + ".java");
            }
        }

        this.submissions.setListData(listData);
    }

    private class ViewCodeListener extends MouseAdapter {


        @Override
        public void mouseClicked(MouseEvent e) {
            int submissionIndex = submissions.getSelectedIndex();
            ClientConnection client = clients.get(submissionIndex);
            CodeViewFrame codeViewFrame = new CodeViewFrame(client);
            codeViewFrame.setVisible(true);
        }
    }
}
