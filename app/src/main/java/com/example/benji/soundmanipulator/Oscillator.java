package com.example.benji.soundmanipulator;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.widget.SeekBar;

import java.io.IOException;

enum WAVETYPE {
    SINE, SAW, SQUARE
}

public class Oscillator extends Thread {
    AudioTrack track;
    SeekBar freqBar;
    WAVETYPE type;
    boolean isActive = true;
    PCMStream buffer;

    Oscillator(SeekBar bar, WAVETYPE type, PCMStream output) {
        WaveSpecs spec = new WaveSpecs();
        this.track = new AudioTrack(AudioManager.STREAM_MUSIC, spec.getRate(),
                spec.getChannels(), spec.getFormat(),
                spec.getMinimumBufferSize(), AudioTrack.MODE_STREAM);
        this.freqBar = bar;
        this.type = type;
    }

    public void end() {
        isActive = false;
    }

    public void run() {
        isActive = true;
        track.play();
        int i = 0;
        if (this.type == WAVETYPE.SINE) {
            double currentAngle = 0;
            while (isActive) {
                int freq = freqBar.getProgress();
                double angleIncrement = (2.0 * Math.PI) * freq / 44100;
                currentAngle += angleIncrement;
                currentAngle = currentAngle % (2.0 * Math.PI);
                double b = Math.sin(currentAngle);
                short s = (short) (b * Short.MAX_VALUE);
                short[] mBuffer = new short[1];
                mBuffer[0] = s;
                track.write(mBuffer, 0 ,1);
                i++;
            }
        } else if (this.type == WAVETYPE.SAW) {
            double currentAmp = 0;
            while (isActive) {
                int freq = freqBar.getProgress();
                double del = (2*(i%(44100/freq))/(44100/freq)-1);
                int j = i - 1;
                double del1 = (2*(j%(44100/freq))/(44100/freq)-1);
                currentAmp += del - del1;
                short s = (short) (currentAmp * Short.MAX_VALUE);
                short[] mBuffer = new short[1];
                mBuffer[0] = s;
                try {
                    buffer.in(s);
                }
                catch (IOException e) {
                    // do not insert if full, this code is clumsy, refactor so its not needed
                }
                i++;
            }
        }

    }
}
