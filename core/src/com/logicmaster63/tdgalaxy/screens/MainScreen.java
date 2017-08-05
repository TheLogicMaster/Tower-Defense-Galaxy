package com.logicmaster63.tdgalaxy.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.logicmaster63.tdgalaxy.TDGalaxy;
import com.logicmaster63.tdgalaxy.constants.Constants;

public class MainScreen extends TDScreen {

    private Button signInButton, signOutButton;
    private Texture background;
    private MainScreen screen;

    public MainScreen(TDGalaxy game) {
        super(game);
        screen = this;
    }

    @Override
    public void show () {
        super.show();

        background = new Texture("theme/basic/ui/Window.png");

        final TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = game.getFonts().get("moonhouse64");
        textButtonStyle.up = new TextureRegionDrawable(new TextureRegion(background));

        //Sign in button
        signInButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("theme/basic/ui/SignIn.png"))));
        signInButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(TDGalaxy.onlineServices != null)
                    TDGalaxy.onlineServices.signIn();
            }
        });
        signOutButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("theme/basic/ui/SignOut.png"))));
        signOutButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(TDGalaxy.onlineServices != null)
                    TDGalaxy.onlineServices.signOut();
                game.preferences.changePref("autoSignIn", false);
            }
        });
        signOutButton.setVisible(false);
        Stack signInStack = new Stack(signInButton, signOutButton);
        signInStack.setBounds(100, viewport.getWorldHeight() - 250, 600, 150);
        stage.addActor(signInStack);

        //Settings button
        ImageButton settingsButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("theme/basic/ui/SettingsButton.png"))));
        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new SettingsScreen(game, screen));
            }
        });
        settingsButton.setPosition(viewport.getWorldWidth() - settingsButton.getWidth() - 100, viewport.getWorldHeight() - settingsButton.getHeight() - 100);
        stage.addActor(settingsButton);

        Table table = new Table();
        table.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());

        //Play game button
        TextButton playButton = new TextButton("Play Game", textButtonStyle);
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game, 0, "basic"));
            }
        });
        table.add(playButton);

        //Editor button
        TextButton editButton = new TextButton("Editor", textButtonStyle);
        editButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new EditorScreen(game));
            }
        });
        table.add(editButton);

        table.row();

        //Show achievements button
        TextButton achievementShowButton = new TextButton("Show Achievements", textButtonStyle);
        achievementShowButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(TDGalaxy.onlineServices != null)
                    TDGalaxy.onlineServices.showAchievements();
            }
        });
        table.add(achievementShowButton);

        //Rate game button
        TextButton rateButton = new TextButton("Rate game", textButtonStyle);
        rateButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(TDGalaxy.onlineServices != null)
                    TDGalaxy.onlineServices.rateGame();
            }
        });
        table.add(rateButton);

        table.row();

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

        //Reset achievement button
        TextButton resetButton = new TextButton("Reset achievements", textButtonStyle);
        resetButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(TDGalaxy.onlineServices != null)
                    TDGalaxy.onlineServices.resetAchievement(Constants.ACHIEVEMENT_NO_LIFE);
            }
        });
        table.add(resetButton);

        table.center().bottom();
        stage.addActor(table);
    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        spriteBatch.end();

        signOutButton.setVisible(TDGalaxy.onlineServices.isSignedIn());
        signInButton.setVisible(!TDGalaxy.onlineServices.isSignedIn());

        super.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
        background.dispose();
    }
}