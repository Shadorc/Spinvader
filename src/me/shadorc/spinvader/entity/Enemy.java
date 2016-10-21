package me.shadorc.spinvader.entity;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.Storage.Data;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Game;
import me.shadorc.spinvader.graphic.Sprite;
import me.shadorc.spinvader.sprites.AnimatedSprite;

public class Enemy extends Entity {

	private float speedX, speedY;

	private float bulletSpeed, reloadTime;
	private double lastShoot;

	private int toReach;

	private static Direction dir;
	private static Direction nextDir;

	public Enemy(float x, float y, ImageIcon img) {
		super(x, y, (float) Math.pow(Frame.getGame().getLevel(), 2), img);

		toReach = (int) (y+400*Frame.getScaleY());
		speedX = 2 * Frame.getScaleX();
		speedY = 2 * Frame.getScaleY();

		reloadTime = this.generateShootTime();
		bulletSpeed = 15;
		lastShoot = System.currentTimeMillis();

		dir = Direction.DOWN;
		nextDir = Direction.RIGHT;
	}

	@Override
	public void move(double delta) {
		switch(dir) {
			case DOWN:
				y += (float) ((speedY * delta) / 30);

				if(toReach <= y) {
					y = toReach;
					dir = nextDir;
				}

				if(y >= (Frame.getHeight() - img.getIconHeight())) Frame.getGame().gameOver();
				break;

			case LEFT:
				x -= (float) ((speedX * delta) / 30);

				if(x <= 0) {
					x = 0;
					dir = Direction.DOWN;
					nextDir = Direction.RIGHT;
					Frame.getGame().bringDownEnemies();
				}
				break;

			case RIGHT:
				x += (float) ((speedX * delta) / 30);

				if(x >= Frame.getWidth() - img.getIconWidth()) {
					x = (float) (Frame.getWidth() - img.getIconWidth());
					dir = Direction.DOWN;
					nextDir = Direction.LEFT;
					Frame.getGame().bringDownEnemies();
				}
				break;

			default:
				break;
		}
	}

	@Override
	public void shoot() {
		if((System.currentTimeMillis() - lastShoot) >= reloadTime) {
			Frame.getGame().addEntity(new Bullet((x + img.getIconWidth()/2), (y + img.getIconHeight()), bulletSpeed, Direction.DOWN, Type.ENEMY));
			lastShoot = System.currentTimeMillis();
			reloadTime = this.generateShootTime();
		}
	}

	@Override
	public void collidedWith(Entity en) {
		if(en instanceof Bullet) {
			if(((Bullet) en).getType() == Type.SPACESHIP) {
				this.takeDamage(1);
				Frame.getGame().delEntity(en);
				if(Frame.getGame().getSpaceship().hasExplosiveAmmo()) {
					Frame.getGame().explosion(en.getX(), en.getY(), 250);
				}
			}
		}
	}

	@Override
	public void die() {
		Sound.play("AlienDestroyed.wav", 0.10, Data.SOUND_VOLUME);
		Frame.getGame().addEffect(new AnimatedSprite(x, y, Sprite.get("explosion.png", (int) (img.getIconWidth()/Frame.getScaleX()), (int) (img.getIconHeight()/Frame.getScaleY())), 100));
		Frame.getGame().delEntity(this);
		Frame.getGame().incScore(35);
		Item.generate(x, y);
	}

	private int generateShootTime() {
		return Game.rand(6500)+3500;
	}

	public void goDown() {
		toReach = (int) (y + this.getHitbox().getHeight());
	}
}