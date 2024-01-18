package gui.components;

import gui.Const;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.Font;

/**
 * Custom label class with a leading and content label for consistent styling.
 * @author Harry Xu
 * @version 1.0 - December 26th 2023
 */
public class CustomLabel extends JPanel {
    private static final int DEFAULT_FONT_SIZE = 16;

    private final JLabel leadingLabel;
    private final JLabel contentLabel;

    /**
     * Constructs a {@link CustomLabel} with one label in bold
     * and a font size.
     * @param leading the label text
     * @param fontSize the font size of the label
     */
    public CustomLabel(String leading, int fontSize) {
        this(leading, "", fontSize);
    }

    /**
     * Constructs a {@link CustomLabel} with a leading label
     * in bold and a content label in plain style.
     * @param leading the leading label text
     * @param content the content label text
     */
    public CustomLabel(String leading, String content) {
        this(leading, content, DEFAULT_FONT_SIZE);
    }

    /**
     * Constructs a {@link CustomLabel} with a leading label
     * in bold, a content label in plain style, and a font size for both.
     * @param leading the leading label text
     * @param content the content label text
     * @param fontSize the size of the labels
     */
    public CustomLabel(String leading, String content, int fontSize) {
        this.setLayout(new FlowLayout());

        this.leadingLabel = new JLabel(leading);
        this.contentLabel = new JLabel(content);

        this.leadingLabel.setFont(new Font(Const.DEFAULT_FONT, Font.BOLD, fontSize));
        this.contentLabel.setFont(new Font(Const.DEFAULT_FONT, Font.PLAIN, fontSize));

        this.add(this.leadingLabel);
        this.add(this.contentLabel);

        this.setMaximumSize(this.getPreferredSize());
    }

    /**
     * setLeading
     * Sets the text of the leading label.
     * @param leading the new leading text
     */
    public void setLeading(String leading) {
        this.leadingLabel.setText(leading);
    }

    /**
     * setContent
     * Sets the text of the content label.
     * @param content the new content text
     */
    public void setContent(String content) {
        this.contentLabel.setText(content);
    }
}