package ui.host;

import javax.swing.JList;
import javax.swing.JPanel;

public class SubmissionsPanel extends JPanel {
    public SubmissionsPanel() {
        this.add(new JList<>(new String[] {"Hello", "World"}));
    }
}
