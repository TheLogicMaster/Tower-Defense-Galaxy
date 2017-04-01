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
import com.badlogic.gdx.physics.bullet.collision.*;
import com.logicmaster63.tdworld.TDWorld;
import com.logicmaster63.tdworld.map.Spawn;
import com.logicmaster63.tdworld.projectiles.Projectile;
import com.logicmaster63.tdworld.tools.*;
import com.logicmaster63.tdworld.map.EnemyHandler;
import com.logicmaster63.tdworld.tools.ContactHandler;
import com.logicmaster63.tdworld.tower.Tower;
import com.logicmaster63.tdworld.tower.basic.Gun;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import static com.logicmaster63.tdworld.TDWorld.font;

public class GameScreen extends TDScreen {

    private SpriteBatch batch;
    private Texture background;
    private int map, theme, planetRadius;
    private ArrayList<Vector3> path;
    private EnemyHandler enemies;
    private ArrayList<Tower> towers;
    private ModelBatch modelBatch;
    private Environment environment;
    private AssetManager assets;
    private HashMap<String, Model> towerModels, enemyModels;
    private boolean loading, hasPlanetModel, running;
    private ModelInstance planet;
    private InputMultiplexer inputMultiplexer;
    private CameraHandler cam;
    private InputHandler inputHandler;
    private ArrayList<Projectile> tProjectiles;
    private ArrayList<Projectile> eProjectiles;
    private Vector3 planetSize, spawnPos;
    private String planetName;
    private HashMap<String, Class<?>> enemyClasses, towerClasses;
    private ArrayList<Spawn> spawns;
    private ArrayList<String> towerNames, enemyNames;
    private btCollisionWorld collisionWorld;
    private btBroadphaseInterface broadphase;
    private btCollisionConfiguration collisionConfig;
    private btDispatcher dispatcher;
    private ArrayList<Integer> ids;
    private ContactHandler contactHandler;

    public GameScreen(Game game, int map, int theme) {
        super(game);
        this.map = map;
        this.theme = theme;
    }

    @Override
    public void show() {
        Bullet.init();

        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        collisionWorld = new btCollisionWorld(dispatcher, broadphase, collisionConfig);

        ids = new ArrayList<Integer>();
        spawns = new ArrayList<Spawn>();
        towerNames = new ArrayList<String>();
        enemyNames = new ArrayList<String>();
        inputHandler = new InputHandler();
        towerClasses = new HashMap<String, Class<?>>();
        enemyClasses = new HashMap<String, Class<?>>();
        towerModels = new HashMap<String, Model>();
        enemyModels = new HashMap<String, Model>();
        tProjectiles = new ArrayList<Projectile>();
        eProjectiles = new ArrayList<Projectile>();
        path = new ArrayList<Vector3>();
        towers = new ArrayList<Tower>();
        modelBatch = new ModelBatch();
        contactHandler = new ContactHandler(ids);

        cam = new CameraHandler(new Vector3(250, 20, 250), 1, 5000);
        inputHandler = new InputHandler();
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(inputHandler);
        inputMultiplexer.addProcessor(cam);
        Gdx.input.setInputProcessor(inputMultiplexer);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        batch = new SpriteBatch();
        batch.getProjectionMatrix().setToOrtho2D(0, 0, 480, 320);
        background = new Texture("Background_MainMenu.png");

        BufferedReader reader = FileHandler.getReader("track/Track" + Integer.toString(map));
        path = FileHandler.loadTrack(reader, this);
        reader = FileHandler.getReader("theme/" + TDWorld.themes.get(theme) + "/PlanetData");
        FileHandler.loadPlanet(reader, this);
        reader = FileHandler.getReader("theme/" + TDWorld.themes.get(theme) + "/EnemyData");
        enemyClasses = FileHandler.loadEnemies(reader, TDWorld.themes.get(theme), this);
        reader = FileHandler.getReader("theme/" + TDWorld.themes.get(theme) + "/TowerData");
        towerClasses = FileHandler.loadTowers(reader, TDWorld.themes.get(theme), this);
        reader = FileHandler.getReader("theme/" + TDWorld.themes.get(theme) + "/SpawnData");
        spawns = FileHandler.loadSpawns(reader);

        assets = new AssetManager();
        assets.load("theme/" + TDWorld.themes.get(theme) + "/planet.g3db", Model.class);
        for (int i = 0; i < TDWorld.TOWERS; i++)
            assets.load("theme/" + TDWorld.themes.get(theme) + "/tower/" + towerNames.get(i) + ".g3db", Model.class);
        for (int i = 0; i < TDWorld.ENEMIES; i++)
            assets.load("theme/" + TDWorld.themes.get(theme) + "/enemy/" + enemyNames.get(i) + ".g3db", Model.class);
        loading = true;
        running = false;
        if (!hasPlanetModel)
            assets.load("theme/" + TDWorld.themes.get(theme) + "/" + planetName + ".png", Texture.class);

        FileHandler.addDisposables(batch, modelBatch, background, broadphase, collisionConfig, dispatcher, collisionWorld);
    }

