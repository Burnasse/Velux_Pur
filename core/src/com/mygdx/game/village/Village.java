package com.mygdx.game.village;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.mygdx.game.Entity.EntityPlayer;
import com.mygdx.game.Entity.utils.EntityPosition;
import com.mygdx.game.controller.VillageController;
import com.mygdx.game.screen.GameScreen;

import static com.mygdx.game.village.CallbackFlags.PLAYER_FLAG;
import static com.mygdx.game.village.CallbackFlags.TRIGGER_FLAG;

public class Village {

    /**
     * The type My contact listener is called when there is a collision.
     */
    class MyContactListener extends ContactListener {
        @Override
        public boolean onContactAdded (int userValue0, int partId0, int index0, boolean match0, int userValue1, int partId1,
                                       int index1, boolean match1) {

            if (match0) {
                System.out.println(userValue0 + " --- " + userValue1);
                if(controller.waitTrigger && userValue1 != controller.userValue)
                    controller.notifyTrigger();
                if(userValue1 == 1)
                    screen.goToLevel();
                if(userValue1 == 2)
                    System.out.println("TRADER");
                if(userValue1 == 3)
                    System.out.println("SMITH");
                if(userValue1 == 4 && !controller.waitTrigger)
                    controller.setCanChangeLayout(userValue1,true);
                if(userValue1 == 5 && !controller.waitTrigger)
                    controller.setCanChangeLayout(userValue1, false);
            }
            if(match0) {

            }

            return true;
        }

        @Override
        public void onContactEnded(int userValue0, boolean match0, int userValue1, boolean match1) {
            if (match1) {
                if(userValue0 == 3)
                    controller.canChangeLayout = false;
                if(userValue0 == 4)
                    controller.canChangeLayout = false;
            }
        }
    }

    private WorldManager worldManager;
    private Environment environment;
    private MyContactListener listener;
    private ModelBatch modelBatch;
    private PerspectiveCamera camera;
    private AnimationController animationController;
    private ModelInstance sky;
    private ModelInstance ground;
    private VillageController controller;
    private EntityPlayer player;
    private GameScreen screen;
    private boolean isDispose = false;
    private DebugDrawer debugDrawer;

    public Village(GameScreen screen){
        this.screen = screen;
    }

    public void create(){
        debugDrawer = new DebugDrawer();
        worldManager = new WorldManager(debugDrawer);
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
        listener = new MyContactListener();
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.65f,0.6f,0.6f,1f));
        environment.add((new DirectionalShadowLight(1024, 1024, 60f, 60f, .1f, 50f)).set(1f, 1f, 1f, 40.0f, -35f, -35f));
        environment.add(new DirectionalLight().set(0.4f, 0.4f, 0.4f, -1f, -0.8f, -0.2f));
        ModelBuilder modelBuilder = new ModelBuilder();
        Model groundModel = modelBuilder.createBox(10f,.5f,1f,
                new Material(ColorAttribute.createDiffuse(Color.GRAY)),
                VertexAttributes.Usage.Position
                | VertexAttributes.Usage.Normal);

        /* Trigger: goToLevel() | index: 1 */
        worldManager.createTrigger(new EntityPosition(-4.5f,0,0),.5f,1,.5f);

        /* Trigger: trader() | index: 2 */
        worldManager.createTrigger(new EntityPosition(-0,0,0),.5f,1,.5f);

        /* Trigger: smith() | index: 3 */
        worldManager.createTrigger(new EntityPosition(-5.5f,0,0),.5f,1,.5f);

        /* Trigger: changeLayout1 | index: 4 */
        worldManager.createTrigger(new EntityPosition(5.0f,0,0),2.0f,1.0f,.5f);

        /* Trigger: changeLayout2 | index: 5 */
        worldManager.createTrigger(new EntityPosition(5.0f,0,4),2,1,.5f);

        worldManager.createGround(groundModel, new EntityPosition(0,0,0));
        worldManager.createGround(groundModel, new EntityPosition(4,0,3f));
        worldManager.createGround(groundModel, new EntityPosition(8,0,6f));

        worldManager.createHouse(0,1.5f,2,3,2);
        worldManager.createHouse(-3,1.5f,1.5f,3.5f,2);
        worldManager.createHouse(4,4.5f,3,2.5f,2);
        worldManager.createHouse(11,4.5f,2.5f,4,2);
        worldManager.createHouse(8,7.5f,5,6,2);

        AssetManager assetManager = new AssetManager();
        assetManager.load("skyBox.g3db", Model.class);
        assetManager.finishLoading();
        Model skyBox = assetManager.get("skyBox.g3db",Model.class);
        sky = new ModelInstance(skyBox);

        assetManager.load("groundG3D.g3db", Model.class);
        assetManager.finishLoading();
        Model groundGround = assetManager.get("groundG3D.g3db", Model.class);
        ground = new ModelInstance(groundGround);
        ground.transform.trn(5,-0.5f,5);

        camera = new PerspectiveCamera(80, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.position.set(0f,3f,-4f);
        camera.lookAt(0f,1.5f,0f);
        camera.near = 0.5f;
        camera.far = 1500f;
        camera.update();

        player = PlayerFactory.create(new EntityPosition(3,0.25f,0));

        animationController = new AnimationController(player.getEntity());
        animationController.animate("idle", -1, 1.0f, null, 0.2f);
        worldManager.getWorld().addCollisionObject(player.getEntity().getGhostObject(),
                (short) btBroadphaseProxy.CollisionFilterGroups.CharacterFilter,
                (short) btBroadphaseProxy.CollisionFilterGroups.AllFilter);
        worldManager.getWorld().addAction(player.getEntity().getController());
        player.getEntity().getBody().setContactCallbackFlag(PLAYER_FLAG);
        player.getEntity().getBody().setContactCallbackFilter(TRIGGER_FLAG);
        player.getEntity().getBody().setActivationState(Collision.DISABLE_DEACTIVATION);

        controller = new VillageController(player, animationController);
        CameraInputController cameraInputController = new CameraInputController(camera);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(cameraInputController);
        multiplexer.addProcessor(controller);
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void camFollowPlayer(){
        camera.position.set(player.getEntity().transform.getValues()[12], camera.position.y, player.getEntity().transform.getValues()[14]-4f);
    }

    public void render(){
        final float delta = Math.min(1f/30f, Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0.2f, 0.6f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        animationController.update(Gdx.graphics.getDeltaTime());

        if(!isDispose)
            worldManager.getWorld().stepSimulation(delta, 5, 1f/60f);

        //camFollowPlayer();
        camera.update();

        modelBatch.begin(camera);
        modelBatch.render(worldManager.getObjectsInstance(), environment);
        modelBatch.render(player.getEntity(), environment);
        modelBatch.render(sky);
        modelBatch.render(ground,environment);
        modelBatch.end();

        debugDrawer.begin(camera);
        worldManager.getWorld().debugDrawWorld();
        debugDrawer.end();

        if(!isDispose) player.getEntity().getGhostObject().getWorldTransform(player.getEntity().transform);

    }

    public void dispose(){
        isDispose = true;
        modelBatch.dispose();
        worldManager.dispose();
        player.dispose();
    }

    public VillageController getController(){
        return controller;
    }

}
