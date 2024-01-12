package game;

public class Shop {
    private static final int CURRENCY_PER_POWER = 3;
    private static final int CURRENCY_PER_HEALTH = 2;

    private final Player player;

    public Shop(Player player) {
        this.player = player;
    }

    public void buyPower() {
        if (this.player.getCurrency() < CURRENCY_PER_POWER) {
            return;
        }

        this.player.setCurrency(this.player.getCurrency() - CURRENCY_PER_POWER);
        this.player.setPower(this.player.getPower() + 1);
    }

    public void buyHealth() {
        if (this.player.getHealth() < CURRENCY_PER_HEALTH) {
            return;
        }

        this.player.setCurrency(this.player.getCurrency() - CURRENCY_PER_HEALTH);
        this.player.setHealth(this.player.getHealth() + 1);
    }

    public void sellPower() {
        if (this.player.getPower() <= 1) {
            return;
        }

        this.player.setCurrency(this.player.getCurrency() + CURRENCY_PER_HEALTH);
        this.player.setPower(this.player.getPower() - 1);
    }

    public void sellHealth() {
        if (this.player.getHealth() <= 1) {
            return;
        }

        this.player.setCurrency(this.player.getCurrency() + CURRENCY_PER_HEALTH);
        this.player.setHealth(this.player.getHealth() - 1);
    }
}
