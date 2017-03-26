package com.logicmaster63.tdworld.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.logicmaster63.tdworld.TDWorld;
import com.logicmaster63.tdworld.enemy.basic.Spider;
import com.logicmaster63.tdworld.projectiles.Projectile;
import com.logicmaster63.tdworld.tools.FileHandler;
import com.logicmaster63.tdworld.map.EnemyHandler;
import com.logicmaster63.tdworld.tools.CameraHandler;
import com.logicmaster63.tdworld.tools.InputHandler;
import com.logicmaster63.tdworld.tower.Tower;
import com.logicmaster63.tdworld.tower.basic.Gun;

import java.io.*;
import java.util.ArrayList;

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
    private ArrayList<Model> towerModels, enemyModels;
    private boolean loading;
    private ModelInstance planet;
    private InputMultiplexer inputMultiplexer;
    private CameraHandler cam;
    private InputHandler inputHandler;
    private ArrayList<Projectile> tProjectiles;
    private ArrayList<Projectile> eProjectiles;
    private Vector3 planetSize, spawnPos;
    private boolean hasPlanetModel;
    private String planetName;
    private ArrayList<Class<?>> towerClasses, enemyClasses;

    public GameScreen(Game game, int map, int theme) {
        super(game);
        this.map = map;
        this.theme = theme;
    }

    @Override
    public void show() {
        Bullet.init();

        inputHandler = new InputHandler();
        towerClasses = new ArrayList<Class<?>>();
        enemyClasses = new ArrayList<Class<?>>();
        Texture texture = new Texture(Gdx.files.internal("Background_MainMenu.png"));
        towerModels = new ArrayList<Model>();
        enemyModels = new ArrayList<Model>();
        tProjectiles = new ArrayList<Projectile>();
        eProjectiles = new ArrayList<Projectile>();
        path = new ArrayList<Vector3>();
        enemies = new EnemyHandler(new Vector3(100, 20, 100));
        towers = new ArrayList<Tower>();
        modelBatch = new ModelBatch();

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
        enemyClasses = FileHandler.loadEnemies(reader, TDWorld.themes.get(theme));
        reader = FileHandler.getReader("theme/" + TDWorld.themes.get(theme) + "/TowerData");
        towerClasses = FileHandler.loadTowers(reader, TDWorld.themes.get(theme));

        assets = new AssetManager();
        assets.load("theme/" + TDWorld.themes.get(theme) + "/planet.g3db", Model.class);
        for(int i = 0; i < TDWorld.TOWERS; i++)
            assets.load("theme/" + TDWorld.themes.get(theme) + "/tower/" + Integer.toString(i) + ".g3db", Model.class);
        for(int i = 0; i < TDWorld.ENEMIES; i++)
            assets.load("theme/" + TDWorld.themes.get(theme) + "/enemy/" + Integer.toString(i) + ".g3db", Model.class);
        loading = true;
        if(!hasPlanetModel)
            assets.load("theme/" + TDWorld.themes.get(theme) + "/" + planetName + ".png", Texture.class);

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

        for (Tower tower : towers)
            tower.tick(delta);
        for(Projectile projectile: tProjectiles)
            projectile.tick(delta);
        for(Projectile projectile: eProjectiles)
            projectile.tick(delta);
        enemies.tick(delta);

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
        batch.draw(background, 0, 0, 200, 40);
        font.draw(batch, "X:" + Float.toString(cam.getCam().direction.x), 0, 20);
        font.draw(batch, "Z:" + Float.toString(cam.getCam().direction.z), 0, 40);
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

        if(hasPlanetModel) {
            planet = new ModelInstance(assets.get("theme/" + TDWorld.themes.get(theme) + "/planet.g3db", Model.class));
        } else {
            Texture texture = assets.get("theme/" + TDWorld.themes.get(theme) + "/" + planetName + ".png");//new Texture(Gdx.files.internal("Background_MainMenu.png"));
            Material material = new Material(TextureAttribute.createDiffuse(texture), ColorAttribute.createSpecular(1, 1, 1, 1), FloatAttribute.createShininess(8f));
            ModelBuilder builder = new ModelBuilder();
            planet = new ModelInstance(builder.createSphere(planetSize.x, planetSize.y, planetSize.z, 24, 24, material, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates));
        }

        planetRadius = (int)(planet.calculateBoundingBox(new BoundingBox()).getHeight() / 2f);

        //towers.add(new Gun(new Vector3(0, 0, 0), 50, 50, 0, new ModelInstance(towerModels.get(0))));
        towers.add(new Gun(new Vector3(0, planetRadius, 0), 50, 50, 0, new ModelInstance(towerModels.get(1))));
        ModelInstance instance = new ModelInstance(enemyModels.get(0));
        instance.materials.get(0).set(new BlendingAttribute(0.5f));
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
}
