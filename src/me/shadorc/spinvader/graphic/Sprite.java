package me.shadorc.spinvader.graphic;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Sprite {

	public static ImageIcon generateSprite(int level) {

		BufferedImage img = null;

		//FIXME: Launch boss when no image available anymore

		//10px is the separation between two sprites
		try {
			img = ImageIO.read(new File(Sprite.class.getResource("/img/invaders1.png").toURI()));
			img = img.getSubimage(img.getWidth() - 10*level - 55*level, 0, 55, 40);
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
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