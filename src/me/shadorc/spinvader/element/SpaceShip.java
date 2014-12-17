package me.shadorc.spinvader.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.graphic.Frame;

public class SpaceShip implements Element {

	private float x, y;
	private float speed;
	private float life;

	private ImageIcon img;
	private ArrayList <Bullet> bullets;

	private double lastShoot;
	private int shootSpeed;
	private int shootTime;

	public SpaceShip(int x, int y) {
		this.x = x;
		this.y = y;

		speed = 25;
		shootSpeed = 50;
		shootTime = 500;
		life = 5;
		lastShoot = 0;
		img = new ImageIcon(new ImageIcon(this.getClass().getResource("/img/spaceship.png")).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
		bullets = new ArrayList <Bullet> ();
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

	public ArrayList <Bullet> getBullets() {
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
		if(x >= 0)
			x -= speed;
	}

	public void moveRight() {
		if(x <= Frame.getScreenWidth() - img.getIconWidth())
			x += speed;
	}

	public void moveForward() {
		if(y >= 0)
			y -= speed;
	}

	public void moveBackward() {
		if(y <= Frame.getScreenHeight() - img.getIconHeight())
			y += speed;
	}

	@Override
	public void shoot() {
		if(this.isReloaded()) {
			bullets.add(new Bullet(x + img.getIconWidth()/2, y, Direction.UP, shootSpeed));
			new Sound("spaceship_shoot.wav", 0.01).start();
			lastShoot = System.currentTimeMillis();
		}
	}
}
