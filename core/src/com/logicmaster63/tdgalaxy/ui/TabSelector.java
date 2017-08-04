package com.logicmaster63.tdgalaxy.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class TabSelector extends Actor {

    private int slot;
    private final int slotNum;
    private Texture[] icons;
    private boolean isVertical;
    private Texture selection;

    public TabSelector(final int slotNum, Texture[] icons, final boolean isVertical, int defaultSlot) {
        if(slotNum != icons.length)
            throw new IllegalArgumentException("Icon array length needs to equal slot quantity");
        setTouchable(Touchable.enabled);
        slot = defaultSlot;
        this.slotNum = slotNum;
        this.isVertical = isVertical;
        this.icons = icons;
        selection = new Texture("theme/basic/ui/SelectionBox.png");
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(isVertical)
                    slot = (int)(y / (getHeight() / slotNum));
                else
                    slot = (int)(x / (getWidth() / slotNum));
                return true;
            }
        });
    }

    public TabSelector(int slotNum, Texture[] icons, boolean isVertical) {
        this(slotNum, icons, isVertical, 0);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for(int i = 0; i < slotNum; i++)
            if(icons[i] != null) {
                if (isVertical)
                    batch.draw(icons[i], getX(), getY() + getHeight() / slotNum * i, getWidth(), getHeight() / slotNum);
                else
                    batch.draw(icons[i], getX() + getWidth() / slotNum * i,getY(), getWidth() / slotNum, getHeight());
            }
        if(isVertical)
            batch.draw(selection, getX(), getY() + getHeight() / slotNum * slot, getWidth(), getHeight() / slotNum);
        else
            batch.draw(selection, getX() + getWidth() / slotNum * slot, getY(), getWidth() / slotNum, getHeight());
        }

    @Override
    public boolean remove() {
        selection.dispose();
        return super.remove();
    }
}
