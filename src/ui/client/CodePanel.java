package ui.client;

import client.ClientConnection;
import ui.Const;
import ui.components.CustomButton;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class CodePanel extends JPanel {
    private final ClientConnection client;
    private final Consumer<String> onSubmit;
    private final JTextArea codeField;

    public CodePanel(ClientConnection client, Consumer<String> onSubmit) {
        this.client = client;
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
        this.codeField.setFont(new Font(Const.MONOSPACE_FONT, Font.PLAIN, 16));
        this.codeField.setLineWrap(true);
        this.codeField.setTabSize(2);
        this.codeField.setWrapStyleWord(true);
        this.codeField.setColumns(20);
        this.codeField.setText(generateStarterCode(this.client.getName()));

        JScrollPane scrollPane = new JScrollPane(this.codeField);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        this.add(scrollPane, constraints);

        constraints.gridx = 1;
        constraints.weightx = 2;
        constraints.weighty = 1;
        this.add(submitButton, constraints);
    }

    private static String generateStarterCode(String name) {
        return "import temp.*;\n" +
                "\n" +
                "public class " + name + "Player extends Player {\n" +
                "\tpublic void move(Data data) {\n" +
                "\t\t\\\\ Your code here ...\n" +
                "\t}\n" +
                "}\n";
    }

    private class SubmitListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            onSubmit.accept(codeField.getText());
        }
    }
}
