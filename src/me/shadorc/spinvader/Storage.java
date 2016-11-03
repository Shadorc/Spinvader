package me.shadorc.spinvader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

public class Storage {

	private static File CONFIG_FILE = new File("config.properties");
	private static Properties prop = new Properties();

	public enum Data {
		ANTIALIASING_ENABLE,
		FULLSCREEN_ENABLE,
		RESOLUTION,
		MUSIC_VOLUME,
		SOUND_VOLUME,
		SCORES;
	}

	public static void init() throws IOException {
		CONFIG_FILE.createNewFile();
		if(Storage.getData(Data.FULLSCREEN_ENABLE) == null) 	Storage.save(Data.FULLSCREEN_ENABLE, true);
		if(Storage.getData(Data.ANTIALIASING_ENABLE) == null) 	Storage.save(Data.ANTIALIASING_ENABLE, false);
		if(Storage.getData(Data.MUSIC_VOLUME) == null) 			Storage.save(Data.MUSIC_VOLUME, 50);
		if(Storage.getData(Data.SOUND_VOLUME) == null) 			Storage.save(Data.SOUND_VOLUME, 50);
	}

	public static void save(Object data, Object value) {
		OutputStream output = null;

		try {
			output = new FileOutputStream(CONFIG_FILE);

			prop.setProperty(data.toString(), value.toString());
			prop.store(output, null);

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			try {
				if (output != null) output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getData(Data data) {
		InputStream input = null;

		try {
			input = new FileInputStream(CONFIG_FILE);
			prop.load(input);

			return prop.getProperty(data.toString());

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			try {
				if (input != null) input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public static ArrayList<Integer> getScores() {
		String str_scores = Storage.getData(Data.SCORES);
		ArrayList <Integer> list_scores = new ArrayList <Integer> ();
		if(str_scores != null) {
			String[] arr_scores = str_scores.replaceAll("\\[", "").replaceAll("\\]", "").split(", ");
			for(String str_score : arr_scores) {
				list_scores.add(Integer.parseInt(str_score));
			}
		}
		return list_scores;
	}

	public static void addScore(int score) {
		ArrayList <Integer> list_scores = Storage.getScores();
		list_scores.add(score);
		Collections.sort(list_scores);
		Collections.reverse(list_scores);
		list_scores = new ArrayList <Integer> (list_scores.subList(0, Math.min(5, list_scores.size())));
		Storage.save(Data.SCORES, list_scores);
	}

	public static boolean isEnable(Data data) {
		return Boolean.parseBoolean(Storage.getData(data));
	}
}
