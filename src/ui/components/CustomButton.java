package ui.components;

import ui.Const;

import javax.swing.JButton;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomButton extends JButton {
    public CustomButton(String text) {
        super(text);

        this.setFont(new Font(Const.DEFAULT_FONT, Font.PLAIN, 16));
        this.setBackground(Const.SECONDARY_COLOR);
        this.setForeground(Const.PRIMARY_COLOR);
        this.addMouseListener(new ButtonHoverListener());
    }

    private class ButtonHoverListener extends MouseAdapter {
        @Override
        public void mouseEntered(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setCursor(Cursor.getDefaultCursor());
        }
    }
}
