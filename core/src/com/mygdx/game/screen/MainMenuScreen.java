package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.controller.MenuController;
import com.mygdx.game.scene.MenuStage;
import com.mygdx.game.VeluxPurGame;
import com.mygdx.game.scene.TextButtonContainer;


/**
 * The type Main menu screen.
 */
public class MainMenuScreen implements Screen {

    private VeluxPurGame manager;
    private MenuStage stage;
    private TextButtonContainer container;

    /**
     * Instantiates a new Main menu screen.
     *
     * @param manager the main class who manage all screen
     */
    public MainMenuScreen(final VeluxPurGame manager) {
        this.manager = manager;

        container = new TextButtonContainer("Play", "Settings", "Quit");

        container.getButtonByName("Play").addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                manager.changeScreen(new GameScreen(manager));
                return true;
            }
        });

        container.getButtonByName("Settings").addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                manager.changeScreen(new SettingsScreen(manager));
                return true;
            }
        });

        container.getButtonByName("Quit").addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
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
    }

    public void setInputProcessor(){
        Gdx.input.setInputProcessor(stage);
    }
}
