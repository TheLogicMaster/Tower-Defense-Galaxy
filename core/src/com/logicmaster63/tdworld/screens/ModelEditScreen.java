package com.logicmaster63.tdworld.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.logicmaster63.tdworld.tools.TextListener;

import java.util.ArrayList;

public class ModelEditScreen extends TDScreen{

    private final int buttonCount = 6;
    private String name = "Basic", prevName = "model";
    private SpriteBatch batch;
    private TextListener textListener;
    private Stage stage;
    private ArrayList<TextButton> buttons;
    private TextButton.TextButtonStyle textButtonStyle;
    private BitmapFont font;
    private Texture background;
    private ModelBatch modelBatch;
    private PerspectiveCamera cam;
    private AssetManager assets;
    private Model model;
    private boolean isLoading = false;
    private ModelInstance modelInstance;
    private Environment environment;
    private CameraInputController cameraInputController;
    private InputMultiplexer inputMultiplexer;
    private int index, xPos = 0, yPos = 0, zPos = 0, xSize = 0, ySize = 0, zSize = 0, xRot = 0, yRot = 0, zRot = 0, radius = 0, height = 0;
    private ArrayList<btCollisionObject> collisionObjects;
    private btCollisionWorld collisionWorld;
    private btBroadphaseInterface broadphase;
    private btCollisionConfiguration collisionConfig;
    private btDispatcher dispatcher;
    private DebugDrawer debugDrawer;
    private Vector3 tempVector;
    private Quaternion tempQuaternion;
    private Matrix4 tempMatrix;

    public ModelEditScreen(Game game) {
        super(game);
    }

