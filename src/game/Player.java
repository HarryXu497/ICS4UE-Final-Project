package game;

import game.actions.Direction;
import game.actions.Move;
import game.internal.assets.Assets;
import game.internal.assets.PlayerAssets;
import game.internal.entities.GameObject;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;

/**
 * Represents a player in the game.
 * This class is to be subclassed and the {@link Player#cycle(Data, Shop)} method be implemented.
 * This class is designed such that a user can implement a custom subclass
 * with access to certain methods and information, which is intended to be used to create
 * artificial intelligence for the player.
 * @author Tommy Shan and Harry Xu
 * @version 1.0 - January 8th 2023
 */
public abstract class Player extends GameObject {
    private static final int DEFAULT_HEALTH = 10;
    private static final int DEFAULT_POWER = 1;
    private static final int DEFAULT_CURRENCY = 0;
    private static final int DEFAULT_RANGE = 1;

    public static final int MIN_HEALTH = 0;
    public static final int MIN_POWER = 1;
    public static final int MIN_CURRENCY = 0;
    public static final int MIN_RANGE = 1;

    /** Sprites */
    private int currentSprite;
    private Image[] sprites;

    /** Attributes */
    private String name;
    private int health;
    private int power;
    private int currency;
    private int range;

    /** Movement */
    private Move move;
    private Direction direction;
    private Direction lastXDirection;
    private boolean moved;
    private boolean damaged;

    /**
     * Constructs an instance with default attributes.
     */
    public Player() {
        this.health = DEFAULT_HEALTH;
        this.power = DEFAULT_POWER;
        this.currency = DEFAULT_CURRENCY;
        this.range = DEFAULT_RANGE;

        this.move = null;
        this.direction = Direction.UP;
        this.lastXDirection = Direction.RIGHT;
        this.damaged = false;

        // Sprites
        this.currentSprite = 0;

        this.sprites = Assets.getInstance()
                .getPlayer()
                .getIdleSprites();
    }

    // Methods accessible to game
    /**
     * update
     * Updates the player state by calling the user implemented
     * {@link Player#cycle(Data, Shop)} method.
     * @param data game information provided to the player
     * @param shop allows the player to purchase upgrades in exchange for currency
     */
    void update(Data data, Shop shop) {
        this.move = null;
        this.cycle(data, shop);

        PlayerAssets sprites = Assets.getInstance().getPlayer();

        if (this.damaged) {
            this.sprites = sprites.getHurtSprites();
        } else if (this.move != null) {
            this.sprites = sprites.getMovingSprites();
        } else {
            this.sprites = sprites.getIdleSprites();
        }

        this.damaged = false;
    }

    /**
     * tick
     * Advances the sprite image
     */
    @Override
    public void tick() {
        this.currentSprite = (this.currentSprite + 1) % this.sprites.length;
    }

    /**
     * fight
     * Fights 2 players by inflicting damage to both of them
     * equal to their power.
     * @param enemy the other player
     */
    void fight(Player enemy) {
        this.setHealth(this.getHealth() - enemy.getPower());
        enemy.setHealth(enemy.getHealth() - this.getPower());
    }

    /**
     * draw
     * Draws the player at the specified coordinate
     * @param g the {@link Graphics} object
     * @param x the x coordinate of the top left corner of the drawing location
     * @param y the y coordinate of the top left corner of the drawing location
     */
    @Override
    public void draw(Graphics g, int x, int y) {
        Image sprite = this.sprites[this.currentSprite];
        Image heart = Assets.getInstance().getGui().getHeart();

        if (this.lastXDirection == Direction.RIGHT) {
            g.drawImage(sprite, x, y, null);
        } else if (this.lastXDirection == Direction.LEFT) {
            int width = sprite.getWidth(null);
            int height = sprite.getHeight(null);

            g.drawImage(sprite, x + width, y, -width, height, null);
        }

        // Hearts
        int heartsY = y + sprite.getHeight(null);
        int heartsWidth = (this.getHealth() * heart.getWidth(null)) + (this.getHealth() - 1);
        int centerX = x + (sprite.getWidth(null) / 2);
        int heartsX = centerX - (heartsWidth / 2);

        for (int i = 0; i < this.getHealth(); i++) {
            int offset = 0;

            if (i != 0) {
                offset = 1;
            }

            g.drawImage(heart, heartsX + (i * heart.getWidth(null)) + offset, heartsY, null);
        }

        // Name
        Font font = Assets.getInstance().getPlayer().getFont();

        FontMetrics fontMetrics = g.getFontMetrics(font);
        int width = fontMetrics.stringWidth(this.name);
        int nameX = centerX - (width / 2);

        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(this.name, nameX, y);
    }

    /**
     * setName
     * Sets the name of the player if it is null.
     * @param name the player name
     */
    public void setName(String name) {
        if (this.name != null) {
            return;
        }

        this.name = name;
    }

    /**
     * setHealth
     * Sets the health of the player.
     * @param newHealth the new health
     */
    void setHealth(int newHealth) {
        if (newHealth < this.health) {
            this.damaged = true;
        }

        this.health = Math.max(MIN_HEALTH, newHealth);
    }

    /**
     * setPower
     * Sets the power of the player.
     * @param newPower the new health
     */
    void setPower(int newPower) {
        this.power = Math.max(MIN_POWER, newPower);
    }

    /**
     * setCurrency
     * Sets the currency of the player.
     * @param newCurrency the new health
     */
    void setCurrency(int newCurrency) {
        this.currency = Math.max(MIN_CURRENCY, newCurrency);
    }

    /**
     * getRange
     * Sets the range of the player.
     * @param newRange the new health
     */
    void setRange(int newRange) {
        this.range = Math.max(MIN_RANGE, newRange);
    }

    /**
     * setDirection
     * Sets the direction of the player.
     * @param newDirection the new direction
     */
    public void setDirection(Direction newDirection) {
        this.direction = newDirection;
    }

    // Methods accessible to user
    /**
     * move
     * Adds a {@link Move} action onto the action stack
     * in the current direction of the player.
     */
    public void move() {
        if ((this.direction == Direction.LEFT) || (this.direction == Direction.RIGHT)) {
            this.lastXDirection = this.direction;
        }

        this.move = Move.fromDirection(this.direction);
    }

    /**
     * getName
     * Gets the player's name.
     * @return the name of the player
     */
    public String getName() {
        return this.name;
    }

    /**
     * getHealth
     * Gets the player's health.
     * @return the health of the player
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * getCurrency
     * Gets the amount of currency.
     * @return the currency
     */
    public int getCurrency() {
        return this.currency;
    }

    /**
     * getPower
     * Gets the player's power.
     * @return the power of the player
     */
    public int getPower() {
        return this.power;
    }

    /**
     * getRange
     * Gets the player's range.
     * @return the range of the player
     */
    public int getRange() {
        return this.range;
    }

    /**
     * getDirection
     * Gets the player's direction.
     * @return the direction of the player
     */
    public Direction getDirection() {
        return this.direction;
    }

    /**
     * getMove
     * Gets the player's move.
     * @return the move of the player
     */
    public Move getMove() {
        return this.move;
    }

    // Abstract methods
    /**
     * cycle
     * Updates the player state
     * @param data game information provided to the player
     * @param shop allows the player to purchase upgrades in exchange for currency
     */
    public abstract void cycle(Data data, Shop shop);
}
