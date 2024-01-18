package gui.host;

import gui.components.CustomLabel;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.util.List;

/**
 * Host panel displaying ranked standings for the game.
 * @author Harry Xu
 * @version 1.0 - January 12th 2024
 */
class WinPanel extends JPanel {
    /**
     * Constructs a {@link WinPanel}.
     * @param standings an ordered list of usernames
     *                  representing the game standings
     */
    public WinPanel(List<String> standings) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Adding components
        this.add(Box.createVerticalGlue());

        if (standings.size() == 0) {
            this.add(new CustomLabel("Nobody Wins :(", 32));
        }

        // Display top 5 standings
        for (int i = 0; i < Math.min(standings.size(), 5); i++) {
            int fontSize = 24;

            if (i == 0) {
                fontSize = 32;
            }

            this.add(new CustomLabel("#" + (i + 1) + ": ", standings.get(standings.size() - i - 1), fontSize));
            this.add(Box.createVerticalStrut(5));
        }

        this.add(Box.createVerticalGlue());
    }
}
