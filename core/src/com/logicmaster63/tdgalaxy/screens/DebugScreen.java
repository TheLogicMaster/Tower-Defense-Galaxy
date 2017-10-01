package com.logicmaster63.tdgalaxy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.logicmaster63.tdgalaxy.TDGalaxy;
import com.logicmaster63.tdgalaxy.constants.Constants;

public class DebugScreen extends TDScreen {

    private Texture background;

    public DebugScreen(TDGalaxy game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        background = new Texture("theme/basic/ui/Window.png");

        final TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = game.getFonts().get("moonhouse64");
        textButtonStyle.up = new TextureRegionDrawable(new TextureRegion(background));

        Table table = new Table();
        table.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());

        //Unlock achievement button
        TextButton achievementButton = new TextButton("Unlock Achievement", textButtonStyle);
        achievementButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(TDGalaxy.onlineServices != null)
                    TDGalaxy.onlineServices.unlockAchievement(Constants.ACHIEVEMENT_NO_LIFE);
            }
        });
        table.add(achievementButton);

        table.center();
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
        background.dispose();
    }
}
