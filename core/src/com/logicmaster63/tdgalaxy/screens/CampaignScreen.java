package com.logicmaster63.tdgalaxy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.logicmaster63.tdgalaxy.TDGalaxy;
import com.logicmaster63.tdgalaxy.interfaces.CameraRenderer;
import com.logicmaster63.tdgalaxy.map.world.CampaignWorld;

import java.util.ArrayList;
import java.util.List;

public class CampaignScreen extends TDScreen implements InputProcessor, CameraRenderer {

    private final int MAX_VELOCITY = 200;
    private final int MAX_AUTO_VELOCITY = 200;
    private final int STARTING_AUTO_VELOCITY = 6;
    private final int ICON_SIZE = 1000;
    private final float AUTO_ACCELERATION = 0.4f;
    private final float DECELERATION = 0.8f;

    private Texture background, textBack;
    private float velocity, autoVelocity = STARTING_AUTO_VELOCITY;
    private int scroll, lastScroll, staticPointerFrames; //Frames since last pointer drag
    private List<CampaignWorld> campaignWorlds;
    private boolean autoMove = false;
    private GlyphLayout layout;
    private int touchedWorld = -1, moveToWorld = -1; //-1 means no world
    private Vector3 tempVector;

    private int halfFreeWidth; //Half of the area that ism't part of icon
    private int segmentWidth; //The width of what is taken up by one world/associated empty space
    private int length; //Distance that can be travelled by foreground icons
    private int selection; //Selected world

    public CampaignScreen(TDGalaxy game) {
        super(game);
    }

    public CampaignScreen(TDGalaxy game, int worldIndex) {
        super(game);
        moveToWorld = worldIndex;
        autoMove = true;
    }

