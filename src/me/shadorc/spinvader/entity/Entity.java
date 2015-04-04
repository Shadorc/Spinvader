package me.shadorc.spinvader.entity;

import java.awt.Image;
import java.awt.Rectangle;

public interface Entity {

	public enum Direction {
		UP, DOWN, RIGHT, LEFT;
	}

	public enum Type {
		ENEMY, SPACESHIP, BOSS;
	}

	public enum Bonus {
		MONEY, LIFE;
	}

	public int getX();
	public int getY();
	public float getLife();

	public Rectangle getHitbox();
	public Image getImage();

	public void collidedWith(Entity en);
	public void move(double delta);
	public void shoot();
}