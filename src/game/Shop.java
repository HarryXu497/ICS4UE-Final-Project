package game;

public class Shop {
    public static final int POWER_PER_CURRENCY = 3;
    public static final int HEALTH_PER_CURRENCY = 2;
    public static final int RANGE_PER_CURRENCY = 3;

    public static final int CURRENCY_PER_POWER = 2;
    public static final int CURRENCY_PER_HEALTH = 1;
    public static final int CURRENCY_PER_RANGE = 2;

    private final Player player;

    Shop(Player player) {
        this.player = player;
    }

    public void buyPower() {
        if (this.player.getCurrency() < POWER_PER_CURRENCY) {
            return;
        }

        this.player.setCurrency(this.player.getCurrency() - POWER_PER_CURRENCY);
        this.player.setPower(this.player.getPower() + 1);
    }

    public void buyHealth() {
        if ((this.player.getCurrency() < HEALTH_PER_CURRENCY)|| (this.player.getHealth() >= Player.MAX_HEALTH)) {
            return;
        }

        this.player.setCurrency(this.player.getCurrency() - HEALTH_PER_CURRENCY);
        this.player.setHealth(this.player.getHealth() + 1);
    }

    public void buyRange() {
        if (this.player.getCurrency() < RANGE_PER_CURRENCY) {
            return;
        }

        this.player.setCurrency(this.player.getCurrency() - RANGE_PER_CURRENCY);
        this.player.setRange(this.player.getRange() + 1);
    }

    public void sellPower() {
        if (this.player.getPower() <= Player.MIN_POWER) {
            return;
        }

        this.player.setCurrency(this.player.getCurrency() + CURRENCY_PER_POWER);
        this.player.setPower(this.player.getPower() - 1);
    }

    public void sellHealth() {
        if (this.player.getHealth() <= Player.MIN_HEALTH) {
            return;
        }

        this.player.setCurrency(this.player.getCurrency() + CURRENCY_PER_HEALTH);
        this.player.setHealth(this.player.getHealth() - 1);
    }

    public void sellRange() {
        if (this.player.getRange() <= Player.MIN_RANGE) {
            return;
        }

        this.player.setCurrency(this.player.getCurrency() + CURRENCY_PER_RANGE);
        this.player.setRange(this.player.getRange() - 1);
    }
}
