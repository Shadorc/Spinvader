package me.shadorc.spinvader;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class Storage {

    private final static File CONFIG_FILE = new File("config.properties");
    private final static Properties PROPERTIES = new Properties();

    public enum Data {
        ANTIALIASING_ENABLE,
        FULLSCREEN_ENABLE,
        RESOLUTION,
        MUSIC_VOLUME,
        SOUND_VOLUME,
        SCORES;
    }

    static {
        try {
            CONFIG_FILE.createNewFile();
        } catch (IOException err) {
            System.err.println("Error while initializing storage file : " + err.getMessage());
            err.printStackTrace();
            System.exit(1);
        }

        if (Storage.getData(Data.FULLSCREEN_ENABLE) == null) {
            Storage.save(Data.FULLSCREEN_ENABLE, true);
        }
        if (Storage.getData(Data.ANTIALIASING_ENABLE) == null) {
            Storage.save(Data.ANTIALIASING_ENABLE, false);
        }
        if (Storage.getData(Data.MUSIC_VOLUME) == null) {
            Storage.save(Data.MUSIC_VOLUME, 50);
        }
        if (Storage.getData(Data.SOUND_VOLUME) == null) {
            Storage.save(Data.SOUND_VOLUME, 50);
        }
        if (Storage.getData(Data.RESOLUTION) == null) {
            Storage.save(Data.RESOLUTION, "1920x1080");
        }
    }

    public static void save(Object data, Object value) {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            PROPERTIES.setProperty(data.toString(), value.toString());
            PROPERTIES.store(output, null);
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    public static String getData(Data data) {
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            PROPERTIES.load(input);
            return PROPERTIES.getProperty(data.toString());
        } catch (IOException err) {
            err.printStackTrace();
        }
        return null;
    }

    public static List<Integer> getScores() {
        String scoresStr = Storage.getData(Data.SCORES);
        List<Integer> scoresList = new ArrayList<>();
        if (scoresStr != null) {
            String[] scoresArray = scoresStr.replaceAll("\\[", "").replaceAll("\\]", "").split(", ");
            for (String scoreStr : scoresArray) {
                scoresList.add(Integer.parseInt(scoreStr));
            }
        }
        return scoresList;
    }

    public static void addScore(int score) {
        List<Integer> scoresList = Storage.getScores();
        scoresList.add(score);
        Collections.sort(scoresList);
        Collections.reverse(scoresList);
        scoresList = scoresList.subList(0, Math.min(5, scoresList.size()));
        Storage.save(Data.SCORES, scoresList);
    }

    public static boolean isEnable(Data data) {
        return Boolean.parseBoolean(Storage.getData(data));
    }
}
