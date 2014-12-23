package me.shadorc.spinvader.entity;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.Random;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Game;
import me.shadorc.spinvader.graphic.Sprite;

public class BossEntity implements Entity {

	private Game game;
	private Random rand;

	private float x, y;
	private float speed, shootSpeed, shootTime;
	private double lastShoot;
	private float life;

	private ImageIcon img;
	private Direction dir;

	public BossEntity(float x, float y, Game game) {
		this.x = x;
		this.y = y;
		this.game = game;

		rand = new Random();
		dir = Direction.RIGHT;
		img = Sprite.resize(Sprite.getSprite("/img/boss.png"), 335, 170);

		speed = 15;
		shootSpeed = 20;
		lastShoot = System.currentTimeMillis();
		shootTime = rand.nextInt(1000)+500;
		life = 50;
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
	public Rectangle getHitbox() {
		return new Rectangle((int) x, (int) y, img.getIconWidth(), img.getIconHeight());
	}

	@Override
	public Image getImage() {
		return img.getImage();
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
					game.increaseScore(300);
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

	@Override
	public void move(double delta) {
		if(dir == Direction.RIGHT) {
			x += (float) ((speed * delta) / 30);

			if(x >= Frame.getWidth() - img.getIconWidth()) {
				x = (float) (Frame.getWidth() - img.getIconWidth());
				dir = Direction.LEFT;
			}

		} else if(dir == Direction.LEFT) {
			x -= (float) ((speed * delta) / 30);

			if(x <= 0) {
				x = 0;
				dir = Direction.RIGHT;
			}
		}
	}

	@Override
	public void shoot() {
		if((System.currentTimeMillis() - lastShoot) >= shootTime) {
			game.addEntity(new BulletEntity((x + 50), (y + img.getIconHeight()), Direction.DOWN, shootSpeed, Type.ENEMY, game));
			game.addEntity(new BulletEntity((img.getIconWidth() - 50), (y + img.getIconHeight()), Direction.DOWN, shootSpeed, Type.ENEMY, game));

			lastShoot = System.currentTimeMillis();
			shootTime = rand.nextInt(1000)+500;
		}
	}
}
