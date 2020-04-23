package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * The type Preferences manager.
 */
public class PreferencesManager {
    /**
     * The Preferences get the veluxgamePrefs file in the .prefs folder (C://Users/USER_NAME/.prefs in windows).
     */
    Preferences preferences = Gdx.app.getPreferences("veluxgamePrefs");

    /**
     * Initialize prefs.
     * This method must be used in launch.
     */
    public void initializePrefs() {
        preferences = Gdx.app.getPreferences("veluxgamePrefs");

        if (!preferences.getBoolean("isInit")) {
            initSound();
            preferences.putBoolean("isInit", true);
        }

        if (preferences.contains("isFullscreen")) {
            if (preferences.getBoolean("isFullscreen"))
                setFullscreen();
            else
                setWindowed();
        } else {
            preferences.putBoolean("isFullscreen", true).flush();
            setFullscreen();
        }
    }

    /**
     * Retrieve the correct display index.
     *
     * @param width  the width
     * @param height the height
     * @return the index of the display width x height
     */
    private int getDisplayIndex(int width, int height) {
        int length = Gdx.graphics.getDisplayModes().length;
        for (int i = 0; i < length; i++) {
            if (width == Gdx.graphics.getDisplayModes()[i].width && height == Gdx.graphics.getDisplayModes()[i].height)
                return i;
        }

        return 0;
    }

    /**
     * Set fullscreen.
     */
    public void setFullscreen() {
        if (preferences.contains("width") && preferences.contains("height")) {
            int width = preferences.getInteger("width");
            int height = preferences.getInteger("height");
            int index = getDisplayIndex(width, height);
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayModes()[index]);
        } else {
            preferences.putInteger("width", Gdx.graphics.getDisplayMode().width);
            preferences.putInteger("height", Gdx.graphics.getDisplayMode().height);
            preferences.flush();

            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }

        preferences.putBoolean("isFullscreen", true).flush();
    }

    /**
     * Set windowed.
     */
    public void setWindowed() {
        int width = preferences.getInteger("width");
        int height = preferences.getInteger("height");
        Gdx.graphics.setWindowedMode(width, height);

        preferences.putBoolean("isFullscreen", false).flush();
    }

    /**
     * Set resolution.
     *
     * @param width  the width
     * @param height the height
     */
    public void setResolution(int width, int height) {
        preferences.putInteger("width", width);
        preferences.putInteger("height", height);
        preferences.flush();

        if (Gdx.graphics.isFullscreen()) {
            int index = getDisplayIndex(width, height);
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayModes()[index]);
        } else {
            Gdx.graphics.setWindowedMode(width, height);
        }

    }

    /**
     * Gets preferences.
     *
     * @return the preferences
     */
    public Preferences getPreferences() {
        return preferences;
    }

    /**
     * init sound (used in the first launch)
     */
    private void initSound() {
        preferences.putInteger("volume", 100);
        preferences.flush();
    }
}
