package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class VeluxPurGame extends Game {

    private MainMenuScreen menuScreen;

    @Override
    public void create() {
        menuScreen = new MainMenuScreen(this);
        backToMenu();
    }

    public void changeScreen(Screen newScreen){
        Screen previousScreen = getScreen();
        setScreen(newScreen);

        if(previousScreen != null)
            previousScreen.dispose();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();
    }

    public void backToMenu(){
        setScreen(menuScreen);
    }
}
