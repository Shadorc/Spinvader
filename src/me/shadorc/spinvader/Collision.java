package me.shadorc.spinvader;

import me.shadorc.spinvader.element.Element;

public class Collision {

	public static boolean test(Element e1, Element e2) {
		return e1.getX() > e2.getHitbox().getX() 
				&& e1.getX() < e2.getHitbox().getMaxX() 
				&& e1.getY() > e2.getHitbox().getY() 
				&& e1.getY() < e2.getHitbox().getMaxY();
	}
}
