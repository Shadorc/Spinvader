package me.shadorc.spinvader.entity;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.Random;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Game;

public class EnemyEntity implements Entity {

	private Game game;
	private Random rand;

	private float x, y;
	private double lastShoot;
	private float speed, shootSpeed, shootTime;
	private int randomTime, minTime;
	private float life;

	private ImageIcon img;
	private static Direction dir;
	private static Direction nextDir;

	private int toReach;

	public EnemyEntity(float x, float y, ImageIcon img, Game game) {
		this.x = x;
		this.y = y;
		this.img = img;
		this.game = game;

		rand = new Random();

		speed = 5;
		life = 2;

		shootSpeed = 15;
		randomTime = 7500;
		minTime = 2500;
		lastShoot = System.currentTimeMillis();
		shootTime = rand.nextInt(randomTime)+minTime;

		toReach = (int) (y+400);
		dir = Direction.DOWN;
		nextDir = Direction.RIGHT;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
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
			game.addEntity(new BulletEntity((x + img.getIconWidth()/2), (y + img.getIconHeight()), Direction.DOWN, shootSpeed, Type.ENEMY, game));
			lastShoot = System.currentTimeMillis();
			shootTime = rand.nextInt(randomTime)+minTime;
		}
	}

	@Override
	public void move(double delta) {

		if(dir == Direction.DOWN) {
			y += (float) ((speed * delta) / 30);

			if(toReach <= y) {
				y = toReach;
				dir = nextDir;
			}

			if(y >= (Frame.getHeight() - img.getIconHeight())) {
				game.gameOver();
			}

		} else if(dir == Direction.RIGHT) {
			x += (float) ((speed * delta) / 30);

			if(x >= Frame.getWidth() - img.getIconWidth()) {
				x = (float) (Frame.getWidth() - img.getIconWidth());
				dir = Direction.DOWN;
				nextDir = Direction.LEFT;
				game.bringDownEnemies();
			}

		} else if(dir == Direction.LEFT) {
			x -= (float) ((speed * delta) / 30);

			if(x <= 0) {
				x = 0;
				dir = Direction.DOWN;
				nextDir = Direction.RIGHT;
				game.bringDownEnemies();
			}
		}
	}

	@Override
	public void collidedWith(Entity en) {
		if(en instanceof BulletEntity) {
			if(((BulletEntity) en).getType() == Type.SPACESHIP) {
				life--;
				game.removeEntity(en);

				if(this.life <= 0) {
					new Sound("AlienDestroyed.wav", 0.15).start();
					game.removeEntity(this);
					game.increaseScore(35);
					if(rand.nextInt(25) == 0) {
						if(rand.nextInt(5) == 0) {
							game.addEntity(new Item(x, y, Bonus.LIFE, game));
						} else {
							game.addEntity(new Item(x, y, Bonus.MONEY, game));
						}
					}
				}
			}
		}
	}

	public void goDown() {
		toReach = (int) (y + this.getHitbox().getHeight());
	}
}