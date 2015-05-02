package me.shadorc.spinvader.graphic;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import me.shadorc.spinvader.Sound;

public class Frame {

	private static JFrame frame;
	private static Sound music;

	private static Options options;
	private static Game game;
	private static Menu menu;

	private final static int NORMAL_WIDTH = 1920;
	private final static int NORMAL_HEIGHT = 1080;
	private static float scaleX, scaleY;

	public enum Mode {
		OPTIONS, GAME, MENU;
	}

	public static void main(String[] args) {
		frame = new JFrame("Spinvader");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		options = new Options();
		game = new Game();
		menu = new Menu();

		music = new Sound("B-Complex - Beautiful Lies.wav", 0.25);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		scaleX = (float) (d.getWidth()/NORMAL_WIDTH);
		scaleY = (float) (d.getHeight()/NORMAL_HEIGHT);

		System.err.println(scaleX + " : " + scaleY);

		setPanel(Mode.MENU);

		frame.setUndecorated(true);
		frame.pack();

		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		if(device.isFullScreenSupported()) {
			device.setFullScreenWindow(frame);
		} else {
			JOptionPane.showMessageDialog(null, "Le mode plein ecran n'est pas disponible", "Erreur", JOptionPane.ERROR_MESSAGE);
		}

		frame.setVisible(true);
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
			//TODO: Find better solution
			System.gc(); //Avoid memory leaks when reloading several times the game
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

	public static int getWidth() {
		return frame.getWidth();
	}

	public static int getHeight() {
		return frame.getHeight();
	}

	public static float getScaleX() {
		return scaleX;
	}

	public static float getScaleY() {
		return scaleY;
	}
}
