package com.logicmaster63.tdworld;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.logicmaster63.tdworld.screens.GameScreen;
import com.logicmaster63.tdworld.tools.ClassGetter;
import com.logicmaster63.tdworld.tools.FileHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class TDWorld extends Game {

	private static final Map<String, Integer> TYPES;
	private static final List<String> themes = new ArrayList<String>();
	private static Map<String, BitmapFont> fonts;
	private static GameScreen gameScreen;
    public static ClassGetter classGetter;
    public static String ip = "";

    private Preferences prefs;
    public static int res = 10;
    public static float sensitivity = 0.5f;
    public static boolean debug = true;

	static {
		TYPES = new HashMap<String, Integer>();
		TYPES.put("ice", 1);
		TYPES.put("fire", 2);
		TYPES.put("sharp", 4);
        themes.add("basic");
	}

	public TDWorld(ClassGetter getter) {
        classGetter = getter;
    }

    public TDWorld() {}

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
        updateNetwork();
    }

    @Override
	public void create() {
		Bullet.init();

        prefs = Gdx.app.getPreferences("My Preferences");
        //prefs.clear();
        //prefs.flush();
        //changePref("res", 30);
        //changePref("sensitivity", 0.6f);
        //changePref("debug", false);
        loadPrefs();

        fonts = new HashMap<String, BitmapFont>();
		FileHandler.loadFonts(fonts);

        if(debug)
            Gdx.app.setLogLevel(Application.LOG_DEBUG);

        gameScreen = new GameScreen(this, 0, themes.get(0));
		setScreen(gameScreen);
	}

    public static void updateNetwork() {
	    try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            ip = in.readLine();
        } catch (IOException e) {
            Gdx.app.error("UpdateNetwork", e.toString());
        }
    }

    private void loadPrefs() {
        debug = (Boolean) getPref("debug", debug);
        sensitivity = (Float) getPref("sensitivity", sensitivity);
        res = (Integer) getPref("res", res);
    }

    public Object getPref(String name, Object defaultValue) {
        if (prefs.get().containsKey(name))
            try {
                return defaultValue.getClass().getConstructor(String.class).newInstance(prefs.get().get(name));
            } catch (Exception e) {
                Gdx.app.error("getPref", e.toString());
            }
        return defaultValue;
    }

    public void changePref(String name, Object object) {
	    Map map = prefs.get();
	    map.put(name, object);
	    prefs.put(map);
	    prefs.flush();
        try {
            this.getClass().getField(name).set(this, object);
        } catch (Exception e) {
            Gdx.app.error("changePref", e.toString());
        }
    }

	@Override
	public void dispose() {
	    List<BitmapFont> fontArray = new ArrayList<BitmapFont>(fonts.values());
	    for(BitmapFont font: fontArray)
		    font.dispose();
	}

    public static Map<String, BitmapFont> getFonts() {
        return fonts;
    }

    public static Map<String, Integer> getTYPES() {
        return TYPES;
    }
}
