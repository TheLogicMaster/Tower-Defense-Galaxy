package com.logicmaster63.tdworld.tools;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.Disposable;
import com.logicmaster63.tdworld.TDWorld;
import com.logicmaster63.tdworld.map.Curve;
import com.logicmaster63.tdworld.map.Strait;
import com.logicmaster63.tdworld.map.Track;
import com.logicmaster63.tdworld.tools.Tools;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class FileHandler {

    private static ArrayList<Disposable> disposables = new ArrayList<Disposable>();

    public static ArrayList<Point> loadTrack(BufferedReader data) {
        ArrayList<Track> track = new ArrayList<Track>();
        ArrayList<Point> path = new ArrayList<Point>();
        try {
            String line = "";
            int parsed[] = Tools.doubleParseInt(data.readLine());
            path.add(new Point(parsed[0], parsed[1]));
            line = data.readLine();
            while (!line.equals("@")) {
                //if(line.equals("@"))
                //break;
                int speeed = Integer.parseInt(data.readLine());
                if (line.equals("|")) { //Strait
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
            for (Track t : track)
                path.addAll(t.getPoints(TDWorld.res));
            parsed = Tools.doubleParseInt(data.readLine());
            path.add(new Point(parsed[0], parsed[1]));
        } catch (IOException e) {
            Gdx.app.log("Error", e.toString());
        }
        return path;
    }

    public static BufferedReader getReader(String path) {
        BufferedReader reader = null;
        try {
            if (Gdx.app.getType().equals(Application.ApplicationType.Android)) {
                File file = Gdx.files.internal(path).file();
                reader = new BufferedReader(new FileReader(file));
            } else {
                try {
                    reader = new BufferedReader(new FileReader(path));
                } catch (FileNotFoundException e) {
                    Gdx.app.log("Error", e.toString());
                }
            }
        } catch(IOException e) {
            Gdx.app.log("Error", e.toString());
        }
        return reader;
    }



    public static void addDisposables(Disposable...d) {
        disposables.addAll(Arrays.asList(d));
    }

    public static void dispose() {
        if(disposables != null)
            for(Disposable d: disposables)
                d.dispose();
    }
}
