package game;

import javax.swing.JPanel;
import java.util.Set;

public class GamePanel extends JPanel {

    private final GameObject[][] map;

    public GamePanel(Set<Player> players) {
        this.map = new GameObject[30][30];
    }

}
