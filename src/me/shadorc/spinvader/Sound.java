package me.shadorc.spinvader;

import me.shadorc.spinvader.Storage.Data;

import javax.sound.sampled.*;
import java.io.IOException;

public class Sound {

    private final String name;
    private FloatControl gainControl;
    private Clip clip;

    public Sound(String name, double gain, Data volumeType) {
        this.name = name;

        try {
            clip = AudioSystem.getClip();
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(this.getClass().getResource("/snd/" + name));
            clip.open(audioIn);

            gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            this.setGain(Integer.parseInt(Storage.getData(volumeType)) * gain / 100);

            // Close sound thread when the sound finished
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent evt) {
                    if (evt.getType() == LineEvent.Type.STOP) {
                        evt.getLine().close();
                    }
                }
            });
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException err) {
            err.printStackTrace();
        }
    }

    public static void play(String name, double gain, Data volumeType) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Sound(name, gain, volumeType).start();
            }
        }).start();
    }

    public void start() {
        clip.start();
    }

    public void stop() {
        clip.stop();
    }

    public void restart() {
        if (!clip.isOpen()) {
            try {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(this.getClass().getResource("/snd/" + name));
                clip.open(audioIn);
            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException err) {
                err.printStackTrace();
            }
        }
        this.start();
    }

    public boolean isPlaying() {
        return clip.isRunning();
    }

    public void setGain(double gain) {
        gainControl.setValue((float) (Math.log(gain) / Math.log(10.0) * 20.0));
    }
}