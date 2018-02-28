package com.example.benji.soundmanipulator;

import android.media.AudioFormat;
import android.media.AudioTrack;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class ADSR implements Runnable {
    private Cable output;
    private Button trigger;
    private SeekBar attack;
    private SeekBar decay;
    private SeekBar sustain;
    private SeekBar release;
    private boolean isActive;
    private boolean postPress;
    private int bufferSize;

    ADSR(SeekBar attack, SeekBar decay, SeekBar sustain, SeekBar release, Button trigger) {
        this.attack = attack;
        this.decay = decay;
        this.sustain = sustain;
        this.release = release;
        this.trigger = trigger;
        this.bufferSize = AudioTrack.getMinBufferSize(22050,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
    }

    public void setOutputCable(Cable out) {
        if (out.isInputTaken()) {
            Log.d("AV", "Cable has input taken already");
        }
        else {
            this.output = out;
        }
    }

    public void setOutputCable() {
        this.output.setInputTaken(false);
        this.output = new Cable();
        this.output.setInputTaken(true);
    }

    public Cable getOutputCable() {
        return this.output;
    }

    @Override
    public void run() {
        // listener for button click then create ADSR envelope
        on();
    }

    private void on() {
        isActive = true;
        double[] buffer = new double[bufferSize];
        int step = 0;
        while(isActive) {
            // create envelope
            // assume ADSR values are constant during processing, at least for now
            for (int i = 0; i < bufferSize; i++) {
                double val = 0;
                if (trigger.isPressed()) {
                    // work to create attack, then decay, then sustain
                    // val = getADS(step);
                    val = 1;
                }
                if (!trigger.isPressed()) {
                    // work to move to release
                    // val = getR(step);
                    val = 0;
                }
                buffer[i] = val;
            }

            output.getBuffer().offer(buffer);
        }
    }

    private double getADS(int step) {
        // break down into attack, decay, sustain functions
        double attack_length = attack.getProgress();
        double decay_length = decay.getProgress();
        double sustain_amount = sustain.getProgress();

        return 0;
    }

    private double getR(int step) {
        // should change postPress value when done to terminate release looping
        double release_length = release.getProgress();
        return 0;
    }

    public void off() {
        isActive = false;
    }
}
