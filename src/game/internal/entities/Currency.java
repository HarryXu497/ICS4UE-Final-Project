package game.internal.entities;

import game.internal.assets.Assets;

import java.awt.Graphics;
import java.awt.Image;

public class Currency extends GameObject {
    private final Image[] sprites;
    private int currentSprite;

    public Currency() {
        this.sprites = Assets.getInstance().getCoins().getCoins();
        this.currentSprite = 0;
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(this.sprites[this.currentSprite], x, y, null);
    }

    @Override
    public void tick() {
        this.currentSprite = (this.currentSprite + 1) % this.sprites.length;
    }
}
