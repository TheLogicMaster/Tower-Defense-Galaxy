package com.logicmaster63.tdworld.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.logicmaster63.tdworld.TDWorld;
import com.logicmaster63.tdworld.map.Curve;
import com.logicmaster63.tdworld.map.Strait;
import com.logicmaster63.tdworld.map.Track;
import com.logicmaster63.tdworld.tools.Tools;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GameScreen extends TDScreen{

    private SpriteBatch batch;
    private Texture background;
    private int map;
    private ArrayList<Track> track = new ArrayList<Track>();
    private ArrayList<Point> path = new ArrayList<Point>();
    private BufferedReader data;

    public GameScreen(Game game, int map) {
        super(game);
        this.map = map;
    }

    @Override
    public void show () {
        batch = new SpriteBatch();
        batch.getProjectionMatrix().setToOrtho2D(0, 0, 480, 320);
        background = new Texture("Background_MainMenu.png");
        try {
            data = new BufferedReader(new FileReader("track/Track" + Integer.toString(map)));
        } catch (FileNotFoundException e) {
            Gdx.app.debug("File not found", e.toString());
        }
        try {
            String line = "";
            int parsed[] = Tools.doubleParseInt(data.readLine());
            path.add(new Point(parsed[0], parsed[1]));
            line = data.readLine();
            while(!line.equals("@")) {
                //if(line.equals("@"))
                    //break;
                int speeed = Integer.parseInt(data.readLine());
                if(line.equals("|")) { //Strait
                    line = data.readLine();
                    track.add(new Strait(speeed, new Point(Tools.doubleParseInt(line)[0], Tools.doubleParseInt(line)[1])));
                } else { //Curve
                    parsed = Tools.doubleParseInt(data.readLine());
                    Point k0 = new Point(parsed[0], parsed[1]);
                    parsed = Tools.doubleParseInt(data.readLine());
                    Point k1 = new Point(parsed[0], parsed[1]);
                    parsed = Tools.doubleParseInt(data.readLine());
                    Point k2 = new Point(parsed[0], parsed[1]);
                    track.add(new Curve(speeed, k0, k1, k2));
                }
                line = data.readLine();
            }
            for(Track t: track)
                path.addAll(t.getPoints(TDWorld.res));
            parsed = Tools.doubleParseInt(data.readLine());
            path.add(new Point(parsed[0], parsed[1]));
        } catch (IOException e) {
            Gdx.app.debug("File not found", e.toString());
        }
        System.out.println(track);
        System.out.println(path);
    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(background, 0, 0);
        batch.end();
    }

    @Override
    public void hide () {
        batch.dispose();
        background.dispose();
    }
}
