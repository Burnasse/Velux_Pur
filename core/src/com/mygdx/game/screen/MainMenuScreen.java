package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Assets;
import com.mygdx.game.VeluxPurGame;
import com.mygdx.game.scene.menu.*;
import com.mygdx.game.ui.HealthBar;

/**
 * The type Main menu screen.
 */
public class MainMenuScreen implements Screen, StageManager {

    private VeluxPurGame manager;
    private Assets assets;
    private Stage stage;
    private ScreenViewport viewport = new ScreenViewport();
    private MenuManager menuManager;
    private MainMenu mainMenu;
    private Music musicMenu;
    private TextureRegion mainBackground;
    private SpriteBatch batch;

    private HealthBar healthBar;
    /**
     * Instantiates a new Main menu screen.
     *
     * @param manager the main class who manage all screen
     */
    public MainMenuScreen(final VeluxPurGame manager, Assets assets) {
        this.manager = manager;
        this.assets = assets;
    }

    public void initScreen() {
        mainMenu = new MainMenu(this,viewport,false, assets);
        Controllers.addListener((ControllerListener) mainMenu.getStage());
        this.stage = mainMenu.getStage();

        healthBar = new HealthBar();
        musicMenu= assets.manager.get(Assets.menuTheme);
        musicMenu.setVolume(0.5f);
        musicMenu.setLooping(true);
        musicMenu.play();
        menuManager = new MenuManager();
        menuManager.addMenuStage("Main", mainMenu);
        menuManager.addMenuStage("Settings", new SettingsMenu(this, assets));
        menuManager.addMenuStage("Audio", new AudioMenu(this, assets));
        menuManager.addMenuStage("Advanced", new AdvancedMenu(this, assets));
        menuManager.addMenuStage("Controls", new ControlsMenu(this, assets));

        mainBackground = new TextureRegion(assets.manager.get(Assets.mainBackground), 0, 0, 1789, 1440);
        batch = new SpriteBatch();
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

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(mainBackground, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.end();

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
        menuManager.dispose();
        musicMenu.dispose();
    }

    public void displayStage(String stageName) {
        stage = menuManager.changeStage(stageName, viewport);
    }

    public void changeGameState() {
        manager.changeScreen(new GameScreen(manager, assets));
    }

    public void startMultiplayerGame() {
        manager.setScreen(new MultiplayerGameScreen(manager, assets));
    }

}
