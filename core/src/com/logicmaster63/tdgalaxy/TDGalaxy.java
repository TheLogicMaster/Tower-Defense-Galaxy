package com.logicmaster63.tdgalaxy;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.logicmaster63.tdgalaxy.constants.Theme;
import com.logicmaster63.tdgalaxy.interfaces.Debug;
import com.logicmaster63.tdgalaxy.interfaces.FileStuff;
import com.logicmaster63.tdgalaxy.interfaces.OnlineServices;
import com.logicmaster63.tdgalaxy.screens.MainScreen;
import com.logicmaster63.tdgalaxy.tools.FileHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TDGalaxy extends Game {

    //Interfaces
    public static FileStuff fileStuff;
    public static OnlineServices onlineServices;

	private static List<String> themes = new ArrayList<String>();
	private static Map<String, BitmapFont> fonts;
    private static String ip = "";
    private static Debug debugger;
    private static boolean isMe = false;
    private static MainScreen mainScreen;

    //Preferences
    private static Preferences prefs;
    private static int res = 10;
    private static float sensitivity = 0.5f;
    private static float masterVolume = 1f;
    private static float effectVolume = 0.7f;
    private static float musicVolume = 0.3f;
    private static boolean debug = true;
    private static boolean debugWindow = false;
    private static boolean vr = false;

	public TDGalaxy(FileStuff fileStuff, Debug debugger, OnlineServices onlineServices) {
        TDGalaxy.fileStuff = fileStuff;
        TDGalaxy.debugger = debugger;
        TDGalaxy.onlineServices = onlineServices;
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
		FileHandler.loadFonts(fonts);

        if(debug) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        }

        mainScreen = new MainScreen(this, loadUI(new AssetManager(), Theme.BASIC));
        setScreen(mainScreen);
		//setScreen(new GameScreen(this, 0, themes.get(0)));
	}

	public static AssetManager loadUI(AssetManager assets, Theme theme) {
	    assets.clear();

	    return assets;
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
        masterVolume = (Float) getPref("masterVolume", masterVolume);
        effectVolume = (Float) getPref("effectVolume", effectVolume);
        musicVolume = (Float) getPref("musicVolume", musicVolume);
        res = (Integer) getPref("res", res);
    }

    public static Object getPref(String name, Object defaultValue) {
        if (prefs.get().containsKey(name))
            try {
                if(prefs.get().get(name) instanceof String)
                    return defaultValue.getClass().getConstructor(String.class).newInstance(prefs.get().get(name));
                else return prefs.get().get(name);
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
            TDGalaxy.class.getDeclaredField(name).set(null, object);
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

    public void mainMenu() {
	    setScreen(mainScreen);
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

    public static List<String> getThemes() {
        return themes;
    }

    public static boolean isMe() {
        return isMe;
    }

    public static boolean isDebugWindow() {
        return debugWindow;
    }

    public static float getMasterVolume() {
        return masterVolume;
    }

    public static float getEffectVolume() {
        return effectVolume;
    }

    public static float getEffectVolumeCombined() {
        return effectVolume * masterVolume;
    }

    public static float getMusicVolume() {
        return musicVolume;
    }

    public static float getMusicVolumeCombined() {
        return musicVolume * masterVolume;
    }
}