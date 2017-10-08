package com.logicmaster63.tdgalaxy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.batches.PointSpriteParticleBatch;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.brummid.vrcamera.RendererForVR;
import com.brummid.vrcamera.VRCameraInputAdapter;
import com.logicmaster63.tdgalaxy.TDGalaxy;
import com.logicmaster63.tdgalaxy.constants.Source;
import com.logicmaster63.tdgalaxy.entity.EntityTemplate;
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
import com.logicmaster63.tdgalaxy.tower.basic.Gun;
import com.logicmaster63.tdgalaxy.tower.basic.Laser;
import com.logicmaster63.tdgalaxy.ui.CameraHandler;
import com.logicmaster63.tdgalaxy.ui.PauseWindow;
import com.logicmaster63.tdgalaxy.ui.PlacementCell;
import com.logicmaster63.tdgalaxy.ui.PlacementWindow;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;

public class GameScreen extends TDScreen implements RendererForVR, InputProcessor {

    private Texture background, loading;
    private int map, planetRadius;
    private Money money = new Money(100);
    private List<Vector3> path;
    private EnemySpawner enemies;
    private ModelBatch modelBatch;
    private Environment environment;
    private AssetManager assets, externalAssets;
    private Map<String, Model> models;
    private boolean isLoading, hasPlanetModel, paused = false;
    private ModelInstance planet;
    private CameraHandler camHandler;
    private List<Projectile> projectiles;
    private Vector3 planetSize, spawnPos;
    private String planetName;
    private String theme;
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
    private Map<String, Sound> sounds;
    private Set<Pool> pools;
    private ParticleSystem particleSystem;
    private PauseWindow pauseWindow;
    private Button button;

    public GameScreen(TDGalaxy game, int map, String theme) {
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
        background = new Texture("Background_MainMenu.png");
        loading = new Texture("theme/basic/ui/Loading.png");
        entities = new IntMap<Entity>();
        spawns = new ArrayList<Spawn>();
        camHandler = new CameraHandler(new Vector3(250, 20, 250), game, 1, 10000, this);

        addInputProcessor(camHandler);
        addInputProcessor(this);

        vrCameraInputAdapter = new VRCameraInputAdapter(camHandler.getVRCam());

        classes = new HashMap<String, Class<?>>();
        models = new HashMap<String, Model>();
        projectiles = new ArrayList<Projectile>();
        path = new ArrayList<Vector3>();
        modelBatch = new ModelBatch();
        sounds = new HashMap<String, Sound>();
        pools = new HashSet<Pool>();

        debugDrawer = new DebugDrawer();
        collisionWorld.setDebugDrawer(debugDrawer);
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
        contactHandler = new ContactHandler(entities, planet);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(1f, 1f, 1f, -1f, -0.8f, -0.2f));
        //environment.add(new SpotLight().set(Color.RED, 200, 200, 200, -1, -1, -1, 1, 130, 100));

