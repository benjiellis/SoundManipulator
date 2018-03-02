package com.example.benji.soundmanipulator;


import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Port {
    private Cable link;
    private String id;
    private boolean isOut;
    private Button trigger;
    private CableManager manager;

    Port(String id, boolean isOut, CableManager manager, Button trigger) {
        this.link = new Cable();
        this.id = id;
        this.isOut = isOut;
        this.manager = manager;
        this.trigger = trigger;

        trigger.setOnClickListener(new PortOnClickListener(manager, this));
    }

    public Cable getLink() {
        return this.link;
    }

    public ConcurrentLinkedQueue<double[]> getLinkBuffer() {
        return this.link.getBuffer();
    }

    public void setLink(Cable in) {
        if (isOut) {
            if (in.isOutputTaken()) {
                Log.d("AV", "Cable already has output assigned");
                return;
            }
            this.link = in;
            link.setInputTaken(true);
        }
        else {
            if (in.isInputTaken()) {
                Log.d("AV", "Cable already has input assigned");
                return;
            }
            this.link = in;
            link.setOutputTaken(true);
        }
    }

    public void setLink() {
        this.link = new Cable();
        if (isOut) {
            link.setInputTaken(true);
        }
        else {
            link.setOutputTaken(true);
        }
    }

    public static void link(Port port1, Port port2) {
        port1.setLink();
        port2.setLink(port1.getLink());
    }
}


