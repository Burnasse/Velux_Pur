package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.VeluxPurGame;
import com.mygdx.game.scene.menu.*;

/**
 * The type Main menu screen.
 */
public class MainMenuScreen implements Screen, StageManager {

    private VeluxPurGame manager;
    private Stage stage;
    private ScreenViewport viewport = new ScreenViewport();
    private MenuManager stageManager;
    private MainMenu mainMenu;

    /**
     * Instantiates a new Main menu screen.
     *
     * @param manager the main class who manage all screen
     */
    public MainMenuScreen(final VeluxPurGame manager) {
        this.manager = manager;
    }

    public void initScreen() {
        mainMenu = new MainMenu(this,viewport,false);
        Controllers.addListener((ControllerListener) mainMenu.getStage());
        this.stage = mainMenu.getStage();
        stageManager = new MenuManager();
        stageManager.addStage("Main", stage);
        stageManager.addStage("Settings", new SettingsMenu(this).getStage());
        stageManager.addStage("Audio", new AudioMenu(this).getStage());
        stageManager.addStage("Advanced", new AdvancedMenu(this).getStage());
        stageManager.addStage("Controls", new ControlsMenu(this).getStage());
    }

    @Override
    public void show() {
        initScreen();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 1, 1);
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
        mainMenu.dispose();
        stageManager.dispose();
    }

    public void displayStage(String stageName) {
        stage = stageManager.changeStage(stageName, viewport);
    }

    public void startGame() {
        manager.changeScreen(new GameScreen(manager));
    }

    public void startMultiplayerGame() {
        manager.setScreen(new MultiplayerGameScreen(manager));
    }

}