        BufferedReader reader = FileHandler.getReader("track/Track" + Integer.toString(map));
        path = FileHandler.loadTrack(reader, game, this);

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
            Gdx.app.error("Error", e.toString());
        }

        particleSystem = ParticleSystem.get();
        PointSpriteParticleBatch pointSpriteBatch = new PointSpriteParticleBatch();
        pointSpriteBatch.setCamera(camHandler.getCam());
        particleSystem.add(pointSpriteBatch);
        Pools.get(ParticleEffect.class);

        assets = new AssetManager();
        assets.load("theme/" + theme + "/planet.g3db", Model.class);
        //assets.load("Transmission.wav", Music.class);
        externalAssets = game.getGameAssets();
        externalAssets.load("X.png", Texture.class);
        externalAssets.load("Transmission.mp3", Music.class);
        //externalAssets.load("Jump Up.mp3", Music.class);
        //externalAssets.load("Crystal Waters.mp3", Music.class);

        for (Class<?> clazz : new ArrayList<Class>(classes.values())) {
            try {
                Method method = clazz.getMethod("getAssets");
                ArrayList<Asset> assetsList = null;
                if (method != null)
                    assetsList = new ArrayList<Asset>((List<Asset>) method.invoke(null));
                if (assetsList != null)
                    for (Asset asset : assetsList)
                        getAssetManager(asset.source).load(asset.path, asset.clazz);
            } catch (Exception e) {
                Gdx.app.error("GameScreen", e.toString());
            }
        }

        isLoading = true;

        if (!hasPlanetModel && planetName != null)
            assets.load("theme/" + theme + "/" + planetName + ".png", Texture.class);
        if (planetName == null)
            planetName = "planet";
        addDisposables(spriteBatch, modelBatch, background, collisionWorld, broadphase, collisionConfig, dispatcher, debugDrawer, shapeRenderer, assets);
    }

    @Override
    public void renderForVR(PerspectiveCamera perspectiveCamera) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(perspectiveCamera);
        shapeRenderer.setProjectionMatrix(perspectiveCamera.combined);
        for (IntMap.Entry<Entity> entry : entities.entries())
            entry.value.render(Gdx.graphics.getDeltaTime(), modelBatch, shapeRenderer, environment);
        modelBatch.render(planet, environment);
        modelBatch.end();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        orthographicCamera.update();
        spriteBatch.setProjectionMatrix(orthographicCamera.combined);
        if (isLoading) {
            if (assets.update() && (externalAssets == null || externalAssets.update()))
                doneLoading();
            else {
                float progress;
                if (externalAssets == null)
                    progress = assets.getProgress();
                else
                    progress = 0.5f * assets.getProgress() + 0.5f * externalAssets.getProgress();
                spriteBatch.begin();
                spriteBatch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
                spriteBatch.draw(loading, viewport.getWorldWidth() / 2 - 200, viewport.getWorldHeight() / 4 - 50, 400, 100, 0, 0.5f, 1, 1);
                spriteBatch.draw(loading, viewport.getWorldWidth() / 2 - 200 + 400f * progress, viewport.getWorldHeight() / 4 - 50, 400 - progress * 400f, 100, progress, 0, 1, 0.5f);
                Color color = new Color(game.getFonts().get("moonhouse64").getColor());
                game.getFonts().get("moonhouse64").setColor(Color.BLACK);
                game.getFonts().get("moonhouse64").draw(spriteBatch, "Loading...", viewport.getWorldWidth() / 2 - 170, viewport.getWorldHeight() / 4 + 30);
                game.getFonts().get("moonhouse64").setColor(color);
                spriteBatch.end();
                return;
            }
        }

        if (!paused) {
            /*for (IntMap.Entry<Entity> entry : entities.entries()) {
                entry.value.tick(delta);
                if (entry.value.isDead) {
                    entry.value.destroy();
                }
            }*/
            Iterator<IntMap.Entry<Entity>> iterator = entities.entries().iterator();

            while (iterator.hasNext()) {
                IntMap.Entry<Entity> entry = iterator.next();
                entry.value.tick(delta);
                if (entry.value.isDead) {
                    iterator.remove();
                }
            }
            enemies.tick(delta, this);

            collisionWorld.performDiscreteCollisionDetection();

            if (Gdx.graphics.getDeltaTime() > 0)
                vrCameraInputAdapter.update(Gdx.graphics.getDeltaTime());
            camHandler.update(delta);
        }

        spriteBatch.begin();
        //spriteBatch.draw(background, 0, 0, 200, 60);
        spriteBatch.end();
        camHandler.render(spriteBatch);

        if (game.preferences.isDebug()) {
            debugDrawer.begin(camHandler.getCam());
            collisionWorld.debugDrawWorld();
            debugDrawer.end();
        }

        spriteBatch.begin();
        //TDWorld.getFonts().get("moonhouse32").draw(spriteBatch, "Size:" + entities.size(), 0, 20);
        //TDWorld.getFonts().get("moonhouse64").draw(spriteBatch, "Num:" + collisionWorld.getNumCollisionObjects(), 0, 40);
        game.getFonts().get("moonhouse64").draw(spriteBatch, "$" + money.$, 0, viewport.getWorldHeight() - 30);
        game.getFonts().get("moonhouse64").draw(spriteBatch, Gdx.graphics.getFramesPerSecond() + " FPS", 0, viewport.getWorldHeight() - 80);
        game.getFonts().get("moonhouse64").draw(spriteBatch, entities.size + " Entities", 0, viewport.getWorldHeight() - 130);

        spriteBatch.end();

        super.render(delta);
    }

    @Override
    public void hide() {
        dispose();
    }

    private void doneLoading() {
        isLoading = false;

        music = externalAssets.get("Transmission.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(game.preferences.getMusicVolumeCombined());
        music.play();

        getAssetMap(externalAssets, Sound.class, sounds);
        getAssetMap(assets, Model.class, models);

        if (hasPlanetModel) {
            planet = new ModelInstance(assets.get("theme/" + theme + "/planet.g3db", Model.class));
        } else {
            Texture texture;
            if (assets.isLoaded("theme/" + theme + "/" + planetName + ".png"))
                texture = assets.get("theme/" + theme + "/" + planetName + ".png");//new Texture(Gdx.files.internal("Background_MainMenu.png"));
            else
                texture = null;
            Material material = new Material(TextureAttribute.createDiffuse(texture), ColorAttribute.createSpecular(1, 1, 1, 1), FloatAttribute.createShininess(8f));
            ModelBuilder builder = new ModelBuilder();
            if (planetSize == null)
                planetSize = new Vector3(100, 100, 100);
            planet = new ModelInstance(builder.createSphere(planetSize.x, planetSize.y, planetSize.z, 60, 60, material, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates));
        }

        planetRadius = (int) (planet.calculateBoundingBox(new BoundingBox()).getHeight() / 2f);
        btCollisionObject collisionObject = new btCollisionObject();
        collisionObject.setCollisionShape(new btSphereShape(planetRadius));
        collisionObject.setWorldTransform(planet.transform);
        collisionObject.setUserValue(0);
        collisionWorld.addCollisionObject(collisionObject);
        ArrayList<Region> regions = new ArrayList<Region>();
        regions.add(new SphereRegion(new Vector3(), planetRadius));
        world = new PlanetWorld(regions, collisionWorld);
        //addDisposables(collisionObject);

        enemies = new EnemySpawner(spawnPos, classes, spawns, models, path, collisionWorld, entities, sounds);

        try {
            if (models.containsKey("Laser"))//new Vector3(0, planetRadius + 10, 0)
                new Laser(new Matrix4().setToTranslation(new Vector3(0, planetRadius + 10, 0)), models, collisionWorld, entities, sounds);
            if (models.containsKey("Gun"))
                new Gun(new Matrix4().setToTranslation(new Vector3(100, planetRadius + 100, 0)), models, collisionWorld, entities, sounds);
        } catch (NoSuchMethodException e) {
            Gdx.app.error("Gamescreen add towers", e.toString());
        }

        PlacementCell[][] placementCells = new PlacementCell[2][2];
        try {
            placementCells[0][0] = new PlacementCell(background, models.get("Gun"), new EntityTemplate(Gun.class.getConstructors()[1], models, collisionWorld, entities, sounds), (Integer) classes.get("Gun").getField("PRICE").get(null));
            placementCells[1][0] = new PlacementCell(background, models.get("Laser"), new EntityTemplate(Laser.class.getConstructors()[1], models, collisionWorld, entities, sounds), (Integer) classes.get("Laser").getField("PRICE").get(null));
        } catch (NoSuchFieldException e) {
            Gdx.app.error("Create placement window", e.toString());
        } catch (IllegalAccessException e) {
            Gdx.app.error("Create placement window", e.toString());
        }
        stage.addActor(new PlacementWindow(0, 0, 400, 400, 2, 2, placementCells, collisionWorld, camHandler.getCam(), modelBatch, environment, world, money));

        button = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("theme/basic/ui/menuButton.png"))), new TextureRegionDrawable(new TextureRegion(new Texture("theme/basic/ui/menuButtonOver.png"))));
        button.setBounds(viewport.getWorldWidth() - 300, viewport.getWorldHeight() - 300, 200, 200);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (paused)
                    unpauseGame();
                else
                    pauseGame();
            }
        });
        stage.addActor(button);
    }

    private AssetManager getAssetManager(Source source) {
        switch (source) {
            case EXTERNAL:
                return externalAssets;
            case INTERNAL:
                return assets;
            case ZIP:
                return assets;
            default:
                return assets;
        }
    }

    private <T> Map<String, T> getAssetMap(AssetManager assetManager, Class<T> clazz, Map<String, T> map) {
        for (T asset : assetManager.getAll(clazz, new Array<T>())) {
            String name = assetManager.getAssetFileName(asset);
            map.put(name.substring(name.lastIndexOf("/") + 1, name.indexOf(".")), asset);
        }
        return map;
    }

    private void pauseGame() {
        if (paused)
            return;
        paused = true;

        final Window.WindowStyle pauseWindowStyle = new Window.WindowStyle();
        pauseWindowStyle.titleFont = game.getFonts().get("moonhouse64");
        pauseWindowStyle.background = new TextureRegionDrawable(new TextureRegion(background));

        pauseWindow = new PauseWindow(pauseWindowStyle, game.getFonts().get("moonhouse64"), new Runnable() {
            @Override
            public void run() {
                unpauseGame();
            }
        }, new Runnable() {
            @Override
            public void run() {
                save();
            }
        }, game, uiAssets, button);
        pauseWindow.setBounds(viewport.getWorldWidth() / 2 - viewport.getWorldWidth() / 3, viewport.getWorldHeight() / 2 - viewport.getWorldHeight() / 3, viewport.getWorldWidth() / 3 * 2, viewport.getWorldHeight() / 3 * 2);
        stage.addActor(pauseWindow);
    }

    private void unpauseGame() {
        if (pauseWindow != null)
            pauseWindow.resume();
        paused = false;
    }

    private void save() {

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE)
            if (paused)
                unpauseGame();
            else
                pauseGame();
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
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public Camera getCamera() {
        return camHandler.getCam();
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

    public boolean isPaused() {
        return paused;
    }
}