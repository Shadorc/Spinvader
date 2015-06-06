package me.shadorc.spinvader.entity;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Sprite;

public class Item implements Entity {

	private float x, y;
	private float speed;

	private int fireMode;

	private Bonus type;
	private ImageIcon img;

	Item(float x, float y, Bonus type) {
		this.x = x;
		this.y = y;
		this.type = type;

		this.speed = 5;

		if(type == Bonus.LIFE) {
			img = Sprite.get("life.png", 50, 50);
		}
		else if(type == Bonus.FIREMODE) {
			this.fireMode = Frame.getGame().getSpaceship().getFireMode()+1;
			img = Sprite.get("firemode_" + fireMode + ".png", 50, 50);
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
			Frame.getGame().delEntity(this);
			if(type == Bonus.LIFE){
				Frame.getGame().getSpaceship().heal(1);
				Sound.play("life.wav", 0.20);
			}
			else if(type == Bonus.FIREMODE) {
				Frame.getGame().getSpaceship().setFireMode(fireMode);
			}
		}
	}

	@Override
	public void move(double delta) {
		y += (float) ((speed * delta) / 30);

		if(y >= Frame.getHeight()) {
			Frame.getGame().delEntity(this);
		}
	}

	@Override
	public void shoot() {	}
}
