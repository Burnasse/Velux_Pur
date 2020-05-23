package com.mygdx.game.gameGeneration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseProxy;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Assets;
import com.mygdx.game.Entity.EntityMonster;
import com.mygdx.game.Entity.EntityPlayer;
import com.mygdx.game.Entity.PlayerFactory;
import com.mygdx.game.Entity.instances.Entity;
import com.mygdx.game.Entity.instances.EntityInstance;
import com.mygdx.game.FloorGeneration.FloorData;
import com.mygdx.game.FloorGeneration.FloorFactory;
import com.mygdx.game.FrustumCulling;
import com.mygdx.game.IA.Gunner;
import com.mygdx.game.controller.PlayerController;
import com.mygdx.game.physics.DynamicWorld;
import com.mygdx.game.ui.HealthBar;
import com.mygdx.game.ui.Minimap;

/**
 * The type Generate level.
 */
public class GenerateLevel {

    /**
     * The constant GROUND_FLAG.
     */
    final static short GROUND_FLAG = 1 << 8;
    /**
     * The constant OBJECT_FLAG.
     */
    final static short OBJECT_FLAG = 1 << 9;

    /**
     * The type My contact listener is called when there is a collision.
     */
    public class MyContactListener extends ContactListener {
        @Override
        public boolean onContactAdded(int userValue0, int partId0, int index0, boolean match0, int userValue1, int partId1,
                                      int index1, boolean match1) {

            return true;
        }
    }

    private final boolean DEBUG_MODE;
    private DebugDrawer debugDrawer;

    private Assets assets;

    private ModelBatch modelBatch;
    private Environment environment;
    private PerspectiveCamera cam;
    private FrustumCulling frustum;
    private FrustumCulling tempFrustum;

    private CameraInputController camController;
    private DynamicWorld world;
    private FloorData floorData;
    private EntityPlayer player;
    private PlayerController playerController;
    private MyContactListener contactListener;
    private Minimap minimap;
    private HealthBar healthBar;

    private PointLight followLight;

    Array<EntityInstance> temp = new Array<>();

    public GenerateLevel(Assets assets, boolean DEBUG_MODE) {
        this.DEBUG_MODE = DEBUG_MODE;
        this.assets = assets;
    }

