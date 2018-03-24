package com.logicmaster63.tdgalaxy.tools;

import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

public class SoundHandler {

    private static volatile SoundHandler instance;

    private Map<String, Sound> audios;

    private SoundHandler() {
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
            instance = new SoundHandler();
    }

    public static SoundHandler get() {
        if(instance == null)
            initialize();
        return instance;
    }
}
