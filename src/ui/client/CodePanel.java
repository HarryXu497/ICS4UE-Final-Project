package ui.client;

import ui.Const;
import ui.components.CustomButton;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class CodePanel extends JPanel {

    private final Consumer<String> onSubmit;
    private final JTextArea codeField;

    public CodePanel(Consumer<String> onSubmit) {
        this.onSubmit = onSubmit;

        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        this.codeField = new JTextArea(50, 50);
        CustomButton submitButton = new CustomButton("Submit");

        submitButton.addMouseListener(new SubmitListener());

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 8;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        Font codeFieldFont = new Font(Const.MONOSPACE_FONT, Font.PLAIN, 16);
        this.codeField.setFont(codeFieldFont);
        this.codeField.setLineWrap(true);
        this.codeField.setTabSize(2);
        this.codeField.setWrapStyleWord(true);
        this.codeField.setColumns(20);

        JScrollPane scrollPane = new JScrollPane(this.codeField);
//        scrollPane.add(this.codeField);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        this.add(scrollPane, constraints);

        constraints.gridx = 1;
        constraints.weightx = 2;
        constraints.weighty = 1;
        this.add(submitButton, constraints);
    }

    private class SubmitListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            onSubmit.accept(codeField.getText());
        }
    }


}
