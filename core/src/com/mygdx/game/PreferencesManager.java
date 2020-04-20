package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;

public class PreferencesManager {
    Preferences preferences = Gdx.app.getPreferences("veluxgamePrefs");;

    public void initializePrefs(){
        preferences = Gdx.app.getPreferences("veluxgamePrefs");

        if(!preferences.getBoolean("isInit")){
            initKey();
            initSound();
            preferences.putBoolean("isInit", true);
        }


        if(preferences.contains("isFullscreen")){
            if(preferences.getBoolean("isFullscreen"))
                setFullscreen();
            else
                setWindowed();
        }
        else{
            preferences.putBoolean("isFullscreen", true).flush();
            setFullscreen();
        }
    }

    private int getDisplayIndex(int width, int height){
        int length = Gdx.graphics.getDisplayModes().length;
        for (int i = 0; i < length; i++) {
            if(width == Gdx.graphics.getDisplayModes()[i].width && height == Gdx.graphics.getDisplayModes()[i].height)
                return i;
        }

        return 0;
    }

    public void setFullscreen(){
        if(preferences.contains("width") && preferences.contains("height")){
            int width = preferences.getInteger("width");
            int height = preferences.getInteger("height");
            int index = getDisplayIndex(width, height);
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayModes()[index]);
        }else{
            preferences.putInteger("width", Gdx.graphics.getDisplayMode().width);
            preferences.putInteger("height", Gdx.graphics.getDisplayMode().height);
            preferences.flush();

            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }

        preferences.putBoolean("isFullscreen", true).flush();
    }

    public void setWindowed(){
        int width = preferences.getInteger("width");
        int height = preferences.getInteger("height");
        Gdx.graphics.setWindowedMode(width, height);

        preferences.putBoolean("isFullscreen", false).flush();
    }

    public void setResolution(int width, int height){
        preferences.putInteger("width", width);
        preferences.putInteger("height", height);
        preferences.flush();

        if(Gdx.graphics.isFullscreen()){
            int index = getDisplayIndex(width, height);
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayModes()[index]);
        }
        else{
            Gdx.graphics.setWindowedMode(width, height);
        }

    }

    public Preferences getPreferences() {
        return preferences;
    }

    private void initKey(){
        preferences.putInteger("UP_ARR", Input.Keys.UP);
        preferences.putInteger("DOWN_ARR", Input.Keys.DOWN);
        preferences.putInteger("LEFT_ARR", Input.Keys.LEFT);
        preferences.putInteger("RIGHT_ARR", Input.Keys.RIGHT);

        preferences.putInteger("UP_KEY", Input.Keys.Z);
        preferences.putInteger("DOWN_KEY", Input.Keys.S);
        preferences.putInteger("LEFT_KEY", Input.Keys.Q);
        preferences.putInteger("RIGHT_KEY", Input.Keys.D);

        preferences.flush();
    }

    private void initSound(){
        preferences.putInteger("volume", 100);
        preferences.flush();
    }
}
