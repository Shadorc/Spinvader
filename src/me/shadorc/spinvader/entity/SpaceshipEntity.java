package me.shadorc.spinvader.entity;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Game;
import me.shadorc.spinvader.graphic.Sprite;

public class SpaceshipEntity implements Entity {

	private Game game;

	private float x, y;
	private float speed;
	private float lifeMax, life;

	private double lastShoot;
	private int shootSpeed, shootTime;

	private ImageIcon img;

	public SpaceshipEntity(int x, int y, Game game) {
		this.x = x;
		this.y = y;
		this.game = game;

		speed = 25;
		lifeMax = 5;
		life = lifeMax;

		lastShoot = 0;
		shootSpeed = 50;
		shootTime = 200;

		img = Sprite.get("spaceship_normal.png", 150, 150);
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

	public void heal(int i) {
		if(life < lifeMax) {
			life += i;
		}
	}

	public float getMaximumLife() {
		return lifeMax;
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
	public void collidedWith(Entity en) {
		if(en instanceof EnemyEntity) {
			game.gameOver();
		}

		else if(en instanceof BulletEntity) {
			if(((BulletEntity) en).getType() == Type.ENEMY) {
				life--;
				game.removeEntity(en);
				if(life <= 0) {
					game.gameOver();
				}
			}
		}
	}

	@Override
	public void shoot() {
		if((System.currentTimeMillis() - lastShoot) >= shootTime) {
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
		}
	}

	public void moveRight(double delta) {
		if(x <= Frame.getWidth() - img.getIconWidth()) {
			x += (speed * delta) / 30;
		}
	}

	public void moveForward(double delta) {
		if(y >= 0) {
			y -= (speed * delta) / 30;
		}
	}

	public void moveBackward(double delta) {
		if(y <= Frame.getHeight() - img.getIconHeight()) {
			y += (speed * delta) / 30;
		}
	}
}
