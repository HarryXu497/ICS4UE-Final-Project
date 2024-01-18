package gui.components;

import gui.Const;

import javax.swing.JButton;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Custom button class for consistent styling.
 * @author Harry Xu
 * @version 1.0 - December 25th 2023
 */
public class CustomButton extends JButton {
    private static final int DEFAULT_FONT_SIZE = 16;

    /**
     * Constructs a {@link CustomButton} with a text label
     * @param text the button text
     */
    public CustomButton(String text) {
        this(text, Font.PLAIN, DEFAULT_FONT_SIZE);
    }

    /**
     * Constructs a {@link CustomButton} with a text label
     * @param text the button text
     * @param style the style constant for the {@code Font}
     * The style argument is an integer bitmask that may
     * be {@code PLAIN}, or a bitwise union of {@code BOLD} and/or
     * {@code ITALIC} (for example, {@code ITALIC} or {@code BOLD|ITALIC}).
     * If the style argument does not conform to one of the expected
     * integer bitmasks then the style is set to {@code PLAIN}.
     * @param size the point size of the {@code Font}
     */
    public CustomButton(String text, int style, int size) {
        super(text);

        this.setFont(new Font(Const.DEFAULT_FONT, style, size));
        this.setBackground(Const.SECONDARY_COLOR);
        this.setForeground(Const.PRIMARY_COLOR);
        this.addMouseListener(new ButtonHoverListener());
    }

    /**
     * Listener to change cursor on button hover.
     * @author Harry Xu
     * @version 1.0 - December 25th 2023
     */
    private class ButtonHoverListener extends MouseAdapter {
        /**
         * mouseEntered
         * Invoked when the mouse enters the button.
         * @param e the {@link MouseEvent} object
         */
        @Override
        public void mouseEntered(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        /**
         * mouseExited
         * Invoked when the mouse exits the button.
         * @param e the {@link MouseEvent} object
         */
        @Override
        public void mouseExited(MouseEvent e) {
            setCursor(Cursor.getDefaultCursor());
        }
    }
}
