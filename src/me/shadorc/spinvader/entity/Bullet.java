package me.shadorc.spinvader.entity;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Game;
import me.shadorc.spinvader.graphic.Sprite;

public class Bullet implements Entity {

	private float x, y;
	private float speed;
	private ImageIcon img;
	private Direction dir;
	private Type type;

	Bullet(float x, float y, Direction dir, float speed, Type type) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.speed = speed;
		this.type = type;

		img = Sprite.get("bullet.png");
	}

	@Override
	public int getX() {
		return (int) x;
	}

	@Override
	public int getY() {
		return (int) y;
	}

	@Override
	public Image getImage() {
		return img.getImage();
	}

	public Type getType() {
		return type;
	}

	@Override
	public void move(double delta) {
		y += (speed * delta) / 30 * (dir == Direction.DOWN ? 1 : -1);

		if(y <= 0 || y >= Frame.getHeight()) {
			Game.removeEntity(this);
		}
	}

	@Override
	public Rectangle getHitbox() {
		return new Rectangle((int) x, (int) y, img.getIconWidth(), img.getIconHeight());
	}

	@Override
	public float getLife() {
		return 0;
	}

	@Override
	public void shoot() { }

	@Override
	public void collidedWith(Entity en) { }
}
