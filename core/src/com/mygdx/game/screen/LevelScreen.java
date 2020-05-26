package com.mygdx.game.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Assets;
import com.mygdx.game.gameGeneration.GenerateLevel;
import com.mygdx.game.VeluxPurGame;
import com.mygdx.game.scene.menu.*;

public class LevelScreen implements Screen, StageManager{

    private GenerateLevel level;

    /**
     * Used to manage the menu
     */
    private MenuManager menuManager;

    /**
     * Used to display properly the menu
     */
    private ScreenViewport viewport = new ScreenViewport();

    private Boolean isInMenu = false;

    private VeluxPurGame manager;
    private Assets assets;

    /**
     * Instantiates a new GameScreen.
     * It's equivalent to the create() method.
     *
     * @param manager the main class who manage all screen
     */
    public LevelScreen(VeluxPurGame manager, Assets assets) {
        this.manager = manager;
        this.assets = assets;
        assets.loadLevel();
        assets.manager.finishLoading();
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
        level.resize(width,height);
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
        level.dispose();
        menuManager.dispose();
    }

    @Override
    public void displayStage(String stageName) {
    }

    @Override
    public void changeGameState() {
        assets.unloadLevel();
        manager.changeScreen(new GameScreen(manager, assets));
    }

    @Override
    public void initScreen() {
        level = new GenerateLevel(this, assets,false);
        level.create();

        menuManager = new MenuManager();
        menuManager.addMenuStage("Main", new MainMenu(this, viewport, true, assets));
        menuManager.addMenuStage("Settings", new SettingsMenu(this, assets));
        menuManager.addMenuStage("Audio", new AudioMenu(this, assets));
        menuManager.addMenuStage("Advanced", new AdvancedMenu(this, assets));
        menuManager.addMenuStage("Controls", new ControlsMenu(this, assets));
    }

    @Override
    public ScreenViewport getViewport() {
        return viewport;
    }
}
