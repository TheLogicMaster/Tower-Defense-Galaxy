package com.logicmaster63.tdgalaxy.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.logicmaster63.tdgalaxy.TDGalaxy;
import com.logicmaster63.tdgalaxy.constants.Eye;
import com.logicmaster63.tdgalaxy.interfaces.CameraRenderer;
import com.logicmaster63.tdgalaxy.ui.MessageWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class TDScreen implements Screen, CameraRenderer {

    protected TDGalaxy game;
    protected SpriteBatch spriteBatch;
    protected Camera camera;
    protected Viewport viewport;
    protected Stage stage;
    protected List<Disposable> disposables = new ArrayList<Disposable>();
    protected AssetManager uiAssets;
    protected Matrix4 matrix4;

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
        //orthographicCamera = new OrthographicCamera(3840, 2160);
        matrix4 = new Matrix4();
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(2560, 1440, new OrthographicCamera());
        viewport.apply();
        stage = new Stage(viewport);
        camera = createCamera();
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

    protected void update(float delta) {

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
        update(delta);
        camera.update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        if(TDGalaxy.preferences.isVr() && game.getVr().isInitialized()) {
            game.getVr().update(delta);
            game.getVr().startRender();
            Camera vrCamera = game.getVr().beginCamera(Eye.LEFT);
            renderForCamera(vrCamera);
            renderUI(vrCamera);
            game.getVr().endCamera(Eye.LEFT);
            vrCamera = game.getVr().beginCamera(Eye.RIGHT);
            renderForCamera(vrCamera);
            renderUI(vrCamera);
            game.getVr().endCamera(Eye.RIGHT);
            game.getVr().endRender();
        } else {
            renderForCamera(camera);
            renderUI(camera);
        }
    }

    private void renderUI(Camera uiCamera) {
        matrix4.set(uiCamera.combined);
        matrix4.setToOrtho2D(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        spriteBatch.setProjectionMatrix(matrix4);
        spriteBatch.begin();
        //TDWorld.getFonts().get("ui32").draw(spriteBatch, orthographicCamera.unproject(tmp.set(Gdx.input.getX(), Gdx.input.getY(), 0)).toString(), 100, 100);
        spriteBatch.end();
        stage.draw();
    }

    protected Camera createCamera() {
        Camera camera = new OrthographicCamera(2560, 1440);
        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        camera.far = 10000;
        camera.near = 0;
        return camera;
    }

    protected void addDisposables(Disposable... d) {
        disposables.addAll(Arrays.asList(d));
    }
}
