package com.logicmaster63.tdworld.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.logicmaster63.tdworld.screens.TDScreen;

import java.lang.reflect.Method;

public class TextListener implements Input.TextInputListener {

    private String parameter;
    private TDScreen screen;
    private Class clazz;

    public TextListener(TDScreen screen) {
        this.screen = screen;
    }

    @Override
    public void input(String text) {
        try {
            Method method = screen.getClass().getDeclaredMethod(parameter, clazz);
            if(clazz.equals(int.class))
                method.invoke(screen, Integer.parseInt(text));
            if(clazz.equals(String.class))
                method.invoke(screen, text);
            if(clazz.equals(Double.class))
                method.invoke(screen, Double.parseDouble(text));
            if(clazz.equals(Float.class))
                method.invoke(screen, Float.parseFloat(text));
        } catch(Exception e) {
            Gdx.app.log("Error", e.toString());
        }
    }

    @Override
    public void canceled() {

    }

    public void inputSet(String parameter, Class clazz) {
        this.parameter = parameter;
        Gdx.input.getTextInput(this, "Change " + parameter, "", "New " + clazz.getName());
        this.clazz = clazz;
    }
}
