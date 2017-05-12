package com.logicmaster63.tdworld.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.logicmaster63.tdworld.ui.Window;

import java.util.ArrayList;
import java.util.List;

public abstract class TDScreen implements Screen {

    Game game;
    protected List<Window> windows;
    protected SpriteBatch batch;

    public TDScreen (Game game) {
        this.game = game;
        windows = new ArrayList<Window>();
    }

    @Override
    public void resize (int width, int height) {
        System.out.println("Resize");
    }

    @Override
    public void show () {
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

    @Override
    public void render(float delta) {
        if(windows.size() > 0)
            for(Window window: windows)
                window.render();
    }
}
