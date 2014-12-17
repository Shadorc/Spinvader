package me.shadorc.spinvader.element;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public interface Element {

	public enum Direction {
		UP, DOWN, RIGHT, LEFT;
	}

	public float getX();
	public float getY();
	public float getLife();

	public Rectangle getHitbox();
	public Image getImage();
	
	public void drawHitbox(Graphics g);

	public void shoot();
	public void hit();
}