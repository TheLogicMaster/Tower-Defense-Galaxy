package com.logicmaster63.tdgalaxy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.logicmaster63.tdgalaxy.TDGalaxy;
import com.logicmaster63.tdgalaxy.constants.Constants;
import com.logicmaster63.tdgalaxy.interfaces.CameraRenderer;

public class LevelSelectScreen extends T3DScreen implements CameraRenderer, InputProcessor {

    private AssetManager assets;
    private boolean isLoading = true;
    private ModelInstance scene;
    private ModelBatch modelBatch;
    private Environment environment;
    private int worldIndex;

    public LevelSelectScreen(TDGalaxy game, int worldIndex) {
        super(game);
        this.worldIndex = worldIndex;
    }

    @Override
    public void show() {
        super.show();
        assets = new AssetManager();
        assets.load("theme/basic/campaign/scene.g3db", Model.class);
        modelBatch = new ModelBatch();
        environment = new Environment();

        //Back button
        Button backButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("theme/basic/ui/BackButton.png"))));
        backButton.setPosition(100, viewport.getWorldHeight() - 250);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new CampaignScreen(game, worldIndex));
            }
        });
        stage.addActor(backButton);

        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
        //environment.add(new DirectionalLight().set(1f, 1f, 1f, -1f, -1f, -1f));
        addDisposables(modelBatch, assets);
        addInputProcessor(this);
    }

    @Override
    public void renderForCamera(Camera camera) {
        modelBatch.begin(camera);
        modelBatch.render(scene, environment);
        modelBatch.end();
    }

    @Override
    public void render(float delta) {
        if(isLoading) {
            if (!assets.update())
                return;
            else {
                isLoading = false;
                scene = new ModelInstance(assets.get("theme/basic/campaign/scene.g3db", Model.class));
            }
        }
        super.render(delta);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
