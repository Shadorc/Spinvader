package me.shadorc.spinvader.entity;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Game;
import me.shadorc.spinvader.graphic.Sprite;

public class Item implements Entity {

	private Game game;

	private float x, y;
	private float speed;

	private Bonus type;
	private ImageIcon img;

	Item(float x, float y, Bonus type, Game game) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.game = game;

		this.speed = 5;

		if(type == Bonus.MONEY) {
			img = Sprite.get("dollar.png", 50, 50);
		} else if(type == Bonus.LIFE) {
			img = Sprite.get("life.png", 50, 50);
		}
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
		return 0;
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
		if(en instanceof SpaceshipEntity) {
			game.removeEntity(this);
			if(type == Bonus.MONEY) {
				game.increaseMoney(10);
				new Sound("cash.wav", 0.20).start();
			} else if(type == Bonus.LIFE){
				game.increaseLife(1);
				new Sound("life.wav", 0.20).start();
			}
		}
	}

	@Override
	public void move(double delta) {
		y += (float) ((speed * delta) / 30);

		if(y >= Frame.getHeight()) {
			game.removeEntity(this);
		}
	}

	@Override
	public void shoot() {	}
}
