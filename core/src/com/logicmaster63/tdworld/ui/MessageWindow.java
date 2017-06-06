package com.logicmaster63.tdworld.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.logicmaster63.tdworld.TDWorld;

public class MessageWindow extends Actor {

    private Texture texture;
    private int state = 0;
    private String message;
    private Table table;

    public MessageWindow(String message, float width, float height) {
        setTouchable(Touchable.enabled);
        setBounds(width / 2 - width / 4, height / 2 - height / 10, width / 2, height / 5);
        texture = new Texture("theme/basic/ui/Window.png");
        this.message = message;
        table = new Table();
        table.setSize(getWidth(), getHeight());
        table.align(Align.center | Align.top);
        table.setPosition(getX(), getY());
        Label label = new Label(message, new Label.LabelStyle(TDWorld.getFonts().get("moonhouse64"), Color.BLACK));
        label.setWrap(true);
        label.setFontScale(0.7f);
        Label label2 = new Label("Tap to continue", new Label.LabelStyle(TDWorld.getFonts().get("moonhouse64"), Color.BLACK));
        label2.setFontScale(0.6f);
        table.add(label).width(getWidth());
        table.row();
        table.add(label2).width(getWidth()).expandY();
        table.pad(0, 30, 0, 30);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        table.act(delta);
        switch (state) {
            case 0:
                if(!Gdx.input.isTouched())
                    state++;
                break;
            case 1:
                if(Gdx.input.isTouched())
                    state++;
                break;
            case 2:
                if(!Gdx.input.isTouched()) {
                    AlphaAction action = new AlphaAction();
                    action.setAlpha(0f);
                    action.setDuration(0.3f);
                    addAction(new SequenceAction(action, new RunnableAction() {
                        @Override
                        public void run() {
                            remove();
                        }
                    }));
                    state++;
                }
                break;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, parentAlpha * color.a);
        batch.draw(texture, getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());
        table.draw(batch, parentAlpha * color.a);
        batch.setColor(color.r, color.g, color.b, 1f);
    }

    @Override
    public boolean remove() {
        texture.dispose();
        return super.remove();
    }
}
