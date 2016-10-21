package me.shadorc.spinvader.graphic;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.Storage;
import me.shadorc.spinvader.Storage.Data;

public class Frame {

	private static JFrame frame;
	private static Sound music;

	private static Options options;
	private static Game game;
	private static Menu menu;

	private final static float NORMAL_WIDTH = 1920;
	private final static float NORMAL_HEIGHT = 1080;

	public enum Mode {
		OPTIONS, GAME, MENU;
	}

	public static void main(String[] args) {
		try {
			Storage.init();
		} catch (IOException e) {
			System.err.println("Error while initializing storage file : " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame = new JFrame("Spinvader");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				options = new Options();
				game = new Game();
				menu = new Menu();

				music = new Sound("B-Complex - Beautiful Lies.wav", 0.10, Data.MUSIC_VOLUME);

				Frame.setPanel(Mode.MENU);

				frame.pack();
				frame.setMinimumSize(new Dimension(800, 600));
				frame.setLocationRelativeTo(null);

				Frame.setFullscreen(Storage.isEnable(Data.FULLSCREEN_ENABLE));
			}
		});
	}

	public static void setPanel(Mode mode) {
		frame.remove(frame.getContentPane());

		if(mode == Mode.OPTIONS) {
			frame.setContentPane(options);
			if(!music.isPlaying()) {
				music.restart();
			}
		} 

		else if(mode == Mode.GAME) {
			music.stop();
			game = new Game();
			frame.setContentPane(game);
			game.start();
		} 

		else if(mode == Mode.MENU){
			frame.setContentPane(menu);
			if(!music.isPlaying()) {
				music.restart();
			}
		}

		frame.getContentPane().setFocusable(true);
		frame.getContentPane().requestFocus();
		frame.getContentPane().revalidate();
		frame.getContentPane().repaint();
	}

	public static Game getGame() {
		return game;
	}

	public static void setFullscreen(boolean activate) {
		frame.dispose();
		frame.setUndecorated(activate);
		if(Frame.getScreen().isFullScreenSupported()) {
			Frame.getScreen().setFullScreenWindow(activate ? frame : null);
		} else {
			JOptionPane.showMessageDialog(null, "Le mode plein ecran n'est pas disponible", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
		frame.setVisible(true);
	}

	public static GraphicsDevice getScreen() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	}

	public static int getWidth() {
		return frame.getWidth();
	}

	public static int getHeight() {
		return frame.getHeight();
	}

	public static float getScaleX() {
		return (float) (Frame.getScreen().getDisplayMode().getWidth()/NORMAL_WIDTH);
	}

	public static float getScaleY() {
		return (float) (Frame.getScreen().getDisplayMode().getHeight()/NORMAL_HEIGHT);
	}
}
