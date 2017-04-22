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
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.logicmaster63.tdworld.TDWorld;
import com.logicmaster63.tdworld.map.Spawn;
import com.logicmaster63.tdworld.projectiles.Projectile;
import com.logicmaster63.tdworld.tools.*;
import com.logicmaster63.tdworld.tools.EnemyHandler;
import com.logicmaster63.tdworld.tools.ContactHandler;
import com.logicmaster63.tdworld.object.Object;
import com.logicmaster63.tdworld.tower.Tower;
import com.logicmaster63.tdworld.tower.basic.Gun;
import com.logicmaster63.tdworld.tower.basic.Laser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.logicmaster63.tdworld.TDWorld.isDebugging;

public class GameScreen extends TDScreen {

    private SpriteBatch batch;
    private Texture background;
    private int map, planetRadius;
    private List<Vector3> path;
    private EnemyHandler enemies;
    private List<Tower> towers;
    private ModelBatch modelBatch;
    private Environment environment;
    private AssetManager assets;
    private Map<String, Model> models;
    private boolean loading, hasPlanetModel, running;
    private ModelInstance planet;
    private InputMultiplexer inputMultiplexer;
    private CameraHandler cam;
    private InputHandler inputHandler;
    private List<Projectile> projectiles;
    private Vector3 planetSize, spawnPos;
    private String planetName, theme, debugString = "DEFAULT";
    private Map<String, Class<?>> classes;
    private List<Spawn> spawns;
    private List<String> towerNames, enemyNames;
    private btCollisionWorld collisionWorld;
    private btBroadphaseInterface broadphase;
    private btCollisionConfiguration collisionConfig;
    private btDispatcher dispatcher;
    private ContactHandler contactHandler;
    private Map<Integer, Object> objects;
    private DebugDrawer debugDrawer;

    public GameScreen(Game game, int map, String theme) {
        super(game);
        this.map = map;
        this.theme = theme;
    }

