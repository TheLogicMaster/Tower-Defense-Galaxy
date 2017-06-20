package com.logicmaster63.tdgalaxy.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.scenes.scene2d.*;
import com.logicmaster63.tdgalaxy.map.world.World;
import com.logicmaster63.tdgalaxy.tools.Money;
import com.logicmaster63.tdgalaxy.tools.Tools;

public class PlacementWindow extends Actor {

    private Texture texture, cellX;
    private int xCells, yCells;
    private PlacementCell[][] cells;
    private PlacementCell lastCell;
    private ModelBatch modelBatch;
    private ModelInstance ghost;
    private btCollisionWorld btcollisionWorld;
    private Camera camera;
    private World world;
    private Money money;

    public PlacementWindow(float x, float y, float width, float height, int xCells, int yCells, PlacementCell[][] cells, btCollisionWorld collisionWorld, Camera cam, final ModelBatch modelBatch, World world, final Money money) {
        this.xCells = xCells;
        this.yCells = yCells;
        this.cells = cells;
        this.modelBatch = modelBatch;
        this.btcollisionWorld = collisionWorld;
        this.world = world;
        this.camera = cam;
        this.money = money;

        setTouchable(Touchable.enabled);
        setBounds(x, y, width, height);

        texture = new Texture("theme/basic/ui/Window.png");
        cellX = new Texture("theme/basic/ui/X.png");

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(getCell((int) x, (int) y) != null) {
                    if (money.$ >= getCell((int) x, (int) y).price) {
                        lastCell = getCell((int) x, (int) y);
                        ghost = new ModelInstance(lastCell.model);
                    }
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(lastCell != null) {
                    Ray ray = camera.getPickRay(Gdx.input.getX(), Gdx.input.getY());
                    Vector3 pos = Tools.closestRayTest(btcollisionWorld, new ClosestRayResultCallback(ray.origin, new Vector3(ray.direction).setLength(9999).add(ray.origin)));
                    if(pos != null) {
                        lastCell.template.create(new Matrix4().setTranslation(pos));
                        money.$ -= lastCell.price;
                    }
                }
                lastCell = null;
            }
        });
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
        for(int i = 0; i < xCells; i++)
            for(int j = 0; j < yCells; j++)
                if(cells[i][j] != null) {
                    batch.draw(cells[i][j].texture, getX() + i * getWidth() / xCells, getY() + j * getHeight() / yCells, getWidth() / xCells, getHeight() / yCells);
                    if(cells[i][j].price > money.$)
                        batch.draw(cellX, getX() + i * getWidth() / xCells, getY() + j * getHeight() / yCells, getWidth() / xCells, getHeight() / yCells);
                }

        if(Gdx.input.isTouched() && lastCell != null) {
            Ray ray = camera.getPickRay(Gdx.input.getX(), Gdx.input.getY());
            Vector3 pos = Tools.closestRayTest(btcollisionWorld, new ClosestRayResultCallback(ray.origin, new Vector3(ray.direction).setLength(9999).add(ray.origin)));
            if(pos != null && Tools.closestRayTestObject(btcollisionWorld, new ClosestRayResultCallback(ray.origin, new Vector3(ray.direction).setLength(9999).add(ray.origin))).getUserValue() == 0) {
                Vector3 vector = world.getVector(pos);
                if(vector != null && ghost != null) {
                    ghost.transform.setToTranslation(pos);
                    ghost.transform.rotate(vector, 30);
                }
                if(ghost != null)
                    modelBatch.render(ghost);
            }
        }
    }

    private PlacementCell getCell(int x, int y) {
        return cells[(int)((x - getX())  / getWidth() * xCells)][(int)((y - getY()) / getHeight() * yCells)];
    }

    @Override
    public boolean remove() {
        texture.dispose();
        return super.remove();
    }
}
