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
import com.mygdx.game.ui.HealthBar;

/**
 * The type Main menu screen.
 */
public class MainMenuScreen implements Screen, StageManager {

    private VeluxPurGame manager;
    private Stage stage;
    private ScreenViewport viewport = new ScreenViewport();
    private MenuManager menuManager;
    private MainMenu mainMenu;

    private HealthBar healthBar;
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

        healthBar = new HealthBar();

        menuManager = new MenuManager();
        menuManager.addStage("Main", stage);
        menuManager.addStage("Settings", new SettingsMenu(this).getStage());
        menuManager.addStage("Audio", new AudioMenu(this).getStage());
        menuManager.addStage("Advanced", new AdvancedMenu(this).getStage());
        menuManager.addStage("Controls", new ControlsMenu(this).getStage());
    }

    @Override
    public ScreenViewport getViewport() {
        return viewport;
    }

    @Override
    public void show() {
        initScreen();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1f);
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
        menuManager.dispose();
    }

    public void displayStage(String stageName) {
        stage = menuManager.changeStage(stageName, viewport);
    }

    public void startGame() {
        manager.changeScreen(new GameScreen(manager));
    }

    public void startMultiplayerGame() {
        manager.setScreen(new MultiplayerGameScreen(manager));
    }

}
