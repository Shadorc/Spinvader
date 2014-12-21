package me.shadorc.spinvader.entity;

import java.awt.Color;
import java.awt.Graphics;
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
	private Direction dir;

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
		toReach = (int) (-y-100);

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
	public void hit() {
		life--;
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
	public void drawHitbox(Graphics g) {
		Rectangle re = this.getHitbox();
		g.setColor(new Color(1f, 0f, 0f, 0.5f));
		g.drawRect((int) re.getX(), (int) re.getY(), (int) re.getWidth(), (int) re.getHeight());
	}

	@Override
	public void move(double delta) {
		if(toReach != y) {
			y += (float) ((speed * delta) / 30);

			if(y >= toReach)	
				y = toReach;

			if(y >= (Frame.getScreenHeight() - img.getIconHeight()))
				game.gameOver();

		} else {
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
		}
	}

	@Override
	public void collidedWith(Entity en) {
		if(en instanceof BulletEntity) {
			if(((BulletEntity) en).getType() == Type.SPACESHIP) {
				this.hit();
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
			ennemies.add(
					new EnemyEntity(10 + (100 * x),
							(10 + (100 * y) - 3*(110)),
							Sprite.resize(Sprite.generateSprite(level), 100, 100),
							game));
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