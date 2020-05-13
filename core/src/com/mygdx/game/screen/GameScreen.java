package com.mygdx.game.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.FloorGeneration.GenerateLevel;
import com.mygdx.game.VeluxPurGame;
import com.mygdx.game.scene.menu.*;

/**
 * The type Game screen.
 */
public class GameScreen implements Screen, StageManager {

    GenerateLevel level;

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
        level.render();
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
        stage.dispose();
        level.dispose();
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
        level = new GenerateLevel();
        level.create();

        stageManager = new MenuManager();
        stageManager.addStage("Main", new MainMenu(this, viewport, true).getStage());
        stageManager.addStage("Settings", new SettingsMenu(this).getStage());
        stageManager.addStage("Audio", new AudioMenu(this).getStage());
        stageManager.addStage("Advanced", new AdvancedMenu(this).getStage());
        stageManager.addStage("Controls", new ControlsMenu(this).getStage());
    }
}
