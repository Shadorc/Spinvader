package me.shadorc.spinvader.entity;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.Sound;
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
	private static Direction nextDir;

	private int toReach;

	private EnemyEntity(float x, float y, ImageIcon img, Game game) {
		this.x = x;
		this.y = y;
		this.img = img;
		this.game = game;

		rand = new Random();

		speed = 5;
		shootSpeed = 15;
		lastShoot = System.currentTimeMillis();
		shootTime = rand.nextInt(7500)+2500;
		life = 3;
		toReach = (int) (y+350);

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
		game.addEntity(new BulletEntity((x + img.getIconWidth()/2), (y + img.getIconHeight()), Direction.DOWN, shootSpeed, Type.ENEMY, game));
		lastShoot = System.currentTimeMillis();
		shootTime = rand.nextInt(7500)+2500;
	}

	@Override
	public void move(double delta) {

		if(dir == Direction.DOWN) {
			y += (float) ((speed * delta) / 30);

			if(toReach <= y) {
				y = toReach;
				dir = nextDir;
			}

			if(y >= (Frame.getScreenHeight() - img.getIconHeight())) {
				game.gameOver();
			}

		} else if(dir == Direction.RIGHT) {
			x += (float) ((speed * delta) / 30);

			if(x >= Frame.getScreenWidth() - img.getIconWidth()) {
				x = (float) (Frame.getScreenWidth() - img.getIconWidth());
				dir = Direction.DOWN;
				nextDir = Direction.LEFT;
				game.descendreEnnemis();
			}

		} else if(dir == Direction.LEFT) {
			x -= (float) ((speed * delta) / 30);

			if(x <= 0) {
				x = 0;
				dir = Direction.DOWN;
				nextDir = Direction.RIGHT;
				game.descendreEnnemis();
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
					if(rand.nextInt(10) == 0) {
						if(rand.nextInt(3) == 0) {
							game.addEntity(new Item(x, y, Bonus.LIFE, game));
						} else {
							game.addEntity(new Item(x, y, Bonus.MONEY, game));
						}
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

	public void setToReach() {
		toReach = (int) (y + this.getHitbox().getHeight());
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