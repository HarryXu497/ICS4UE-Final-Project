package gui.components;

import gui.Const;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

/**
 * A vertical group of horizontal {@link JLabel} and {@link JTextField} pairs.
 * @author Harry Xu
 * @version 1.0 - December 26th 2023
 */
public class FormControls extends JPanel {
    private static final int DEFAULT_ROWS = 20;
    private static final int PADDING = 3;

    private final Map<String, JTextField> inputComponents;

    /**
     * Constructs a {@link FormControls} with text field labels.
     * @param labels the form field labels
     */
    public FormControls(String[] labels) {
        this(labels, DEFAULT_ROWS);
    }

    /**
     * Constructs a {@link FormControls} with text field labels
     * and the width of the text fields.
     * @param labels the form field labels
     * @param inputRows the width of the form controls
     */
    public FormControls(String[] labels, int inputRows) {
        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        this.inputComponents = new HashMap<>();

        // Create label and input pairs
        for (int i = 0; i < labels.length; i++) {
            String label = labels[i];

            // Create Components
            JTextField inputComponent = new JTextField(inputRows);
            inputComponent.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            Dimension preferredSize = inputComponent.getPreferredSize();
            inputComponent.setMinimumSize(new Dimension(preferredSize.width, preferredSize.height));

            JLabel controlLabel = new JLabel(label);

            // Add components
            constraints.gridx = 0;
            constraints.gridy = i;
            constraints.weightx = 1;
            constraints.insets = new Insets(PADDING, 0, PADDING, PADDING * 4);
            controlLabel.setMinimumSize(new Dimension(controlLabel.getWidth(), controlLabel.getHeight()));
            controlLabel.setFont(new Font(Const.DEFAULT_FONT, Font.PLAIN, 16));
            this.add(controlLabel, constraints);

            constraints.gridx = 1;
            constraints.gridy = i;
            constraints.weightx = 4;
            constraints.insets.right = 0;
            inputComponent.setFont(new Font(Const.DEFAULT_FONT, Font.PLAIN, 16));
            this.add(inputComponent, constraints);

            // Add component to map
            this.inputComponents.put(label, inputComponent);
        }

        this.setMaximumSize(new Dimension(this.getPreferredSize().width, this.getPreferredSize().height));
    }

    /**
     * getInputComponent
     * Gets the underlying {@link JTextField} associated
     * with the specific label.
     * @param label the label associated with the component
     * @return the {@link JTextField} associated with the label,
     *          or {@code null} if the label is not found
     */
    public JTextField getInputComponent(String label) {
        return this.inputComponents.get(label);
    }
}
