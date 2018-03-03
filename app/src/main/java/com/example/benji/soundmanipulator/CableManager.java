package com.example.benji.soundmanipulator;


public class CableManager {
    private static CableManager instance;
    private Port onDeck;

    private CableManager() {
        this.onDeck = null;
    }

    public void send(Port toLink) {
        if (onDeck == null) {
            onDeck = toLink;
        }
        else {
            Port.link(onDeck, toLink);
            onDeck = null;
        }
    }

    public static synchronized CableManager getInstance() {
        if (instance == null) {
            instance = new CableManager();
        }
        return instance;
    }
}
