package com.example.benji.soundmanipulator;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

enum WAVETYPE {
    SINE, SAW, SQUARE
}

public class Oscillator implements Runnable {
    AudioTrack track;
    SeekBar freqBar;
    WAVETYPE type;
    boolean isActive = true;
    ConcurrentLinkedQueue<short[]> output;

    Oscillator(SeekBar bar, WAVETYPE type, ConcurrentLinkedQueue<short[]> output) {
        WaveSpecs spec = new WaveSpecs();
        this.track = new AudioTrack(AudioManager.STREAM_MUSIC, spec.getRate(),
                spec.getChannels(), spec.getFormat(),
                spec.getMinimumBufferSize(), AudioTrack.MODE_STREAM);
        this.freqBar = bar;
        this.type = type;
        this.output = output;
    }

    public void end() {
        isActive = false;
    }

    short[] sineCalc(int freq, double[] currentAngle) {
        double angleIncrement = (2.0 * Math.PI) * freq / 44100;
        currentAngle[0] += angleIncrement;
        currentAngle[0] = currentAngle[0] % (2.0 * Math.PI);
        double b = Math.sin(currentAngle[0]);
        short s = (short) (b * Short.MAX_VALUE);
        short[] mBuffer = new short[1];
        mBuffer[0] = s;
        return mBuffer;
    }

    short[] sawCalc(int freq, double[] currentAmp, int i) {
        freq = freqBar.getProgress();
        double del = (2*(i%(44100/freq))/(44100/freq)-1);
        int j = i - 1;
        double del1 = (2*(j%(44100/freq))/(44100/freq)-1);
        currentAmp[0] += del - del1;
        short s = (short) (currentAmp[0] * Short.MAX_VALUE);
        short[] mBuffer = new short[1];
        mBuffer[0] = s;
        return mBuffer;
    }

    void on() {
        isActive = true;
        track.play();
        int i = 0;
        if (this.type == WAVETYPE.SINE) {
            double[] currentAngle = {0};
            while (isActive) {
                int freq = freqBar.getProgress();
                short[] mBuffer = sineCalc(freq, currentAngle);
                // track.write(mBuffer, 0 ,1);
                output.offer(mBuffer);
            }
        } else if (this.type == WAVETYPE.SAW) {
            double[] currentAmp = {0};
            while (isActive) {
                int freq = freqBar.getProgress();
                short[] mBuffer = sawCalc(freq, currentAmp, i);
                // track.write(mBuffer, 0 ,1);
                boolean success = output.add(mBuffer);
                i++;
            }
        }
    }

    @Override
    public void run() {
        this.on();
    }
}
