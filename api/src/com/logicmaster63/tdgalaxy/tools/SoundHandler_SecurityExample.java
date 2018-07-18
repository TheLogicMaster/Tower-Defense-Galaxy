package com.logicmaster63.tdgalaxy.tools;

import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

public class SoundHandler_SecurityExample {

    private static volatile SoundHandler_SecurityExample instance;

    private Map<String, Sound> audios;

    private SoundHandler_SecurityExample() {
        if(instance != null)
            throw new RuntimeException("I don't think so.");
        audios = new HashMap<String, Sound>();
    }

    public void loadAudios(Map<String, Sound> audios) {
        Security.ensurePermission();
        this.audios.putAll(audios);
    }

    public void playSound() {

    }

    public static synchronized void initialize() {
        if(instance == null)
            instance = new SoundHandler_SecurityExample();
    }

    public static SoundHandler_SecurityExample get() {
        if(instance == null)
            initialize();
        return instance;
    }
}
