package com.logicmaster63.tdgalaxy.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.scenes.scene2d.*;
import com.logicmaster63.tdgalaxy.tools.Tools;

public class PlacementWindow extends Actor {

    private Texture texture;
    private int xCells, yCells;
    private PlacementCell[][] cells;
    private PlacementCell lastCell;
    private ModelBatch modelBatch;
    private ModelInstance ghost;
    private btCollisionWorld world;
    private Camera camera;

    public PlacementWindow(float x, float y, float width, float height, int xCells, int yCells, PlacementCell[][] cells, btCollisionWorld world, Camera camera, ModelBatch modelBatch) {
        this.xCells = xCells;
        this.yCells = yCells;
        this.cells = cells;
        this.modelBatch = modelBatch;
        this.world = world;
        this.camera = camera;

        setTouchable(Touchable.enabled);
        setBounds(x, y, width, height);
        texture = new Texture("theme/basic/ui/Window.png");
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                lastCell = getCell((int)x, (int)y);
                if(lastCell != null)
                    ghost = new ModelInstance(lastCell.model);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(lastCell != null)
                    System.out.println(lastCell);
                lastCell = null;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {

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
                if(cells[i][j] != null)
                    batch.draw(cells[i][j].texture, getX() + i * getWidth() / xCells, getY() + j * getHeight() / xCells, getWidth() / xCells, getHeight() / yCells);

        if(Gdx.input.isTouched() && lastCell != null) {
            Ray ray = camera.getPickRay(Gdx.input.getX(), Gdx.input.getY());
            Vector3 pos = Tools.closestRayTest(world, new ClosestRayResultCallback(ray.origin, new Vector3(ray.direction).setLength(9999).add(ray.origin)));
            if(pos != null && Tools.closestRayTestObject(world, new ClosestRayResultCallback(ray.origin, new Vector3(ray.direction).setLength(9999).add(ray.origin))).getUserValue() == 0) {
                ghost.transform.setToTranslation(pos);
                modelBatch.begin(camera);
                modelBatch.render(ghost);
                modelBatch.end();
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
