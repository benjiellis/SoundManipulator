package com.example.benji.soundmanipulator;


import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;

public class AudioInterfaceView extends ConstraintLayout {

    public Switch getPower() {
        return power;
    }

    public void setPower(Switch power) {
        this.power = power;
    }

    public PortButton getInput1Btn() {
        return input1Btn;
    }

    public void setInput1Btn(PortButton input1Btn) {
        this.input1Btn = input1Btn;
    }

    public PortButton getInput2Btn() {
        return input2Btn;
    }

    public void setInput2Btn(PortButton input2Btn) {
        this.input2Btn = input2Btn;
    }

    public SeekBar getVolumeBar() {
        return volumeBar;
    }

    public void setVolumeBar(SeekBar volumeBar) {
        this.volumeBar = volumeBar;
    }

    public AudioInterface getAmp() {
        return amp;
    }

    public void setAmp(AudioInterface amp) {
        this.amp = amp;
    }

    private Switch power;
    private PortButton input1Btn;
    private PortButton input2Btn;
    private SeekBar volumeBar;

    private AudioInterface amp;

    private Thread thread;

    public AudioInterfaceView(Context context) {
        super(context, null, 0);

        init(context);
    }

    public AudioInterfaceView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);

        init(context);
    }

    public AudioInterfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.audio_interface_view, this);
        this.power = (Switch) findViewById(R.id.power);
        this.volumeBar = (SeekBar) findViewById(R.id.volume);
        this.input1Btn = (PortButton) findViewById(R.id.input1_btn);
        this.input2Btn = (PortButton) findViewById(R.id.input2_btn);

        amp = new AudioInterface(volumeBar);

        power.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                if (power.isChecked()) {
                    thread = new Thread(amp);
                    thread.start();
                }
                else {
                    amp.off();
                }

            }
        });

        input1Btn.setPort(amp.getInput1());
        input2Btn.setPort(amp.getInput2());
        input1Btn.setOnClickListener(new PortOnClickListener(amp.getInput1(), input1Btn));

        input2Btn.setOnClickListener(new PortOnClickListener(amp.getInput2(), input2Btn));

    }
}
