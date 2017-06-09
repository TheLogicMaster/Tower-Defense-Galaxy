package com.logicmaster63.tdgalaxy;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.logicmaster63.tdgalaxy.screens.GameScreen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class TDWorld extends Game {

	private static final Map<String, Integer> TYPES;
	private static List<String> themes = new ArrayList<String>();
	private static Map<String, BitmapFont> fonts;
    private static String ip = "";
    private static com.logicmaster63.tdgalaxy.interfaces.FileStuff fileStuff;
    private static com.logicmaster63.tdgalaxy.interfaces.Debug debugger;
    private static boolean isMe = false;
    private static com.logicmaster63.tdgalaxy.interfaces.GooglePlayServices googlePlayServices;

    private static Preferences prefs;
    private static int res = 10;
    private static float sensitivity = 0.5f;
    private static boolean debug = true;
    private static boolean debugWindow = false;
    private static boolean vr = false;

	static {
		TYPES = new HashMap<String, Integer>();
		TYPES.put("ice", 1);
		TYPES.put("fire", 2);
		TYPES.put("sharp", 4);
	}

	public TDWorld(com.logicmaster63.tdgalaxy.interfaces.FileStuff getter, com.logicmaster63.tdgalaxy.interfaces.Debug debugger, com.logicmaster63.tdgalaxy.interfaces.GooglePlayServices googlePlayServices) {
        fileStuff = getter;
        TDWorld.debugger = debugger;
        TDWorld.googlePlayServices = googlePlayServices;
    }

    public TDWorld(com.logicmaster63.tdgalaxy.interfaces.FileStuff getter, com.logicmaster63.tdgalaxy.interfaces.Debug debugger) {
        fileStuff = getter;
    }

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
        updateNetwork();
    }

    @Override
	public void create() {
		Bullet.init();

        themes.add("basic");

        prefs = Gdx.app.getPreferences("My Preferences");
        //clearPrefs();
        //changePref("debug", true);
        loadPrefs();

        fonts = new HashMap<String, BitmapFont>();
		com.logicmaster63.tdgalaxy.tools.FileHandler.loadFonts(fonts);

        if(debug) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);

        }

		setScreen(new GameScreen(this, 0, themes.get(0)));
	}

    public void updateNetwork() {
            Gdx.net.sendHttpRequest(new HttpRequestBuilder().newRequest().method(Net.HttpMethods.GET).url("http://checkip.amazonaws.com").build(), new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    try {
                        ip = new BufferedReader(new InputStreamReader(httpResponse.getResultAsStream())).readLine();
                        isMe = "99.36.127.68".equals(ip);
                    } catch (IOException e) {
                        Gdx.app.error("HTTP", e.toString());
                    }
                }
                @Override
                public void failed(Throwable t) {

                }
                @Override
                public void cancelled() {

                }
            });
    }

    private void loadPrefs() {
        debug = (Boolean) getPref("debug", debug);
        debugWindow = (Boolean) getPref("debugWindow", debugWindow);
        vr = (Boolean) getPref("vr", vr);
        sensitivity = (Float) getPref("sensitivity", sensitivity);
        res = (Integer) getPref("res", res);
    }

    public static Object getPref(String name, Object defaultValue) {
        if (prefs.get().containsKey(name))
            try {
                return defaultValue.getClass().getConstructor(String.class).newInstance(prefs.get().get(name));
            } catch (Exception e) {
                Gdx.app.error("getPref", e.toString());
            }
        return defaultValue;
    }

    public static void changePref(String name, Object object) {
	    Map map = prefs.get();
	    map.put(name, object);
	    prefs.put(map);
	    prefs.flush();
        try {
            TDWorld.class.getDeclaredField(name).set(null, object);
        } catch (Exception e) {
            Gdx.app.error("changePref", e.toString());
        }
    }

    public static void clearPrefs() {
	    prefs.clear();
	    prefs.flush();
    }

	@Override
	public void dispose() {
	    List<BitmapFont> fontArray = new ArrayList<BitmapFont>(fonts.values());
	    for(BitmapFont font: fontArray)
		    font.dispose();
	}

	public static void addTheme() {

    }

    public static void createDebugWindow(Object ... values) {
	    if(debugger != null)
            debugger.create();
    }

    public static void addDebugTextButton(String name, com.logicmaster63.tdgalaxy.interfaces.ValueReturner valueReturner) {
        if(debugger != null)
            debugger.addTextButton(name, valueReturner);
    }

    public static void addDebugButton(String name, Runnable runnable) {
        if(debugger != null)
            debugger.addButton(name, runnable);
    }

    public static void removeDebugTextButton(String name) {
        debugger.removeTextButton(name);
    }

    public static void removeDebugButton(String name) {
        debugger.removeButton(name);
    }

    public static void updateDebug(Map<String, Object> values) {
	    debugger.update(values);
    }

    public static Map<String, BitmapFont> getFonts() {
        return fonts;
    }

    public static Map<String, Integer> getTYPES() {
        return TYPES;
    }

    public static int getRes() {
        return res;
    }

    public static float getSensitivity() {
        return sensitivity;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static boolean isVr() {
        return vr;
    }

    public static String getIp() {
        return ip;
    }

    public static com.logicmaster63.tdgalaxy.interfaces.FileStuff getFileStuff() {
        return fileStuff;
    }

    public static List<String> getThemes() {
        return themes;
    }

    public static boolean isMe() {
        return isMe;
    }

    public static boolean isDebugWindow() {
        return debugWindow;
    }
}
