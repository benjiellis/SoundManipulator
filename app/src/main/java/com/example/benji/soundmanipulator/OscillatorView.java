package com.example.benji.soundmanipulator;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

public class OscillatorView extends ConstraintLayout {

    public Switch getPower() {
        return power;
    }
    public void setPower(Switch power) {
        this.power = power;
    }

    public PortButton getOutputBtn() {
        return outputBtn;
    }
    public void setOutputBtn(PortButton outputBtn) {
        this.outputBtn = outputBtn;
    }

    public PortButton getFreqBtn() {
        return freqBtn;
    }
    public void setFreqBtn(PortButton freqBtn) {
        this.freqBtn = freqBtn;
    }

    public PortButton getVolt() {
        return volt;
    }
    public void setVolt(PortButton volt) {
        this.volt = volt;
    }

    public SeekBar getVolume() {
        return volume;
    }
    public void setVolume(SeekBar volume) {
        this.volume = volume;
    }

    public SeekBar getFrequency() {
        return frequency;
    }
    public void setFrequency(SeekBar frequency) {
        this.frequency = frequency;
    }

    public SeekBar getFreqMod() {
        return freqMod;
    }
    public void setFreqMod(SeekBar freqMod) {
        this.freqMod = freqMod;
    }

    public Switch getType() {
        return type;
    }
    public void setType(Switch type) {
        this.type = type;
    }

    public Switch getSpare() {
        return spare;
    }
    public void setSpare(Switch spare) {
        this.spare = spare;
    }

    public Oscillator getOsc() {
        return osc;
    }
    public void setOsc(Oscillator osc) {
        this.osc = osc;
    }

    private Switch power;
    private PortButton outputBtn;
    private PortButton freqBtn;
    private PortButton volt;
    private SeekBar volume;
    private SeekBar frequency;
    private SeekBar freqMod;
    private Switch type;
    private Switch spare;

    private Oscillator osc;

    private Thread thread;

    public OscillatorView(Context context) {
        super(context, null, 0);

        init(context);
    }

    public OscillatorView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);

        init(context);
    }

    public OscillatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.oscillator_view, this);

        // wrap up
        power = (Switch) findViewById(R.id.power);
        outputBtn = (PortButton) findViewById(R.id.output_btn);
        freqBtn = (PortButton) findViewById(R.id.freq_btn);
        volt = (PortButton) findViewById(R.id.volt_btn);
        volume = (SeekBar) findViewById(R.id.volume_bar);
        frequency = (SeekBar) findViewById(R.id.freq_bar);
        freqMod = (SeekBar) findViewById(R.id.freq_mod_bar);
        type = (Switch) findViewById(R.id.type);
        spare = (Switch) findViewById(R.id.spare);

        osc = new Oscillator(frequency, volume, freqMod);

        power.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                if (power.isChecked()) {
                    thread = new Thread(osc);
                    thread.start();
                }
                else {
                    osc.off();
                }

            }
        });

        type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                osc.switchType();

            }
        });


        outputBtn.setPort(osc.getOutput());
        freqBtn.setPort(osc.getFM());
        volt.setPort(osc.getVolt());
        outputBtn.setOnClickListener(new PortOnClickListener(osc.getOutput(), outputBtn));
        freqBtn.setOnClickListener(new PortOnClickListener(osc.getFM(), freqBtn));
        volt.setOnClickListener(new PortOnClickListener(osc.getVolt(), volt));

    }
}
