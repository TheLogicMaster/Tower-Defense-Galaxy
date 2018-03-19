package com.logicmaster63.tdgalaxy.tools;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TDStage extends Stage {



    public TDStage() {
    }

    public TDStage(Viewport viewport) {
        super(viewport);
    }

    public TDStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
    }


}
