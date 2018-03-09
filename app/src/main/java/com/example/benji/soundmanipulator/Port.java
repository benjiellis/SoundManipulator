package com.example.benji.soundmanipulator;

import android.util.Log;

public class Port {
    private Cable link;
    private boolean isOut;

    public boolean isOut() {
        return this.isOut;
    }

    Port(boolean isOut) {
        this.isOut = isOut;
        this.link = new Cable();
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

    }
}
