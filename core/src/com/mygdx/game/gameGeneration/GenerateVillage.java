package com.mygdx.game.gameGeneration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.mygdx.game.Entity.EntityPlayer;
import com.mygdx.game.Entity.PlayerFactory;
import com.mygdx.game.Entity.utils.EntityPosition;
import com.mygdx.game.controller.VillageController;
import com.mygdx.game.screen.GameScreen;

import static com.mygdx.game.physics.CallbackFlags.PLAYER_FLAG;
import static com.mygdx.game.physics.CallbackFlags.TRIGGER_FLAG;

/**
 * The type Village.
 */
public class GenerateVillage {

    /**
     * The type My contact listener is called when there is a collision.
     */
    class MyContactListener extends ContactListener {
        @Override
        public boolean onContactAdded(int userValue0, int partId0, int index0, boolean match0, int userValue1, int partId1,
                                      int index1, boolean match1) {

            if (match1) {
                if (controller.waitTrigger && userValue1 != controller.userValue)
                    controller.notifyTrigger();
                if (userValue0 == 1)
                    exitVillage();
                if (userValue0 == 2)
                    System.out.println("TRADER");
                if (userValue0 == 3)
                    System.out.println("SMITH");
                if (userValue0 == 4 && !controller.waitTrigger)
                    controller.setCanChangeLayout(userValue0, true);
                if (userValue0 == 5 && !controller.waitTrigger)
                    controller.setCanChangeLayout(userValue0, false);
                if (userValue0 == 6 && !controller.waitTrigger)
                    controller.setCanChangeLayout(userValue0, true);
                if (userValue0 == 7 && !controller.waitTrigger)
                    controller.setCanChangeLayout(userValue0, false);
            }
            return true;
        }

        @Override
        public void onContactEnded(int userValue0, boolean match0, int userValue1, boolean match1) {
            if (match0) {
                if (userValue1 == 4)
                    controller.canChangeLayout = false;
                if (userValue1 == 5)
                    controller.canChangeLayout = false;
                if (userValue1 == 6)
                    controller.canChangeLayout = false;
                if (userValue1 == 7)
                    controller.canChangeLayout = false;
            }
        }
    }

    private final boolean DEBUG_MODE;

    private VillageBuilder villageBuilder;
    private Environment environment;
    private MyContactListener listener;
    private ModelBatch modelBatch;
    private PerspectiveCamera camera;
    private AnimationController animationController;
    private VillageController controller;
    private EntityPlayer player;
    private GameScreen screen;
    private boolean isDispose = false;
    private DebugDrawer debugDrawer;

    /**
     * Instantiates a new Village.
     *
     * @param screen the screen
     */
    public GenerateVillage(GameScreen screen) {
        DEBUG_MODE = false;
        this.screen = screen;
    }

    /**
     * Instantiates a new Village.
     *
     * @param screen     the screen
     * @param DEBUG_MODE the debug mode
     */
    public GenerateVillage(GameScreen screen, boolean DEBUG_MODE) {
        this.DEBUG_MODE = DEBUG_MODE;
        this.screen = screen;
    }

