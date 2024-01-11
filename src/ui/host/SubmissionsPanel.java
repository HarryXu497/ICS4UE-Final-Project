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
        this.submissions.addMouseListener(new ViewCodeListener());
        this.submissions.setFont(new Font(Const.MONOSPACE_FONT, Font.PLAIN, 16));
//        this.submissions.setMinimumSize(new Dimension(Const.FRAME_WIDTH - 200, Const.FRAME_HEIGHT - 100));
        this.submissions.setVisibleRowCount(20);
        JScrollPane scrollPane = new JScrollPane(this.submissions);

        JButton startGameButton = new CustomButton("Start Game");
        startGameButton.addActionListener(new GameStartListener());

        this.add(header, BorderLayout.PAGE_START);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(startGameButton, BorderLayout.PAGE_END);
    }

    public void updateData(ClientConnection client) {
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

    private class ViewCodeListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int submissionIndex = submissions.getSelectedIndex();
            ClientConnection client = clients.get(submissionIndex);
            CodeViewFrame codeViewFrame = new CodeViewFrame(client);
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
