package me.shadorc.spinvader.graphic;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import me.shadorc.spinvader.entity.Entity.Type;

public class Sprite {

	public static ImageIcon generateSprite(Type type) {

		BufferedImage img = null;

		if(type == Type.ENEMY) {
			try {
				img = ImageIO.read(new File(Sprite.class.getResource("/img/invaders.png").toURI()));
				//				img = img.getSubimage(new Random().nextInt(img.getWidth()/69)*69, new Random().nextInt(img.getHeight()/55)*55, 69, 55);
				img = img.getSubimage(0, 0, 69, 55);
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
				img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
			}
		}

		return new ImageIcon(img);
	}

	public static ImageIcon resize(ImageIcon img, int width, int height) {
		return new ImageIcon(img.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
	}

	public static ImageIcon getSprite(String path) {
		return new ImageIcon(Sprite.class.getResource(path));
	}

	public static ImageIcon getSprite(String path, int width, int height) {
		return new ImageIcon(new ImageIcon(Sprite.class.getResource(path)).getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
	}
}