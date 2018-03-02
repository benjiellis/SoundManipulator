package com.example.benji.soundmanipulator;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
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

    public Port getOutput() {
        return output;
    }

    public Port getFreqMod() {
        return freqMod;
    }

    public Port getVolt() {
        return volt;
    }

    private Port output;
    private Port freqMod;
    private Port volt;
    private SeekBar freqModAmount;

    Oscillator(SeekBar freqBar, SeekBar volBar, SeekBar freqModAmount, CableManager manager,
               Button outBtn, Button voltBtn, Button fmBtn) {
        super(manager);
        WaveSpecs spec = new WaveSpecs();
        this.BUFFER_SIZE = spec.getMinimumBufferSize();
        this.SAMPLE_RATE = spec.getRate();
        this.track = new AudioTrack(AudioManager.STREAM_MUSIC, spec.getRate(),
                spec.getChannels(), spec.getFormat(),
                spec.getMinimumBufferSize(), AudioTrack.MODE_STREAM);
        this.freqBar = freqBar;
        this.volBar = volBar;
        this.type = WAVETYPE.SINE;
        this.output = new Port("output", true, manager, outBtn);
        this.freqMod = new Port("freqMod", false, manager, fmBtn);
        this.volt = new Port("volt", false, manager, voltBtn);
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

        double[] fmBuffer = freqMod.getLinkBuffer().poll();
        if (fmBuffer == null) {fmBuffer = new double[bufferSize];}

        double[] buffer = new double[bufferSize];
        double volume = (volBar.getProgress() / 100.0);

        double[] voltBuffer = volt.getLinkBuffer().poll();
        if (voltBuffer == null) {
            voltBuffer = new double[bufferSize];
            for (int i = 0; i < bufferSize; i++) {
                voltBuffer[i] = 1;
            }
        }


        for (int i = 0; i < bufferSize; i++) {
            //Log.d("VALUE", String.valueOf(fmBuffer[i]));
            double vol = volume * voltBuffer[i];
            buffer[i] = limit(sineCalc(currentAngle, fmBuffer[i]) * volume);
        }
        return buffer;
    }

    @Override
    void on() {
        track.play();
        MutableDouble currentAngle = new MutableDouble(0);
        if (this.type == WAVETYPE.SINE) {
            while (isActive) {
                if (output.getLinkBuffer().isEmpty()) {
                    //short[] buffer = getSineBuffer(currentAngle);
                    output.getLinkBuffer().offer(getSineBuffer(currentAngle));
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