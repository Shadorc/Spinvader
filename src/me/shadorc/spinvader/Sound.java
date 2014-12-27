package me.shadorc.spinvader;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

	private Clip clip;
	private FloatControl gainControl;

	public Sound(String name, double gain) {
		//gain : 0 - 1.14 : loudest
		try {
			clip = AudioSystem.getClip();
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(this.getClass().getResource("/snd/" + name));
			clip.open(audioIn);

			gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			this.setGain(gain);

			//Close sound thread when the sound finished
			clip.addLineListener(new LineListener() {
				public void update(LineEvent evt) {
					if(evt.getType() == LineEvent.Type.STOP) {
						evt.getLine().close();
					}
				}
			});
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

	public boolean isPlaying() {
		return clip.isRunning();
	}

	public void setGain(double gain) {
		float volume = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
		gainControl.setValue(volume);
	}
}