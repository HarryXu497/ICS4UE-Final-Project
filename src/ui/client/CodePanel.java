package ui.client;

import client.ClientConnection;
import function.CodeSubmissionConsumer;
import ui.Const;
import ui.components.CustomButton;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * Client panel for code submissions.
 * @author Harry Xu
 * @version 1.0 - December 28th 2023
 */
public class CodePanel extends JPanel {
    private final CodeSubmissionConsumer onSubmit;
    private final JTextArea codeField;

    /**
     * Constructs a {@link CodePanel}.
     * @param client the client submitting the code
     * @param onSubmit the callback to call when the code is submitted
     */
    CodePanel(ClientConnection client, CodeSubmissionConsumer onSubmit) {
        this.onSubmit = onSubmit;

        // Swing layout
        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        // Code field component
        this.codeField = new JTextArea(50, 50);
        this.codeField.setFont(new Font(Const.MONOSPACE_FONT, Font.PLAIN, 16));
        this.codeField.setLineWrap(true);
        this.codeField.setTabSize(2);
        this.codeField.setWrapStyleWord(true);
        this.codeField.setColumns(20);
        this.codeField.setText(generateStarterCode(client.getName()));

        // Wrapping scroll panel
        JScrollPane scrollPane = new JScrollPane(this.codeField);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Submit button
        CustomButton submitButton = new CustomButton("Submit");
        submitButton.addMouseListener(new SubmitListener());

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 8;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        this.add(scrollPane, constraints);

        constraints.gridx = 1;
        constraints.weightx = 2;
        constraints.weighty = 1;
        this.add(submitButton, constraints);
    }

    /**
     * generateStarterCode
     * Generates a code template for the client.
     * @param name the client name
     */
    private static String generateStarterCode(String name) {
        return  "import game.*;\n" +
                "import game.actions.*;\n" +
                "\n" +
                "public class " + name + "Player extends Player {\n" +
                "\tpublic void cycle(Data data, Shop shop) {\n" +
                "\t\t// Your code here ...\n" +
                "\t}\n" +
                "}\n";
    }

    /**
     * Listens for submit button clicks.
     * @author Harry Xu
     * @version 1.0 - December 28th 2023
     */
    private class SubmitListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                onSubmit.submit(codeField.getText());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Failed to submit code.");
            }
        }
    }
}
