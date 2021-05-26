package me.shadorc.spinvader;

import me.shadorc.spinvader.Storage.Data;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Game;
import me.shadorc.spinvader.graphic.Menu;
import me.shadorc.spinvader.graphic.Options;

import javax.swing.*;

public class Main {

    protected static Frame frame;

    protected static Menu menu;
    protected static Options options;
    protected static Game game;

    public enum Mode {
        OPTIONS, GAME, MENU;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame = new Frame();

                menu = new Menu();
                options = new Options();

                frame.setPanel(menu);
                frame.setFullscreen(Storage.isEnable(Data.FULLSCREEN_ENABLE));
            }
        });
    }

    public static void setMode(Mode mode) {
        switch (mode) {
            case MENU:
                frame.setPanel(menu);
                break;
            case OPTIONS:
                frame.setPanel(options);
                break;
            case GAME:
                game = new Game();
                frame.setPanel(game);
                game.start();
                break;
        }
    }

    public static Frame getFrame() {
        return frame;
    }

    public static Game getGame() {
        return game;
    }
}
