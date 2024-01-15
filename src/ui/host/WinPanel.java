package ui.host;

import ui.components.CustomLabel;

import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class WinPanel extends JPanel {
    public WinPanel(String winner) {
        this.setLayout(new GridBagLayout());

        this.add(new CustomLabel(winner + " Wins!", "", 32), new GridBagConstraints());
    }
}
