package me.shadorc.spinvader.entity;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.Main;
import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.Storage.Data;
import me.shadorc.spinvader.Utils;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Sprite;
import me.shadorc.spinvader.sprites.AnimatedSprite;

public class Enemy extends Entity {

	private ImageIcon explosionSprite;

	private float speedX, speedY;

	private float bulletSpeed, reloadTime;
	private double lastShoot;

	private int toReach;

	private static Direction dir;
	private static Direction nextDir;

	public Enemy(float x, float y, ImageIcon img) {
		super(x, y, Main.getGame().getLevel(), img);

		this.explosionSprite = Sprite.get("explosion.png", (int) (img.getIconWidth()/Frame.getScaleX()), (int) (img.getIconHeight()/Frame.getScaleY()));

		this.toReach = (int) (y+400*Frame.getScaleY());
		this.speedX = 0.07f * Frame.getScaleX();
		this.speedY = 0.07f * Frame.getScaleY();

		this.bulletSpeed = 0.5f * Frame.getScaleY();
		this.reloadTime = this.generateShootTime();
		this.lastShoot = System.currentTimeMillis();

		dir = Direction.DOWN;
		nextDir = Direction.RIGHT;
	}

	@Override
	public void move(double delta) {
		switch(dir) {
			case DOWN:
				y += speedY*delta;

				if(y >= toReach) {
					y = toReach;
					dir = nextDir;
				}

				if(y >= (Main.getFrame().getHeight() - img.getIconHeight())) {
					Main.getGame().gameOver();
				}
				break;

			case LEFT:
				x -= speedX*delta;

				if(x <= 0) {
					x = 0;
					dir = Direction.DOWN;
					nextDir = Direction.RIGHT;
					Main.getGame().bringDownEnemies();
				}
				break;

			case RIGHT:
				x += speedX*delta;

				if(x >= Main.getFrame().getWidth() - img.getIconWidth()) {
					x = (float) (Main.getFrame().getWidth() - img.getIconWidth());
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
		if(System.currentTimeMillis() - lastShoot >= reloadTime) {
			Main.getGame().addEntity(new Bullet(x+img.getIconWidth()/2, y+img.getIconHeight(), bulletSpeed, Direction.DOWN, Type.ENEMY));
			lastShoot = System.currentTimeMillis();
			reloadTime = this.generateShootTime();
		}
	}

	@Override
	public void collidedWith(Entity en) {
		if(en instanceof Bullet) {
			if(((Bullet) en).getType() == Type.SPACESHIP) {
				Main.getGame().delEntity(en);
				if(Main.getGame().getSpaceship().hasExplosiveAmmo()) {
					Main.getGame().genExplosion(en.getX(), en.getY(), 250);
				} else {
					this.takeDamage(1);
				}
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

	private int generateShootTime() {
		return Utils.randInt(5000)+5000;
	}

	public void goDown() {
		toReach = (int) (y + this.getHitbox().getHeight());
	}
}