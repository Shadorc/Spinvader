package me.shadorc.spinvader.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Sprite;

public class SpaceshipEntity implements Entity {

	private float x, y;
	private float speed;
	private float life;

	private double lastShoot;
	private int shootSpeed;
	private int shootTime;

	private ImageIcon img;
	private ArrayList <BulletEntity> bullets;

	public SpaceshipEntity(int x, int y) {
		this.x = x;
		this.y = y;

		speed = 25;
		life = 5;

		lastShoot = 0;
		shootSpeed = 50;
		shootTime = 50;

		img = Sprite.getSprite("/img/spaceship_normal.png", 150, 150);
		bullets = new ArrayList <BulletEntity> ();
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

	public boolean isReloaded() {
		return (System.currentTimeMillis() - lastShoot) >= shootTime;
	}

	public ArrayList <BulletEntity> getBullets() {
		return bullets;
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

	public void moveLeft() {
		if(x >= 0) {
			x -= speed;
			img = Sprite.getSprite("/img/spaceship_left.png", 150, 150);
		}
	}

	public void moveRight() {
		if(x <= Frame.getScreenWidth() - img.getIconWidth()) {
			x += speed;
			img = Sprite.getSprite("/img/spaceship_right.png", 150, 150);
		}
	}

	public void moveForward() {
		if(y >= 0) {
			y -= speed;
		}
	}

	public void moveBackward() {
		if(y <= Frame.getScreenHeight() - img.getIconHeight()) {
			y += speed;
		}
	}

	@Override
	public void shoot() {
		if(this.isReloaded()) {
			bullets.add(new BulletEntity(x + img.getIconWidth()/2, y, Direction.UP, shootSpeed));
			new Sound("spaceship_shoot.wav", 0.01).start();
			lastShoot = System.currentTimeMillis();
		}
	}
}