    @Override
    public void show() {
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        collisionWorld = new btCollisionWorld(dispatcher, broadphase, collisionConfig);

        objects = new HashMap<Integer, Object>();
        spawns = new ArrayList<Spawn>();
        towerNames = new ArrayList<String>();
        enemyNames = new ArrayList<String>();
        cam = new CameraHandler(new Vector3(250, 20, 250), 1, 5000);
        inputHandler = new InputHandler(cam);
        classes = new HashMap<String, Class<?>>();
        models = new HashMap<String, Model>();
        models = new HashMap<String, Model>();
        projectiles = new ArrayList<Projectile>();
        path = new ArrayList<Vector3>();
        towers = new ArrayList<Tower>();
        modelBatch = new ModelBatch();
        debugDrawer = new DebugDrawer();
        collisionWorld.setDebugDrawer(debugDrawer);
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
        contactHandler = new ContactHandler(objects, planet);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(inputHandler);
        Gdx.input.setInputProcessor(inputMultiplexer);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        batch = new SpriteBatch();
        batch.getProjectionMatrix().setToOrtho2D(0, 0, 480, 320);
        background = new Texture("Background_MainMenu.png");

        BufferedReader reader = FileHandler.getReader("track/Track" + Integer.toString(map));
        path = FileHandler.loadTrack(reader, this);
        reader = FileHandler.getReader("theme/" + theme + "/PlanetData");
        FileHandler.loadPlanet(reader, this);
        reader = FileHandler.getReader("theme/" + theme + "/EnemyData");
        Map<String, Class<?>> hashMap = FileHandler.loadEnemies(reader, theme, this);
        if(hashMap != null)
            classes.putAll(hashMap);
        reader = FileHandler.getReader("theme/" + theme + "/TowerData");
        hashMap = FileHandler.loadTowers(reader, theme, this);
        if(hashMap != null)
            classes.putAll(hashMap);
        FileHandler.loadDependencies(classes);
        reader = FileHandler.getReader("theme/" + theme + "/SpawnData");
        spawns = FileHandler.loadSpawns(reader);
        try {
            reader.close();
        } catch (IOException e) {
            Gdx.app.log("Error", e.toString());
        }

        assets = new AssetManager();
        assets.load("theme/" + theme + "/enemy/" + "/Spider.g3dj", Model.class);
        assets.load("theme/" + theme + "/planet.g3db", Model.class);
        for (int i = 0; i < towerNames.size(); i++)
            assets.load("theme/" + theme + "/tower/" + towerNames.get(i) + ".g3db", Model.class);
        for (int i = 0; i < enemyNames.size(); i++)
            assets.load("theme/" + theme + "/enemy/" + enemyNames.get(i) + ".g3db", Model.class);
        loading = true;
        running = false;
        if (!hasPlanetModel && planetName != null)
            assets.load("theme/" + theme + "/" + planetName + ".png", Texture.class);
        if(planetName == null)
            planetName = "planet";
        FileHandler.addDisposables(batch, modelBatch, background, broadphase, collisionConfig, dispatcher, collisionWorld, debugDrawer);
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
        for (Projectile projectile : projectiles)
            projectile.tick(delta);
        enemies.tick(delta, this);

        collisionWorld.performDiscreteCollisionDetection();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        inputHandler.update(delta);
        modelBatch.begin(cam.getCam());
        for (Tower tower : towers)
            tower.render(delta, modelBatch);
        for (Projectile projectile : projectiles)
            projectile.render(delta, modelBatch);
        enemies.render(delta, modelBatch);
        modelBatch.render(planet);
        modelBatch.end();

        if(isDebugging) {
            debugDrawer.begin(cam.getCam());
            collisionWorld.debugDrawWorld();
            debugDrawer.end();
        }

        batch.begin();
        //batch.draw(background, 0, 0, 200, 60);
        TDWorld.getFonts().get("pixelade").draw(batch, "Size:" + enemies.getEnemies().size(), 0, 20);
        if(TDWorld.isDebugging)
            TDWorld.getFonts().get("pixelade").draw(batch, debugString, 0, 40);
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
        for (int i = 0; i < towerNames.size(); i++)
            models.put(towerNames.get(i), assets.get("theme/" + theme + "/tower/" + towerNames.get(i) + ".g3db", Model.class));
        for (int i = 0; i < enemyNames.size(); i++)
            models.put(enemyNames.get(i), assets.get("theme/" + theme + "/enemy/" + enemyNames.get(i) + ".g3db", Model.class));
        if (hasPlanetModel) {
            planet = new ModelInstance(assets.get("theme/" + theme + "/planet.g3db", Model.class));
        } else {
            Texture texture;
            if(assets.isLoaded("theme/" + theme + "/" + planetName + ".png"))
                texture = assets.get("theme/" + theme + "/" + planetName + ".png");//new Texture(Gdx.files.internal("Background_MainMenu.png"));
            else
                texture = null;
            Material material = new Material(TextureAttribute.createDiffuse(texture), ColorAttribute.createSpecular(1, 1, 1, 1), FloatAttribute.createShininess(8f));
            ModelBuilder builder = new ModelBuilder();
            if(planetSize == null)
                planetSize = new Vector3(100, 100, 100);
            planet = new ModelInstance(builder.createSphere(planetSize.x, planetSize.y, planetSize.z, 60, 60, material, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates));
        }
        planetRadius = (int)(planet.calculateBoundingBox(new BoundingBox()).getHeight() / 2f);
        btCollisionObject collisionObject = new btCollisionObject();
        collisionObject.setCollisionShape(new btSphereShape(planetRadius));
        collisionObject.setWorldTransform(planet.transform);
        collisionWorld.addCollisionObject(collisionObject);
        //System.out.println(planet.calculateBoundingBox(new BoundingBox()).getHeight());
        FileHandler.addDisposables(collisionObject);
        for(int i = 0; i < models.get("Basic").animations.size; i++)
            System.out.println(models.get("Basic").animations.get(i).id);
        enemies = new EnemyHandler(spawnPos, classes, spawns, models, path, collisionWorld, objects);

        //towers.add(new Gun(new Vector3(0, 0, 0), 50, 50, 0, new ModelInstance(models.get(0))));
        if(models.containsKey("Laser"))
            towers.add(new Laser(new Vector3(0, planetRadius + 10, 0), 0, new ModelInstance(models.get("Laser")), collisionWorld, objects));
        if(models.containsKey("Gun"))
            towers.add(new Gun(new Vector3(0, planetRadius + 100, 0), 0, new ModelInstance(models.get("Gun")), collisionWorld, objects));
        //ModelInstance instance = new ModelInstance(models.get(0));
        //instance.materials.get(0).set(new BlendingAttribute(0.5f));
        //enemies.add(new Spider(new Vector3(0, 0, 0), 20d, 10, 500, 0, instance, new btBoxShape(instance.model.calculateBoundingBox(new BoundingBox()).getDimensions(new Vector3()))));
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
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

    public void setTowerNames(List<String> towerNames) {
        this.towerNames = towerNames;
    }

    public void setEnemyNames(List<String> enemyNames) {
        this.enemyNames = enemyNames;
    }

    public String getDebugString() {
        return debugString;
    }

    public void setDebugString(String debugString) {
        this.debugString = debugString;
    }
}

