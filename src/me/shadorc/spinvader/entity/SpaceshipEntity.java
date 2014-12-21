package me.shadorc.spinvader.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Game;
import me.shadorc.spinvader.graphic.Sprite;

public class SpaceshipEntity implements Entity {

	private float x, y;
	private float speed;
	private float lifeMax, life;

	private Game game;

	private double lastShoot;
	private int shootSpeed;
	private int shootTime;

	private ImageIcon img;

	public SpaceshipEntity(int x, int y, Game game) {
		this.x = x;
		this.y = y;
		this.game = game;

		speed = 25;
		lifeMax = 25;
		life = lifeMax;

		lastShoot = 0;
		shootSpeed = 50;
		shootTime = 200;

		img = Sprite.getSprite("/img/spaceship_normal.png", 150, 150);
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

	public float getMaximumLife() {
		return lifeMax;
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
	public void drawHitbox(Graphics g) {
		Rectangle re = this.getHitbox();
		g.setColor(new Color(1f, 0f, 0f, 0.5f));
		g.drawRect((int) re.getX(), (int) re.getY(), (int) re.getWidth(), (int) re.getHeight());
	}

	@Override
	public void collidedWith(Entity en) {
		if(en instanceof EnemyEntity) {
			game.gameOver();
		}

		else if(en instanceof BulletEntity) {
			if(((BulletEntity) en).getType() == Type.ENEMY) {
				this.hit();
				game.removeEntity(en);
				if(life <= 0) {
					game.gameOver();
				}
			}
		}
	}

	public boolean isReloaded() {
		return (System.currentTimeMillis() - lastShoot) >= shootTime;
	}

	@Override
	public void shoot() {
		if(this.isReloaded()) {

			/*
			Point a = new Point(new Random().nextInt(Frame.getScreenWidth()), 0);
			Point b = new Point((int) x, (int) y);

			float coef = (float) ((b.getY() - a.getY())/(b.getX() - a.getX()));
			float ord = (float) (b.getY() - coef * b.getX());
			 */

			game.addEntity(new BulletEntity(x + img.getIconWidth() / 2, y, Direction.UP, shootSpeed, Type.SPACESHIP, game));
			game.addEntity(new BulletEntity(x, y+img.getIconHeight()/2, Direction.UP, shootSpeed, Type.SPACESHIP, game));
			game.addEntity(new BulletEntity(x + img.getIconWidth(), y+img.getIconHeight()/2, Direction.UP, shootSpeed, Type.SPACESHIP, game));
			new Sound("spaceship_shoot.wav", 0.1).start();
			lastShoot = System.currentTimeMillis();
		}
	}

	@Override
	public void move(double delta) {

	}

	public void moveLeft(double delta) {
		if(x >= 0) {
			x -= (speed * delta) / 30;
			//			img = Sprite.getSprite("/img/spaceship_left.png", 150, 150);
		}
	}

	public void moveRight(double delta) {
		if(x <= Frame.getScreenWidth() - img.getIconWidth()) {
			x += (speed * delta) / 30;
			//			img = Sprite.getSprite("/img/spaceship_right.png", 150, 150);
		}
	}

	public void moveForward(double delta) {
		if(y >= 0) {
			y -= (speed * delta) / 30;
		}
	}

	public void moveBackward(double delta) {
		if(y <= Frame.getScreenHeight() - img.getIconHeight()) {
			y += (speed * delta) / 30;
		}
	}
}
