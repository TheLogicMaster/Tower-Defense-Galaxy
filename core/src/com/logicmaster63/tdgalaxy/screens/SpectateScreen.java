package com.logicmaster63.tdgalaxy.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.logicmaster63.tdgalaxy.TDGalaxy;
import com.logicmaster63.tdgalaxy.entity.Entity;

import java.util.HashMap;
import java.util.Map;

public class SpectateScreen extends TDScreen {

    private Map<Integer, Entity> entities;

    public SpectateScreen(TDGalaxy game) {
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
