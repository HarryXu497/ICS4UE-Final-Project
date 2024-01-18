package gui.components;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.HeadlessException;

/**
 * Custom frame to allow for dynamic switching between panels (screens).
 * without previous declaration
 * @author Harry Xu
 * @version 1.0 - January 17th 2024
 */
public class MultiScreenFrame extends JFrame {
    private JPanel currentScreen;

    /**
     * Constructs a {@link MultiScreenFrame}.
     * @param title the frame title
     * @param initialScreen the starting panel of the frame
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless()} returns true
     *                           i.e. if the environment does not support a keyboard, display, or mouse.
     */
    public MultiScreenFrame(String title, JPanel initialScreen) throws HeadlessException {
        super(title);

        this.currentScreen = initialScreen;
        this.add(this.currentScreen);
    }

    /**
     * switchScreen
     * Switches the current screen.
     * @param nextScreen the new screen
     */
    public void switchScreen(JPanel nextScreen) {
        this.remove(this.currentScreen);
        this.currentScreen = nextScreen;
        this.add(nextScreen, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

    /**
     * getCurrentScreen
     * Returns the current screen
     * @return the current screen
     */
    public JPanel getCurrentScreen() {
        return this.currentScreen;
    }
}
