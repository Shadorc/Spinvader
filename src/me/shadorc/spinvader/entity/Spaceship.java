package me.shadorc.spinvader.entity;

import me.shadorc.spinvader.Main;
import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.Storage.Data;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Sprite;

import javax.swing.*;

public class Spaceship extends Entity {

    private final static ImageIcon SPRITE = Sprite.get("spaceship.png", 150, 150);

    private final float speedX, speedY;
    private final float lifeMax;

    private double lastShoot;
    private final float bulletSpeed, reloadTime;

    private boolean explosiveAmmo;
    private int fireMode;

    public Spaceship(int x, int y) {
        super(x, y, 5, SPRITE);

        this.speedX = 1.5f * Frame.getScaleX();
        this.speedY = 1.5f * Frame.getScaleY();
        this.lifeMax = this.getLife();

        this.bulletSpeed = 1.7f * Frame.getScaleY();
        this.reloadTime = 150;
        this.lastShoot = 0;

        this.fireMode = 1;
        this.explosiveAmmo = false;
    }

    @Override
    public void collidedWith(Entity en) {
        if (en instanceof Enemy) {
            this.die();
        } else if (en instanceof Bullet && ((Bullet) en).getType() == Type.ENEMY) {
            this.takeDamage(1);
            Main.getGame().delEntity(en);
        }
    }

    @Override
    public void shoot() {
        if ((System.currentTimeMillis() - lastShoot) >= reloadTime) {
            switch (fireMode) {
                case 1:
                    Main.getGame().addEntity(new Bullet(x + img.getIconWidth() / 2, y, bulletSpeed, Direction.UP, Type.SPACESHIP));
                    break;
                case 2:
                    Main.getGame().addEntity(new Bullet(x, y + img.getIconHeight() / 2, bulletSpeed, Direction.UP, Type.SPACESHIP));
                    Main.getGame().addEntity(new Bullet(x + img.getIconWidth(), y + img.getIconHeight() / 2, bulletSpeed, Direction.UP, Type.SPACESHIP));
                    break;
                case 3:
                    Main.getGame().addEntity(new Bullet(x, y + img.getIconHeight() / 2, bulletSpeed, Direction.UP, Type.SPACESHIP));
                    Main.getGame().addEntity(new Bullet(x + img.getIconWidth() / 2, y, bulletSpeed, Direction.UP, Type.SPACESHIP));
                    Main.getGame().addEntity(new Bullet(x + img.getIconWidth(), y + img.getIconHeight() / 2, bulletSpeed, Direction.UP, Type.SPACESHIP));
                    break;
                case 4:
                    Main.getGame().addEntity(new Bullet(x, y + img.getIconHeight(), bulletSpeed, Direction.UP, Type.SPACESHIP));
                    Main.getGame().addEntity(new Bullet(x + img.getIconWidth() / 3, y + img.getIconHeight() / 2, bulletSpeed, Direction.UP, Type.SPACESHIP));
                    Main.getGame().addEntity(new Bullet(x + img.getIconWidth() - (img.getIconWidth() / 3), y + img.getIconHeight() / 2, bulletSpeed, Direction.UP, Type.SPACESHIP));
                    Main.getGame().addEntity(new Bullet(x + img.getIconWidth(), y + img.getIconHeight(), bulletSpeed, Direction.UP, Type.SPACESHIP));
                    break;
            }

            Sound.play("spaceship_shoot.wav", 0.10, Data.SOUND_VOLUME);
            lastShoot = System.currentTimeMillis();
        }
    }

    @Override
    public void die() {
        Main.getGame().gameOver();
    }

    public void moveLeft(double delta) {
        if (x >= 0) {
            x -= speedX * delta;
        }
    }

    public void moveRight(double delta) {
        if (x <= (Main.getFrame().getWidth() - img.getIconWidth())) {
            x += speedX * delta;
        }
    }

    public void moveForward(double delta) {
        if (y >= 0) {
            y -= speedY * delta;
        }
    }

    public void moveBackward(double delta) {
        if (y <= (Main.getFrame().getHeight() - img.getIconHeight())) {
            y += speedY * delta;
        }
    }

    public void heal(int i) {
        life += i;
        life = Math.min(life, lifeMax);
    }

    public void setFireMode(int fireMode) {
        this.fireMode = fireMode;
    }

    public void activeBomb() {
        explosiveAmmo = true;
    }

    public int getFireMode() {
        return fireMode;
    }

    public float getMaximumLife() {
        return lifeMax;
    }

    public boolean hasExplosiveAmmo() {
        return explosiveAmmo;
    }
}
