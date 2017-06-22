package com.logicmaster63.tdgalaxy.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.SpotLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.IntMap;
import com.brummid.vrcamera.RendererForVR;
import com.brummid.vrcamera.VRCameraInputAdapter;
import com.logicmaster63.tdgalaxy.TDGalaxy;
import com.logicmaster63.tdgalaxy.entity.Template;
import com.logicmaster63.tdgalaxy.map.Spawn;
import com.logicmaster63.tdgalaxy.map.region.Region;
import com.logicmaster63.tdgalaxy.map.region.SphereRegion;
import com.logicmaster63.tdgalaxy.map.world.PlanetWorld;
import com.logicmaster63.tdgalaxy.map.world.World;
import com.logicmaster63.tdgalaxy.projectiles.Projectile;
import com.logicmaster63.tdgalaxy.entity.Entity;
import com.logicmaster63.tdgalaxy.tools.*;
import com.logicmaster63.tdgalaxy.entity.ContactHandler;
import com.logicmaster63.tdgalaxy.enemy.EnemySpawner;
import com.logicmaster63.tdgalaxy.tower.Tower;
import com.logicmaster63.tdgalaxy.tower.basic.Gun;
import com.logicmaster63.tdgalaxy.tower.basic.Laser;
import com.logicmaster63.tdgalaxy.ui.CameraHandler;
import com.logicmaster63.tdgalaxy.ui.PlacementCell;
import com.logicmaster63.tdgalaxy.ui.PlacementWindow;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class GameScreen extends TDScreen implements RendererForVR{

    private Texture background, loading;
    private int map, planetRadius;
    private Money money = new Money(100);
    private List<Vector3> path;
    private EnemySpawner enemies;
    private List<Tower> towers;
    private ModelBatch modelBatch;
    private Environment environment;
    private AssetManager assets, externalAssets;
    private Map<String, Model> models;
    private boolean isLoading, hasPlanetModel, running;
    private ModelInstance planet;
    private CameraHandler cam;
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
    private IntMap<Entity> entities;
    private DebugDrawer debugDrawer;
    private ShapeRenderer shapeRenderer;
    private VRCameraInputAdapter vrCameraInputAdapter;
    private World world;
    private Music music;

    public GameScreen(Game game, int map, String theme) {
        super(game);
        this.map = map;
        this.theme = theme;
    }

    @Override
    public void show() {
        super.show();

        Encrypter encrypter = new Encrypter("1234123412341234", "1234123412341234");
        Encrypter.Encryption encryption = encrypter.encrypt("This is a message, y'all");
        System.out.println(encrypter.decryptString(encryption));

        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        collisionWorld = new btCollisionWorld(dispatcher, broadphase, collisionConfig);
        shapeRenderer = new ShapeRenderer();
        addDisposables(shapeRenderer);
        background = new Texture("Background_MainMenu.png");
        loading = new Texture("theme/basic/ui/Loading.png");

        entities = new IntMap<Entity>();

        spawns = new ArrayList<Spawn>();
        cam = new CameraHandler(new Vector3(250, 20, 250), 1, 10000, this);

        addInputProcessor(cam);

        vrCameraInputAdapter = new VRCameraInputAdapter(cam.getVRCam());
        //vrCameraInputAdapter.setLogging(try();
        classes = new HashMap<String, Class<?>>();
        models = new HashMap<String, Model>();
        projectiles = new ArrayList<Projectile>();
        path = new ArrayList<Vector3>();
        towers = new ArrayList<Tower>();
        modelBatch = new ModelBatch();

        debugDrawer = new DebugDrawer();
        collisionWorld.setDebugDrawer(debugDrawer);
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
        contactHandler = new ContactHandler(entities, planet);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(1f, 1f, 1f, -1f, -0.8f, -0.2f));
        //environment.add(new SpotLight().set(Color.RED, 200, 200, 200, -1, -1, -1, 1, 130, 100));

        BufferedReader reader = FileHandler.getReader("track/Track" + Integer.toString(map));
        path = FileHandler.loadTrack(reader, this);

        reader = FileHandler.getReader("theme/" + theme + "/PlanetData");
        FileHandler.loadPlanet(reader, this);

        classes.putAll(FileHandler.loadClasses("com.logicmaster63.tdgalaxy.tower.basic"));
        classes.putAll(FileHandler.loadClasses("com.logicmaster63.tdgalaxy.enemy.basic"));
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
        //assets.load("Transmission.wav", Music.class);
        externalAssets = TDGalaxy.fileStuff.getExternalAssets();
        if(externalAssets != null) {
            externalAssets.load("X.png", Texture.class);
            externalAssets.load("Transmission.mp3", Music.class);
            externalAssets.load("Jump Up.mp3", Music.class);
            externalAssets.load("Crystal Waters.mp3", Music.class);
        }

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

        isLoading = true;
        running = false;

        if (!hasPlanetModel && planetName != null)
            assets.load("theme/" + theme + "/" + planetName + ".png", Texture.class);
        if(planetName == null)
            planetName = "planet";
        addDisposables(spriteBatch, modelBatch, background, broadphase, collisionConfig, dispatcher, collisionWorld, debugDrawer);
    }

    @Override
    public void renderForVR(PerspectiveCamera perspectiveCamera) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(perspectiveCamera);
        shapeRenderer.setProjectionMatrix(perspectiveCamera.combined);
        for(IntMap.Entry<Entity> entry: entities.entries())
            entry.value.render(Gdx.graphics.getDeltaTime(), modelBatch, shapeRenderer, environment);
        modelBatch.render(planet, environment);
        modelBatch.end();
    }

    @Override
    public void render(float delta) {
        orthographicCamera.update();
        spriteBatch.setProjectionMatrix(orthographicCamera.combined);
        if (isLoading) {
            if (assets.update() && (externalAssets == null || externalAssets.update()))
                doneLoading();
            else {
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                float progress;
                if(externalAssets == null)
                    progress = assets.getProgress();
                else
                    progress = 0.5f * assets.getProgress() + 0.5f * externalAssets.getProgress();
                spriteBatch.begin();
                spriteBatch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
                spriteBatch.draw(loading, viewport.getWorldWidth() / 2 - 200, viewport.getWorldHeight() / 4 - 50, 400, 100, 0, 0.5f, 1, 1);
                spriteBatch.draw(loading, viewport.getWorldWidth() / 2 - 200 + 400f * progress, viewport.getWorldHeight() / 4 - 50, 400 - progress * 400f, 100, progress, 0, 1, 0.5f);
                Color color = new Color(TDGalaxy.getFonts().get("moonhouse64").getColor());
                TDGalaxy.getFonts().get("moonhouse64").setColor(Color.BLACK);
                TDGalaxy.getFonts().get("moonhouse64").draw(spriteBatch, "Loading...", viewport.getWorldWidth() / 2 - 170, viewport.getWorldHeight() / 4 + 30);
                TDGalaxy.getFonts().get("moonhouse64").setColor(color);
                spriteBatch.end();
                return;
            }
        }
        for(IntMap.Entry<Entity> entry: entities.entries()) {
            entry.value.tick(delta);
            if(entry.value.isDead) {
                entry.value.destroy();
            }
        }

        enemies.tick(delta, this);

        collisionWorld.performDiscreteCollisionDetection();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        spriteBatch.begin();
        //spriteBatch.draw(background, 0, 0, 200, 60);
        spriteBatch.end();

        if(Gdx.graphics.getDeltaTime() > 0)
            vrCameraInputAdapter.update(Gdx.graphics.getDeltaTime());
        cam.update(delta);
        cam.render(spriteBatch);

        if(TDGalaxy.isDebug()) {
            debugDrawer.begin(cam.getCam());
            collisionWorld.debugDrawWorld();
            debugDrawer.end();
        }

        spriteBatch.begin();
        //TDWorld.getFonts().get("moonhouse32").draw(spriteBatch, "Size:" + entities.size(), 0, 20);
        //TDWorld.getFonts().get("moonhouse64").draw(spriteBatch, "Num:" + collisionWorld.getNumCollisionObjects(), 0, 40);
        TDGalaxy.getFonts().get("moonhouse64").draw(spriteBatch, "$" + money.$, 0, viewport.getWorldHeight() - 30);
        spriteBatch.end();

        super.render(delta);
    }

    @Override
    public void hide() {
        dispose();
    }

    private void doneLoading() {
        isLoading = false;
        running = true;

        background = externalAssets.get("X.png", Texture.class);
        music = externalAssets.get("Transmission.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(1);
        music.play();

        for(Class clazz: new ArrayList<Class>(classes.values())) {
            try {
                //clazz.getMethod("getAssets", null);
                Method method = clazz.getMethod("getAssets");
                ArrayList<com.logicmaster63.tdgalaxy.tools.Asset> assetsList = null;
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
        ArrayList<Region> regions = new ArrayList<Region>();
        regions.add(new SphereRegion(new Vector3(), planetRadius));
        world = new PlanetWorld(regions, collisionWorld);
        //System.out.println(planet.calculateBoundingBox(new BoundingBox()).getHeight());
        addDisposables(collisionObject);
        //if(models.containsKey("Basic"))
            //for(int i = 0; i < models.get("Basic").animations.size; i++)
                //System.out.println(models.get("Basic").animations.get(i).id);
        enemies = new EnemySpawner(spawnPos, classes, spawns, models, path, collisionWorld, entities);

        //towers.add(new Gun(new Vector3(0, 0, 0), 50, 50, 0, new ModelInstance(models.get(0))));
        try {
            if(models.containsKey("Laser"))//new Vector3(0, planetRadius + 10, 0)
                towers.add(new Laser(new Matrix4().setToTranslation(new Vector3(0, planetRadius + 10, 0)), models, collisionWorld, entities));
            if(models.containsKey("Gun"))
                towers.add(new Gun(new Matrix4().setToTranslation(new Vector3(100, planetRadius + 100, 0)), models, collisionWorld, entities));

        } catch (NoSuchMethodException e) {
            Gdx.app.error("Gamescreen add towers", e.toString());
        }
         //ModelInstance instance = new ModelInstance(models.get(0));
        //instance.materials.get(0).set(new BlendingAttribute(0.5f));
        //enemies.add(new Spider(new Vector3(0, 0, 0), 20d, 10, 500, 0, instance, new btBoxShape(instance.model.calculateBoundingBox(new BoundingBox()).getDimensions(new Vector3()))));
        PlacementCell[][] placementCells = new PlacementCell[2][2];
        try {
            placementCells[0][0] = new PlacementCell(background, models.get("Gun"), new Template(Gun.class.getConstructors()[1], models, collisionWorld, entities), (Integer) classes.get("Gun").getField("PRICE").get(null));
            placementCells[1][0] = new PlacementCell(background, models.get("Laser"), new Template(Laser.class.getConstructors()[1], models, collisionWorld, entities), (Integer)classes.get("Laser").getField("PRICE").get(null));
        } catch (NoSuchFieldException e) {
            Gdx.app.error("Create placement window", e.toString());
        } catch (IllegalAccessException e) {
            Gdx.app.error("Create placement window", e.toString());
        }
        stage.addActor(new PlacementWindow(0, 0, 400, 400, 2, 2, placementCells, collisionWorld, cam.getCam(), modelBatch, environment, world, money));
    }

    public Camera getCamera() {
        return cam.getCam();
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