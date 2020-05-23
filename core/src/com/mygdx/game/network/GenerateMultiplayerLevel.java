package com.mygdx.game.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseProxy;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Entity.EntityPlayer;
import com.mygdx.game.Entity.instances.EntityInstance;
import com.mygdx.game.Entity.instances.EntityInstancePlayer;
import com.mygdx.game.Entity.utils.EntityPosition;
import com.mygdx.game.network.data.DataPlayerPosition;
import com.mygdx.game.network.data.DataPosition;
import com.mygdx.game.physics.DynamicWorld;
import com.mygdx.game.VeluxPurGame;
import com.mygdx.game.controller.PlayerController;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The type Generate multiplayer.
 */
public class GenerateMultiplayerLevel implements Screen {

    /**
     * The constant GROUND_FLAG.
     */
    final static short GROUND_FLAG = 1 << 8;
    /**
     * The constant OBJECT_FLAG.
     */
    final static short OBJECT_FLAG = 1 << 9;

    private VeluxPurGame manager;
    private ClientVelux client;

    private ModelBatch modelBatch;
    private Environment environment;
    private PerspectiveCamera cam;
    private CameraInputController camController;
    private DynamicWorld world;

    private EntityPlayer player;
    private PlayerController playerController;

    private boolean playerPov = true;
    private int clock;

    private Array<EntityInstance> entityInstances;
    private HashMap<Integer, EntityPlayer> players;
    private Array<EntityInstancePlayer> entityInstancePlayers;

    /**
     * Instantiates a new Generate multiplayer.
     *
     * @param manager the manager
     */
    public GenerateMultiplayerLevel(VeluxPurGame manager) {
        this.manager = manager;
    }


