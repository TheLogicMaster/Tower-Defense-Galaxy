package com.logicmaster63.tdgalaxy;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.logicmaster63.tdgalaxy.constants.Dialogs;
import com.logicmaster63.tdgalaxy.constants.GameMode;
import com.logicmaster63.tdgalaxy.interfaces.Debug;
import com.logicmaster63.tdgalaxy.interfaces.FileStuff;
import com.logicmaster63.tdgalaxy.interfaces.OnlineServices;
import com.logicmaster63.tdgalaxy.screens.MainScreen;
import com.logicmaster63.tdgalaxy.tools.FileHandler;
import com.logicmaster63.tdgalaxy.tools.PreferenceHandler;

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

    public static Dialogs dialogs;
    public static PreferenceHandler preferences;

	private static List<String> themes = new ArrayList<String>();
    private static String ip = "";
    private static Debug debugger;
    private static boolean isMe = false;
    private static MainScreen mainScreen;

    private static String startingMode;

    private Map<String, BitmapFont> fonts;
    private AssetManager uiAssets, gameAssets;

	public TDGalaxy(String mode, FileStuff fileStuff, Debug debugger, OnlineServices onlineServices) {
        TDGalaxy.fileStuff = fileStuff;
        TDGalaxy.debugger = debugger;
        TDGalaxy.onlineServices = onlineServices;
        TDGalaxy.startingMode = mode;
    }

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
        onlineServices.showBanner(screen instanceof MainScreen);
        updateNetwork();
    }

    @Override
	public void create() {
		Bullet.init();

        themes.add("basic");

        //clearPrefs();
        //changePref("debug", true);
        preferences = new PreferenceHandler();
        dialogs = new Dialogs();

        fonts = new HashMap<String, BitmapFont>();
		FileHandler.loadFonts(fonts);

        if(preferences.isDebug())
            Gdx.app.setLogLevel(Application.LOG_DEBUG);

        loadExternalAssets();
        loadTheme("basic");

        GameMode mode;
        if(startingMode != null) {
            try {
                mode = GameMode.valueOf(startingMode);
                Gdx.app.log("StartingMode", mode.name());
            } catch (Exception e) {
                Gdx.app.error("TDGalaxy", "Invalid starting mode", e);
            }
        }

        mainScreen = new MainScreen(this);
        setScreen(mainScreen);
		//setScreen(new GameScreen(this, 0, themes.get(0)));
	}

	public AssetManager getUIAssets() {
	    return uiAssets;
    }

    public AssetManager getGameAssets() {
        return gameAssets;
    }

    public void loadExternalAssets() {
        gameAssets = fileStuff.getExternalAssets();
    }

    public void loadTheme(String theme) {
	    if(uiAssets == null)
	        uiAssets = new AssetManager();
	    uiAssets.clear();
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

	@Override
	public void dispose() {
	    List<BitmapFont> fontArray = new ArrayList<BitmapFont>(fonts.values());
	    for(BitmapFont font: fontArray)
		    font.dispose();
	}

	public void addTheme() {

    }

    public void createDebugWindow(Object ... values) {
	    if(debugger != null)
            debugger.create();
    }

    public void addDebugTextButton(String name, com.logicmaster63.tdgalaxy.interfaces.ValueReturner valueReturner) {
        if(debugger != null)
            debugger.addTextButton(name, valueReturner);
    }

    public void addDebugButton(String name, Runnable runnable) {
        if(debugger != null)
            debugger.addButton(name, runnable);
    }

    public String getIp() {
        return ip;
    }

    public List<String> getThemes() {
        return themes;
    }

    public boolean isMe() {
        return isMe;
    }

    public void removeDebugTextButton(String name) {
        debugger.removeTextButton(name);
    }

    public void removeDebugButton(String name) {
        debugger.removeButton(name);
    }

    public void updateDebug(Map<String, Object> values) {
	    debugger.update(values);
    }

    public Map<String, BitmapFont> getFonts() {
        return fonts;
    }
}