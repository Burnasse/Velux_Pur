package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.VeluxPurGame;
import com.mygdx.game.scene.menu.*;

/**
 * The type Game screen.
 */
public class GameScreen implements Screen, StageManager {

    private PerspectiveCamera cam;
    private ModelBatch modelBatch;
    private Model model;
    private ModelInstance instance;
    private Environment environment;
    private CameraInputController camController;

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

        modelBatch = new ModelBatch();

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(5f, 5f, 5f,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model);

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        stageManager = new MenuManager();
        stageManager.addStage("Main", new MainMenu(this, viewport, true).getStage());
        stageManager.addStage("Settings", new SettingsMenu(this).getStage());
        stageManager.addStage("Audio", new AudioMenu(this).getStage());
        stageManager.addStage("Advanced", new AdvancedMenu(this).getStage());

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        cam.update();
        camController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        modelBatch.render(instance, environment);
        modelBatch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            if (isInMenu) {
                Gdx.input.setInputProcessor(camController);
                isInMenu = false;
            } else {
                stage = stageManager.getStageByName("Main");
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
        modelBatch.dispose();
        model.dispose();
    }

    @Override
    public void show(String stageName) {
        stage = stageManager.changeStage(stageName, viewport);
    }

    @Override
    public void startGame() {
    }
}
