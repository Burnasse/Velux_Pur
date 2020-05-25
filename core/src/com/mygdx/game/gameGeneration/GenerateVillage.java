package com.mygdx.game.gameGeneration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Assets;
import com.mygdx.game.Entity.EntityPlayer;
import com.mygdx.game.Entity.NonPlayerCharacter;
import com.mygdx.game.Entity.PlayerFactory;
import com.mygdx.game.Entity.utils.EntityPosition;
import com.mygdx.game.FrustumCulling;
import com.mygdx.game.Trigger;
import com.mygdx.game.TriggersManager;
import com.mygdx.game.controller.VillageController;
import com.mygdx.game.physics.VillageContactListener;
import com.mygdx.game.screen.GameScreen;
import com.mygdx.game.ui.UIDialog;

import java.util.HashMap;

import static com.mygdx.game.physics.CallbackFlags.PLAYER_FLAG;
import static com.mygdx.game.physics.CallbackFlags.TRIGGER_FLAG;

/**
 * The type Village.
 */
public class GenerateVillage {

    private final boolean DEBUG_MODE;
    private DebugDrawer debugDrawer;

    private final GameScreen screen;
    private final Assets assets;

    private VillageContactListener listener;
    private VillageBuilder villageBuilder;
    private Environment environment;
    private DirectionalShadowLight shadowLight;
    private ModelBatch shadowBatch;
    private ModelBatch modelBatch;
    private PerspectiveCamera camera;
    private FrustumCulling frustum;

    private AnimationController animationController;
    private VillageController controller;
    private EntityPlayer player;

    private Stage stage;
    private HashMap<String, UIDialog> dialogHashMap;
    private Label interactLabel;

    private Array<NonPlayerCharacter> npcArray;
    private TriggersManager triggersManager;

    /**
     * Instantiates a new Village.
     *
     * @param screen the screen
     */
    public GenerateVillage(GameScreen screen, Assets assets) {
        DEBUG_MODE = false;
        this.screen = screen;
        this.assets = assets;
    }

    /**
     * Instantiates a new Village.
     *
     * @param screen     the screen
     * @param DEBUG_MODE the debug mode
     */
    public GenerateVillage(GameScreen screen, Assets assets, boolean DEBUG_MODE) {
        this.DEBUG_MODE = DEBUG_MODE;
        this.screen = screen;
        this.assets = assets;
    }

