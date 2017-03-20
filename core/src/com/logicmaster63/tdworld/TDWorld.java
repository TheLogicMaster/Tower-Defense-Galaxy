package com.logicmaster63.tdworld;

import com.badlogic.gdx.Game;
import com.logicmaster63.tdworld.screens.GameScreen;

import java.util.ArrayList;

public class TDWorld extends Game {

	public static final int TOWERS = 2;
	public static int res = 10;
	public static ArrayList<String> themes = new ArrayList<String>();
	public static ArrayList<String> tracks = new ArrayList<String>();

	@Override
	public void create() {
		themes.add("basic"); //Change to Default
		tracks.add("test");

		setScreen(new GameScreen(this, 0, 0));
	}
}
