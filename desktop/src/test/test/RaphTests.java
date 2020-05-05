

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
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.mygdx.game.Entity.*;
import com.mygdx.game.Entity.instances.Entity;
import com.mygdx.game.Entity.instances.EntityInstance;
import com.mygdx.game.Entity.utils.EntityPosition;
import com.mygdx.game.FloorGeneration.DynamicWorld;
import com.mygdx.game.FloorGeneration.FloorData;
import com.mygdx.game.FloorGeneration.FloorFactory;
import com.mygdx.game.controller.PovController;
import com.mygdx.game.controller.PlayerController;
import com.mygdx.game.item.CreatedItems;
import com.mygdx.game.item.Weapon;
import sun.awt.AWTAccessor;

import java.util.ArrayList;

/**
 * The type Generate level.
 */
public class RaphTests extends ApplicationAdapter {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new RaphTests(), config);
    }

    /**
     * The constant GROUND_FLAG.
     */
    final static short GROUND_FLAG = 1 << 8;
    /**
     * The constant OBJECT_FLAG.
     */
    final static short OBJECT_FLAG = 1 << 9;

    final static short WEAPON_FLAG = 1 << 7;


    /**
     * The type My contact listener is called when there is a collision.
     */
    public class MyContactListener extends ContactListener {

        @Override
        public boolean onContactAdded (int userValue0, int partId0, int index0, boolean match0, int userValue1, int partId1,
                                       int index1, boolean match1) {
            // Colore en blanc quand il y a une collision entre le joueur et un autre objets
            if (match0)
                ((ColorAttribute)floorData.objectsInstances.get(userValue0).materials.get(0).get(ColorAttribute.Diffuse)).color.set(Color.WHITE);
            if (match1)
                ((ColorAttribute)floorData.objectsInstances.get(userValue1).materials.get(0).get(ColorAttribute.Diffuse)).color.set(Color.WHITE);
            return true;
        }
    }

    private ModelBatch modelBatch;
    private Model model;
    private Environment environment;
    private PerspectiveCamera cam;
    private DynamicWorld world;
    private FloorData floorData;
    private EntityPlayer player;
    private PlayerController playerController;
    private RaphTests.MyContactListener contactListener;
    private ArrayList<EntityInstance> instances;


    private boolean playerPov =true;
    private int clock;

    /**
     * Create.
     */
    public void create() {
        Bullet.init();

        world = new DynamicWorld();
        contactListener = new RaphTests.MyContactListener();

        instances = new ArrayList<>();
        modelBatch = new ModelBatch();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        modelBuilder.node().id = "box";
        MeshPartBuilder builder = modelBuilder.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position
                | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GRAY)));
        BoxShapeBuilder.build(builder,1f,1f,1f);
        model = modelBuilder.end();

        floorData = FloorFactory.create("Labyrinth", 100, 15 , 3 ,15, model);

        ModelBuilder modelBuilder1 = new ModelBuilder();
        Model model1 = modelBuilder1.createCapsule(0.1f,0.5f,16, new Material(ColorAttribute.createDiffuse(Color.BLUE)),VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        player = new EntityPlayer("Player", model1, floorData.playerSpawnPosition);
        player.equipWeapon(CreatedItems.getSword());
        world.getDynamicsWorld().addCollisionObject(player.getEntity().getGhostObject(),(short)btBroadphaseProxy.CollisionFilterGroups.CharacterFilter,(short) btBroadphaseProxy.CollisionFilterGroups.AllFilter);
        world.getDynamicsWorld().addAction(player.getEntity().getController());
        player.getEntity().getBody().setContactCallbackFlag(GROUND_FLAG);
        player.getEntity().getBody().setContactCallbackFilter(0);
        player.getEntity().getBody().setActivationState(Collision.DISABLE_DEACTIVATION);

        /**
         * ajoute arme dans le jeu
         */
        player.getWeapon().getEntity().getBody().setUserValue(1);
        player.getWeapon().getEntity().getBody().setCollisionFlags(player.getWeapon().getEntity().getBody().getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);;
        player.getWeapon().getEntity().getBody().setContactCallbackFlag(WEAPON_FLAG);
        player.getWeapon().getEntity().getBody().setContactCallbackFilter(OBJECT_FLAG);
        player.getWeapon().getEntity().getBody().setActivationState(Collision.DISABLE_DEACTIVATION);
        instances.add(player.getWeapon().getEntity());
        world.addRigidBody((btRigidBody) player.getWeapon().getEntity().getBody());


        for(EntityInstance obj : floorData.objectsInstances){
            obj.getBody().setUserValue(floorData.objectsInstances.indexOf(obj, false));
            obj.getBody().setCollisionFlags(obj.getBody().getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
            obj.getBody().setContactCallbackFlag(OBJECT_FLAG);
            obj.getBody().setContactCallbackFilter(GROUND_FLAG);
            world.addRigidBody((btRigidBody) obj.getBody());
        }

        cam = new PerspectiveCamera(80, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(14f, 6f, /**/55f);
        cam.lookAt(10f, 0, 30f);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();


        playerController = new PlayerController(player,playerPov);


        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(playerController);
        /**
         * free cam :inputMultiplexer.addProcessor(camController);
         */
        Gdx.input.setInputProcessor(inputMultiplexer);
    }


    private void camFollowPlayer(){
        cam.position.set(new Vector3(player.getEntity().transform.getValues()[12]+0.4531824f,player.getEntity().transform.getValues()[13]+5.767706f,player.getEntity().transform.getValues()[14]+-5.032133f));
        float[] fov = {-0.9991338f,3.6507862E-7f,-0.04161331f,0.14425309f,-0.02119839f,0.8605174f,0.50898004f,-2.485553f,0.035809156f,0.5094212f,-0.85977185f,-7.252268f,0.14425309f,-2.485553f,-7.252268f,1.0f};
        cam.view.set(fov);
        cam.direction.set(-0.047802035f,-0.36853015f,0.9283842f);
    }



    /**
     * Render.
     */
    public void render() {

        camFollowPlayer();
        cam.update();

        final float delta = Math.min(1f/30f, Gdx.graphics.getDeltaTime());

        world.getDynamicsWorld().stepSimulation(delta, 5, 1f/60f);

        clock += 1; // add the time since the last frame

        player.getWeapon().getEntity().move(new EntityPosition(player.getPosition().x+0.2f,player.getPosition().y,player.getPosition().z+0.2f));

        Gdx.gl.glClearColor(0.2f, 0.6f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


        modelBatch.begin(cam);
        modelBatch.render(floorData.objectsInstances, environment);
        modelBatch.render(player.getEntity(),environment);
        modelBatch.render(instances,environment);
        modelBatch.end();

        player.getEntity().getGhostObject().getWorldTransform(player.getEntity().transform);
    }

    /**
     * Dispose.
     */
    public void dispose() {
        for(Entity obj : floorData.objectsInstances)
            obj.dispose();

        floorData.objectsInstances.clear();

        world.dispose();
        contactListener.dispose();

        modelBatch.dispose();
        model.dispose();

    }



}