    /**
     * Create.
     */
    public void create() {
        if (DEBUG_MODE) {
            debugDrawer = new DebugDrawer();
            villageBuilder = new VillageBuilder(assets, debugDrawer);
            debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
        } else
            villageBuilder = new VillageBuilder(assets);

        camera = new PerspectiveCamera(80, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 2.5f, -4f);
        camera.lookAt(0f, 1f, 0f);
        camera.near = 0.5f;
        camera.far = 1000f;
        camera.update();

        DefaultShader.Config config = new DefaultShader.Config();
        config.numDirectionalLights = 1;
        config.numPointLights = 5;
        modelBatch = new ModelBatch(new DefaultShaderProvider(config));

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.2f, 0.3f, 0.5f, 1f));
        environment.add((shadowLight = new DirectionalShadowLight(4096, 4096, 25, 25, 0.1f, 10)).set(0.4f, 0.5f, 0.7f, -15.0f, -35f, 30f));
        environment.shadowMap = shadowLight;
        shadowBatch = new ModelBatch(new DepthShaderProvider());

        frustum = new FrustumCulling(villageBuilder.getObjectsInstance(),environment,camera,modelBatch,shadowBatch);

        triggersManager = new TriggersManager(villageBuilder.getWorld());
        triggersManager.add("goToLevel",new Trigger(.5f, 1, .5f,new EntityPosition(-4.5f, 0, 0)));
        triggersManager.add("trader",new Trigger(.5f, 1, .5f,new EntityPosition(-0, 0, 0)));
        triggersManager.add("smith",new Trigger(.5f, 1, .5f,new EntityPosition(-5.5f, 0, 0)));
        triggersManager.add("changeLayout1",new Trigger(0.5f, 0.25f, .5f,new EntityPosition(4.0f, 0.25f, 0)));
        triggersManager.add("changeLayout2",new Trigger(0.5f, 0.25f, .5f,new EntityPosition(4.0f, 0.25f, 3f)));
        triggersManager.add("changeLayout3",new Trigger(0.5f, 0.25f, .5f,new EntityPosition(8f, 0.25f, 3)));
        triggersManager.add("changeLayout4",new Trigger(0.5f, 0.25f, .5f,new EntityPosition(8f, 0.25f, 6)));

        villageBuilder.createGround(0, 0, 0);
        villageBuilder.createGround(4, 0, 3f);
        villageBuilder.createGround(8, 0, 6f);

        villageBuilder.createHouse(0, 1.5f, 2, 1, 2);
        villageBuilder.createHouse(-3, 1.5f, 1.5f, 3.5f, 2);
        villageBuilder.createHouse(4, 4.5f, 3, 2.5f, 2);
        villageBuilder.createHouse(11, 4.5f, 2.5f, 4, 2);
        villageBuilder.createHouse(8, 7.5f, 5, 6, 2);

        villageBuilder.createLightBox(environment, 4.75f,0.4f,0.25f,10);
        villageBuilder.createLightBox(environment, -4.25f, 0.4f, 0.25f,-30);
        villageBuilder.createLightBox(environment, 2.5f,0.4f,3.25f,10);
        villageBuilder.createLightBox(environment, 4f,0.4f,6.25f,10);

        npcArray = new Array<>();
        NonPlayerCharacter nonPlayerCharacter1 = new NonPlayerCharacter(assets, new EntityPosition(0,1f,1.30f), NonPlayerCharacter.AnimationID.SITTING);
        NonPlayerCharacter nonPlayerCharacter2 = new NonPlayerCharacter(assets, new EntityPosition(4.5f,1.75f,3.8f), NonPlayerCharacter.AnimationID.IDLE);
        npcArray.add(nonPlayerCharacter1, nonPlayerCharacter2);

        dialogHashMap = new HashMap<>();
        stage = new Stage();
        UIDialog traderDialog = new UIDialog("Trader", "ACHETE MA MERDE", assets);
        UIDialog exitDialog = new UIDialog("", "Do you want to exit the village ?", assets);

        traderDialog.getNoButton().addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                showDialog("");
                return true;
            }
        });

        exitDialog.getNoButton().addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                showDialog("");
                return true;
            }
        });

        exitDialog.getYesButton().addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                exitVillage();
                return true;
            }
        });

        stage.addActor(traderDialog.getDialog());
        stage.addActor(exitDialog.getDialog());

        dialogHashMap.put("Trader", traderDialog);
        dialogHashMap.put("Exit", exitDialog);

        for(Actor actor : stage.getActors()){
            actor.setVisible(false);
        }

        interactLabel = new Label("Press F to interact", assets.manager.get(Assets.menuSkin));
        interactLabel.setVisible(false);
        interactLabel.getStyle().fontColor.set(Color.WHITE);
        interactLabel.setFontScale(1.1f);

        interactLabel.setPosition(Gdx.graphics.getWidth()/2 - interactLabel.getWidth()/2, interactLabel.getHeight()*10);
        stage.addActor(interactLabel);

        stage.act();

        player = PlayerFactory.create(new EntityPosition(0, 1f, 0), assets);

        animationController = new AnimationController(player.getEntity());
        animationController.animate("idle", -1, 1.0f, null, 0.2f);
        villageBuilder.getWorld().addCollisionObject(player.getEntity().getGhostObject(),
                (short) btBroadphaseProxy.CollisionFilterGroups.CharacterFilter,
                (short) btBroadphaseProxy.CollisionFilterGroups.AllFilter);
        villageBuilder.getWorld().addAction(player.getEntity().getController());
        player.getEntity().getBody().setContactCallbackFlag(PLAYER_FLAG);
        player.getEntity().getBody().setContactCallbackFilter(TRIGGER_FLAG);
        player.getEntity().getBody().setActivationState(Collision.DISABLE_DEACTIVATION);

        controller = new VillageController(this, player, animationController);

        listener = new VillageContactListener(this, controller,triggersManager);

        Gdx.input.setInputProcessor(controller);

    }

    private void camFollowPlayer() {
        camera.position.set(player.getEntity().transform.getValues()[12], camera.position.y, player.getEntity().transform.getValues()[14] - 3f);
    }

    /**
     * Render.
     */
    public void render() {
        Gdx.gl.glClearColor(0.2f, 0.3f, 0.4f, 1);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        for(NonPlayerCharacter nonPlayerCharacter : npcArray)
            nonPlayerCharacter.render();
        animationController.update(Gdx.graphics.getDeltaTime());

        villageBuilder.getWorld().stepSimulation(Gdx.graphics.getDeltaTime(), 5, 1f / 60f);

        camFollowPlayer();
        camera.update();

        shadowLight.begin(Vector3.Zero,camera.direction);
        shadowBatch.begin(shadowLight.getCamera());
        frustum.renderShadow();
        shadowBatch.render(player.getEntity());
        shadowBatch.render(villageBuilder.getBackground());
        shadowBatch.render(villageBuilder.getBoxLightInstance());
        shadowBatch.render(npcArray);
        shadowBatch.end();
        shadowLight.end();

        modelBatch.begin(camera);
        frustum.render();
        modelBatch.render(player.getEntity(), environment);
        modelBatch.render(villageBuilder.getSky());
        modelBatch.render(villageBuilder.getBackground(), environment);
        modelBatch.render(villageBuilder.getBoxLightInstance(), environment);
        modelBatch.render(npcArray,environment);
        modelBatch.end();

        stage.act();
        stage.draw();

        villageBuilder.getSky().transform.rotate(Vector3.X,0.02f);

        if (DEBUG_MODE) {
            debugDrawer.begin(camera);
            villageBuilder.getWorld().debugDrawWorld();
            debugDrawer.end();
        }

        player.getEntity().getGhostObject().getWorldTransform(player.getEntity().transform);
    }

    public void resize(int width, int height){
        camera.viewportWidth = width;
        camera.viewportHeight = height;
    }

    /**
     * Dispose.
     */
    public void dispose() {
        modelBatch.dispose();
        villageBuilder.getWorld().removeAction(player.getEntity().getController());
        villageBuilder.getWorld().removeCollisionObject(player.getEntity().getGhostObject());
        triggersManager.dispose();
        villageBuilder.dispose();
        player.dispose();
        if (debugDrawer != null) debugDrawer.dispose();
        Controllers.removeListener(controller);
        stage.dispose();
    }

    /**
     * Set controller.
     */
    public void setController() {
        Controllers.clearListeners();
        Controllers.addListener(controller);
        Gdx.input.setInputProcessor(controller);
    }

    public void exitVillage() {
        screen.goToLevel();
    }

    public void showDialog(String dialogName){
        for(Actor actor : stage.getActors()){
            actor.setVisible(false);
        }

        if(dialogName.isEmpty()) {
            Gdx.input.setInputProcessor(controller);
            return;
        }

        UIDialog dialog = dialogHashMap.get(dialogName);
        dialog.getDialog().setVisible(true);
        Gdx.input.setInputProcessor(stage);
    }

    public void showInteract(boolean visible){
        interactLabel.setVisible(visible);
    }

}
