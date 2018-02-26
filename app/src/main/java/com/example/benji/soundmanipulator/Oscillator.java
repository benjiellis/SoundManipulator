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

    private int BUFFER_SIZE;
    private int SAMPLE_RATE;

    AudioTrack track;
    SeekBar freqBar;
    WAVETYPE type;
    private boolean isActive = true;
    ConcurrentLinkedQueue<double[]> output;
    ConcurrentLinkedQueue<double[]> fmMod;

    Oscillator(SeekBar bar, WAVETYPE type, ConcurrentLinkedQueue<double[]> output, ConcurrentLinkedQueue<double[]> fmInput) {
        WaveSpecs spec = new WaveSpecs();
        this.BUFFER_SIZE = spec.getMinimumBufferSize();
        this.SAMPLE_RATE = spec.getRate();
        this.track = new AudioTrack(AudioManager.STREAM_MUSIC, spec.getRate(),
                spec.getChannels(), spec.getFormat(),
                spec.getMinimumBufferSize(), AudioTrack.MODE_STREAM);
        this.freqBar = bar;
        this.type = type;
        this.output = output;
        this.fmMod = fmInput;
    }

    public boolean isActive() {
        return isActive;
    }

    public void end() {
        isActive = false;
    }

    private double sineCalc(MutableDouble currentAngle, double fm) {
        double angleIncrement = (2.0 * Math.PI) * (freqBar.getProgress()+(fm*500)) / SAMPLE_RATE;
        currentAngle.setValue(currentAngle.getValue() + angleIncrement);
        currentAngle.setValue(currentAngle.getValue() % (2 * Math.PI));
        return Math.sin(currentAngle.getValue());
    }

    private double[] getSineBuffer(MutableDouble currentAngle) {
        int bufferSize = AudioTrack.getMinBufferSize(22050,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        //bufferSize *= 2;
        double[] fmBuffer = fmMod.poll();
        if (fmBuffer == null) {fmBuffer = new double[bufferSize];}
        double[] buffer = new double[bufferSize];
        for (int i = 0; i < bufferSize; i++) {
            //Log.d("VALUE", String.valueOf(fmBuffer[i]));
            buffer[i] = sineCalc(currentAngle, fmBuffer[i]);
        }
        return buffer;
    }

    void on() {
        isActive = true;
        track.play();
        MutableDouble currentAngle = new MutableDouble(0);
        if (this.type == WAVETYPE.SINE) {
            while (isActive) {
                if (output.isEmpty()) {
                    //short[] buffer = getSineBuffer(currentAngle);
                    output.offer(getSineBuffer(currentAngle));
                }
            }
        }
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

    @Override
    public void run() {
        this.on();
    }
}

class MutableDouble {

    private double value;

    public MutableDouble(double value) {
        this.value = value;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}