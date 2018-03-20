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
    private boolean decaying;

    private int timer;
    private int releaseTimer;

    public Port getOutput() { return this.output; }

    ADSR(SeekBar a, SeekBar d, SeekBar s, SeekBar r, Button t) {
        super();
        this.attack = a;
        this.decay = d;
        this.sustain = s;
        this.release = r;
        this.trigger = t;
        this.output = new Port(true);
        this.decaying = false;
    }

    @Override
    public void on() {
        while(isActive()) {
            MutableDouble amp = new MutableDouble(0.0);
            if (output.getLink().getBuffer().isEmpty()) {
                output.getLink().getBuffer().offer(getADSRbuffer(amp));
            }
        } // end isActive while
    }// end on()

    private double[] getADSRbuffer(MutableDouble ampValue) {
        double[] buffer = new double[getMinBufferSize()];

        double amp = ampValue.getValue();
        double att = attack.getProgress()/3000.0;
        double dec = decay.getProgress()/3000.0;
        double sus = sustain.getProgress()/100.0;
        double rel = release.getProgress()/3000.0;

        double base = 20000.0f;
        double maxTime = 10.0f;

        for (int i = 0; i < getMinBufferSize(); i++) {

            // check if still in attack and button still pressed down
            if (trigger.isPressed()) {
                if (decaying) {
                    // DECAY
                    if (dec < 1e-4) {
                        amp = sus;
                    } else {
                        amp += Math.pow(base, 1 - dec) / maxTime * (sus - amp) * getSampleRate();
                    }
                }
                else {
                    // ATTACK
                    if (att < 1e-4) {
                        amp = 1.0;
                    }
                    else {
                        amp += Math.pow(base, 1 - att) / maxTime * (1.01f - amp) * getSampleRate();
                    }
                    if (amp >= 1.0) {
                        amp = 1.0;
                        decaying = true;
                    }
                }
            } // trigger.isPressed
            else {
                // RELEASE
                if (rel < 1e-4) {
                    amp = 0.0;
                }
                else {
                    amp += Math.pow(base, 1-rel) / maxTime * (0.0f - amp) * getSampleRate();
                }
            }

            buffer[i] = amp;
        }
        ampValue.setValue(amp);
        return buffer;
    }

}
