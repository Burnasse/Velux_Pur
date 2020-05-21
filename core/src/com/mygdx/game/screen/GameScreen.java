package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.VeluxPurGame;
import com.mygdx.game.controller.ButtonStageController;
import com.mygdx.game.scene.menu.*;
import com.mygdx.game.gameGeneration.GenerateVillage;

/**
 * The type Game screen.
 */
public class GameScreen implements Screen, StageManager {

    private GenerateVillage village;

    private MainMenu menu;

    /**
     * Used to manage the menu
     */
    private MenuManager stageManager;

    /**
     * Used to display properly the menu
     */
    private ScreenViewport viewport = new ScreenViewport();

    /**
     * Used to display all elements of the menu
     */
    private Stage stage;
    private Boolean isInMenu = false;

    private VeluxPurGame manager;

    /**
     * Instantiates a new GameScreen.
     * It's equivalent to the create() method.
     *
     * @param manager the main class who manage all screen
     */
    public GameScreen(VeluxPurGame manager) {
        this.manager = manager;
    }

    @Override
    public void show() {
        initScreen();
    }

    @Override
    public void render(float delta) {
        village.render();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            if (isInMenu) {
                village.setController();
                isInMenu = false;
            } else {
                stage = stageManager.getStageByName("Main").getStage();
                Controllers.clearListeners();
                Controllers.addListener((ButtonStageController)menu.getStage());
                Gdx.input.setInputProcessor(stage);
                isInMenu = true;
            }
        }

        if (isInMenu) {
            stage.act(delta);
            stage.draw();
        }

    }

    @Override
    public void resize(int width, int height) {
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
        stageManager.dispose();
        village.dispose();
    }

    @Override
    public void displayStage(String stageName) {
        stage = stageManager.changeStage(stageName, viewport);
    }

    @Override
    public void startGame() {
    }

    @Override
    public void initScreen() {
        village = new GenerateVillage(this,false);
        village.create();

        menu = new MainMenu(this, viewport, true);
        stageManager = new MenuManager();
        stageManager.addMenuStage("Main", menu);
        stageManager.addMenuStage("Settings", new SettingsMenu(this));
        stageManager.addMenuStage("Audio", new AudioMenu(this));
        stageManager.addMenuStage("Advanced", new AdvancedMenu(this));
        stageManager.addMenuStage("Controls", new ControlsMenu(this));
    }

    @Override
    public ScreenViewport getViewport() {
        return viewport;
    }

    public void goToLevel(){
        manager.changeScreen(new LevelScreen(manager));
    }
}