    /**
     * Create.
     */
    public void create() {
        if (DEBUG_MODE) {
            debugDrawer = new DebugDrawer();
            world = new DynamicWorld(debugDrawer);
            debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
        } else
            world = new DynamicWorld();

        contactListener = new MyContactListener();

        DefaultShader.Config config = new DefaultShader.Config();
        config.numDirectionalLights = 2;
        config.numPointLights = 1;
        modelBatch = new ModelBatch(new DefaultShaderProvider(config));

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, new Color(0.1f, 0.1f, 0.1f, 0.1f)));
        environment.add(new DirectionalLight().set(new Color(0.1f, 0.1f, 0.1f, 0.1f), 0, -10f, 0));

        followLight = new PointLight().set(new Color(1, 0.6f, 0.4f, 1f), new Vector3(0, 0, 0), 5f);
        environment.add(followLight);

        floorData = FloorFactory.create("Labyrinth", 20, 4, 3, 7, assets);

        minimap = floorData.minimap;
        healthBar = new HealthBar();

        player = PlayerFactory.create(floorData.playerSpawnPosition, assets);
        System.out.println(floorData.playerSpawnPosition);

        world.getDynamicsWorld().addCollisionObject(player.getEntity().getGhostObject(), (short) btBroadphaseProxy.CollisionFilterGroups.CharacterFilter, (short) btBroadphaseProxy.CollisionFilterGroups.AllFilter);
        world.getDynamicsWorld().addAction(player.getEntity().getController());
        player.getEntity().getBody().setContactCallbackFlag(GROUND_FLAG);
        player.getEntity().getBody().setContactCallbackFilter(0);
        player.getEntity().getBody().setActivationState(Collision.DISABLE_DEACTIVATION);

        for (EntityInstance obj : floorData.objectsInstances) {
            obj.getBody().setUserValue(floorData.objectsInstances.indexOf(obj));
            obj.getBody().setCollisionFlags(obj.getBody().getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
            obj.getBody().setContactCallbackFlag(OBJECT_FLAG);
            obj.getBody().setContactCallbackFilter(GROUND_FLAG);
            world.addRigidBody((btRigidBody) obj.getBody());
        }

        for (EntityMonster monster : floorData.entityMonsters) {
            monster.getEntity().getBody().setCollisionFlags(monster.getEntity().getBody().getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
            monster.getEntity().getBody().setContactCallbackFilter(GROUND_FLAG);
            world.addRigidBody((btRigidBody) monster.getEntity().getBody());
            world.getDynamicsWorld().addCollisionObject(monster.getEntity().getBody(), (short) btBroadphaseProxy.CollisionFilterGroups.CharacterFilter, (short) btBroadphaseProxy.CollisionFilterGroups.AllFilter);
            monster.getEntity().getBody().setContactCallbackFilter(0);
            monster.getEntity().getBody().setActivationState(Collision.DISABLE_DEACTIVATION);
            monster.getBehavior().Surroundings(player, world);
        }

        cam = new PerspectiveCamera(80, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(player.getEntity().transform.getValues()[12], 15, player.getEntity().transform.getValues()[14] - 1);
        cam.lookAt(player.getEntity().transform.getValues()[12], player.getEntity().transform.getValues()[13], player.getEntity().transform.getValues()[14] + 1);
        cam.near = 3f;
        cam.far = 300f;
        cam.update();

        Array<EntityInstance> instances = new Array<>();
        for (EntityInstance instance : floorData.objectsInstances)
            instances.add(instance);

        frustum = new FrustumCulling(instances, environment, cam, modelBatch);
        tempFrustum = new FrustumCulling(temp, environment, cam, modelBatch);

        camController = new CameraInputController(cam);

        playerController = new PlayerController(player,cam);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(camController);
        inputMultiplexer.addProcessor(playerController);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }


    private void camFollowPlayer() {
        cam.position.set(player.getEntity().transform.getValues()[12], cam.position.y, player.getEntity().transform.getValues()[14] - 1);
    }


    /**
     * Render.
     */
    public void render() {

        temp.clear();
        for (EntityMonster foe : floorData.entityMonsters) {
            foe.getBehavior().update(Gdx.graphics.getDeltaTime());
            if (foe.getBehavior() instanceof Gunner)
                ((Gunner) foe.getBehavior()).projectiles().removeAll(((Gunner) foe.getBehavior()).getDoneProjectiles(), true);
            temp.addAll(((Gunner) foe.getBehavior()).projectiles());
        }


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        world.getDynamicsWorld().stepSimulation(Gdx.graphics.getDeltaTime(), 5, 1f / 60f);

        camFollowPlayer();
        cam.update();

        followLight.position.x = player.getEntity().transform.getValues()[12];
        followLight.position.y = player.getEntity().transform.getValues()[13] + 1;
        followLight.position.z = player.getEntity().transform.getValues()[14];

        modelBatch.begin(cam);
        frustum.render();
        tempFrustum.render();

        modelBatch.render(player.getEntity(), environment);
        modelBatch.end();

        minimap.render(player.getEntity().transform.getValues()[12], player.getEntity().transform.getValues()[14]);
        healthBar.render(100, player.getCharacteristics().getHealth());

        if (DEBUG_MODE) {
            debugDrawer.begin(cam);
            world.getDynamicsWorld().debugDrawWorld();
            debugDrawer.end();
        }

        player.getEntity().getGhostObject().getWorldTransform(player.getEntity().transform);
    }

    public void resize(int width, int height) {
        cam.viewportWidth = width;
        cam.viewportHeight = height;
    }

    /**
     * Dispose.
     */
    public void dispose() {
        for (Entity obj : floorData.objectsInstances)
            obj.dispose();

        floorData.objectsInstances.clear();

        world.dispose();
        contactListener.dispose();

        modelBatch.dispose();
        minimap.dispose();
        healthBar.dispose();
    }
}
