package me.shadorc.spinvader.entity;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Game;
import me.shadorc.spinvader.graphic.Sprite;

public class Enemy implements Entity {

	private float x, y;
	private float life;
	private float speed, shootSpeed, shootTime;
	private double lastShoot;
	private int randomTime, minTime;
	private int toReach;

	private ImageIcon img;
	private static Direction dir;
	private static Direction nextDir;

	private boolean dead;
	private double animationStart;

	public Enemy(float x, float y, ImageIcon img) {
		this.x = x;
		this.y = y;
		this.img = img;

		dead = false;

		speed = 2;
		life = 2;

		shootSpeed = 15;
		randomTime = 7500;
		minTime = 2500;
		lastShoot = System.currentTimeMillis();
		shootTime = Game.rand(randomTime)+minTime;

		toReach = (int) (y+400);
		dir = Direction.DOWN;
		nextDir = Direction.RIGHT;
	}

	@Override
	public int getX() {
		return (int) x;
	}

	@Override
	public int getY() {
		return (int) y;
	}

	@Override
	public float getLife() {
		return life;
	}

	@Override
	public Image getImage() {
		return img.getImage();
	}

	@Override
	public Rectangle getHitbox() {
		return new Rectangle((int) x, (int) y, img.getIconWidth(), img.getIconHeight());
	}

	@Override
	public void shoot() {
		if((System.currentTimeMillis() - lastShoot) >= shootTime) {
			Frame.getGame().addEntity(new Bullet((x + img.getIconWidth()/2), (y + img.getIconHeight()), Direction.DOWN, shootSpeed, Type.ENEMY));
			lastShoot = System.currentTimeMillis();
			shootTime = Game.rand(randomTime)+minTime;
		}
	}

	@Override
	public void move(double delta) {

		if(dead) {
			if((System.currentTimeMillis() - animationStart) >= 100) {
				Frame.getGame().delEntity(this);
			}
			return;
		}

		if(dir == Direction.DOWN) {
			y += (float) ((speed * delta) / 30);

			if(toReach <= y) {
				y = toReach;
				dir = nextDir;
			}

			if(y >= (Frame.getHeight() - img.getIconHeight())) {
				Frame.getGame().gameOver();
			}
		} 

		else if(dir == Direction.RIGHT) {
			x += (float) ((speed * delta) / 30);

			if(x >= Frame.getWidth() - img.getIconWidth()) {
				x = (float) (Frame.getWidth() - img.getIconWidth());
				dir = Direction.DOWN;
				nextDir = Direction.LEFT;
				Frame.getGame().bringDownEnemies();
			}
		} 

		else if(dir == Direction.LEFT) {
			x -= (float) ((speed * delta) / 30);

			if(x <= 0) {
				x = 0;
				dir = Direction.DOWN;
				nextDir = Direction.RIGHT;
				Frame.getGame().bringDownEnemies();
			}
		}
	}

	@Override
	public void collidedWith(Entity en) {
		if(en instanceof Bullet) {
			if(((Bullet) en).getType() == Type.SPACESHIP) {
				life--;
				Frame.getGame().delEntity(en);

				if(life <= 0) {
					this.die();
				}
			}
		}
	}

	private void die() {
		dead = true;
		animationStart = System.currentTimeMillis();

		Sound.play("AlienDestroyed.wav", 0.10);

		img = Sprite.get("explosion.png", 110, 80);
		Frame.getGame().incScore(35);

		int rand = Game.rand(50);

		if(rand == 0) {
			Frame.getGame().addEntity(new Item(x, y, Bonus.LIFE));
		} else if(rand > 30 && Frame.getGame().getSpaceship().getFireMode() < 4) {
			Frame.getGame().addEntity(new Item(x, y, Bonus.FIREMODE));
		}
	}

	public void goDown() {
		toReach = (int) (y + this.getHitbox().getHeight());
	}
}