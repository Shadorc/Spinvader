package me.shadorc.spinvader.graphic;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.Storage;
import me.shadorc.spinvader.Storage.Data;
import me.shadorc.spinvader.utils.DisplayUtils;

public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final float NORMAL_WIDTH = 1920;
	private static final float NORMAL_HEIGHT = 1080;

	private final Sound music;

	public Frame() {
		super("Spinvader");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		this.music = new Sound("B-Complex - Beautiful Lies.wav", 0.10, Data.MUSIC_VOLUME);

		this.pack();
		this.setMinimumSize(new Dimension(800, 600));
		this.setLocationRelativeTo(null);
	}

	public void setPanel(JPanel panel) {
		this.remove(this.getContentPane());
		this.setContentPane(panel);

		if(panel instanceof Options || panel instanceof Menu) {
			if(!music.isPlaying()) {
				music.restart();
			}
		} else if(panel instanceof Game) {
			music.stop();
		}

		this.getContentPane().setFocusable(true);
		this.getContentPane().requestFocus();
		this.getContentPane().revalidate();
		this.getContentPane().repaint();
	}

	public void setFullscreen(boolean activate) {
		this.dispose();
		this.setUndecorated(activate);
		if(DisplayUtils.SCREEN.isFullScreenSupported()) {
			DisplayUtils.SCREEN.setFullScreenWindow(activate ? this : null);
			if(activate) {
				DisplayUtils.SCREEN.setDisplayMode(DisplayUtils.RESOLUTIONS_MAP.get(Storage.getData(Data.RESOLUTION)));
			}
		} else {
			JOptionPane.showMessageDialog(null, "Le mode plein ecran n'est pas disponible", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
		this.setVisible(true);
	}

	public static float getScaleX() {
		return DisplayUtils.DISPLAY_MODE.getWidth() / NORMAL_WIDTH;
	}

	public static float getScaleY() {
		return DisplayUtils.DISPLAY_MODE.getHeight() / NORMAL_HEIGHT;
	}
}
