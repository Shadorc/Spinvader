package me.shadorc.spinvader.entity;

import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Game;
import me.shadorc.spinvader.graphic.Sprite;

public class Item extends Entity {

	private float speed;
	private int fireMode;
	private Bonus type;

	Item(float x, float y, Bonus type) {
		super(x, y, 0, null);

		this.type = type;
		this.speed = 5;

		if(type == Bonus.LIFE) {
			this.img = Sprite.get("life.png", 50, 50);
		}
		else if(type == Bonus.FIREMODE) {
			this.fireMode = Frame.getGame().getSpaceship().getFireMode()+1;
			this.img = Sprite.get("firemode_" + fireMode + ".png", 50, 50);
		}
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

	public static void generate(float x, float y) {
		int rand = Game.rand(100);
		if(rand == 0) {
			Frame.getGame().addEntity(new Item(x, y, Bonus.LIFE));
		} else if(rand > 95 && Frame.getGame().getSpaceship().getFireMode() < 4) {
			Frame.getGame().addEntity(new Item(x, y, Bonus.FIREMODE));
		}
	}
}
