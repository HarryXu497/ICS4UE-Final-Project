package gui.client;

import gui.components.CustomLabel;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel for waiting until the host starts the game.
 * @author Harry Xu
 * @version 1.0 - December 25th 2023
 */
class WaitingPanel extends JPanel implements ActionListener {
    private static final int MAX_PERIODS = 3;

    private int numPeriods;
    private final CustomLabel text;
    private final Timer textAnimationTimer;

    /**
     * Constructs a {@link WaitingPanel}.
     */
    public WaitingPanel() {
        this.setLayout(new GridBagLayout());

        this.numPeriods = 0;

        this.text = new CustomLabel("Waiting to start", "", 32);
        this.add(this.text, new GridBagConstraints());

        this.textAnimationTimer = new Timer(500, this);
        this.textAnimationTimer.start();
    }

    /**
     * stopTimer
     * Stops the animation timer.
     */
    public void stopTimer() {
        this.textAnimationTimer.stop();
    }

    /**
     * actionPerformed
     * Runs an animation update.
     * Animates the amount of ellipses in the label text.
     * @param e the {@link ActionEvent} object
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        StringBuilder dots = new StringBuilder();

        for (int i = 0; i < this.numPeriods; i++) {
            dots.append('.');
        }

        this.text.setContent(dots.toString());
        this.numPeriods = (this.numPeriods + 1) % (MAX_PERIODS + 1);
    }
}
