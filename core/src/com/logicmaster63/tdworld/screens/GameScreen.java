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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.viewport.*;
import com.google.common.reflect.ClassPath;
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
import java.lang.reflect.Method;
import java.util.*;

import static com.logicmaster63.tdworld.TDWorld.isDebugging;

public class GameScreen extends TDScreen{

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
    private String planetName, theme;
    private Map<String, Class<?>> classes;
    private List<Spawn> spawns;
    private btCollisionWorld collisionWorld;
    private btBroadphaseInterface broadphase;
    private btCollisionConfiguration collisionConfig;
    private btDispatcher dispatcher;
    private ContactHandler contactHandler;
    private Map<Integer, Object> objects;
    private DebugDrawer debugDrawer;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;

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
        shapeRenderer = new ShapeRenderer();
        FileHandler.addDisposables(shapeRenderer);

        objects = new HashMap<Integer, Object>();
        spawns = new ArrayList<Spawn>();
        cam = new CameraHandler(new Vector3(250, 20, 250), 1, 5000);
        //viewport = new StretchViewport(100, 100, cam.getCam());
        //viewport.apply();
        inputHandler = new InputHandler(cam);
        classes = new HashMap<String, Class<?>>();
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

        classes.putAll(FileHandler.loadClasses("com.logicmaster63.tdworld.tower.basic"));
        classes.putAll(FileHandler.loadClasses("com.logicmaster63.tdworld.enemy.basic"));
        FileHandler.loadDependencies(classes);

        reader = FileHandler.getReader("theme/" + theme + "/SpawnData");
        spawns = FileHandler.loadSpawns(reader);
        try {
            reader.close();
        } catch (IOException e) {
            Gdx.app.log("Error", e.toString());
        }

        assets = new AssetManager();
        assets.load("theme/" + theme + "/planet.g3db", Model.class);

        for(Class<?> clazz: new ArrayList<Class>(classes.values())) {
            try {
                Method method = clazz.getMethod("getAssets");
                ArrayList<Asset> assetsList = null;
                if(method != null)
                    assetsList = (ArrayList<Asset>) method.invoke(null);
                if(assetsList != null)
                    for(Asset asset: assetsList)
                        assets.load(asset.getPath(), asset.getClazz());
            } catch (Exception e) {
                Gdx.app.error("GameScreen", e.toString());
            }
        }

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

        Object objectArray[] = objects.values().toArray(new Object[]{});
        for(Object object: objectArray) {
            object.tick(delta);
        }
        enemies.tick(delta, this);

        collisionWorld.performDiscreteCollisionDetection();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        inputHandler.update(delta);
        modelBatch.begin(cam.getCam());
        shapeRenderer.setProjectionMatrix(cam.getCam().combined);
        objectArray = objects.values().toArray(new Object[]{});
        for(Object object: objectArray)
            object.render(delta, modelBatch, shapeRenderer);
        modelBatch.render(planet);
        modelBatch.end();

        if(isDebugging) {
            debugDrawer.begin(cam.getCam());
            collisionWorld.debugDrawWorld();
            debugDrawer.end();
        }

        batch.begin();
        //batch.draw(background, 0, 0, 200, 60);
        TDWorld.getFonts().get("moonhouse").draw(batch, "Size:" + objects.size(), 0, 20);
        TDWorld.getFonts().get("moonhouse").draw(batch, "Num:" + collisionWorld.getNumCollisionObjects(), 0, 40);
        batch.end();

        super.render(delta);
    }

    @Override
    public void hide() {
        FileHandler.dispose();
    }

    private void doneLoading() {
        System.out.println("Done");
        loading = false;
        running = true;
        for(Class clazz: new ArrayList<Class>(classes.values())) {
            try {
                //clazz.getMethod("getAssets", null);
                Method method = clazz.getMethod("getAssets");
                ArrayList<Asset> assetsList = null;
                if(method != null)
                    assetsList = (ArrayList<Asset>) method.invoke(null);
                if(assetsList != null)
                    for(Asset asset: assetsList)
                        models.put(clazz.getSimpleName(), (Model) assets.get(asset.getPath(), asset.getClazz()));
                        //assets.load(asset.getPath(), asset.getClazz());
            } catch (Exception e) {
                Gdx.app.error("GameScreen", e.toString());
            }
        }
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
        collisionObject.setUserValue(0);
        collisionWorld.addCollisionObject(collisionObject);
        //System.out.println(planet.calculateBoundingBox(new BoundingBox()).getHeight());
        FileHandler.addDisposables(collisionObject);
        if(models.containsKey("Basic"))
            for(int i = 0; i < models.get("Basic").animations.size; i++)
                System.out.println(models.get("Basic").animations.get(i).id);
        enemies = new EnemyHandler(spawnPos, classes, spawns, models, path, collisionWorld, objects);

        //towers.add(new Gun(new Vector3(0, 0, 0), 50, 50, 0, new ModelInstance(models.get(0))));
        if(models.containsKey("Laser"))
            towers.add(new Laser(new Vector3(0, planetRadius + 10, 0), new ModelInstance(models.get("Laser")), collisionWorld, objects, false));
        if(models.containsKey("Gun"))
            towers.add(new Gun(new Vector3(100, planetRadius + 100, 0), 0, new ModelInstance(models.get("Gun")), new ModelInstance(models.get("Bullet")),collisionWorld, objects, false));
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
}