    /**
     * Init level.
     *
     * @param client the client
     */
    public void initLevel(ClientVelux client) {
        this.client = client;
        players = new HashMap<>();
        entityInstancePlayers = new Array<>();

        world = new DynamicWorld();

        modelBatch = new ModelBatch();

        /* Wait the labyrinth data send by the server*/
        while (client.getInstancePosition() == null)
            continue;

        createInstance(client.getInstancePosition());

        System.out.println(entityInstances.size);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(80, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(14f, 6f, /**/55f);
        cam.lookAt(10f, 0, 30f);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        ModelBuilder modelBuilder1 = new ModelBuilder();
        Model model1 = modelBuilder1.createCapsule(0.1f, 0.5f, 16, new Material(ColorAttribute.createDiffuse(Color.BLUE)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        /* Wait the player position send by the server*/
        while (client.getSpawnPosition() == null)
            continue;

        System.out.println(client.getSpawnPosition().x + " - " + client.getSpawnPosition().y + " - " + client.getSpawnPosition().z);
        player = new EntityPlayer("Player", model1, new EntityPosition(client.getSpawnPosition().x, client.getSpawnPosition().y, client.getSpawnPosition().z));
        world.getDynamicsWorld().addCollisionObject(player.getEntity().getGhostObject(), (short) btBroadphaseProxy.CollisionFilterGroups.CharacterFilter, (short) btBroadphaseProxy.CollisionFilterGroups.AllFilter);
        world.getDynamicsWorld().addAction(player.getEntity().getController());
        player.getEntity().getBody().setContactCallbackFlag(GROUND_FLAG);
        player.getEntity().getBody().setContactCallbackFilter(0);
        player.getEntity().getBody().setActivationState(Collision.DISABLE_DEACTIVATION);

        camController = new CameraInputController(cam);

        playerController = new PlayerController(player,cam);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(camController);
        inputMultiplexer.addProcessor(playerController);
        Gdx.input.setInputProcessor(inputMultiplexer);

    }

    private void camFollowPlayer() {
        cam.position.set(new Vector3(player.getEntity().transform.getValues()[12] + 0.4531824f, player.getEntity().transform.getValues()[13] + 5.767706f, player.getEntity().transform.getValues()[14] + -5.032133f));
        float[] fov = {-0.9991338f, 3.6507862E-7f, -0.04161331f, 0.14425309f, -0.02119839f, 0.8605174f, 0.50898004f, -2.485553f, 0.035809156f, 0.5094212f, -0.85977185f, -7.252268f, 0.14425309f, -2.485553f, -7.252268f, 1.0f};
        cam.view.set(fov);
        cam.direction.set(-0.047802035f, -0.36853015f, 0.9283842f);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        client.getClient().sendTCP(new DataPlayerPosition(client.getClient().getID(), player.getEntity().transform.val));

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

        Gdx.gl.glClearColor(0.2f, 0.6f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        modelBatch.render(entityInstances, environment);
        modelBatch.render(player.getEntity(), environment);
        modelBatch.render(entityInstancePlayers, environment);
        modelBatch.end();

        player.getEntity().getGhostObject().getWorldTransform(player.getEntity().transform);
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
        for (EntityInstance instance : entityInstances)
            instance.dispose();

        for (EntityInstancePlayer instancePlayer : entityInstancePlayers)
            instancePlayer.dispose();

        modelBatch.dispose();
        player.dispose();

    }

    /**
     * Create instance.
     *
     * @param instancePosition the instance position
     */
    public void createInstance(ArrayList<DataPosition> instancePosition) {
        entityInstances = new Array<>();

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        modelBuilder.node().id = "box";
        MeshPartBuilder builder = modelBuilder.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position
                | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GRAY)));
        BoxShapeBuilder.build(builder, 1f, 1f, 1f);
        Model model = modelBuilder.end();

        btBoxShape shape = new btBoxShape(new Vector3(0.5f, 0.5f, 0.5f));

        int id = 0;
        for (DataPosition data : instancePosition) {
            EntityInstance newInstance = new EntityInstance(model, shape, 0f, new EntityPosition(data.x, data.y, data.z));
            newInstance.getBody().setUserValue(id);
            newInstance.getBody().setCollisionFlags(newInstance.getBody().getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
            newInstance.getBody().setContactCallbackFlag(OBJECT_FLAG);
            newInstance.getBody().setContactCallbackFilter(GROUND_FLAG);
            world.addRigidBody((btRigidBody) newInstance.getBody());
            entityInstances.add(newInstance);
            id++;
        }

    }

    /**
     * Add player.
     *
     * @param data the data
     */
    public void addPlayer(DataPlayerPosition data) {
        ModelBuilder modelBuilder1 = new ModelBuilder();
        Model model1 = modelBuilder1.createCapsule(0.1f, 0.5f, 16, new Material(ColorAttribute.createDiffuse(Color.BLUE)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
        String playerID = String.valueOf(data.id);
        EntityPlayer newPlayer = new EntityPlayer(playerID, model1, data.matrix4);
        world.getDynamicsWorld().addCollisionObject(newPlayer.getEntity().getGhostObject(), (short) btBroadphaseProxy.CollisionFilterGroups.CharacterFilter, (short) btBroadphaseProxy.CollisionFilterGroups.AllFilter);
        world.getDynamicsWorld().addAction(newPlayer.getEntity().getController());
        newPlayer.getEntity().getBody().setContactCallbackFlag(GROUND_FLAG);
        newPlayer.getEntity().getBody().setContactCallbackFilter(0);
        newPlayer.getEntity().getBody().setActivationState(Collision.DISABLE_DEACTIVATION);

        players.put(data.id, newPlayer);
        entityInstancePlayers.add(newPlayer.getEntity());

    }

    /**
     * Move player.
     *
     * @param data the data
     */
    public void movePlayer(DataPlayerPosition data) {
        players.get(data.id).getEntity().transform.set(data.matrix4);
    }

    /**
     * Gets manager.
     *
     * @return the manager
     */
    public VeluxPurGame getManager() {
        return manager;
    }

}

