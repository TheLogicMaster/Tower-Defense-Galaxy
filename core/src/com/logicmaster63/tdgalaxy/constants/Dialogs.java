package com.logicmaster63.tdgalaxy.constants;

import com.badlogic.gdx.Gdx;
import de.tomgrill.gdxdialogs.core.GDXDialogs;
import de.tomgrill.gdxdialogs.core.GDXDialogsSystem;
import de.tomgrill.gdxdialogs.core.dialogs.GDXButtonDialog;
import de.tomgrill.gdxdialogs.core.listener.ButtonClickListener;

public class Dialogs {

    public GDXButtonDialog needAssets;
    public GDXDialogs dialogs;

    public Dialogs() {
        dialogs = GDXDialogsSystem.install();

        needAssets = dialogs.newDialog(GDXButtonDialog.class);
        needAssets.setTitle("Error");
        needAssets.setMessage("Missing expansion file, download now?");
        needAssets.addButton("Cancel");
        needAssets.addButton("Yes");
        needAssets.setCancelable(false);
        needAssets.setClickListener(new ButtonClickListener() {
            @Override
            public void click(int button) {
                if(button == 1);

                else
                    needAssets.dismiss();
            }
        });
    }
}
