package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Assets;
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
    private Assets assets;

    /**
     * Instantiates a new GameScreen.
     * It's equivalent to the create() method.
     *
     * @param manager the main class who manage all screen
     */
    public GameScreen(VeluxPurGame manager, Assets assets) {
        this.manager = manager;
        this.assets = assets;
        assets.loadGame();
        assets.loadVillage();
        assets.manager.finishLoading();
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
        village.resize(width,height);
        viewport.update(width,height,true);
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
    public void changeGameState() {
        assets.unloadVillage();
        manager.changeScreen(new LevelScreen(manager, assets));
    }

    @Override
    public void initScreen() {
        village = new GenerateVillage(this,assets,false);
        village.create();

        menu = new MainMenu(this, viewport, true, assets);
        stageManager = new MenuManager();
        stageManager.addMenuStage("Main", menu);
        stageManager.addMenuStage("Settings", new SettingsMenu(this, assets));
        stageManager.addMenuStage("Audio", new AudioMenu(this, assets));
        stageManager.addMenuStage("Advanced", new AdvancedMenu(this, assets));
        stageManager.addMenuStage("Controls", new ControlsMenu(this, assets));
    }

    @Override
    public ScreenViewport getViewport() {
        return viewport;
    }
}
