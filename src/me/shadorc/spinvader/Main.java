package me.shadorc.spinvader;

import java.io.IOException;

import javax.swing.SwingUtilities;

import me.shadorc.spinvader.Storage.Data;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Game;
import me.shadorc.spinvader.graphic.Menu;
import me.shadorc.spinvader.graphic.Options;

public class Main {

	private static Frame frame;

	private static Menu menu;
	private static Options options;
	private static Game game;

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
				frame = new Frame();

				menu = new Menu();
				options = new Options();
				game = new Game();

				frame.setPanel(menu);
				frame.setFullscreen(Storage.isEnable(Data.FULLSCREEN_ENABLE));
			}
		});
	}

	public static void setMode(Mode mode) {
		if(mode == Mode.MENU){
			frame.setPanel(menu);
		}

		else if(mode == Mode.OPTIONS) {
			frame.setPanel(options);
		} 

		else if(mode == Mode.GAME) {
			game = new Game();
			frame.setPanel(game);
			game.start();
		} 
	}

	public static Frame getFrame() {
		return frame;
	}

	public static Game getGame() {
		return game;
	}

}
