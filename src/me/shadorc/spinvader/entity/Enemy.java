package me.shadorc.spinvader.entity;

import me.shadorc.spinvader.Main;
import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.Storage.Data;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Sprite;
import me.shadorc.spinvader.sprites.AnimatedSprite;
import me.shadorc.spinvader.utils.RandUtils;

import javax.swing.*;

public class Enemy extends Entity {

    private final static int LINE = 3; // Lines
    private final static int BLANK = 100; // Space without enemies
    private final static int SPACE = 20; // Space between enemies

    private final ImageIcon explosionSprite;

    private final float speedX, speedY;
    private final float bulletSpeed;

    private float reloadTime;
    private double lastShoot;

    private int toReach;

    private static Direction dir;
    private static Direction nextDir;

    public Enemy(float x, float y, ImageIcon img) {
        super(x, y, Main.getGame().getLevel(), img);

        this.explosionSprite = Sprite.get("explosion.png", (int) (img.getIconWidth() / Frame.getScaleX()), (int) (img.getIconHeight() / Frame.getScaleY()));

        this.toReach = (int) (y + 400 * Frame.getScaleY());
        this.speedX = 0.07f * Frame.getScaleX();
        this.speedY = 0.07f * Frame.getScaleY();

        this.bulletSpeed = 0.5f * Frame.getScaleY();
        this.reloadTime = RandUtils.randInt(5000, 10000);
        this.lastShoot = System.currentTimeMillis();

        dir = Direction.DOWN;
        nextDir = Direction.RIGHT;
    }

    @Override
    public void move(double delta) {
        switch (dir) {
            case DOWN:
                y += speedY * delta;

                if (y >= toReach) {
                    y = toReach;
                    dir = nextDir;
                }

                if (y >= (Main.getFrame().getHeight() - img.getIconHeight())) {
                    Main.getGame().gameOver();
                }
                break;

            case LEFT:
                x -= speedX * delta;

                if (x <= 0) {
                    x = 0;
                    dir = Direction.DOWN;
                    nextDir = Direction.RIGHT;
                    Main.getGame().bringDownEnemies();
                }
                break;

            case RIGHT:
                x += speedX * delta;

                if (x >= Main.getFrame().getWidth() - img.getIconWidth()) {
                    x = Main.getFrame().getWidth() - img.getIconWidth();
                    dir = Direction.DOWN;
                    nextDir = Direction.LEFT;
                    Main.getGame().bringDownEnemies();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void shoot() {
        if (System.currentTimeMillis() - lastShoot >= reloadTime) {
            Main.getGame().addEntity(new Bullet(x + img.getIconWidth() / 2.0f,
                    y + img.getIconHeight(), bulletSpeed, Direction.DOWN, Type.ENEMY));
            lastShoot = System.currentTimeMillis();
            reloadTime = RandUtils.randInt(5000, 10000);
        }
    }

    @Override
    public void collidedWith(Entity en) {
        if (en instanceof Bullet && ((Bullet) en).getType() == Type.SPACESHIP) {
            Main.getGame().delEntity(en);
            if (Main.getGame().getSpaceship().hasExplosiveAmmo()) {
                Main.getGame().genExplosion(en.getX(), en.getY(), 250);
            } else {
                this.takeDamage(1);
            }
        }
    }

    @Override
    public void die() {
        Sound.play("AlienDestroyed.wav", 0.10, Data.SOUND_VOLUME);
        Main.getGame().addEffect(new AnimatedSprite(x, y, explosionSprite, 150));
        Main.getGame().delEntity(this);
        Main.getGame().incScore(35);
        Item.generate(x, y);
    }

    public void goDown() {
        toReach = (int) (this.y + this.getHitbox().getHeight());
    }

    public static int genEnemies(int level, int count) {
        int newLevel = level + 1;

        if (newLevel < 8) {
            // Get the original sprite
            ImageIcon enemySprite = Sprite.generateSprite();

            final int column = count / LINE; // Columns

            // Space occupied by each enemy counting the blank without enemies
            int xSize = (Main.getFrame().getWidth() - BLANK) / column;

            // Enemy width without space between them
            int enemyWidth = xSize - SPACE;

            // Resize enemy height depending upon its width
            float scale = (float) Math.max(enemySprite.getIconWidth(), enemyWidth) / Math.min(enemySprite.getIconWidth(), enemyWidth);
            int enemyHeight = (int) (enemySprite.getIconHeight() * scale);

            // Space occupied by each enemy counting the space between them
            int ySize = enemyHeight + SPACE;

            // Resize original sprite with new dimension
            enemySprite = Sprite.resize(enemySprite, enemyWidth, enemyHeight);

            for (int y = 0; y < LINE; y++) {
                for (int x = 0; x < column; x++) {
                    Main.getGame().addEntity(new Enemy(xSize * x,
                            ySize * y - Main.getFrame().getHeight() / 3.0f, enemySprite));
                }
            }

        } else if (newLevel == 8) {
            Main.getGame().addEntity(new Boss(100, 50));

        } else {
            newLevel = 1;
        }

        return newLevel;
    }
}