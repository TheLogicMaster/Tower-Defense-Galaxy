package com.logicmaster63.tdworld.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.logicmaster63.tdworld.TDWorld;
import com.logicmaster63.tdworld.enemy.basic.Basic;
import com.logicmaster63.tdworld.projectiles.Projectile;
import com.logicmaster63.tdworld.tools.FileHandler;
import com.logicmaster63.tdworld.map.EnemyHandler;
import com.logicmaster63.tdworld.tools.CameraHandler;
import com.logicmaster63.tdworld.tools.InputHandler;
import com.logicmaster63.tdworld.tower.Tower;
import com.logicmaster63.tdworld.tower.basic.Gun;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class GameScreen extends TDScreen {

    private SpriteBatch batch;
    private Texture background;
    private int map, theme;
    private ArrayList<Point> path;
    private EnemyHandler enemies;
    private ArrayList<Tower> towers;
    private ModelBatch modelBatch;
    private Environment environment;
    private AssetManager assets;
    private ArrayList<Model> towerModels, enemyModels;
    private boolean loading;
    private Model model;
    private ModelInstance planet;
    private InputMultiplexer inputMultiplexer;
    private CameraHandler cam;
    private InputHandler inputHandler;
    private ArrayList<Projectile> tProjectiles;
    private ArrayList<Projectile> eProjectiles;

    public GameScreen(Game game, int map, int theme) {
        super(game);
        this.map = map;
        this.theme = theme;
    }

    @Override
    public void show() {
        Bullet.init();
        inputHandler = new InputHandler();
        Texture texture = new Texture(Gdx.files.internal("Background_MainMenu.png"));
        Material material = new Material(TextureAttribute.createDiffuse(texture), ColorAttribute.createSpecular(1, 1, 1, 1), FloatAttribute.createShininess(8f));
        ModelBuilder builder = new ModelBuilder();
        //model = builder.createCapsule(200f, 400f, 16, material, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
        towerModels = new ArrayList<Model>();
        enemyModels = new ArrayList<Model>();
        tProjectiles = new ArrayList<Projectile>();
        eProjectiles = new ArrayList<Projectile>();
        path = new ArrayList<Point>();
        enemies = new EnemyHandler(new Vector3(100, 20, 100));
        towers = new ArrayList<Tower>();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        modelBatch = new ModelBatch();
        cam = new CameraHandler(new Vector3(250, 20, 250), 1, 5000);
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(inputHandler);
        inputMultiplexer.addProcessor(cam);
        Gdx.input.setInputProcessor(inputMultiplexer);

        assets = new AssetManager();
        assets.load("theme/" + TDWorld.themes.get(theme) + "/planet.g3db", Model.class);
        for(int i = 0; i < TDWorld.TOWERS; i++)
            assets.load("theme/" + TDWorld.themes.get(theme) + "/tower/" + Integer.toString(i) + ".g3db", Model.class);
        for(int i = 0; i < TDWorld.ENEMIES; i++)
            assets.load("theme/" + TDWorld.themes.get(theme) + "/enemy/" + Integer.toString(i) + ".g3db", Model.class);
        loading = true;

        batch = new SpriteBatch();
        batch.getProjectionMatrix().setToOrtho2D(0, 0, 480, 320);
        background = new Texture("Background_MainMenu.png");

        BufferedReader reader = FileHandler.getReader("track/Track" + Integer.toString(map));
        if(reader != null)
            path = FileHandler.loadTrack(reader);

        FileHandler.addDisposables(batch, modelBatch, background);
    }

    @Override
    public void render(float delta) {
        if (loading) {
            if (assets.update())
                doneLoading();
            else
                return;
        }
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        cam.update(delta);
        modelBatch.begin(cam.getCam());
        for (Tower tower : towers)
            tower.render(delta, modelBatch);
        for(Projectile projectile: tProjectiles)
            projectile.render(delta, modelBatch);
        for(Projectile projectile: eProjectiles)
            projectile.render(delta, modelBatch);
        enemies.render(delta, modelBatch);
        modelBatch.render(planet);
        modelBatch.end();

        batch.begin();
        batch.draw(background, 0, 0, 20, 20);
        batch.end();
    }

    @Override
    public void hide() {
        FileHandler.dispose();
    }

    private void doneLoading() {
        System.out.println("Done");
        loading = false;
        for(int i = 0; i < TDWorld.TOWERS; i++)
            towerModels.add(assets.get("theme/" + TDWorld.themes.get(theme) + "/tower/" + Integer.toString(i) + ".g3db", Model.class));
        for(int i = 0; i < TDWorld.ENEMIES; i++)
            enemyModels.add(assets.get("theme/" + TDWorld.themes.get(theme) + "/enemy/" + Integer.toString(i) + ".g3db", Model.class));
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
        planet = new ModelInstance(assets.get("theme/" + TDWorld.themes.get(theme) + "/planet.g3db", Model.class));
        Gdx.app.log("Planet Size", Float.toString(planet.calculateBoundingBox(new BoundingBox()).getDimensions(new Vector3()).x));
        towers.add(new Gun(new Vector3(0, 0, 0), 50, 50, 0, new ModelInstance(towerModels.get(0))));
        towers.add(new Gun(new Vector3(100, 100, 100), 50, 50, 0, new ModelInstance(towerModels.get(1))));
        //towers.add(new Gun(new Vector3(200, 200, 200), new ModelInstance(towerModels.get(2))));
        ModelInstance instance = new ModelInstance(enemyModels.get(0));
        enemies.add(new Basic(new Vector3(0, 0, 0), 20d, 10, 500,0, instance, new btBoxShape(instance.model.calculateBoundingBox(new BoundingBox()).getDimensions(new Vector3()))));
    }

    public ArrayList<Projectile> getTProjectiles() {
        return tProjectiles;
    }

    public ArrayList<Projectile> getEProjectiles() {
        return eProjectiles;
    }
}
