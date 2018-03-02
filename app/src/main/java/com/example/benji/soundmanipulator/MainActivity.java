package com.example.benji.soundmanipulator;

import android.content.DialogInterface;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ConcurrentLinkedQueue;

import static android.widget.SeekBar.*;

public class MainActivity extends AppCompatActivity {

    private static final String RIFF_HEADER = "RIFF";
    private static final String WAVE_HEADER = "WAVE";
    private static final String FMT_HEADER = "fmt ";
    private static final String DATA_HEADER = "data";

    private static final int HEADER_SIZE = 44;

    private static final String CHARSET = "ASCII";

    Oscillator osc1;
    Oscillator osc2;
    AudioInterface amp;

    ADSR adsr1;

    SeekBar osc1_freqBar;
    SeekBar osc2_freqBar;
    Switch osc1_powerSwitch;
    Switch osc2_powerSwitch;
    Switch mixerSwitch;
    Switch osc1_waveTypeSwitch;
    Switch osc2_waveTypeSwitch;
    SeekBar volumeBar;
    SeekBar osc1_volumeBar;
    SeekBar osc2_volumeBar;
    SeekBar osc1_fmModBar;
    SeekBar osc2_fmModBar;

    SeekBar attackBar;
    SeekBar decayBar;
    SeekBar sustainBar;
    SeekBar releaseBar;

    Button adsrOutput;
    Button adsrTrigger;
    Switch adsrPower;

    Button osc1_outBtn;
    Button osc2_outBtn;
    Button osc1_fmBtn;
    Button osc2_fmBtn;
    Button mix_in1Btn;
    Button mix_in2Btn;
    Button osc1_voltBtn;
    Button osc2_voltBtn;

    Thread osc1Thread;
    Thread osc2Thread;
    Thread ampThread;
    Thread adsrThread;

    CableManager manager = new CableManager();

    Cable osc1Cable = new Cable();
    Cable osc2Cable = new Cable();
    Cable spareCable = new Cable();
    Cable spareCable2 = new Cable();

    Port onDeck = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // v importante

        osc1_powerSwitch = (Switch) this.findViewById(R.id.osc1_power);
        osc2_powerSwitch = (Switch) this.findViewById(R.id.osc2_power);
        osc2_freqBar = (SeekBar) this.findViewById(R.id.osc2_freq);
        osc1_freqBar = (SeekBar) this.findViewById(R.id.osc1_freq);
        osc1_volumeBar = (SeekBar) this.findViewById(R.id.osc1_volume);
        osc2_volumeBar = (SeekBar) this.findViewById(R.id.osc2_volume);
        osc1_fmModBar = (SeekBar) this.findViewById(R.id.osc1_fmmod);
        osc2_fmModBar = (SeekBar) this.findViewById(R.id.osc2_fmmod);
        mixerSwitch = (Switch) this.findViewById(R.id.mixer_switch);
        osc2_waveTypeSwitch = (Switch) this.findViewById(R.id.osc2_type);
        osc1_waveTypeSwitch = (Switch) this.findViewById(R.id.osc1_type);
        volumeBar = (SeekBar) this.findViewById(R.id.volume_seek_bar);

        attackBar = (SeekBar) this.findViewById(R.id.attack_bar);
        decayBar = (SeekBar) this.findViewById(R.id.decay_bar);
        sustainBar = (SeekBar) this.findViewById(R.id.sustain_bar);
        releaseBar = (SeekBar) this.findViewById(R.id.release_bar);

        adsrOutput = (Button) this.findViewById(R.id.adsr_output_btn);
        adsrPower = (Switch) this.findViewById(R.id.adsr_power);
        adsrTrigger = (Button) this.findViewById(R.id.adsr_trigger);

        osc1_outBtn = (Button) this.findViewById(R.id.osc1_output);
        osc2_outBtn = (Button) this.findViewById(R.id.osc2_output);
        osc1_voltBtn = (Button) this.findViewById(R.id.osc1_volt_btn);
        osc1_fmBtn = (Button) this.findViewById(R.id.osc1_fmmod_btn);
        osc2_fmBtn = (Button) this.findViewById(R.id.osc2_fmmod_btn);
        osc2_voltBtn = (Button) this.findViewById(R.id.osc2_volt_btn);
        mix_in1Btn = (Button) this.findViewById(R.id.mixer_input1);
        mix_in2Btn = (Button) this.findViewById(R.id.mixer_input2);

        amp = new AudioInterface(volumeBar, manager, mix_in1Btn, mix_in2Btn);
        osc1 = new Oscillator(osc1_freqBar, osc1_volumeBar, osc1_fmModBar, manager, osc1_outBtn, osc1_voltBtn, osc1_fmBtn);
        osc2 = new Oscillator(osc2_freqBar, osc2_volumeBar, osc2_fmModBar, manager, osc2_outBtn, osc2_voltBtn, osc2_fmBtn);
        adsr1 = new ADSR(attackBar, decayBar, sustainBar, releaseBar, adsrTrigger, manager, adsrOutput);

        adsrPower.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                if (adsrPower.isChecked()) {
                    adsrThread = new Thread(adsr1);
                    adsrThread.start();
                }
                else {
                    adsr1.off();
                }

            }
        });

        mixerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                if (mixerSwitch.isChecked()) {
                    ampThread = new Thread(amp);
                    ampThread.start();
                }
                else {
                    amp.off();
                }

            }
        });

        osc1_powerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                if (osc1_powerSwitch.isChecked()) {
                    osc1Thread = new Thread(osc1);
                    osc1Thread.start();
                }
                else {
                    osc1.off();
                }

            }
        });

        osc2_powerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (osc2_powerSwitch.isChecked()) {
                    osc2Thread = new Thread(osc2);
                    osc2Thread.start();
                }
                else {
                    osc2.off();
                }

            }
        });


    } // end OnCreate

} // end class MainActivity