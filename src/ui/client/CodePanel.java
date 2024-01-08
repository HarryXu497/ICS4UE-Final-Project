package ui.client;

import ui.components.CustomButton;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class CodePanel extends JPanel {

    private final Consumer<String> onSubmit;
    private final JTextArea codeField;

    public CodePanel(Consumer<String> onSubmit) {
        this.onSubmit = onSubmit;

        this.codeField = new JTextArea(50, 50);
        CustomButton submitButton = new CustomButton("Submit");

        submitButton.addMouseListener(new SubmitListener());

        this.add(this.codeField);
        this.add(submitButton);
    }

    private class SubmitListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            onSubmit.accept(codeField.getText());
        }
    }


}
