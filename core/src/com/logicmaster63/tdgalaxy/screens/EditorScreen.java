package com.logicmaster63.tdgalaxy.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.logicmaster63.tdgalaxy.ui.TabSelector;

public class EditorScreen extends TDScreen {

    private TabSelector tabs;
    private Texture background, back, knob;

    public EditorScreen(Game game, AssetManager uiAssets) {
        super(game, uiAssets);
    }

    @Override
    public void show() {
        super.show();

        background = new Texture("theme/basic/ui/Window.png");
        back = new Texture("theme/basic/ui/SlideBack.png");
        knob = new Texture("theme/basic/ui/Knob.png");

        tabs = new TabSelector(3, new Texture[]{knob, back, knob}, false);
        tabs.setBounds(100, 100, 500, 100);
        stage.addActor(tabs);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        spriteBatch.end();

        super.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
        background.dispose();
    }
}