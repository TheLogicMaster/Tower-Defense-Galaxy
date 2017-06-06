package com.logicmaster63.tdworld.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.common.collect.Lists;
import com.logicmaster63.tdworld.TDWorld;
import com.logicmaster63.tdworld.tools.Tools;
import com.logicmaster63.tdworld.ui.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TDScreen implements Screen, InputProcessor {

    Game game;
    protected List<Element> elements;
    protected SpriteBatch spriteBatch;
    private IntIntMap keys;
    private OrthographicCamera orthographicCamera;
    private Viewport viewport;
    private Vector3 tmp;

    public TDScreen (Game game) {
        this.game = game;
        keys = new IntIntMap();
        elements = new ArrayList<Element>();
        orthographicCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch = new SpriteBatch();
        orthographicCamera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), orthographicCamera);
        viewport.apply();
        //Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        tmp = new Vector3();
    }

    @Override
    public void resize (int width, int height) {
        /*if(Tools.getImplements(elements, com.logicmaster63.tdworld.ui.window.Window.class).size() > 0)
            for(com.logicmaster63.tdworld.ui.window.Window window: Tools.getImplements(elements, com.logicmaster63.tdworld.ui.window.Window.class))
                window.resize(width, height);*/
        viewport.update(width, height);
        System.out.println("Resize");
    }

    @Override
    public void show () {
        Gdx.input.setInputProcessor(this);
        System.out.println("Show");
    }

    @Override
    public void hide () {
        System.out.println("Hide");
    }

    @Override
    public void pause () {
        System.out.println("Pause");
    }

    @Override
    public void resume () {
        System.out.println("Resume");
    }

    @Override
    public void dispose () {
        System.out.println("Dispose");
    }

    public void updateUI(float delta) {
        for(Updatable updatable: Tools.getImplements(elements, Updatable.class))
            updatable.update(delta);
    }

    @Override
    public void render(float delta) {
        orthographicCamera.update();
        spriteBatch.setProjectionMatrix(orthographicCamera.combined);
        spriteBatch.begin();
        if(Tools.getImplements(elements, com.logicmaster63.tdworld.ui.window.Window.class).size() > 0)
            for(com.logicmaster63.tdworld.ui.window.Window window: Tools.getImplements(elements, com.logicmaster63.tdworld.ui.window.Window.class))
                window.render(spriteBatch, orthographicCamera);
        TDWorld.getFonts().get("ui32").draw(spriteBatch, orthographicCamera.unproject(tmp.set(Gdx.input.getX(), Gdx.input.getY(), 0)).toString(), 100, 100);
        spriteBatch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        keys.put(keycode, keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        keys.remove(keycode, 0);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        for(MouseHandler mouseHandler: Lists.reverse(Tools.getImplements(elements, MouseHandler.class)))
            if(mouseHandler.onWindow(screenX, screenY))
                if(mouseHandler.touchDown(screenX, screenY, pointer, button))
                    break;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        for(MouseHandler mouseHandler: Lists.reverse(Tools.getImplements(elements, MouseHandler.class)))
            if(mouseHandler.onWindow(screenX, screenY))
                if(mouseHandler.touchUp(screenX, screenY, pointer, button))
                    break;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        for(MouseHandler mouseHandler: Lists.reverse(Tools.getImplements(elements, MouseHandler.class)))
            if(mouseHandler.onWindow(screenX, screenY))
                if(mouseHandler.touchDragged(screenX, screenY, pointer))
                    break;
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        for(MouseHandler mouseHandler: Lists.reverse(Tools.getImplements(elements, MouseHandler.class)))
            if(mouseHandler.onWindow(screenX, screenY))
                if(mouseHandler.mouseMoved(screenX, screenY))
                    break;
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        for(MouseHandler mouseHandler: Lists.reverse(Tools.getImplements(elements, MouseHandler.class)))
            if(mouseHandler.onWindow(Gdx.input.getX(), Gdx.input.getY()))
                if(mouseHandler.scrolled(amount))
                    break;
        return false;
    }
}