    @Override
    public void show() {
        super.show();

        layout = new GlyphLayout();
        tempVector = new Vector3();

        //Back button
        ImageButton backButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("theme/basic/ui/BackButton.png"))));
        backButton.setPosition(100, viewport.getWorldHeight() - 250);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainScreen(game));
            }
        });
        stage.addActor(backButton);

        // TODO: 1/23/2018
        campaignWorlds = new ArrayList<CampaignWorld>();
        campaignWorlds.add(new CampaignWorld(new Texture("theme/basic/Icon.png"), "World 0's Name", "Put description here for basic"));
        campaignWorlds.add(new CampaignWorld(new Texture("theme/Fallback/Icon.png"), "Put Name Here 1","Put description here for fallback1"));
        campaignWorlds.add(new CampaignWorld(new Texture("theme/Fallback/Icon.png"), "Put Name Here 2","Put description here for fallback2"));
        campaignWorlds.add(new CampaignWorld(new Texture("theme/Fallback/Icon.png"), "Put Name Here 3","Put description here for fallback3"));
        campaignWorlds.add(new CampaignWorld(new Texture("theme/Fallback/Icon.png"), "Put Name Here 4","Put description here for fallback4"));
        campaignWorlds.add(new CampaignWorld(new Texture("theme/Fallback/Icon.png"), "Put Name Here 5","Put description here for fallback5"));
        campaignWorlds.add(new CampaignWorld(new Texture("theme/Fallback/Icon.png"), "Put Name Here 6","Put description here for fallback6"));

        halfFreeWidth = (int)((viewport.getWorldWidth() - ICON_SIZE) / 2);
        segmentWidth = ICON_SIZE + halfFreeWidth;
        length = segmentWidth * (campaignWorlds.size() - 1);

        background = new Texture("menus/Background_Campaign.png");
        textBack = new Texture("menus/text_back_campaign.png");

        addDisposables(background, textBack);

        addInputProcessor(this);
    }

    @Override
    public void renderForCamera(Camera camera) {
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight(), (int)(scroll / (float)length * (background.getWidth() - viewport.getScreenWidth())), 0, viewport.getScreenWidth(), (int)viewport.getWorldHeight(), false, false);
        for(int i = 0; i < campaignWorlds.size(); i++)
            if(segmentWidth + i * segmentWidth - scroll > 0 && halfFreeWidth + i * segmentWidth - scroll < background.getWidth() - viewport.getWorldWidth())
                spriteBatch.draw(campaignWorlds.get(i).icon, halfFreeWidth + i * segmentWidth - scroll, viewport.getWorldHeight() / 2f - ICON_SIZE / 2);
        if(selection != -1) {
            layout.setText(game.getFonts().get("moonhouse64"), campaignWorlds.get(selection).name);
            spriteBatch.draw(textBack, viewport.getWorldWidth() / 2 - (layout.width + 40) / 2, viewport.getWorldHeight() - 72 - layout.height, layout.width + 40, layout.height + 40);
            game.getFonts().get("moonhouse64").draw(spriteBatch, campaignWorlds.get(selection).name, viewport.getWorldWidth() / 2 - layout.width / 2, viewport.getWorldHeight() - 10 - layout.height);
            layout.setText(game.getFonts().get("moonhouse64"), campaignWorlds.get(selection).desc);
            spriteBatch.draw(textBack, viewport.getWorldWidth() / 2 - (layout.width + 40) / 2, layout.height + 20, layout.width + 40, layout.height + 40);
            game.getFonts().get("moonhouse64").draw(spriteBatch, campaignWorlds.get(selection).desc, viewport.getWorldWidth() / 2 - layout.width / 2, layout.height + 83);
        }
        spriteBatch.end();
    }

    @Override
    protected void update(float delta) {
        staticPointerFrames++;

        if(Gdx.input.isTouched() && staticPointerFrames > 2) //Frames without dragging until velocity is reset
            velocity = 0;
        if (Math.abs(velocity) > MAX_VELOCITY)
            velocity = velocity < 0 ? -MAX_VELOCITY : MAX_VELOCITY;
        if (Math.abs(velocity) < DECELERATION)
            velocity = 0;
        if (velocity != 0)
            velocity += velocity < 0 ? DECELERATION : -DECELERATION;
        if(!Gdx.input.isTouched())
            scroll += velocity;
        if (scroll > length || scroll < 0) {
            scroll = Math.max(0, Math.min(scroll, length));
            velocity = 0;
        }

        if(autoMove && moveToWorld != -1 && Math.abs(scroll - (moveToWorld * segmentWidth)) >= autoVelocity) {
            autoVelocity += AUTO_ACCELERATION;
            if(autoVelocity > MAX_AUTO_VELOCITY)
                autoVelocity = MAX_AUTO_VELOCITY;
            scroll += (scroll - (moveToWorld * segmentWidth) > 0 ? -autoVelocity : autoVelocity);
        }
        if(Math.abs(scroll - (moveToWorld * segmentWidth)) < autoVelocity) {
            autoMove = false;
            scroll = moveToWorld * segmentWidth;
            autoVelocity = STARTING_AUTO_VELOCITY;
        }

        selection = getWorldFromX(scroll + ICON_SIZE / 2 + halfFreeWidth);
    }

    //Return world number or -1 if not contained
    private int getWorldFromX(int x) {
        int halfFreeWidth = (int)((viewport.getWorldWidth() - ICON_SIZE) / 2);
        for(int i = 0; i < campaignWorlds.size(); i++)
            if(Math.abs(x - (halfFreeWidth + ICON_SIZE) * i - ICON_SIZE / 2 - halfFreeWidth) < ICON_SIZE / 2)
                return x / ((int)((viewport.getWorldWidth() - ICON_SIZE) / 2) + ICON_SIZE);
        return -1;
    }

    //Return world number or -1 if not contained
    private int getTouchedWorld(int touchX, int touchY) {
        int halfFreeHeight = (int)((viewport.getWorldHeight() - ICON_SIZE) / 2);
        if(touchY > halfFreeHeight && touchY < viewport.getWorldHeight() - halfFreeHeight && getWorldFromX(touchX + scroll) != -1) {
            return getWorldFromX(touchX + scroll);
        }
        return -1;
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        tempVector.set(screenX, screenY, 0);
        viewport.unproject(tempVector);
        if(pointer == 0) {
            touchedWorld = getTouchedWorld((int) tempVector.x, (int) tempVector.y);
            lastScroll = scroll;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        tempVector.set(screenX, screenY, 0);
        viewport.unproject(tempVector);
        if(pointer == 0 && Math.abs(scroll - lastScroll) < 100 && touchedWorld != -1 && touchedWorld == getTouchedWorld((int)tempVector.x, (int)tempVector.y)) {
            if(Math.abs(scroll - (touchedWorld * (int)((viewport.getWorldWidth() - ICON_SIZE) / 2 + ICON_SIZE))) < 200) {
                Gdx.app.error("CampaignStarting", campaignWorlds.get(touchedWorld).name);
                game.setScreen(new LevelSelectScreen(game, touchedWorld));
            } else {
                autoMove = true;
                moveToWorld = touchedWorld;
            }
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float change = Gdx.input.getDeltaX() * viewport.getWorldWidth() / viewport.getScreenWidth();//Scale to screen width
        velocity -= change / 10f;
        scroll -= change;
        autoMove = false;
        staticPointerFrames = 0;
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}