package com.logicmaster63.tdworld.tools;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.logicmaster63.tdworld.TDWorld;
import com.logicmaster63.tdworld.map.Curve;
import com.logicmaster63.tdworld.map.Strait;
import com.logicmaster63.tdworld.map.Track;
import com.logicmaster63.tdworld.screens.GameScreen;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class FileHandler {

    private static ArrayList<Disposable> disposables = new ArrayList<Disposable>();

    public static ArrayList<Vector3> loadTrack(BufferedReader data, GameScreen screen) {
        if(data == null)
            return null;
        ArrayList<Track> track = new ArrayList<Track>();
        ArrayList<Vector3> path = new ArrayList<Vector3>();
        try {
            String line = "";
            int parsed[] = Tools.trippleParseInt(data.readLine());
            screen.setSpawnPos(new Vector3(parsed[0], parsed[1], parsed[2]));
            parsed = Tools.trippleParseInt(data.readLine());
            path.add(new Vector3(parsed[0], parsed[1], parsed[2]));
            line = data.readLine();
            while (!line.equals("@")) {
                //if(line.equals("@"))
                //break;
                int speeed = Integer.parseInt(data.readLine());
                if (line.equals("|")) { //Strait
                    line = data.readLine();
                    track.add(new Strait(speeed, new Vector3(Tools.trippleParseInt(line)[0], Tools.trippleParseInt(line)[1], Tools.trippleParseInt(line)[2])));
                } else { //Curve
                    parsed = Tools.trippleParseInt(data.readLine());
                    Vector3 k0 = new Vector3(parsed[0], parsed[1], parsed[2]);
                    parsed = Tools.trippleParseInt(data.readLine());
                    Vector3 k1 = new Vector3(parsed[0], parsed[1], parsed[2]);
                    parsed = Tools.trippleParseInt(data.readLine());
                    Vector3 k2 = new Vector3(parsed[0], parsed[1], parsed[2]);
                    track.add(new Curve(speeed, k0, k1, k2));
                }
                line = data.readLine();
            }
            for (Track t : track)
                path.addAll(t.getPoints(TDWorld.res));
            parsed = Tools.trippleParseInt(data.readLine());
            path.add(new Vector3(parsed[0], parsed[1], parsed[2]));
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

    public static void loadPlanet(BufferedReader data, GameScreen screen) {
        int coords[] = {0, 0, 0};
        try {
            screen.setHasPlanetModel(Boolean.parseBoolean(data.readLine()));
            coords = Tools.trippleParseInt(data.readLine());
            screen.setPlanetName(data.readLine());
        } catch(IOException e) {
            Gdx.app.log("Error", e.toString());
        }
        screen.setPlanetSize(new Vector3(coords[0], coords[1], coords[2]));
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
