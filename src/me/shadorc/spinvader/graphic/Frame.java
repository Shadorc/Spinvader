package me.shadorc.spinvader.graphic;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Frame {

	private static JFrame frame;

	private static Options options;
	private static Game game;
	private static Menu menu;

	public enum Mode {
		OPTIONS, GAME, MENU;
	}

	public static void main(String[] args) {
		frame = new JFrame("Spinvader");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		options = new Options();
		game = new Game();
		menu = new Menu();

		frame.setContentPane(menu);
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
		frame.revalidate();

		if(mode == Mode.OPTIONS) {
			frame.setContentPane(options);
		} else if(mode == Mode.GAME) {
			//TODO: Find better solution
			System.gc(); //Avoid memory leaks when reloading several times the game
			game = new Game();
			frame.setContentPane(game);
			game.start();
		} else if(mode == Mode.MENU){
			frame.setContentPane(menu);
		}

		frame.getContentPane().requestFocus();
		frame.getContentPane().revalidate();
		frame.repaint();
	}

	public static int getWidth() {
		return frame.getWidth();
	}

	public static int getHeight() {
		return frame.getHeight();
	}
}
