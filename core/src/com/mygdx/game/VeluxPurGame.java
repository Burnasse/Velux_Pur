package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.mygdx.game.controller.PrefKeys;
import com.mygdx.game.screen.MainMenuScreen;

/**
 * The main class of the game.
 */
public class VeluxPurGame extends Game {

    private MainMenuScreen menuScreen;

    @Override
    public void create() {
        Bullet.init();
        PreferencesManager pref = new PreferencesManager();
        pref.initializePrefs();
        PrefKeys.initKeys(pref.getPreferences());

        Assets assets = new Assets();
        assets.loadMenu();
        assets.manager.finishLoading();

        menuScreen = new MainMenuScreen(this, assets);
        backToMenu();
    }

    /**
     * Change screen.
     *
     * @param newScreen the new screen
     */
    public void changeScreen(Screen newScreen){
        final Screen previousScreen = getScreen();
        setScreen(newScreen);

        if(previousScreen != null){
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    previousScreen.dispose();
                }
            });
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();
    }

    /**
     * Back to menu.
     */
    public void backToMenu(){
        changeScreen(menuScreen);
    }

}
