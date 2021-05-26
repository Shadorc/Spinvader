package me.shadorc.spinvader.entity;

import javax.swing.*;
import java.awt.*;

public class Entity {

    public enum Direction {
        UP, DOWN, RIGHT, LEFT
    }

    public enum Type {
        SPACESHIP, ENEMY
    }

    public enum Bonus {
        COIN, LIFE, FIREMODE, EXPLOSIVE
    }

    protected ImageIcon img;
    protected float x, y;
    protected float life;

    public Entity(float x, float y, float life, ImageIcon img) {
        this.x = x;
        this.y = y;
        this.life = life;
        this.img = img;
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    public float getLife() {
        return life;
    }

    public Image getImage() {
        return img.getImage();
    }

    public Rectangle getHitbox() {
        return new Rectangle((int) x, (int) y, img.getIconWidth(), img.getIconHeight());
    }

    public void takeDamage(float damage) {
        life -= damage;
        if (life <= 0) {
            this.die();
        }
    }

    public void collidedWith(Entity en) {
    }

    public void move(double delta) {
    }

    public void shoot() {
    }

    public void die() {
    }
}
