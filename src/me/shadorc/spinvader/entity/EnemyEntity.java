package me.shadorc.spinvader.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Game;
import me.shadorc.spinvader.graphic.Sprite;
import me.shadorc.spinvader.graphic.Sprite.Type;

public class EnemyEntity implements Entity {

	private float x, y;
	private float speed, shootSpeed;
	private float life;

	private ImageIcon img;
	private Direction dir;
	private static ArrayList <BulletEntity> bullets;

	EnemyEntity(float x, float y, ImageIcon img) {
		this.x = x;
		this.y = y;
		this.img = img;

		speed = 10;
		shootSpeed = 20;
		life = 3;

		dir = Direction.RIGHT;
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
		bullets.add(new BulletEntity(x, y, Direction.DOWN, shootSpeed));
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
			Game.gameOver();
		}
	}

	public void setDirection(Direction dir) {
		this.dir = dir;
	}

	public Direction move() {
		Direction newDir = null;

		if(dir == Direction.RIGHT) {
			x += speed;
		} else if(dir == Direction.LEFT) {
			x -= speed;
		}

		if(x <= 0) {
			newDir = Direction.RIGHT;
		} else if(x >= Frame.getScreenWidth() - img.getIconWidth()) {
			newDir = Direction.LEFT;
		}

		return newDir;
	}

	public static ArrayList <BulletEntity> getBullets() {
		return bullets;
	}

	public static ArrayList <EnemyEntity> generate(int count) {
		ArrayList <EnemyEntity> ennemies = new ArrayList <EnemyEntity> ();
		int x = 0;
		int y = 0;

		for(int i = 1; i < count+1; i++) {
			ennemies.add(new EnemyEntity(10+(100*x), 10+(100*y), Sprite.resize(Sprite.generateSprite(Type.ENNEMY), 100, 100)));
			if(i%12 == 0) {
				y++;
				x = 0;
			} else {
				x++;
			}
		}

		return ennemies;
	}
}