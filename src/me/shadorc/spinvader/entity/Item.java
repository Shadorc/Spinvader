package me.shadorc.spinvader.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Game;
import me.shadorc.spinvader.graphic.Sprite;

public class Item implements Entity {

	private ImageIcon img;

	private Game game;

	private float x, y;
	private float speed;

	Item(float x, float y, Game game) {
		img = Sprite.resize(Sprite.getSprite("/img/dollar.png"), 50, 50);

		this.x = x;
		this.y = y;
		this.speed = 5;
		this.game = game;
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
			game.increaseMoney(10);
		}
	}

	@Override
	public void drawHitbox(Graphics g) {
		Rectangle re = this.getHitbox();
		g.setColor(new Color(1f, 0f, 0f, 0.5f));
		g.drawRect((int) re.getX(), (int) re.getY(), (int) re.getWidth(), (int) re.getHeight());
	}

	@Override
	public void move(double delta) {
		y += (float) ((speed * delta) / 30);

		if(y >= Frame.getScreenHeight()) {
			game.removeEntity(this);
		}
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
