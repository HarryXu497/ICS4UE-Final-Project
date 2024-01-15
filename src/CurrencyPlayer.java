import game.*;
import game.actions.*;

public class CurrencyPlayer extends Player {
    public void cycle(Data data, Shop shop) {
        Point currencyLocation = data.getCurrencyLocation(1);

        if (currencyLocation != null) {
            System.out.println(currencyLocation);

            QueryData queryData = data.query(currencyLocation);

            System.out.println(queryData.getEnemies());
            System.out.println(queryData.getCurrency());

            Point currentPosition = data.getPosition();

            if (currentPosition.getY() < currencyLocation.getY()) {
                this.setDirection(Direction.DOWN);
            }
            else if (currentPosition.getY() > currencyLocation.getY()) {
                this.setDirection(Direction.UP);
            }
            else if (currentPosition.getX() < currencyLocation.getX()) {
                this.setDirection(Direction.RIGHT);
            }
            else if (currentPosition.getX() > currencyLocation.getX()) {
                this.setDirection(Direction.LEFT);
            }
        }

        shop.buyHealth();

        this.move();
    }
}
