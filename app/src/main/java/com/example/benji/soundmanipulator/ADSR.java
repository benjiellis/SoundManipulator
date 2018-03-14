package com.example.benji.soundmanipulator;


import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;

public class ADSR extends Box {

    private Port output;
    private SeekBar attack;
    private SeekBar decay;
    private SeekBar sustain;
    private SeekBar release;
    private Button trigger;

    public Port getOutput() { return this.output; }

    ADSR(SeekBar a, SeekBar d, SeekBar s, SeekBar r, Button t) {
        super();
        this.attack = a;
        this.decay = d;
        this.sustain = s;
        this.release = r;
        this.trigger = t;
        this.output = new Port(true);
    }

    @Override
    public void on() {
        while(isActive()) {
            if (output.getLink().getBuffer().isEmpty()) {
                output.getLink().getBuffer().offer(getADSRbuffer());
            }
        } // end isActive while
    }// end on()

    private double[] getADSRbuffer() {
        double[] buffer = new double[getMinBufferSize()];
        for (int i = 0; i < getMinBufferSize(); i++) {
            int t = 0;
            if (trigger.isPressed()) { t = 1; }
            buffer[i] = t;
        }
        return buffer;
    }

}
