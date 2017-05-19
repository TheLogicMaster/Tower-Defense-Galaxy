package com.logicmaster63.tdworld.screens;

import com.badlogic.gdx.Game;
import com.logicmaster63.tdworld.entity.Entity;

import java.util.HashMap;
import java.util.Map;

public class SpectateScreen extends TDScreen {

    private Map<Integer, Entity> entities;


    public SpectateScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        entities = new HashMap<Integer, Entity>();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void dispose() {

    }
}
