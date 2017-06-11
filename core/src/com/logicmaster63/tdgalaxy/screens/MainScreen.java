package com.logicmaster63.tdgalaxy.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.logicmaster63.tdgalaxy.TDGalaxy;
import com.logicmaster63.tdgalaxy.constants.Constants;

public class MainScreen extends TDScreen{

    private Table table;
    private Label label;
    private TextButton signInButton, signOutButton;

    public MainScreen(Game game) {
        super(game);
    }

    @Override
    public void show () {
        super.show();

        table = new Table() {
            @Override
            public void act(float delta) {
                if(TDGalaxy.googlePlayServices != null)
                    label.setText(Boolean.toString(TDGalaxy.googlePlayServices.isSignedIn()));
                super.act(delta);
            }
        };
        table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.align(Align.center | Align.top);
        label = new Label("Nope", new Label.LabelStyle(TDGalaxy.getFonts().get("moonhouse64"), Color.BLACK));
        label.setWrap(true);
        label.setFontScale(0.7f);
        Texture background = new Texture("theme/basic/ui/Window.png");
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = TDGalaxy.getFonts().get("moonhouse64");
        textButtonStyle.up = new TextureRegionDrawable(new TextureRegion(background));
        stage.addActor(table);

        signInButton = new TextButton("Click to sign in", textButtonStyle);
        signInButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(TDGalaxy.googlePlayServices != null)
                    TDGalaxy.googlePlayServices.signIn();
            }
        });
        signInButton.setPosition(10, 100);
        stage.addActor(signInButton);

        signOutButton = new TextButton("Click to sign out", textButtonStyle);
        signOutButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(TDGalaxy.googlePlayServices != null)
                    TDGalaxy.googlePlayServices.signOut();
                TDGalaxy.changePref("autoSignIn", false);
            }
        });
        signOutButton.setPosition(10, 100);
        signOutButton.setVisible(false);
        stage.addActor(signOutButton);

        TextButton achievementButton = new TextButton("Unlock Achievement", textButtonStyle);
        achievementButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(TDGalaxy.googlePlayServices != null)
                    TDGalaxy.googlePlayServices.unlockAchievement(Constants.ACHIEVEMENT_NO_LIFE);
            }
        });
        achievementButton.setPosition(50 + signInButton.getWidth(), 100);
        stage.addActor(achievementButton);

        TextButton achievementShowButton = new TextButton("Show Achievements", textButtonStyle);
        achievementShowButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(TDGalaxy.googlePlayServices != null)
                    TDGalaxy.googlePlayServices.showAchievements();
            }
        });
        achievementShowButton.setPosition(10, 140 + signInButton.getHeight());
        stage.addActor(achievementShowButton);

        TextButton playButton = new TextButton("Play Game", textButtonStyle);
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game, 0, TDGalaxy.getThemes().get(0)));
            }
        });
        playButton.setPosition(50 + achievementShowButton.getWidth(), 140 + signInButton.getHeight());
        stage.addActor(playButton);

        TextButton resetButton = new TextButton("Reset achievements", textButtonStyle);
        resetButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(TDGalaxy.googlePlayServices != null)
                    TDGalaxy.googlePlayServices.resetAchievement(Constants.ACHIEVEMENT_NO_LIFE);
            }
        });
        resetButton.setPosition(10, 180 + signInButton.getHeight() + achievementShowButton.getHeight());
        stage.addActor(resetButton);

        TextButton rateButton = new TextButton("Rate game", textButtonStyle);
        rateButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(TDGalaxy.googlePlayServices != null)
                    TDGalaxy.googlePlayServices.rateGame();
            }
        });
        rateButton.setPosition(50 + resetButton.getWidth(), 180 + signInButton.getHeight() + achievementShowButton.getHeight());
        stage.addActor(rateButton);

        if(!TDGalaxy.googlePlayServices.isSignedIn() && TDGalaxy.autoSignIn()) {
            TDGalaxy.googlePlayServices.signIn();
            Gdx.app.debug("Google Play Services", "Sign in");
        }
    }

    @Override
    public void render (float delta) {
        if(TDGalaxy.googlePlayServices.isSignedIn() && !TDGalaxy.autoSignIn())
            TDGalaxy.changePref("autoSignIn", true);

        signOutButton.setVisible(TDGalaxy.googlePlayServices.isSignedIn());
        signInButton.setVisible(!TDGalaxy.googlePlayServices.isSignedIn());

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}