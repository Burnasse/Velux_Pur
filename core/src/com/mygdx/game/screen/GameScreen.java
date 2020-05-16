package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.VeluxPurGame;
import com.mygdx.game.controller.ButtonStageController;
import com.mygdx.game.scene.menu.*;
import com.mygdx.game.gameGeneration.GenerateVillage;
import com.mygdx.game.ui.Minimap;

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
    private Viewport viewport = new ScreenViewport();

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
                stage = stageManager.getStageByName("Main");
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
        village = new GenerateVillage(this,true);
        village.create();

        menu = new MainMenu(this, viewport, true);
        stageManager = new MenuManager();
        stageManager.addStage("Main", menu.getStage());
        stageManager.addStage("Settings", new SettingsMenu(this).getStage());
        stageManager.addStage("Audio", new AudioMenu(this).getStage());
        stageManager.addStage("Advanced", new AdvancedMenu(this).getStage());
        stageManager.addStage("Controls", new ControlsMenu(this).getStage());
    }

    public void goToLevel(){
        manager.changeScreen(new LevelScreen(manager));
    }
}
