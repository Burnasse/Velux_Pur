package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.controller.MenuController;
import com.mygdx.game.scene.MenuStage;
import com.mygdx.game.VeluxPurGame;


/**
 * The type Main menu screen.
 */
public class MainMenuScreen implements Screen {

    private VeluxPurGame manager;
    private MenuStage stage;
    private VerticalGroup buttonGroup;
    private Container container;
    private Skin skin;

    /**
     * Instantiates a new Main menu screen.
     *
     * @param manager the main class who manage all screen
     */
    public MainMenuScreen(final VeluxPurGame manager) {
        this.manager = manager;

        skin = new Skin();
        skin.addRegions(new TextureAtlas(Gdx.files.internal("ButtonAssets/ui.atlas")));
        skin.load(Gdx.files.internal("ButtonAssets/ui.json"));

        TextButton playButton = new TextButton("Play", skin);
        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton quitButton = new TextButton("Quit", skin);

        playButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                manager.changeScreen(new GameScreen(manager));
                return true;
            }
        });

        quitButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return true;
            }
        });

        buttonGroup = new VerticalGroup();
        buttonGroup.addActor(playButton);
        buttonGroup.addActor(settingsButton);
        buttonGroup.addActor(quitButton);
        buttonGroup.space(10);

        container = new Container<VerticalGroup>();
        container.setActor(buttonGroup);
        container.setFillParent(true);

        MenuController menuController = new MenuController(playButton, settingsButton, quitButton);

        stage = new MenuStage(new ScreenViewport(), menuController);
        stage.setKeyboardFocus(buttonGroup);
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
}
