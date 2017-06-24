package com.logicmaster63.tdgalaxy.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class PauseWindow extends Window {

    private Runnable resume;

    public PauseWindow(WindowStyle style, final Runnable resume) {
        super("Pause Window", style);

        this.resume = resume;

        Color color = getColor();
        color.a = 0;
        setColor(color);

        final Button.ButtonStyle backButtonStyle = new Button.ButtonStyle();
        backButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture("theme/basic/ui/BackButton.png")));

        Button resumeButton = new Button(backButtonStyle);
        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                resume();
            }
        });
        add(resumeButton);

        AlphaAction action = new AlphaAction();
        action.setAlpha(1f);
        action.setDuration(0.3f);
        addAction(new SequenceAction(action, new RunnableAction() {
            @Override
            public void run() {
                addListener(new InputListener() {
                    @Override
                    public boolean keyDown(InputEvent event, int keycode) {
                        if(keycode == Input.Keys.ESCAPE) {
                            resume();
                            return true;
                        }
                        return false;
                    }
                });
            }
        }));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = batch.getColor();
        super.draw(batch, parentAlpha);
        batch.setColor(color);
    }

    public void resume() {
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