    @Override
    public void show () {
        tempMatrix = new Matrix4();
        tempQuaternion = new Quaternion();
        tempVector = new Vector3();
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        collisionWorld = new btCollisionWorld(dispatcher, broadphase, collisionConfig);
        debugDrawer = new DebugDrawer();
        collisionWorld.setDebugDrawer(debugDrawer);
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(new Vector3(250, 20, 250));
        cam.lookAt(new Vector3(0, 4, 0));
        cam.near = 1;
        cam.far = 5000;
        cam.update();

        collisionObjects = new ArrayList<btCollisionObject>();
        assets = new AssetManager();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        modelBatch = new ModelBatch();
        buttons = new ArrayList<TextButton>();

        cameraInputController = new CameraInputController(cam);
        stage = new Stage();
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(cameraInputController);
        Gdx.input.setInputProcessor(inputMultiplexer);

        font = new BitmapFont();
        background = new Texture("Background_MainMenu.png");
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = new TextureRegionDrawable(new TextureRegion(background));
        textListener = new TextListener(this);
        //textListener.inputSet("name", String.class);

        buttons.add(createButton("Up", new Vector2(20, 340), new Vector2(25, 20), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                index++;
                if(index > Math.max(collisionObjects.size() - 1, 0))
                    index = 0;
                updateButtons();
            }
        }));
        buttons.add(createButton("Down", new Vector2(20, 320), new Vector2(45, 20), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                index--;
                if(index < 0)
                    index = Math.max(collisionObjects.size() - 1, 0);
                updateButtons();
            }
        }));
        buttons.add(createButton("Remove", new Vector2(20, 290), new Vector2(60, 20), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(collisionObjects.size() > 0 && index < collisionObjects.size()) {
                    collisionWorld.removeCollisionObject(collisionObjects.get(index));
                    collisionObjects.remove(index);
                    updateButtons();
                    if(index > Math.max(collisionObjects.size() - 1, 0))
                        index = 0;
                    if(index < 0)
                        index = Math.max(collisionObjects.size() - 1, 0);
                    updateButtons();
                }
            }
        }));
        buttons.add(createButton("Model", new Vector2(20, 370), new Vector2(50, 20), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                textListener.inputSet("setName", String.class);
                model = null;
            }
        }));

        buttons.add(createButton("Capsule", new Vector2(20, 240), new Vector2(60, 20), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                btCollisionObject object = new btCollisionObject();
                object.setCollisionShape(new btCapsuleShape(10, 20));
                collisionObjects.add(object);
                collisionWorld.addCollisionObject(object);
            }
        }));
        buttons.add(createButton("Box", new Vector2(20, 260), new Vector2(30, 20), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                btCollisionObject object = new btCollisionObject();
                object.setCollisionShape(new btBoxShape(new Vector3(10, 10, 10)));
                collisionObjects.add(object);
                collisionWorld.addCollisionObject(object);
            }
        }));

        buttons.add(createButton("XPos", new Vector2(20, 20), new Vector2(40, 20), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                textListener.inputSet("setXPos", int.class);
            }
        }));
        buttons.add(createButton("YPos", new Vector2(20, 40), new Vector2(40, 20), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                textListener.inputSet("setYPos", int.class);
            }
        }));
        buttons.add(createButton("ZPos", new Vector2(20, 60), new Vector2(40, 20), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                textListener.inputSet("setZPos", int.class);
            }
        }));
        buttons.add(createButton("XRot", new Vector2(80, 20), new Vector2(40, 20), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                textListener.inputSet("setXRot", int.class);
            }
        }));
        buttons.add(createButton("YRot", new Vector2(80, 40), new Vector2(40, 20), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                textListener.inputSet("setYRot", int.class);
            }
        }));
        buttons.add(createButton("ZRot", new Vector2(80, 60), new Vector2(40, 20), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                textListener.inputSet("setZRot", int.class);
            }
        }));
        buttons.add(createButton("XSize", new Vector2(140, 20), new Vector2(40, 20), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                textListener.inputSet("setXSize", int.class);
            }
        }));
        buttons.add(createButton("YSize", new Vector2(140, 40), new Vector2(40, 20), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                textListener.inputSet("setYSize", int.class);
            }
        }));
        buttons.add(createButton("ZSize", new Vector2(140, 60), new Vector2(40, 20), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                textListener.inputSet("setZSize", int.class);
            }
        }));
        buttons.add(createButton("Radius", new Vector2(200, 60), new Vector2(50, 20), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                textListener.inputSet("setRadius", int.class);
            }
        }));
        buttons.add(createButton("Height", new Vector2(200, 40), new Vector2(50, 20), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                textListener.inputSet("setHeight", int.class);
            }
        }));

        batch = new SpriteBatch();
        batch.getProjectionMatrix().setToOrtho2D(0, 0, 480, 320);
    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if(!name.equals(prevName)) {
            isLoading = true;
            assets.load(name + ".g3db", Model.class);
            prevName = name;
        }
        try{
            if(isLoading && assets.update()) {
                isLoading = false;
                model = assets.get(name + ".g3db");
                modelInstance = new ModelInstance(model);
            }
        } catch (GdxRuntimeException e) {
            textListener.inputSet("name", String.class);
        }

        cameraInputController.update();

        modelBatch.begin(cam);
        if(modelInstance != null)
            modelBatch.render(modelInstance, environment);
        modelBatch.end();

        debugDrawer.begin(cam);
        collisionWorld.debugDrawWorld();
        debugDrawer.end();

        stage.draw();
        batch.begin();
        font.draw(batch, name, 230, 300);
        if(modelInstance != null)
            if(collisionObjects.size() > 0 && index < collisionObjects.size()) {
                font.draw(batch, "Shape: " + Integer.toString(index) + ": " + collisionObjects.get(index).getCollisionShape().getName(), 0, 280);
                if(collisionObjects.get(index).getCollisionShape() instanceof btBoxShape) {
                    font.draw(batch, Integer.toString(zSize), 105, 80);
                    font.draw(batch, Integer.toString(ySize), 105, 95);
                    font.draw(batch, Integer.toString(xSize), 105, 110);
                }
                if(collisionObjects.get(index).getCollisionShape() instanceof btCapsuleShape) {
                    font.draw(batch, Integer.toString(height), 105, 95);
                    font.draw(batch, Integer.toString(radius), 105, 110);
                }
            }
            else
                font.draw(batch, "Shape: " + Integer.toString(index), 0, 280);
        font.draw(batch, Integer.toString(xPos), 15, 80);
        font.draw(batch, Integer.toString(yPos), 15, 95);
        font.draw(batch, Integer.toString(zPos), 15, 110);
        font.draw(batch, Integer.toString(xRot), 60, 80);
        font.draw(batch, Integer.toString(yRot), 60, 95);
        font.draw(batch, Integer.toString(zRot), 60, 110);
        batch.end();
    }

    @Override
    public void hide () {

    }

    private void updateButtons() { //Load values and move buttons
        for(int i = buttonCount + 6; i < buttons.size(); i++)
            buttons.get(i).setPosition(-100, - 100);
        if(collisionObjects.size() <= 0 || index >= collisionObjects.size() || buttons.size() < 17)
            return;
        xPos = (int) collisionObjects.get(index).getWorldTransform().getTranslation(tempVector).x;
        yPos = (int) collisionObjects.get(index).getWorldTransform().getTranslation(tempVector).y;
        zPos = (int) collisionObjects.get(index).getWorldTransform().getTranslation(tempVector).z;
        xRot = (int) collisionObjects.get(index).getWorldTransform().getRotation(tempQuaternion).getPitch();
        yRot = (int) collisionObjects.get(index).getWorldTransform().getRotation(tempQuaternion).getRoll();
        zRot = (int) collisionObjects.get(index).getWorldTransform().getRotation(tempQuaternion).getYaw();
        if(collisionObjects.get(index).getCollisionShape() instanceof btCapsuleShape) {
            buttons.get(buttonCount + 9).setPosition(140, 60);
            buttons.get(buttonCount + 10).setPosition(140, 40);
            height = (int) ((btCapsuleShape) collisionObjects.get(index).getCollisionShape()).getHalfHeight() * 2;
            radius = (int) ((btCapsuleShape) collisionObjects.get(index).getCollisionShape()).getRadius();
        }
        if(collisionObjects.get(index).getCollisionShape() instanceof btBoxShape) {
            buttons.get(buttonCount + 8).setPosition(140, 20);
            buttons.get(buttonCount + 7).setPosition(140, 40);
            buttons.get(buttonCount + 6).setPosition(140, 60);
            xSize = (int) ((btBoxShape) collisionObjects.get(index).getCollisionShape()).getHalfExtentsWithMargin().x * 2;
            ySize = (int) ((btBoxShape) collisionObjects.get(index).getCollisionShape()).getHalfExtentsWithMargin().y * 2;
            zSize = (int) ((btBoxShape) collisionObjects.get(index).getCollisionShape()).getHalfExtentsWithMargin().z * 2;
        }
    }

    private void setValues() {
        if(collisionObjects.size() <= 0)
            return;
        btCollisionObject object = new btCollisionObject();
        if(collisionObjects.get(index).getCollisionShape() instanceof btCapsuleShape) {
            object.setCollisionShape(new btCapsuleShape(radius, height));
        }
        if(collisionObjects.get(index).getCollisionShape() instanceof btBoxShape) {
            object.setCollisionShape(new btBoxShape(new Vector3((int)((double)xSize / 2), (int)((double)ySize / 2), (int)((double)zSize / 2))));
        }
        object.setWorldTransform(tempMatrix.set(new Vector3(xPos, yPos, zPos), tempQuaternion.setEulerAngles(xRot, yRot, zRot)));
        collisionWorld.removeCollisionObject(collisionObjects.get(index));
        collisionObjects.set(index, object);
        collisionWorld.addCollisionObject(collisionObjects.get(index));
    }

    private TextButton createButton(String name, Vector2 pos, Vector2 size, ChangeListener listener) {
        TextButton button = new TextButton(name, textButtonStyle);
        button.addListener(listener);
        button.setWidth(size.x);
        button.setHeight(size.y);
        button.setPosition(pos.x, pos.y);
        stage.addActor(button);
        return button;
    }

    public void setXPos(int xPos) {
        this.xPos = xPos;
        setValues();
    }

    public void setYPos(int yPos) {
        this.yPos = yPos;
        setValues();
    }

    public void setZPos(int zPos) {
        this.zPos = zPos;
        setValues();
    }

    public void setXSize(int xSize) {
        this.xSize = xSize;
        setValues();
    }

    public void setYSize(int ySize) {
        this.ySize = ySize;
        setValues();
    }

    public void setZSize(int zSize) {
        this.zSize = zSize;
        setValues();
    }

    public void setXRot(int xRot) {
        this.xRot = xRot;
        setValues();
    }

    public void setYRot(int yRot) {
        this.yRot = yRot;
        setValues();
    }

    public void setZRot(int zRot) {
        this.zRot = zRot;
        setValues();
    }

    public void setRadius(int radius) {
        this.radius = radius;
        setValues();
    }

    public void setHeight(int height) {
        this.height = height;
        setValues();
    }

    public void setName(String name) {
        this.name = name;
    }
}
