package com.logicmaster63.tdgalaxy.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.logicmaster63.tdgalaxy.TDGalaxy;

public class SettingsScreen extends TDScreen {

    private Slider masterSlider, effectsSlider, musicSlider;
    private Label masterLabel, effectsLabel, musicLabel;
    private Texture background;
    private Button backButton;
    private TDScreen lastScreen;

    public SettingsScreen(Game game, AssetManager uiAssets, TDScreen lastScreen) {
        super(game, uiAssets);
        this.lastScreen = lastScreen;
    }

    @Override
    public void show() {
        super.show();

        background = new Texture("theme/basic/ui/Window.png");

        final Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture("theme/basic/ui/SlideBack.png")));
        sliderStyle.knob = new TextureRegionDrawable(new TextureRegion(new Texture("theme/basic/ui/Knob.png")));

        final Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = TDGalaxy.getFonts().get("moonhouse64");
        labelStyle.fontColor = Color.BLACK;

        //Update label and save percentages
        ChangeListener sliderListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                musicLabel.setText("Music Volume: " + (int)(100f * musicSlider.getPercent()));
                effectsLabel.setText("Effects Volume: " + (int)(100f * effectsSlider.getPercent()));
                masterLabel.setText("Master Volume: " + (int)(100f * masterSlider.getPercent()));
                TDGalaxy.changePref("musicVolume", musicSlider.getPercent());
                TDGalaxy.changePref("effectVolume", effectsSlider.getPercent());
                TDGalaxy.changePref("masterVolume", masterSlider.getPercent());
            }
        };

        //Master volume slider
        masterSlider = new Slider(0, 100, 1, false, sliderStyle);
        masterSlider.setBounds(viewport.getWorldWidth() / 2 - 500, viewport.getWorldHeight() / 2 - 50, 1000, 100);
        masterSlider.setValue(100 * TDGalaxy.getMasterVolume());
        stage.addActor(masterSlider);
        masterLabel = new Label("Master Volume: " + (int)(100f * TDGalaxy.getMasterVolume()), labelStyle);
        masterLabel.setPosition(viewport.getWorldWidth() / 2 - 500, viewport.getWorldHeight() / 2 + 70);
        stage.addActor(masterLabel);

        //Effects volume slider
        effectsSlider = new Slider(0, 100, 1, false, sliderStyle);
        effectsSlider.setBounds(viewport.getWorldWidth() / 2 - 500, viewport.getWorldHeight() / 2 - 350, 1000, 100);
        effectsSlider.setValue(100 * TDGalaxy.getEffectVolume());
        stage.addActor(effectsSlider);
        effectsLabel = new Label("Effects Volume: " + (int)(100f * TDGalaxy.getEffectVolume()), labelStyle);
        effectsLabel.setPosition(viewport.getWorldWidth() / 2 - 500, viewport.getWorldHeight() / 2 - 230);
        stage.addActor(effectsLabel);

        //Music volume slider
        musicSlider = new Slider(0, 100, 1, false, sliderStyle);
        musicSlider.setBounds(viewport.getWorldWidth() / 2 - 500, viewport.getWorldHeight() / 2 - 650, 1000, 100);
        musicSlider.setValue(100 * TDGalaxy.getMusicVolume());
        stage.addActor(musicSlider);
        musicLabel = new Label("Music Volume: " + (int)(100f * TDGalaxy.getMusicVolume()), labelStyle);
        musicLabel.setPosition(viewport.getWorldWidth() / 2 - 500, viewport.getWorldHeight() / 2 - 530);
        stage.addActor(musicLabel);

        //Back button
        backButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("theme/basic/ui/BackButton.png"))));
        backButton.setPosition(100, viewport.getWorldHeight() - 250);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(lastScreen);
            }
        });
        stage.addActor(backButton);

        masterSlider.addListener(sliderListener);
        effectsSlider.addListener(sliderListener);
        musicSlider.addListener(sliderListener);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        spriteBatch.end();

        super.render(delta);
    }
}
