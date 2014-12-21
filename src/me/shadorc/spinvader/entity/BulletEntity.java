package me.shadorc.spinvader.entity;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Game;
import me.shadorc.spinvader.graphic.Sprite;

public class BulletEntity implements Entity {

	private float x, y;
	private float speed;
	private ImageIcon img;
	private Direction dir;
	private Type type;
	private Game game;

	BulletEntity(float x, float y, Direction dir, float speed, Type type, Game game) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.speed = speed;
		this.type = type;
		this.game = game;

		img = Sprite.getSprite("/img/bullet.png");
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
	public Image getImage() {
		return img.getImage();
	}

	public Type getType() {
		return type;
	}

	@Override
	public void move(double delta) {
		if(dir == Direction.UP) {
			y -= (speed * delta) / 30;
		} else if(dir == Direction.DOWN) {
			y += (speed * delta) / 30;
		}

		if(y <= 0 || y >= Frame.getScreenHeight()) {
			game.removeEntity(this);
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