    /**
     * Create.
     */
    public void create() {
        if (DEBUG_MODE) {
            debugDrawer = new DebugDrawer();
            villageBuilder = new VillageBuilder(debugDrawer);
            debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
        } else
            villageBuilder = new VillageBuilder();

        listener = new MyContactListener();
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.65f, 0.6f, 0.6f, 1f));
        environment.add((new DirectionalShadowLight(1024, 1024, 60f, 60f, .1f, 50f)).set(1f, 1f, 1f, 40.0f, -35f, -35f));
        environment.add(new DirectionalLight().set(0.4f, 0.4f, 0.4f, -1f, -0.8f, -0.2f));
        ModelBuilder modelBuilder = new ModelBuilder();
        Model groundModel = modelBuilder.createBox(10f, .5f, 1f,
                new Material(ColorAttribute.createDiffuse(Color.GRAY)),
                VertexAttributes.Usage.Position
                        | VertexAttributes.Usage.Normal);
        /* Trigger: goToLevel() | index: 1 */
        villageBuilder.createTrigger(new EntityPosition(-4.5f, 0, 0), .5f, 1, .5f);

        /* Trigger: trader() | index: 2 */
        villageBuilder.createTrigger(new EntityPosition(-0, 0, 0), .5f, 1, .5f);

        /* Trigger: smith() | index: 3 */
        villageBuilder.createTrigger(new EntityPosition(-5.5f, 0, 0), .5f, 1, .5f);

        /* Trigger: changeLayout1 | index: 4 */
        villageBuilder.createTrigger(new EntityPosition(4.0f, 0.25f, 0), 0.5f, 0.25f, .5f);

        /* Trigger: changeLayout2 | index: 5 */
        villageBuilder.createTrigger(new EntityPosition(4.0f, 0.25f, 3f), 0.5f, 0.25f, .5f);

        /* Trigger: changeLayout3 | index: 6 */
        villageBuilder.createTrigger(new EntityPosition(8f, 0.25f, 3), 0.5f, 0.25f, .5f);

        /* Trigger: changeLayout4 | index: 7 */
        villageBuilder.createTrigger(new EntityPosition(8f, 0.25f, 6), 0.5f, 0.25f, .5f);

        villageBuilder.createGround(groundModel, 0, 0, 0);
        villageBuilder.createGround(groundModel, 4, 0, 3f);
        villageBuilder.createGround(groundModel, 8, 0, 6f);

        villageBuilder.createHouse(0, 1.5f, 2, 3, 2);
        villageBuilder.createHouse(-3, 1.5f, 1.5f, 3.5f, 2);
        villageBuilder.createHouse(4, 4.5f, 3, 2.5f, 2);
        villageBuilder.createHouse(11, 4.5f, 2.5f, 4, 2);
        villageBuilder.createHouse(8, 7.5f, 5, 6, 2);

        camera = new PerspectiveCamera(80, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 3f, -4f);
        camera.lookAt(0f, 1.5f, 0f);
        camera.near = 0.5f;
        camera.far = 1500f;
        camera.update();
        player = PlayerFactory.create(new EntityPosition(0, 1f, 0));

        animationController = new AnimationController(player.getEntity());
        animationController.animate("idle", -1, 1.0f, null, 0.2f);
        villageBuilder.getWorld().addCollisionObject(player.getEntity().getGhostObject(),
                (short) btBroadphaseProxy.CollisionFilterGroups.CharacterFilter,
                (short) btBroadphaseProxy.CollisionFilterGroups.AllFilter);
        villageBuilder.getWorld().addAction(player.getEntity().getController());
        player.getEntity().getBody().setContactCallbackFlag(PLAYER_FLAG);
        player.getEntity().getBody().setContactCallbackFilter(TRIGGER_FLAG);
        player.getEntity().getBody().setActivationState(Collision.DISABLE_DEACTIVATION);

        controller = new VillageController(player, animationController);

        Gdx.input.setInputProcessor(controller);

    }

    private void camFollowPlayer() {
        camera.position.set(player.getEntity().transform.getValues()[12], camera.position.y, player.getEntity().transform.getValues()[14] - 4f);
    }

    /**
     * Render.
     */
    public void render() {
        Gdx.gl.glClearColor(0.2f, 0.6f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        animationController.update(Gdx.graphics.getDeltaTime());

        if (!isDispose)
            villageBuilder.getWorld().stepSimulation(Gdx.graphics.getDeltaTime(), 5, 1f / 60f);

        camFollowPlayer();
        camera.update();

        modelBatch.begin(camera);
        modelBatch.render(villageBuilder.getObjectsInstance(), environment);
        modelBatch.render(player.getEntity(), environment);
        modelBatch.render(villageBuilder.getSky());
        modelBatch.render(villageBuilder.getBackground(), environment);
        modelBatch.end();

        if (DEBUG_MODE) {
            debugDrawer.begin(camera);
            villageBuilder.getWorld().debugDrawWorld();
            debugDrawer.end();
        }

        if (!isDispose) player.getEntity().getGhostObject().getWorldTransform(player.getEntity().transform);

    }

    /**
     * Dispose.
     */
    public void dispose() {
        isDispose = true;
        modelBatch.dispose();
        villageBuilder.getWorld().removeAction(player.getEntity().getController());
        villageBuilder.getWorld().removeCollisionObject(player.getEntity().getGhostObject());
        villageBuilder.dispose();
        player.dispose();
        if (debugDrawer != null) debugDrawer.dispose();
        Controllers.removeListener(controller);
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

}
