package com.example.benji.soundmanipulator;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class CableManager {
    private static CableManager instance;
    private Port onDeck;
    private PortOnClickListener onDeckListener;

    private CableManager() {
        this.onDeckListener = null;
        this.onDeck = null;
    }

    public PortOnClickListener send(Port toLink, PortOnClickListener listener) {
        if (onDeck == null) {
            onDeck = toLink;
            onDeckListener = listener;
            return null;
        }
        else if (onDeck == toLink) {
            Log.d("AV", "Cannot link port with self");
            onDeck = null;
            onDeckListener = null;
            return null;
        }
        else {
            onDeck.getLink().detach();
            toLink.getLink().detach();

            Log.d("AV", "Linking two ports.");

            Port.link(onDeck, toLink);
            onDeck = null;
            PortOnClickListener toReturn = onDeckListener;
            onDeckListener = null;
            return toReturn;
        }
    }

    public static synchronized CableManager getInstance() {
        if (instance == null) {
            instance = new CableManager();
        }
        return instance;
    }

}
