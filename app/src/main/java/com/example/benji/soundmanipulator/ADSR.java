package com.example.benji.soundmanipulator;


import android.widget.SeekBar;

public class ADSR extends Box {

    Port output;
    SeekBar attack;
    SeekBar decay;
    SeekBar sustain;
    SeekBar release;

    ADSR(SeekBar a, SeekBar d, SeekBar s, SeekBar r) {
        super();
        this.attack = a;
        this.decay = d;
        this.sustain = s;
        this.release = r;
    }

    @Override
    public void on() {

        while(isActive()) {

        }
    }
}
