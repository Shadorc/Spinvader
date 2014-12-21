package me.shadorc.spinvader.entity;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Game;
import me.shadorc.spinvader.graphic.Sprite;

public class EnemyEntity implements Entity {

	private Game game;
	private Random rand;

	private float x, y;
	private float speed, shootSpeed, shootTime;
	private double lastShoot;
	private float life;

	private ImageIcon img;
	private static Direction dir;

	private int toReach;

	private EnemyEntity(float x, float y, ImageIcon img, Game game) {
		this.x = x;
		this.y = y;
		this.img = img;
		this.game = game;

		rand = new Random();

		speed = 5;
		shootSpeed = 20;
		lastShoot = System.currentTimeMillis();
		shootTime = rand.nextInt(5000)+1000;
		life = 1;
		toReach = (int) (y+350);

		dir = Direction.RIGHT;
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
		game.addEntity(new BulletEntity((x + img.getIconWidth()/2), (y + img.getIconHeight()), Direction.DOWN, shootSpeed, Type.ENEMY, game));
		lastShoot = System.currentTimeMillis();
		shootTime = rand.nextInt(5000)+1000;
	}

	@Override
	public void move(double delta) {

		if(dir == Direction.DOWN) {
			y += (float) ((speed * delta) / 30);

			if(Math.abs(toReach - y) < 0.1) {
				if(x > 500) {
					dir = Direction.LEFT;
				} else {
					dir = Direction.RIGHT;
				}
			}

			if(y >= (Frame.getScreenHeight() - img.getIconHeight())) {
				game.gameOver();
			}

		} else if(dir == Direction.RIGHT) {
			x += (float) ((speed * delta) / 30);

			if(x >= Frame.getScreenWidth() - img.getIconWidth()) {
				dir = Direction.LEFT;
				x = (float) (Frame.getScreenWidth() - img.getIconWidth());
				dir = Direction.DOWN;
				toReach = (int) (y + this.getHitbox().getHeight());
			}

		} else if(dir == Direction.LEFT) {
			x -= (float) ((speed * delta) / 30);

			if(x <= 0) {
				dir = Direction.RIGHT;
				x = 0;
				dir = Direction.DOWN;
				toReach = (int) (y + this.getHitbox().getHeight());
			}
		}


		/*
		//If y ~= toReach
		if(Math.abs(toReach - y) < 0.00001) {
			if(dir == Direction.RIGHT) {
				x += (float) ((speed * delta) / 30);
			} else if(dir == Direction.LEFT) {
				x -= (float) ((speed * delta) / 30);
			}

			if(x <= 0) {
				dir = Direction.RIGHT;
				x = 0;
				toReach = (int) (y + this.getHitbox().getHeight());
			} else if(x >= Frame.getScreenWidth() - img.getIconWidth()) {
				dir = Direction.LEFT;
				x = (float) (Frame.getScreenWidth() - img.getIconWidth());
				toReach = (int) (y + this.getHitbox().getHeight());
			}
		} else {
			y += (float) ((speed * delta) / 30);

			if(y > toReach)	{
				y = toReach;
			}

			if(y >= (Frame.getScreenHeight() - img.getIconHeight())) {
				game.gameOver();
			}
		}
		 */
	}

	@Override
	public void collidedWith(Entity en) {
		if(en instanceof BulletEntity) {
			if(((BulletEntity) en).getType() == Type.SPACESHIP) {
				life--;
				game.removeEntity(en);

				if(this.life <= 0) {
					game.removeEntity(this);
					game.increaseScore(35);
					if(rand.nextInt(10) == 0) {
						game.addEntity(new Item(x, y, game));
					}
				}
			}
		}
	}

	public void update() {
		if((System.currentTimeMillis() - lastShoot) >= shootTime) {
			this.shoot();
		}
	}

	public static ArrayList <EnemyEntity> generate(int count, int level, Game game) {
		ArrayList <EnemyEntity> ennemies = new ArrayList <EnemyEntity>();
		int x = 0;
		int y = 0;

		for(int i = 1; i < count + 1; i++) {
			ennemies.add(new EnemyEntity((10+110*x), (10+110*y - 3*110), Sprite.resize(Sprite.generateSprite(level), 100, 90),	game));
			if(i % 12 == 0) {
				y++;
				x = 0;
			} else {
				x++;
			}
		}

		return ennemies;
	}
}