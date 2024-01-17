package ui.components;

import ui.Const;

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
    /**
     * Constructs a {@link CustomButton} with a text label
     * @param text the button text
     */
    public CustomButton(String text) {
        super(text);

        this.setFont(new Font(Const.DEFAULT_FONT, Font.PLAIN, 16));
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
