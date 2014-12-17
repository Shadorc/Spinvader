package me.shadorc.spinvader.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.graphic.Frame;

public class Bullet implements Element {

	private float x, y;
	private float speed;
	private ImageIcon img;
	private Direction dir;
	private boolean alive;

	Bullet(float x, float y, Direction dir, float speed) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.speed = speed;

		img = new ImageIcon(this.getClass().getResource("/img/bullet.png"));
		alive = true;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public Image getImage() {
		return img.getImage();
	}

	public boolean isAlive() {
		return alive;
	}

	public void move() {
		if(dir == Direction.UP)
			y -= speed;
		else if(dir == Direction.DOWN)
			y += speed;

		if(y <= 0 || y >= Frame.getScreenHeight())
			alive = false;
	}

	public Rectangle getHitbox() {
		return new Rectangle((int) x, (int) y, img.getIconWidth(), img.getIconHeight());
	}

	public void drawHitbox(Graphics g) {
		Rectangle re = this.getHitbox();
		g.setColor(new Color(1f, 0f, 0f, 0.5f));
		g.drawRect((int) re.getX(), (int) re.getY(), (int) re.getWidth(), (int) re.getHeight());
	}

	@Override
	public float getLife() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void shoot() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hit() {
		// TODO Auto-generated method stub

	}
}
