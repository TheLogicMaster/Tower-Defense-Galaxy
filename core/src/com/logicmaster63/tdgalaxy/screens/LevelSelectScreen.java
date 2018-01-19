package com.logicmaster63.tdgalaxy.screens;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.logicmaster63.tdgalaxy.TDGalaxy;
import com.logicmaster63.tdgalaxy.constants.Constants;
import com.logicmaster63.tdgalaxy.interfaces.CameraRenderer;

public class LevelSelectScreen extends TDScreen implements CameraRenderer {

    public LevelSelectScreen(TDGalaxy game) {
        super(game);
    }

    private AssetManager assets;
    private boolean isLoading = true;
    private ModelInstance scene;
    private ModelBatch modelBatch;
    private Environment environment;

    @Override
    public void show() {
        super.show();
        camera.near = 1;
        camera.far = 10000;
        camera.position.set(0, 500, 0);
        camera.lookAt(0, 0, 0);
        assets = new AssetManager();
        assets.load("theme/basic/campaign/scene.g3db", Model.class);
        modelBatch = new ModelBatch();
        environment = new Environment();

        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
        //environment.add(new DirectionalLight().set(1f, 1f, 1f, -1f, -1f, -1f));
        addDisposables(modelBatch, assets);
    }

    @Override
    protected Camera createCamera() {
        return new PerspectiveCamera();
    }

    @Override
    public void renderForCamera(Camera camera) {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if(isLoading) {
            if (!assets.update())
                return;
            else {
                isLoading = false;
                scene = new ModelInstance(assets.get("theme/basic/campaign/scene.g3db", Model.class));
            }
        }

        camera.update();
        modelBatch.begin(camera);
        modelBatch.render(scene, environment);
        modelBatch.end();
        super.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
