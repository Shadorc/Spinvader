package me.shadorc.spinvader;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

	private Clip clip;
	private FloatControl gainControl;

	public Sound(String name, double gain) {
		//gain : 0 - 1 : loudest
		try {
			clip = AudioSystem.getClip();
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(this.getClass().getResource("/snd/" + name));
			clip.open(audioIn);

			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(todB(gain));
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		clip.start();
	}

	public void stop() {
		clip.stop();
	}

	public void setVolume(int gain) {
		gainControl.setValue(todB(gain));
	}

	private static float todB(double gain) {
		return (float) (Math.log(gain) / Math.log(10.0) * 20.0);
	}

}