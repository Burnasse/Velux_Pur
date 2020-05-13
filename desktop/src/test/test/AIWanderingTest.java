package test.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseProxy;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Entity.EntityMonster;
import com.mygdx.game.Entity.EntityPlayer;
import com.mygdx.game.Entity.instances.Entity;
import com.mygdx.game.Entity.instances.EntityInstance;
import com.mygdx.game.FloorGeneration.DynamicWorld;
import com.mygdx.game.FloorGeneration.FloorData;
import com.mygdx.game.FloorGeneration.FloorFactory;
import com.mygdx.game.IA.Gunner;
import com.mygdx.game.controller.PlayerController;

/**
 * The type Generate level.
 */
public class AIWanderingTest extends ApplicationAdapter {

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
    class MyContactListener extends ContactListener {
        @Override
        public boolean onContactAdded(int userValue0, int partId0, int index0, boolean match0, int userValue1, int partId1,
                                      int index1, boolean match1) {
            // Colore en blanc quand il y a une collision entre le joueur et un autre objets
            if (match0)
                ((ColorAttribute) floorData.objectsInstances.get(userValue0).materials.get(0).get(ColorAttribute.Diffuse)).color.set(Color.BLUE);
            if (match1)
                ((ColorAttribute) floorData.objectsInstances.get(userValue1).materials.get(0).get(ColorAttribute.Diffuse)).color.set(Color.WHITE);
            return true;
        }
    }

    private ModelBatch modelBatch;
    private Model model;
    private Environment environment;
    private PerspectiveCamera cam;
    private CameraInputController camController;
    private DynamicWorld world;
    private FloorData floorData;
    private EntityPlayer player;
    private PlayerController playerController;
    private MyContactListener contactListener;

    private boolean playerPov = true;
    private int clock;

    /**
     * Create.
     */
    public void create() {
        Bullet.init();

        world = new DynamicWorld();
        contactListener = new MyContactListener();

        modelBatch = new ModelBatch();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        modelBuilder.node().id = "box";
        MeshPartBuilder builder = modelBuilder.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position
                | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GRAY)));
        BoxShapeBuilder.build(builder, 1f, 1f, 1f);
        model = modelBuilder.end();

        floorData = FloorFactory.create("GenericFloor", 20, 2, 4, 10, model);

        ModelBuilder modelBuilder1 = new ModelBuilder();
        Model model1 = modelBuilder1.createCapsule(0.1f, 0.5f, 16, new Material(ColorAttribute.createDiffuse(Color.BLUE)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        player = new EntityPlayer("Player", model1, floorData.playerSpawnPosition);
        world.getDynamicsWorld().addCollisionObject(player.getEntity().getGhostObject(), (short) btBroadphaseProxy.CollisionFilterGroups.CharacterFilter, (short) btBroadphaseProxy.CollisionFilterGroups.AllFilter);
        world.getDynamicsWorld().addAction(player.getEntity().getController());
        player.getEntity().getBody().setContactCallbackFlag(GROUND_FLAG);
        player.getEntity().getBody().setContactCallbackFilter(0);
        player.getEntity().getBody().setActivationState(Collision.DISABLE_DEACTIVATION);

        for (EntityInstance obj : floorData.objectsInstances) {
            obj.getBody().setUserValue(floorData.objectsInstances.indexOf(obj, false));
            obj.getBody().setCollisionFlags(obj.getBody().getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
            obj.getBody().setContactCallbackFlag(OBJECT_FLAG);
            obj.getBody().setContactCallbackFilter(GROUND_FLAG);
            world.addRigidBody((btRigidBody) obj.getBody());
        }

        for (EntityMonster monster : floorData.entityMonsters) {
            monster.getEntity().getBody().setCollisionFlags(monster.getEntity().getBody().getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
            //monster.getEntity().getBody().setContactCallbackFlag(OBJECT_FLAG);
            monster.getEntity().getBody().setContactCallbackFilter(GROUND_FLAG);
            world.addRigidBody((btRigidBody) monster.getEntity().getBody());
            ;
            world.getDynamicsWorld().addCollisionObject(monster.getEntity().getBody(), (short) btBroadphaseProxy.CollisionFilterGroups.CharacterFilter, (short) btBroadphaseProxy.CollisionFilterGroups.AllFilter);
            monster.getEntity().getBody().setContactCallbackFilter(0);
            monster.getEntity().getBody().setActivationState(Collision.DISABLE_DEACTIVATION);

        }


        cam = new PerspectiveCamera(80, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(14f, 6f, /**/55f);
        cam.lookAt(10f, 0, 30f);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        camController = new CameraInputController(cam);

        playerController = new PlayerController(player);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(camController);
        inputMultiplexer.addProcessor(playerController);
        Gdx.input.setInputProcessor(inputMultiplexer);

        for (EntityMonster foe : floorData.entityMonsters)
            foe.behavior.setPlayer(player);
    }


    private void camFollowPlayer() {
        cam.position.set(new Vector3(player.getEntity().transform.getValues()[12] + 0.4531824f, player.getEntity().transform.getValues()[13] + 5.767706f, player.getEntity().transform.getValues()[14] + -5.032133f));
        float[] fov = {-0.9991338f, 3.6507862E-7f, -0.04161331f, 0.14425309f, -0.02119839f, 0.8605174f, 0.50898004f, -2.485553f, 0.035809156f, 0.5094212f, -0.85977185f, -7.252268f, 0.14425309f, -2.485553f, -7.252268f, 1.0f};
        cam.view.set(fov);
        cam.direction.set(-0.047802035f, -0.36853015f, 0.9283842f);
    }


    /**
     * Render.
     */

    public void render() {
        final float delta = Math.min(1f / 30f, Gdx.graphics.getDeltaTime());

        world.getDynamicsWorld().stepSimulation(delta, 5, 1f / 60f);

        clock += 1; // add the time since the last frame

        if (clock > 10) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                playerPov = !playerPov;
            }
            clock = 0; // reset your variable to 0
        }

        if (playerPov) {
            camFollowPlayer();
            cam.update();
        } else {
            camController.update();
        }

        for (EntityMonster foe : floorData.entityMonsters) {
            foe.behavior.update(delta);
            if (foe.behavior instanceof Gunner)
                floorData.objectsInstances.addAll(((Gunner) foe.behavior).projectiles());
        }


        Gdx.gl.glClearColor(0.2f, 0.6f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        modelBatch.render(floorData.objectsInstances, environment);
        modelBatch.render(player.getEntity(), environment);
        modelBatch.end();

        player.getEntity().getGhostObject().getWorldTransform(player.getEntity().transform);
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
        model.dispose();
    }

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new AIWanderingTest(), config);
    }
}
