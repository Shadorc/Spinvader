package me.shadorc.spinvader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class Storage {

	private static File file = new File("data");

	public static void saveData(Integer score) {
		BufferedWriter writer = null;
		try {
			ArrayList <Integer> scores = getScores();
			scores.add(score);

			Collections.sort(scores);
			Collections.reverse(scores);

			writer = new BufferedWriter(new FileWriter(file));

			for(int i = 0; i < scores.size() && i < 5; i++) {
				writer.write(scores.get(i) + "\n");
			}

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			try {
				if(writer != null) writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static ArrayList <Integer> getScores() {
		ArrayList <Integer> scores = new ArrayList <Integer> ();

		try {
			file.createNewFile();

			String text = new String(Files.readAllBytes(Paths.get(file.getPath())), StandardCharsets.UTF_8);
			if(!text.isEmpty()) {
				for(String sc : text.split("\n")) {
					scores.add(Integer.parseInt(sc));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		Collections.sort(scores);
		Collections.reverse(scores);
		return scores;
	}
}
