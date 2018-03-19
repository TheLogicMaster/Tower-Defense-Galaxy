package com.logicmaster63.tdgalaxy.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.logicmaster63.tdgalaxy.constants.Constants;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

public class PreferenceHandler {

    private Preferences prefs;

    private int res = 10;
    private int money = 0;
    private String theme = Constants.THEMES.get(0);
    private float sensitivity = 0.5f;
    private float masterVolume = 1f;
    private float effectVolume = 0.7f;
    private float musicVolume = 0.3f;
    private boolean debug = true;
    private boolean debugWindow = false;
    private boolean vr = false;
    private String id;

    public PreferenceHandler() {
        prefs = Gdx.app.getPreferences("My Preferences");
        loadPrefs();
    }

    public void loadPrefs() {
        debug = (Boolean) getPref("debug", debug);
        debugWindow = (Boolean) getPref("debugWindow", debugWindow);
        vr = (Boolean) getPref("vr", vr);
        sensitivity = (Float) getPref("sensitivity", sensitivity);
        masterVolume = (Float) getPref("masterVolume", masterVolume);
        effectVolume = (Float) getPref("effectVolume", effectVolume);
        musicVolume = (Float) getPref("musicVolume", musicVolume);
        res = (Integer) getPref("res", res);
        money = (Integer) getPref("money", money);
        theme = (String) getPref("theme", theme);

        if(prefs.contains("id")) {
            id = prefs.getString("id");
        } else {
            id = UUID.randomUUID().toString();
            prefs.putString("id", id);
            prefs.flush();
        }
    }

    private Object getPref(String name, Object defaultValue) {
        if (prefs.get().containsKey(name))
            try {
                if(prefs.get().get(name) instanceof String)
                    return defaultValue.getClass().getConstructor(String.class).newInstance(prefs.get().get(name));
                else return prefs.get().get(name);
            } catch (Exception e) {
                Gdx.app.error("getPref(Private)", e.toString());
            }
        return defaultValue;
    }

    public Object getPref(String name) {
        try {
            Field field = this.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(this);
        } catch (Exception e) {
            Gdx.app.error("getPref(Public)", e.toString());
        }
        return null;
    }

    public void changePref(String name, Object object) {
        Map map = prefs.get();
        map.put(name, object);
        prefs.put(map);
        prefs.flush();
        try {
            Field field = this.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(this, object);
        } catch (Exception e) {
            Gdx.app.error("changePref", e.toString());
        }
    }

    public void clearPrefs() {
        prefs.clear();
        prefs.flush();
    }

    public String getTheme() {
        return theme;
    }

    public int getRes() {
        return res;
    }

    public String getId() {
        return id;
    }

    public float getSensitivity() {
        return sensitivity;
    }

    public boolean isDebug() {
        return true; //debug;
    }

    public boolean isDebugWindow() {
        return true /*debugWindow*/;
    }

    public boolean isVr() {
        return vr;
    }

    public float getMasterVolume() {
        return masterVolume;
    }

    public float getEffectVolume() {
        return effectVolume;
    }

    public float getEffectVolumeCombined() {
        return effectVolume * masterVolume;
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public float getMusicVolumeCombined() {
        return musicVolume * masterVolume;
    }

    public int getMoney() {
        return money;
    }
}
