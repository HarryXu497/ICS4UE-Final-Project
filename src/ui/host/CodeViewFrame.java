package ui.host;

import client.ClientConnection;
import function.Procedure;
import ui.Const;
import ui.components.CustomButton;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

public class CodeViewFrame extends JFrame {
    private final ClientConnection client;
    private final Consumer<ClientConnection> onDelete;

    public CodeViewFrame(ClientConnection client, Consumer<ClientConnection> onDelete) {
        this.client = client;
        this.onDelete = onDelete;

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(new Dimension(Const.FRAME_WIDTH, Const.FRAME_HEIGHT));

        JLabel nameView = new JLabel();
        JTextArea codeView = new JTextArea();
        CustomButton deleteSubmission = new CustomButton("Delete Submission");
        deleteSubmission.addActionListener(new DeleteSubmissionHandler());

        nameView.setFont(new Font(Const.DEFAULT_FONT, Font.BOLD, 24));
        nameView.setText(this.client.getName());
        nameView.setHorizontalAlignment(SwingConstants.CENTER);
        nameView.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        codeView.setText(this.client.getCode());
        codeView.setFont(new Font(Const.MONOSPACE_FONT, Font.PLAIN, 16));
        codeView.setLineWrap(true);
        codeView.setTabSize(2);
        codeView.setWrapStyleWord(true);
        codeView.setColumns(20);
        codeView.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(codeView);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        this.add(scrollPane, BorderLayout.CENTER);
        this.add(nameView, BorderLayout.PAGE_START);
        this.add(deleteSubmission, BorderLayout.PAGE_END);
    }

    private class DeleteSubmissionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            client.deleteCode();

            onDelete.accept(client);

            dispatchEvent(new WindowEvent(CodeViewFrame.this, WindowEvent.WINDOW_CLOSING));
        }
    }
}
