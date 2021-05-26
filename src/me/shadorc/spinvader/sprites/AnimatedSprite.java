package me.shadorc.spinvader.sprites;

import javax.swing.*;
import java.awt.*;

public class AnimatedSprite extends Effect {

    private final ImageIcon img;

    public AnimatedSprite(float x, float y, ImageIcon img, int duration) {
        super(x, y, duration);
        this.img = img;
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.drawImage(img.getImage(), (int) x, (int) y, null);
    }
}
