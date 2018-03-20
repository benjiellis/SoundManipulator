package com.example.benji.soundmanipulator;


import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;

public class Trigger extends Box {

    private Port output;
    private Button trigger;

    public Port getOutput() { return this.output; }

    Trigger(Button t) {
        super();
        this.trigger = t;
        this.output = new Port(true);
    }

    @Override
    public void on() {
        while(isActive()) {
            MutableDouble amp = new MutableDouble(0.0);
            if (output.getLink().getBuffer().isEmpty()) {
                output.getLink().getBuffer().offer(getTriggerBuffer(amp));
            }
        } // end isActive while
    }// end on()

    private double[] getTriggerBuffer(MutableDouble ampValue) {
        double[] buffer = new double[getMinBufferSize()];

        for (int i = 0; i < getMinBufferSize(); i++) {

            // check if still in attack and button still pressed down
            if (trigger.isPressed()) {
                buffer[i] = 1;
            }
            else {
                buffer[i] = 0;
            }
        }

        return buffer;
    }

}
