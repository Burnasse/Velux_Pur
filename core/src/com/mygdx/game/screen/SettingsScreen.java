package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.VeluxPurGame;
import com.mygdx.game.controller.MenuController;
import com.mygdx.game.scene.MenuStage;
import com.mygdx.game.scene.TextButtonContainer;

public class SettingsScreen implements Screen {

    private VeluxPurGame manager;
    private TextButtonContainer container;
    private MenuStage stage;

    public SettingsScreen(final VeluxPurGame manager) {
        this.manager = manager;

        container = new TextButtonContainer("Controls", "Audio", "Advanced", "Back");

        container.getButtonByName("Back").addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                manager.backToMenu();
                return true;
            }
        });

        container.getButtonByName("Audio").addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                manager.changeScreen(new AudioScreen(manager));
                return true;
            }
        });

        container.getButtonByName("Advanced").addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                manager.changeScreen(new AdvancedScreen(manager));
                return true;
            }
        });

        TextButton[] buttons = container.getButtons().toArray();
        MenuController menuController = new MenuController(buttons);

        stage = new MenuStage(new ScreenViewport(), menuController);
        stage.setKeyboardFocus(container.getActor());
        stage.addActor(container);
        Gdx.input.setInputProcessor(stage);
        stage.act();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
