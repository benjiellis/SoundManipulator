package com.example.benji.soundmanipulator;

import android.media.AudioFormat;
import android.media.AudioTrack;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class ADSR extends Box {
    private Port output;
    private Button trigger;
    private SeekBar attack;
    private SeekBar decay;
    private SeekBar sustain;
    private SeekBar release;
    private boolean postPress;
    private int bufferSize;

    ADSR(SeekBar attack, SeekBar decay, SeekBar sustain, SeekBar release, Button trigger, CableManager manager, Button outputBtn) {
        super(manager);
        this.attack = attack;
        this.decay = decay;
        this.sustain = sustain;
        this.release = release;
        this.trigger = trigger;
        this.bufferSize = AudioTrack.getMinBufferSize(22050,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        this.output = new Port("output", true, manager, outputBtn);
    }

    public Port getOutput() {
        return this.output;
    }

    @Override
    void on() {
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

            output.getLinkBuffer().offer(buffer);
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

}
