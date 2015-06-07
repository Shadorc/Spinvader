package me.shadorc.spinvader.entity;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Game;
import me.shadorc.spinvader.graphic.Sprite;

public class Enemy extends Entity {

	private float speed;

	private float bulletSpeed, reloadTime;
	private double lastShoot;

	private int toReach;

	private static Direction dir;
	private static Direction nextDir;

	private double animationStart;
	private boolean dead;

	public Enemy(float x, float y, ImageIcon img) {
		super(x, y, (float) Math.pow(Frame.getGame().getLevel(), 2), img);

		dead = false;

		speed = 2;

		bulletSpeed = 15;
		lastShoot = 0;
		reloadTime = this.generateShootTime();

		toReach = (int) (y+400);
		dir = Direction.DOWN;
		nextDir = Direction.RIGHT;
	}

	@Override
	public void move(double delta) {
		if(dead) {
			if((System.currentTimeMillis() - animationStart) >= 100)	Frame.getGame().delEntity(this);
			return;
		}

		switch(dir) {
			case DOWN:
				y += (float) ((speed * delta) / 30);

				if(toReach <= y) {
					y = toReach;
					dir = nextDir;
				}

				if(y >= (Frame.getHeight() - img.getIconHeight()))	Frame.getGame().gameOver();
				break;

			case LEFT:
				x -= (float) ((speed * delta) / 30);

				if(x <= 0) {
					x = 0;
					dir = Direction.DOWN;
					nextDir = Direction.RIGHT;
					Frame.getGame().bringDownEnemies();
				}
				break;

			case RIGHT:
				x += (float) ((speed * delta) / 30);

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
		if(!dead && en instanceof Bullet) {
			if(((Bullet) en).getType() == Type.SPACESHIP) {
				this.takeDamage(1);
				Frame.getGame().delEntity(en);
				Frame.getGame().explosion(en.getX(), en.getY(), 250);
			}
		}
	}

	@Override
	public void die() {
		dead = true;
		animationStart = System.currentTimeMillis();
		img = Sprite.get("explosion.png", img.getIconWidth(), img.getIconHeight());

		Sound.play("AlienDestroyed.wav", 0.10);
		Frame.getGame().incScore(35);
		Item.generate(x, y);
	}

	private int generateShootTime() {
		return Game.rand(7500)+2500;
	}

	public void goDown() {
		toReach = (int) (y + this.getHitbox().getHeight());
	}
}