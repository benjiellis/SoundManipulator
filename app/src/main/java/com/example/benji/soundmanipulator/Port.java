package com.example.benji.soundmanipulator;

import android.util.Log;

import java.util.Random;

public class Port {
    private Cable link;
    private boolean isOut;

    public RGBColor getColor() {
        return color;
    }

    public void setColor(RGBColor color) {
        this.color = color;
    }

    private RGBColor color;

    public boolean isOut() {
        return this.isOut;
    }

    Port(boolean isOut) {
        this.isOut = isOut;
        this.link = new Cable();
        this.color = null;
    }

    public Cable getLink() {
        return this.link;
    }

    public void setLink(Cable in) {
        this.link = in;
    }

    public void setLink() {
        this.link = new Cable();
    }

    public static void link(Port port1, Port port2) {

        if (port1.isOut() == port2.isOut()) {
            Log.d("AV", "Ports must be of different type.");
            return;
        }

        Cable hook = new Cable();
        port1.setLink(hook);
        port2.setLink(hook);

        if (port1.isOut()) {
            hook.setInPort(port1);
            hook.setOutPort(port2);
        }
        else {
            hook.setInPort(port2);
            hook.setOutPort(port1);
        }

        Random rnd = new Random();
        RGBColor color = new RGBColor(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        port1.setColor(color);
        port2.setColor(color);
    }
}
