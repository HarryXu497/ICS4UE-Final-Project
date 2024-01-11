package ui.host;

import client.ClientConnection;
import ui.Const;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

public class CodeViewFrame extends JFrame {
    public CodeViewFrame(ClientConnection client) {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(new Dimension(Const.FRAME_WIDTH, Const.FRAME_HEIGHT));

        JTextArea codeView = new JTextArea();
        JLabel nameView = new JLabel();

        codeView.setText(client.getCode());
        codeView.setFont(new Font(Const.MONOSPACE_FONT, Font.PLAIN, 16));
        codeView.setLineWrap(true);
        codeView.setTabSize(2);
        codeView.setWrapStyleWord(true);
        codeView.setColumns(20);
        codeView.setEditable(false);

        nameView.setFont(new Font(Const.DEFAULT_FONT, Font.BOLD, 24));
        nameView.setText(client.getName());
        nameView.setHorizontalAlignment(SwingConstants.CENTER);
        nameView.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane scrollPane = new JScrollPane(codeView);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        this.add(scrollPane, BorderLayout.CENTER);
        this.add(nameView, BorderLayout.PAGE_START);
    }
}
