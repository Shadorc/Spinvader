package me.shadorc.spinvader;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Storage {

	private static final File DATA_FILE = new File("scores.dat");

	private static void initHistory() {
		ObjectOutputStream objOut = null;
		try {
			DATA_FILE.createNewFile();

			objOut = new ObjectOutputStream(new FileOutputStream(DATA_FILE));
			objOut.writeObject(new Integer[0]);

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			try {
				if(objOut != null) objOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void addScore(int score) {
		if(!DATA_FILE.exists()) initHistory();

		ObjectInputStream objIn = null;
		ObjectOutputStream objOut = null;
		try {
			objIn = new ObjectInputStream(new FileInputStream(DATA_FILE));
			ArrayList <Integer> scores = new ArrayList <Integer> (Arrays.asList((Integer[]) objIn.readObject()));

			scores.add(score);
			Collections.sort(scores);
			Collections.reverse(scores);
			scores = new ArrayList <Integer> (scores.subList(0, (scores.size() > 5) ? 5 : scores.size()));

			objOut = new ObjectOutputStream(new FileOutputStream(DATA_FILE));
			objOut.writeObject(scores.toArray(new Integer[scores.size()]));

		} catch (EOFException ignore) {
			//End of file, ignore it

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();

		} finally {
			try {
				if(objIn != null) objIn.close();
				if(objOut != null) objOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static ArrayList<Integer> getScores() {
		if(!DATA_FILE.exists())	initHistory();

		ArrayList <Integer> scores = new ArrayList<Integer>();

		ObjectInputStream objIn = null;
		try {
			objIn = new ObjectInputStream(new FileInputStream(DATA_FILE));
			scores = new ArrayList <Integer> (Arrays.asList((Integer[]) objIn.readObject()));

		} catch (EOFException ignore) {
			//End of file, ignore it

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();

		} finally {
			try {
				if(objIn != null) objIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return scores;
	}
}
