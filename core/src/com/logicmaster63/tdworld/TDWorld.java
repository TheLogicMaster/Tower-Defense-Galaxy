package com.logicmaster63.tdworld;

import com.badlogic.gdx.Game;
import com.logicmaster63.tdworld.screens.GameScreen;

public class TDWorld extends Game {

	public static int res = 10;

	@Override
	public void create() {
		setScreen(new GameScreen(this, 0));
	}
}
