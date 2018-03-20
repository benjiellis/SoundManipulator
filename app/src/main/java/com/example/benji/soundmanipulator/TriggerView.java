package com.example.benji.soundmanipulator;


import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;

public class ADSRView extends BoxView {

    private ADSR adsr;

    public ADSRView(Context context) {
        super(context, null, 0);
    }

    public ADSRView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public ADSRView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private SeekBar attack;
    private SeekBar decay;
    private SeekBar sustain;
    private SeekBar release;
    private Button trigger;
    private PortButton outputBtn;

    @Override
    public void init(Context context) {

        inflate(context, R.layout.adsr_view, this);
        this.setPower((Switch) findViewById(R.id.power));
        this.outputBtn = (PortButton) findViewById(R.id.output);
        this.trigger = (Button) findViewById(R.id.trigger);
        this.attack = (SeekBar) findViewById(R.id.attack);
        this.decay = (SeekBar) findViewById(R.id.decay);
        this.sustain = (SeekBar) findViewById(R.id.sustain);
        this.release = (SeekBar) findViewById(R.id.release);

        adsr = new ADSR(attack, decay, sustain, release, trigger);
        this.setBox(adsr);

        outputBtn.setPort(adsr.getOutput());
        outputBtn.setOnClickListener(new PortOnClickListener(adsr.getOutput(), outputBtn));
    }

}
