import game.*;
import game.actions.*;

public class EnemyPlayer extends Player {
    public void cycle(Data data, Shop shop) {
        Point enemyLocation = data.getEnemyLocation(1);

        QueryData queryData = data.query(enemyLocation);

        Point currentPosition = data.getPosition();

        if (currentPosition.getY() < enemyLocation.getY()) {
            this.setDirection(Direction.DOWN);
        } else if (currentPosition.getY() > enemyLocation.getY()) {
            this.setDirection(Direction.UP);
        } else if (currentPosition.getX() < enemyLocation.getX()) {
            this.setDirection(Direction.RIGHT);
        } else if (currentPosition.getX() > enemyLocation.getX()) {
            this.setDirection(Direction.LEFT);
        }

        this.move();
    }
}
