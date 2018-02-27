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

    private AudioTrack track;
    private SeekBar freqBar;
    private SeekBar volBar;
    private WAVETYPE type;
    private boolean isActive = true;
//    ConcurrentLinkedQueue<double[]> output;
//    ConcurrentLinkedQueue<double[]> fmMod;
    private Cable output;
    private Cable fmMod;
    private Cable volMod;

    public void setOutputCable(Cable out) {
        if (out.isInputTaken()) {
            Log.d("AV", "Cable already has input assigned");
            return;
        }
        this.output = out;
    }

    public void setFMCable(Cable in) {
        if (in.isOutputTaken()) {
            Log.d("AV", "Cable already has output assigned");
            return;
        }
        this.fmMod = in;
    }

    public void setFMCable() {
        this.fmMod.setOutputTaken(false);
        this.fmMod = new Cable();
    }

    public void setOutputCable() {
        this.output.setInputTaken(false);
        this.output = new Cable();
    }

    public void setVolModCable(Cable in) {
        if (in.isOutputTaken()) {
            Log.d("AV", "Cable already has output assigned");
            return;
        }
        this.volMod = in;
    }

    public void setVolModCable() {
        this.volMod.setOutputTaken(false);
        this.volMod = new Cable();
    }

    Oscillator(SeekBar freqBar, SeekBar volBar, WAVETYPE type, Cable output, Cable fmInput, Cable volMod) {
        WaveSpecs spec = new WaveSpecs();
        this.BUFFER_SIZE = spec.getMinimumBufferSize();
        this.SAMPLE_RATE = spec.getRate();
        this.track = new AudioTrack(AudioManager.STREAM_MUSIC, spec.getRate(),
                spec.getChannels(), spec.getFormat(),
                spec.getMinimumBufferSize(), AudioTrack.MODE_STREAM);
        this.freqBar = freqBar;
        this.volBar = volBar;
        this.type = type;
        this.output = output;
        this.fmMod = fmInput;
        this.volMod = volMod;
    }

    Oscillator(SeekBar freqBar, SeekBar volBar) {
        WaveSpecs spec = new WaveSpecs();
        this.BUFFER_SIZE = spec.getMinimumBufferSize();
        this.SAMPLE_RATE = spec.getRate();
        this.track = new AudioTrack(AudioManager.STREAM_MUSIC, spec.getRate(),
                spec.getChannels(), spec.getFormat(),
                spec.getMinimumBufferSize(), AudioTrack.MODE_STREAM);
        this.freqBar = freqBar;
        this.volBar = volBar;
        this.type = WAVETYPE.SINE;
        this.output = new Cable();
        this.fmMod = new Cable();
        this.volMod = new Cable();
    }

    public boolean isActive() {
        return isActive;
    }

    public void end() {
        isActive = false;
    }

    private double sineCalc(MutableDouble currentAngle, double fm) {
        double angleIncrement = (2.0 * Math.PI) * (freqBar.getProgress()+(fm*600)) / SAMPLE_RATE;
        currentAngle.setValue(currentAngle.getValue() + angleIncrement);
        currentAngle.setValue(currentAngle.getValue() % (2 * Math.PI));
        return Math.sin(currentAngle.getValue());
    }

    private double[] getSineBuffer(MutableDouble currentAngle) {
        int bufferSize = AudioTrack.getMinBufferSize(22050,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        //bufferSize *= 2;
        double[] fmBuffer = fmMod.getBuffer().poll();
        if (fmBuffer == null) {fmBuffer = new double[bufferSize];}
        double[] buffer = new double[bufferSize];
        double volume = volBar.getProgress() / 100.0;
        for (int i = 0; i < bufferSize; i++) {
            //Log.d("VALUE", String.valueOf(fmBuffer[i]));
            buffer[i] = sineCalc(currentAngle, fmBuffer[i]) * volume;
        }
        return buffer;
    }

    void on() {
        isActive = true;
        track.play();
        MutableDouble currentAngle = new MutableDouble(0);
        if (this.type == WAVETYPE.SINE) {
            while (isActive) {
                if (output.getBuffer().isEmpty()) {
                    //short[] buffer = getSineBuffer(currentAngle);
                    output.getBuffer().offer(getSineBuffer(currentAngle));
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