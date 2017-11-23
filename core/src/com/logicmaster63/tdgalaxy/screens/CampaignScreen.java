package com.logicmaster63.tdgalaxy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector3;
import com.logicmaster63.tdgalaxy.TDGalaxy;
import com.logicmaster63.tdgalaxy.map.world.CampaignWorld;

import java.util.ArrayList;
import java.util.List;

public class CampaignScreen extends TDScreen implements InputProcessor {

    private final int MAX_VELOCITY = 200;
    private final int MAX_AUTO_VELOCITY = 40;
    private final int STARTING_AUTO_VELOCITY = 6;
    private final int ICON_SIZE = 1000;
    private final float AUTO_ACCELERATION = 0.4f;
    private final float DECELERATION = 0.8f;

    private Texture background, textBack;
    private float velocity, autoVelocity = STARTING_AUTO_VELOCITY;
    private int scroll, lastScroll;
    private List<CampaignWorld> campaignWorlds;
    private boolean autoMove = false;
    private GlyphLayout layout;
    private int touchedWorld = -1, moveToWorld = -1; //-1 means no world
    private Vector3 tempVector;

    public CampaignScreen(TDGalaxy game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        layout = new GlyphLayout();
        tempVector = new Vector3();

        campaignWorlds = new ArrayList<CampaignWorld>();
        campaignWorlds.add(new CampaignWorld(new Texture("theme/basic/Icon.png"), "World 0's Name", "Put description here for basic"));
        campaignWorlds.add(new CampaignWorld(new Texture("theme/Fallback/Icon.png"), "Put Name Here 1","Put description here for fallback1"));
        campaignWorlds.add(new CampaignWorld(new Texture("theme/Fallback/Icon.png"), "Put Name Here 2","Put description here for fallback2"));
        campaignWorlds.add(new CampaignWorld(new Texture("theme/Fallback/Icon.png"), "Put Name Here 3","Put description here for fallback3"));
        campaignWorlds.add(new CampaignWorld(new Texture("theme/Fallback/Icon.png"), "Put Name Here 4","Put description here for fallback4"));
        campaignWorlds.add(new CampaignWorld(new Texture("theme/Fallback/Icon.png"), "Put Name Here 5","Put description here for fallback5"));
        campaignWorlds.add(new CampaignWorld(new Texture("theme/Fallback/Icon.png"), "Put Name Here 6","Put description here for fallback6"));

        background = new Texture("menus/Background_Campaign.png");
        textBack = new Texture("menus/text_back_campaign.png");

        addDisposables(background, textBack);

        addInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int halfFreeWidth = (int)((viewport.getWorldWidth() - ICON_SIZE) / 2); //Half of the area that ism't part of icon
        int segmentWidth = ICON_SIZE + halfFreeWidth; //The width of what is taken up by one world/associated empty space
        int length = segmentWidth * (campaignWorlds.size() - 1); //Distance that can be travelled by foreground icons

        if (Math.abs(velocity) > MAX_VELOCITY)
            velocity = velocity < 0 ? -MAX_VELOCITY : MAX_VELOCITY;
        if (Math.abs(velocity) < DECELERATION)
            velocity = 0;
        if (velocity != 0)
            velocity += velocity < 0 ? DECELERATION : -DECELERATION;
        if(!Gdx.input.isTouched()) {
            scroll += velocity;
        }
        if (scroll > length || scroll < 0) {
            scroll = Math.max(0, Math.min(scroll, length));
            velocity = 0;
        }

        int selection = getWorldFromX(scroll + ICON_SIZE / 2 + halfFreeWidth); //Selected world

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

        super.render(delta);
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
        orthographicCamera.unproject(tempVector);
        if(pointer == 0) {
            touchedWorld = getTouchedWorld((int) tempVector.x, (int) tempVector.y);
            lastScroll = scroll;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        tempVector.set(screenX, screenY, 0);
        orthographicCamera.unproject(tempVector);
        if(pointer == 0 && Math.abs(scroll - lastScroll) < 100 && touchedWorld != -1 && touchedWorld == getTouchedWorld((int)tempVector.x, (int)tempVector.y)) {
            if(Math.abs(scroll - (touchedWorld * (int)((viewport.getWorldWidth() - ICON_SIZE) / 2 + ICON_SIZE))) < 200) {
                Gdx.app.error("CampaignStarting", campaignWorlds.get(touchedWorld).name);
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