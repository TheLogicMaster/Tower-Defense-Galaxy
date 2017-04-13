package com.logicmaster63.tdworld;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.logicmaster63.tdworld.screens.GameScreen;
import com.logicmaster63.tdworld.screens.ModelEditScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TDWorld extends Game {

	public static final int TOWERS = 2;
	public static final int ENEMIES = 1;
	public static final HashMap<String, Integer> TYPES;
	public static int res = 10;
	public static boolean isDebugging = true;
	public static float sensitivity = 0.5f;
	public static List<String> themes = new ArrayList<String>();
	public static List<String> tracks = new ArrayList<String>();
	public static BitmapFont font;
	static {
		TYPES = new HashMap<String, Integer>();
		TYPES.put("ice", 1);
		TYPES.put("fire", 2);
		TYPES.put("sharp", 4);
	}

	@Override
	public void create() {
		Bullet.init();

		font = new BitmapFont(Gdx.files.internal("pixelade.fnt"),false);
		themes.add("basic");
		tracks.add("test");

		setScreen(new GameScreen(this, 0, themes.get(0)));
	}

	@Override
	public void dispose() {
		font.dispose();
	}
}