    @Override
    public void render(float delta) {
        if (loading) {
            if (assets.update())
                doneLoading();
            else
                return;
        }

        for (Tower tower : towers)
            tower.tick(delta);
        for (Projectile projectile : tProjectiles)
            projectile.tick(delta);
        for (Projectile projectile : eProjectiles)
            projectile.tick(delta);
        enemies.tick(delta, this);

        //if(collisionWorld.getNumCollisionObjects() > 0)
            collisionWorld.performDiscreteCollisionDetection();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        cam.update(delta);
        modelBatch.begin(cam.getCam());
        for (Tower tower : towers)
            tower.render(delta, modelBatch);
        for (Projectile projectile : tProjectiles)
            projectile.render(delta, modelBatch);
        for (Projectile projectile : eProjectiles)
            projectile.render(delta, modelBatch);
        enemies.render(delta, modelBatch);
        modelBatch.render(planet);
        modelBatch.end();
        batch.begin();
        //batch.draw(background, 0, 0, 200, 60);
        font.draw(batch, "Size:" + enemies.getEnemies().size(), 0, 20);
        batch.end();
    }

    @Override
    public void hide() {
        FileHandler.dispose();
    }

    private void doneLoading() {
        System.out.println("Done");
        loading = false;
        running = true;
        for (int i = 0; i < TDWorld.TOWERS; i++)
            towerModels.put(towerNames.get(i), assets.get("theme/" + TDWorld.themes.get(theme) + "/tower/" + towerNames.get(i) + ".g3db", Model.class));
        for (int i = 0; i < TDWorld.ENEMIES; i++)
            enemyModels.put(enemyNames.get(i), assets.get("theme/" + TDWorld.themes.get(theme) + "/enemy/" + enemyNames.get(i) + ".g3db", Model.class));

        if (hasPlanetModel) {
            planet = new ModelInstance(assets.get("theme/" + TDWorld.themes.get(theme) + "/planet.g3db", Model.class));
        } else {
            Texture texture = assets.get("theme/" + TDWorld.themes.get(theme) + "/" + planetName + ".png");//new Texture(Gdx.files.internal("Background_MainMenu.png"));
            Material material = new Material(TextureAttribute.createDiffuse(texture), ColorAttribute.createSpecular(1, 1, 1, 1), FloatAttribute.createShininess(8f));
            ModelBuilder builder = new ModelBuilder();
            planet = new ModelInstance(builder.createSphere(planetSize.x, planetSize.y, planetSize.z, 60, 60, material, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates));
        }
        planetRadius = (int) (planet.calculateBoundingBox(new BoundingBox()).getHeight() / 2f);
        btCollisionObject collisionObject = new btCollisionObject();
        collisionObject.setWorldTransform(planet.transform);
        collisionObject.setCollisionShape(new btSphereShape(planetRadius));
        collisionWorld.addCollisionObject(collisionObject);
        FileHandler.addDisposables(collisionObject);

        enemies = new EnemyHandler(spawnPos, enemyClasses, spawns, enemyModels, path, collisionWorld, ids);

        //towers.add(new Gun(new Vector3(0, 0, 0), 50, 50, 0, new ModelInstance(towerModels.get(0))));
        towers.add(new Gun(new Vector3(0, planetRadius, 0), 0, new ModelInstance(towerModels.get("Gun")), collisionWorld, ids));
        //ModelInstance instance = new ModelInstance(enemyModels.get(0));
        //instance.materials.get(0).set(new BlendingAttribute(0.5f));
        //enemies.add(new Spider(new Vector3(0, 0, 0), 20d, 10, 500, 0, instance, new btBoxShape(instance.model.calculateBoundingBox(new BoundingBox()).getDimensions(new Vector3()))));
    }

    public ArrayList<Projectile> getTProjectiles() {
        return tProjectiles;
    }

    public ArrayList<Projectile> getEProjectiles() {
        return eProjectiles;
    }

    public void setPlanetSize(Vector3 planetSize) {
        this.planetSize = planetSize;
    }

    public void setHasPlanetModel(boolean hasPlanetModel) {
        this.hasPlanetModel = hasPlanetModel;
    }

    public void setPlanetName(String name) {
        planetName = name;
    }

    public void setSpawnPos(Vector3 pos) {
        spawnPos = pos;
    }

    public boolean isRunning() {
        return running;
    }

    public void setTowerNames(ArrayList<String> towerNames) {
        this.towerNames = towerNames;
    }

    public void setEnemyNames(ArrayList<String> enemyNames) {
        this.enemyNames = enemyNames;
    }
}

