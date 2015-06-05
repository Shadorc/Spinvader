package me.shadorc.spinvader.entity;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Game;
import me.shadorc.spinvader.graphic.Game.Stat;
import me.shadorc.spinvader.graphic.Sprite;

public class Item implements Entity {

	private float x, y;
	private float speed;

	private Bonus type;
	private ImageIcon img;

	Item(float x, float y, Bonus type) {
		this.x = x;
		this.y = y;
		this.type = type;

		this.speed = 5;

		if(type == Bonus.MONEY) {
			img = Sprite.get("dollar.png", 50, 50);
		} else if(type == Bonus.LIFE) {
			img = Sprite.get("life.png", 50, 50);
		}
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
		if(en instanceof Spaceship) {
			Game.delEntity(this);
			if(type == Bonus.MONEY) {
				Game.increase(Stat.MONEY, 10);
				Sound.play("cash.wav", 0.20);
			} else if(type == Bonus.LIFE){
				Game.increase(Stat.LIFE, 1);
				Sound.play("life.wav", 0.20);
			}
		}
	}

	@Override
	public void move(double delta) {
		y += (float) ((speed * delta) / 30);

		if(y >= Frame.getHeight()) {
			Game.delEntity(this);
		}
	}

	@Override
	public void shoot() {	}
}
