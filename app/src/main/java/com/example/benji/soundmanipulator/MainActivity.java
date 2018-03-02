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

    OscillatorView oscView1;
    OscillatorView oscView2;

    AudioInterfaceView ampView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // v importante

        oscView1 = (OscillatorView) findViewById(R.id.osc_view1);
        oscView2 = (OscillatorView) findViewById(R.id.osc_view2);

        ampView = (AudioInterfaceView) findViewById(R.id.amp_view);

    } // end OnCreate

} // end class MainActivity