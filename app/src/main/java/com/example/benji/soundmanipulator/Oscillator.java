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

public class Oscillator extends Box {

    private int BUFFER_SIZE;
    private int SAMPLE_RATE;

    private AudioTrack track;
    private SeekBar freqBar;
    private SeekBar volBar;
    private WAVETYPE type;
    private Port output;
    private Port freqMod;
    private SeekBar freqModAmount;

    public Port getOutput() {
        return this.output;
    }

    public Port getFM() {
        return this.freqMod;
    }

    public void switchType() {
        if (type == WAVETYPE.SINE) { type = WAVETYPE.SAW; }
        else { type = WAVETYPE.SINE; }
    }


    Oscillator(SeekBar freqBar, SeekBar volBar, SeekBar freqModAmount) {
        WaveSpecs spec = new WaveSpecs();
        this.BUFFER_SIZE = spec.getMinimumBufferSize();
        this.SAMPLE_RATE = spec.getRate();
        this.track = new AudioTrack(AudioManager.STREAM_MUSIC, spec.getRate(),
                spec.getChannels(), spec.getFormat(),
                spec.getMinimumBufferSize(), AudioTrack.MODE_STREAM);
        this.freqBar = freqBar;
        this.volBar = volBar;
        this.type = WAVETYPE.SAW;
        this.output = new Port(true);
        this.freqMod = new Port(false);
        this.freqModAmount = freqModAmount;
    }

    private double sineCalc(MutableDouble currentAngle, double fm) {
        double angleIncrement = (2.0 * Math.PI) *
                (freqBar.getProgress()+(fm*freqModAmount.getProgress())) / SAMPLE_RATE;
        currentAngle.setValue(currentAngle.getValue() + angleIncrement);
        currentAngle.setValue(currentAngle.getValue() % (2 * Math.PI));
        return Math.sin(currentAngle.getValue());
    }

    private double[] getSineBuffer(MutableDouble currentAngle) {
        int bufferSize = AudioTrack.getMinBufferSize(22050,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        //bufferSize *= 2;

        double[] fmBuffer = freqMod.getLink().getBuffer().poll();
        if (fmBuffer == null) {fmBuffer = new double[bufferSize];}

        double[] buffer = new double[bufferSize];
        double volume = (volBar.getProgress() / 100.0);

        for (int i = 0; i < bufferSize; i++) {
            //Log.d("VALUE", String.valueOf(fmBuffer[i]));
            buffer[i] = limit(sineCalc(currentAngle, fmBuffer[i]) * volume);
        }
        return buffer;
    }

    @Override
    void on() {
        track.play();
        MutableDouble currentAngle = new MutableDouble(0);

        while (this.isActive()) {
            if (this.type == WAVETYPE.SINE) {
                if (output.getLink().getBuffer().isEmpty()) {
                    //short[] buffer = getSineBuffer(currentAngle);
                    output.getLink().getBuffer().offer(getSineBuffer(currentAngle));
                }
            }

            if (this.type == WAVETYPE.SAW) {
                if (output.getLink().getBuffer().isEmpty()) {
                    //short[] buffer = getSineBuffer(currentAngle);
                    output.getLink().getBuffer().offer(getSawBuffer(currentAngle));
                }
            }
        }


    }

    private double[] getSawBuffer(MutableDouble currentAngle) {
        int bufferSize = AudioTrack.getMinBufferSize(22050,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        //bufferSize *= 2;

        double[] fmBuffer = freqMod.getLink().getBuffer().poll();
        if (fmBuffer == null) {fmBuffer = new double[bufferSize];}

        double[] buffer = new double[bufferSize];
        double volume = (volBar.getProgress() / 100.0);

        for (int i = 0; i < bufferSize; i++) {
            //Log.d("VALUE", String.valueOf(fmBuffer[i]));
            buffer[i] = limit(sawCalc(currentAngle, fmBuffer[i]) * volume);
        }
        return buffer;
    }

    private double sawCalc(MutableDouble currentAmp, double fm) {
        double increment = (freqBar.getProgress()+(fm*freqModAmount.getProgress())) / SAMPLE_RATE;
        currentAmp.setValue(currentAmp.getValue() + increment);
        currentAmp.setValue(currentAmp.getValue() % 2);
        return (currentAmp.getValue() - 1);
    }


    private double limit(double b) {
        if (b > 1) { return 1; }
        if (b < -1) { return -1; }
        else { return b; }
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