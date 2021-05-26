package me.shadorc.spinvader.entity;

import me.shadorc.spinvader.Main;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Sprite;

import javax.swing.*;

public class Bullet extends Entity {

    private final static ImageIcon SPRITE = Sprite.get("bullet.png", 5, 22);

    private final float speedY;

    private final Direction dir;
    private final Type type;

    public Bullet(float x, float y, float speed, Direction dir, Type type) {
        super(x, y, 0, SPRITE);

        this.speedY = speed * Frame.getScaleY();
        this.type = type;
        this.dir = dir;
    }

    @Override
    public void move(double delta) {
        y += speedY * delta * (dir == Direction.DOWN ? 1 : -1);

        if (y + this.getHitbox().getHeight() <= 0 || y >= Main.getFrame().getHeight()) {
            Main.getGame().delEntity(this);
        }
    }

    public Type getType() {
        return type;
    }
}