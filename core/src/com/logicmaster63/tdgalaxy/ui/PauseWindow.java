package com.logicmaster63.tdgalaxy.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.SnapshotArray;
import com.logicmaster63.tdgalaxy.TDGalaxy;
import com.logicmaster63.tdgalaxy.screens.MainScreen;

public class PauseWindow extends Window {

    private Runnable resume;
    private Vector2 tempVector;
    private Button closeButton, resumeButton, homeButton;

    public PauseWindow(WindowStyle style, BitmapFont font, final Runnable resume, final Runnable save, final TDGalaxy game, final AssetManager uiAssets, Button button) {
        super("", style);

        this.resume = resume;
        tempVector = new Vector2();
        closeButton = button;

        final Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        final TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        //Set transparent
        Color color = getColor();
        color.a = 0;
        setColor(color);

        addListener(new NullInputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                return !(keycode == Input.Keys.ESCAPE);
            }
        });

        //Close button
        resumeButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("theme/basic/ui/X.png"))));
        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                resume();
            }
        });
        addActor(resumeButton);

        //Quit button
        homeButton = new TextButton("Save and quit", textButtonStyle);
        homeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                save.run();
                game.setScreen(new MainScreen(game));
            }
        });
        addActor(homeButton);

        //Fade in
        AlphaAction action = new AlphaAction();
        action.setAlpha(1f);
        action.setDuration(0.3f);
        addAction(action);
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {
        super.setBounds(x, y, width, height);
        resumeButton.setPosition(10, getHeight() - resumeButton.getHeight() - 10);
        homeButton.setPosition(getWidth() / 2 - homeButton.getWidth() / 2, getHeight() / 2 - homeButton.getHeight() / 2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //Reset spritebatch after alpha action
        Color color = batch.getColor();
        super.draw(batch, parentAlpha);
        batch.setColor(color);
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        //Still include menu button
        tempVector.set(x, y);
        localToStageCoordinates(tempVector);
        closeButton.stageToLocalCoordinates(tempVector);
        if (closeButton.hit(tempVector.x, tempVector.y, touchable) != null)
            return closeButton.hit(tempVector.x, tempVector.y, touchable);

        //Find deepest child at position
        SnapshotArray<Actor> childrenSnapshot = getChildren();
        Actor[] children = childrenSnapshot.items;
        for(int i = childrenSnapshot.size - 1; i >= 0; i--) {
            Actor child = children[i];
            child.parentToLocalCoordinates(tempVector.set(x, y));
            if (child.hit(tempVector.x, tempVector.y, touchable) != null)
                return child;
        }
        return this;
    }

    public void resume() {
        //Fade out
        AlphaAction action = new AlphaAction();
        action.setAlpha(0f);
        action.setDuration(0.3f);
        addAction(new SequenceAction(action, new RunnableAction() {
            @Override
            public void run() {
                resume.run();
                remove();
            }
        }));
    }
}
