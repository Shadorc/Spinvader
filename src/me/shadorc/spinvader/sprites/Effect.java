package me.shadorc.spinvader.sprites;

import me.shadorc.spinvader.Main;

import java.awt.*;

public class Effect {

    protected final float x;
    protected final float y;
    protected int duration;
    protected long start;

    public Effect(float x, float y, int duration) {
        this.x = x;
        this.y = y;
        this.duration = duration;
        this.start = System.currentTimeMillis();
    }

    public void update(double delta) {
        if (System.currentTimeMillis() - start >= duration) {
            Main.getGame().delEffect(this);
        }
    }

    public void render(Graphics2D g2d) {
        // TODO
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }
}