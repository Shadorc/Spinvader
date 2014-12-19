package me.shadorc.spinvader;

import me.shadorc.spinvader.entity.Entity;

public class Collision {

	public static boolean test(Entity e1, Entity e2) {
		return e1.getX() > e2.getHitbox().getX() 
				&& e1.getX() < e2.getHitbox().getMaxX() 
				&& e1.getY() > e2.getHitbox().getY() 
				&& e1.getY() < e2.getHitbox().getMaxY();
	}
}
