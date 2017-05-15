package com.logicmaster63.tdworld;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.logicmaster63.tdworld.screens.GameScreen;
import com.logicmaster63.tdworld.tools.ClassGetter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TDWorld extends Game {

	private static final Map<String, Integer> TYPES;
	private static int res = 10;
    private static float sensitivity = 0.5f;
	public static final boolean isDebugging = false;
	private static final List<String> themes = new ArrayList<String>();
	private static Map<String, BitmapFont> fonts;
	private static GameScreen gameScreen;
    public static ClassGetter classGetter;
	static {
		TYPES = new HashMap<String, Integer>();
		TYPES.put("ice", 1);
		TYPES.put("fire", 2);
		TYPES.put("sharp", 4);
        themes.add("basic");
	}

    public TDWorld() {
    }

	public TDWorld(ClassGetter getter) {
        classGetter = getter;
    }

	@Override
	public void create() {
		Bullet.init();

        //FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/sourcesans.ttf"));
        //FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        //parameter.size = 64;
        fonts = new HashMap<String, BitmapFont>();
        //fonts.put("pixelade", new BitmapFont(Gdx.files.internal("font/pixelade.fnt"),false));
        //fonts.put("moonhouse", new BitmapFont(Gdx.files.internal("font/moonhouse.fnt"),false));
        //fonts.put("moonhouse", generator.generateFont(parameter));
        //parameter.size = 8;
        Texture texture = new Texture(Gdx.files.internal("font/moonhouse64.png"), true); // true enables mipmaps
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fonts.put("moonhouse64", new BitmapFont(Gdx.files.internal("font/moonhouse64.fnt"), new TextureRegion(texture), false));
        texture = new Texture(Gdx.files.internal("font/moonhouse32.png"), true); // true enables mipmaps
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fonts.put("moonhouse32", new BitmapFont(Gdx.files.internal("font/moonhouse32.fnt"), new TextureRegion(texture), false));
        texture = new Texture(Gdx.files.internal("font/moonhouse16.png"), true); // true enables mipmaps
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fonts.put("moonhouse16", new BitmapFont(Gdx.files.internal("font/moonhouse16.fnt"), new TextureRegion(texture), false));

        texture = new Texture(Gdx.files.internal("font/sourcesans64.png"), true); // true enables mipmaps
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fonts.put("ui64", new BitmapFont(Gdx.files.internal("font/sourcesans64.fnt"), new TextureRegion(texture), false));
        texture = new Texture(Gdx.files.internal("font/sourcesans32.png"), true); // true enables mipmaps
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fonts.put("ui32", new BitmapFont(Gdx.files.internal("font/sourcesans32.fnt"), new TextureRegion(texture), false));
        texture = new Texture(Gdx.files.internal("font/sourcesans16.png"), true); // true enables mipmaps
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fonts.put("ui16", new BitmapFont(Gdx.files.internal("font/sourcesans16.fnt"), new TextureRegion(texture), false));
        texture = new Texture(Gdx.files.internal("font/sourcesans8.png"), true); // true enables mipmaps
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fonts.put("ui8", new BitmapFont(Gdx.files.internal("font/sourcesans8.fnt"), new TextureRegion(texture), false));
        //fonts.put("ui", generator.generateFont(parameter));
        //generator = new FreeTypeFontGenerator(Gdx.files.internal("font/sourcesans.ttf"));
        //parameter.size = 32;
        //fonts.put("ui16", generator.generateFont(parameter));
        //generator.dispose();
        //fonts.get("ui").getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        if(isDebugging)
            Gdx.app.setLogLevel(Application.LOG_DEBUG);

        gameScreen = new GameScreen(this, 0, themes.get(0));
		setScreen(gameScreen);
	}

	@Override
	public void dispose() {
	    List<BitmapFont> fontArray = new ArrayList<BitmapFont>(fonts.values());
	    for(BitmapFont font: fontArray)
		    font.dispose();
	}

    public static int getRes() {
        return res;
    }

    public static void setRes(int res) {
        TDWorld.res = res;
    }

    public static float getSensitivity() {
        return sensitivity;
    }

    public static void setSensitivity(float sensitivity) {
        TDWorld.sensitivity = sensitivity;
    }

    public static Map<String, BitmapFont> getFonts() {
        return fonts;
    }

    public static Map<String, Integer> getTYPES() {
        return TYPES;
    }
}
