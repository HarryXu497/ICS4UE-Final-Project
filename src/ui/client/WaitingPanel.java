package ui.client;

import ui.components.CustomLabel;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WaitingPanel extends JPanel implements ActionListener {
    private static final int MAX_PERIODS = 3;

    private int numPeriods = 0;
    private final CustomLabel text;
    private final Timer textAnimationTimer;

    public WaitingPanel() {
        this.setLayout(new GridBagLayout());

        this.text = new CustomLabel("Waiting to start", "", 32);

        this.add(this.text, new GridBagConstraints());

        this.textAnimationTimer = new Timer(500, this);
        this.textAnimationTimer.start();
    }

    public void stopTimer() {
        this.textAnimationTimer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        StringBuilder dots = new StringBuilder();

        for (int i = 0; i < this.numPeriods; i++) {
            dots.append('.');
        }

        this.text.setContent(dots.toString());
        numPeriods = (numPeriods + 1) % (MAX_PERIODS + 1);
    }
}
