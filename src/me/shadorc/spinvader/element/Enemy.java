package me.shadorc.spinvader.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.graphic.Frame;

public class Enemy implements Element {

	private static ArrayList <Bullet> bullets;

	//	private static Direction dir;

	private float x, y;
	private float speed;
	private float shootSpeed;
	private float life;
	private ImageIcon img;
	private Direction dir;

	Enemy(float x, float y) {
		this.x = x;
		this.y = y;

		speed = 15;
		shootSpeed = 20;
		life = 3;
		img = new ImageIcon(new ImageIcon(this.getClass().getResource("/img/ennemy.png")).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
		dir = Direction.RIGHT;
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
		bullets.add(new Bullet(x, y, Direction.DOWN, shootSpeed));
	}

	@Override
	public void drawHitbox(Graphics g) {
		Rectangle re = this.getHitbox();
		g.setColor(new Color(1f, 0f, 0f, 0.5f));
		g.drawRect((int) re.getX(), (int) re.getY(), (int) re.getWidth(), (int) re.getHeight());
	}

	public void goDown() {
		y += img.getIconHeight();
		if(y >= Frame.getScreenHeight() - img.getIconHeight()) {
			Frame.gameOver();
		}
	}

	public void move() {
		if(x <= 0) {
			dir = Direction.RIGHT;
			goDown();
		} else if(x >= Frame.getScreenWidth() - img.getIconWidth()) {
			dir = Direction.LEFT;
			goDown();
		}

		if(dir == Direction.RIGHT) {
			x += speed;
		} else if(dir == Direction.LEFT) {
			x -= speed;
		}
	}

	public static ArrayList <Bullet> getBullets() {
		return bullets;
	}

	public static ArrayList <Enemy> generate(int count) {
		ArrayList <Enemy> ennemies = new ArrayList <Enemy> ();
		int y = 0;
		int x = 0;

		for(int i = 1; i < count+1; i++) {
			ennemies.add(new Enemy(10+(100*x), 10+(100*y)));
			if(i%19 == 0) {
				y++;
				x = 0;
			} else {
				x++;
			}
		}

		return ennemies;
	}
}