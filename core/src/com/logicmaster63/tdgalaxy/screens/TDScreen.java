package com.logicmaster63.tdgalaxy.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.logicmaster63.tdgalaxy.TDGalaxy;
import com.logicmaster63.tdgalaxy.ui.MessageWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class TDScreen implements Screen {

    protected TDGalaxy game;
    protected SpriteBatch spriteBatch;
    protected OrthographicCamera orthographicCamera;
    protected Viewport viewport;
    protected Stage stage;
    protected List<Disposable> disposables = new ArrayList<Disposable>();
    protected AssetManager uiAssets;

    private InputMultiplexer multiplexer;

    public TDScreen (TDGalaxy game) {
        this.game = game;
        this.uiAssets = game.getUIAssets();
    }

    @Override
    public void resize (int width, int height) {
        viewport.update(width, height);
    }

    public void addInputProcessor(InputProcessor processor) {
        multiplexer.addProcessor(processor);
    }

    @Override
    public void show () {
        orthographicCamera = new OrthographicCamera(2560, 1440);
        spriteBatch = new SpriteBatch();
        orthographicCamera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        viewport = new FitViewport(2560, 1440, orthographicCamera);
        viewport.apply();
        stage = new Stage(viewport);
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
        if(TDGalaxy.preferences.isDebug())
            stage.setDebugAll(true);
    }

    @Override
    public void hide () {

    }

    @Override
    public void pause () {

    }

    @Override
    public void resume () {

    }

    @Override
    public void dispose () {
        System.out.println("Dispose");
        if (disposables != null)
            for (Disposable d : disposables)
                d.dispose();
    }

    @Override
    public void render(float delta) {
        stage.act(Gdx.graphics.getDeltaTime());
        orthographicCamera.update();
        spriteBatch.setProjectionMatrix(orthographicCamera.combined);
        spriteBatch.begin();
        //TDWorld.getFonts().get("ui32").draw(spriteBatch, orthographicCamera.unproject(tmp.set(Gdx.input.getX(), Gdx.input.getY(), 0)).toString(), 100, 100);
        spriteBatch.end();
        stage.draw();
    }

    protected void addDisposables(Disposable... d) {
        disposables.addAll(Arrays.asList(d));
    }
}
