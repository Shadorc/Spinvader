package me.shadorc.spinvader.sprites;

import me.shadorc.spinvader.utils.RandUtils;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FireEffect extends Effect {

    private final List<FireParticle> particles;

    public FireEffect(float x, float y, int duration) {
        super(x, y, duration);
        this.particles = new CopyOnWriteArrayList<>();
    }

    @Override
    public void update(double delta) {
        for (int i = 0; i < RandUtils.randInt(10); i++) {
            float a = RandUtils.randFloat(0.35f) * (RandUtils.randBool() ? 1 : -1);
            int radius = 10 + RandUtils.randInt(60);
            Color color = RandUtils.randBool() ? Color.RED : Color.ORANGE;
            particles.add(new FireParticle(x, y, a, radius, color, duration));
        }

        for (FireParticle particle : particles) {
            particle.update(delta);
            if (System.currentTimeMillis() - particle.getStart() >= duration) {
                particles.remove(particle);
            }
        }

        // if(System.currentTimeMillis() - start >= duration) {
        // Main.getGame().delEffect(this);
        // }
    }

    @Override
    public void render(Graphics2D g2d) {
        for (FireParticle part : particles) {
            g2d.setColor(part.getColor());
            g2d.fillOval((int) part.getX(), (int) (part.getY() - part.getRadius() / 2), part.getRadius(), part.getRadius());
        }
    }
}
