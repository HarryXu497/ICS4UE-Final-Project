package game.internal.entities;

import game.internal.assets.Assets;

import java.awt.Graphics;
import java.awt.Image;

/**
 * Represents a currency that can be collected by players.
 * @author Tommy Shan
 * @version 1.0 - January 9th 2023
 */
public class Currency extends GameObject {
    private final Image[] sprites;
    private int currentSprite;

    /**
     * Instantiates a {@link Currency} object.
     */
    public Currency() {
        this.sprites = Assets.getInstance().getCoins().getCoins();
        this.currentSprite = 0;
    }

    /**
     * draw
     * Draws the currency at the specified location.
     * @param g the {@link Graphics} object
     * @param x the x coordinate of the top left corner of the drawing location
     * @param y the y coordinate of the top left corner of the drawing location
     */
    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(this.sprites[this.currentSprite], x, y, null);
    }

    /**
     * tick
     * Advances the sprite image
     */
    @Override
    public void tick() {
        this.currentSprite = (this.currentSprite + 1) % this.sprites.length;
    }
}
