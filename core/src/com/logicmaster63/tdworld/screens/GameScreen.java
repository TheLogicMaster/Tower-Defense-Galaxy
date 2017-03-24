package com.logicmaster63.tdworld.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.UBJsonReader;
import com.logicmaster63.tdworld.TDWorld;
import com.logicmaster63.tdworld.enemy.Enemy;
import com.logicmaster63.tdworld.FileHandler;
import com.logicmaster63.tdworld.tools.MotionState;
import com.logicmaster63.tdworld.tower.Gun;
import com.logicmaster63.tdworld.tower.Tower;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class GameScreen extends TDScreen {

    private SpriteBatch batch;
    private Texture background;
    private int map, theme;
    private ArrayList<Point> path;
    private ArrayList<Enemy> enemies;
    private ArrayList<Tower> towers;
    private PerspectiveCamera cam;
    private Model model;
    private ModelBatch modelBatch;
    private Environment environment;
    private CameraInputController camController;
    private AssetManager assets;
    private ArrayList<Model> towerModels, enemyModels;
    private boolean loading;

    public GameScreen(Game game, int map, int theme) {
        super(game);
        this.map = map;
        this.theme = theme;
    }

    @Override
    public void show() {
        towerModels = new ArrayList<Model>();
        enemyModels = new ArrayList<Model>();
        path = new ArrayList<Point>();
        enemies = new ArrayList<Enemy>();
        towers = new ArrayList<Tower>();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        modelBatch = new ModelBatch();
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)),VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        //model = g3dbModelLoader.loadModel(new FileHandle("theme/basic/tower/invaderscene.g3db"));
        //shipInstance = new ModelInstance(model);
        assets = new AssetManager();
        for(int i = 0; i < TDWorld.TOWERS; i++)
            assets.load("theme/" + TDWorld.themes.get(theme) + "/tower/" + Integer.toString(i) + ".g3db", Model.class);
        //assets.setLoader(Model.class, new ModelLoader(new InternalFileHandleResolver()));
        //assets.load("theme/basic/tower/portalturret.g3db", Model.class);
        loading = true;
        batch = new SpriteBatch();
        batch.getProjectionMatrix().setToOrtho2D(0, 0, 480, 320);
        background = new Texture("Background_MainMenu.png");

        BufferedReader reader = FileHandler.getReader("track/Track" + Integer.toString(map));
        if(reader != null)
            path = FileHandler.loadTrack(reader);
        //System.out.println(track);
        //Gdx.app.log("Path", Gdx.files.getLocalStoragePath());
        //Gdx.app.log("File", Gdx.files.internal("theme/basic/tower/Spider.g3db").read().toString());
        //System.out.println("--------------------" + Gdx.files.internal("theme/basic/tower/spider.g3db").read());
    }

    @Override
    public void render(float delta) {
        if (loading) {
            if (assets.update())
                doneLoading();
            else
                return;
        }
        //System.out.println(Gdx.files.internal("theme/basic/tower/spider.g3db"));

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0);
        batch.end();

        camController.update();
        modelBatch.begin(cam);
        for (Tower tower : towers)
            tower.tick(delta);
        for (Enemy enemy : enemies)
            enemy.tick(delta);
        for (Tower tower : towers)
            tower.render(delta, modelBatch);
        for (Enemy enemy : enemies)
            enemy.render(delta, modelBatch);
        //instance = new ModelInstance(g3dbModelLoader.loadModel(new FileHandle("theme/basic/tower/1.g3db")));
        //instance.transform.rotate(Vector3.X, 1);
        //System.out.println(shipInstance.calculateBoundingBox(new BoundingBox()).getDimensions(new Vector3()).x);
        //modelBatch.render(shipInstance, environment);
        //System.out.println(towerInstances);
        //modelBatch.render(shipInstance);
        //modelBatch.render(towerInstances, environment);
        modelBatch.end();

        /*batch.begin();
        batch.draw(background, 0, 0, 20, 20);
        batch.end();*/
    }

    @Override
    public void hide() {
        batch.dispose();
        background.dispose();
        modelBatch.dispose();
        model.dispose();
    }

    private void doneLoading() {
        System.out.println("Done");
        loading = false;
        for(int i = 0; i < TDWorld.TOWERS; i++)
            towerModels.add(assets.get("theme/" + TDWorld.themes.get(theme) + "/tower/" + Integer.toString(i) + ".g3db", Model.class));
        //Model ship = assets.get("theme/basic/tower/portalturret.g3db", Model.class);
        //ModelLoader loader = new ObjLoader();
        //ship = loader.loadModel(Gdx.files.internal("theme/basic/tower/0.obj"));
        /*shipInstance.materials.get(0).set(new Material(ColorAttribute.createDiffuse(Color.GREEN)));
        Vector3 dimensions = shipInstance.calculateBoundingBox(new BoundingBox()).getDimensions(new Vector3());
        float largest = dimensions.x;
        if(dimensions.y > largest) largest = dimensions.y;
        if(dimensions.z > largest) largest= dimensions.z;
        if(largest > 25){
            float s = 25f / largest;
            shipInstance.transform.setToScaling(s, s, s);
        }else if(largest < 0.1f) {
            float s = 5 / largest;
            shipInstance.transform.setToScaling(s, s, s);
        }
        */
        //towerInstances.add(new ModelInstance(towerModels.get(0)));
        towers.add(new Gun(10, 10, new ModelInstance(towerModels.get(0))));
        towers.add(new Gun(10, 10, new ModelInstance(towerModels.get(1))));
    }
}
