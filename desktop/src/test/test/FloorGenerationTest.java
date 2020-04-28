package test;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
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
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.utils.Array;

import com.mygdx.game.Entity.EntityMonster;
import com.mygdx.game.Entity.EntityObjects;
import com.mygdx.game.Entity.EntityPlayer;
import com.mygdx.game.Entity.InGameObject;
import com.mygdx.game.Entity.EntityPosition;
import com.mygdx.game.FloorLayout.RoomTypes.EnemyRoom;
import com.mygdx.game.FloorLayout.RoomTypes.Room;
import com.mygdx.game.FloorLayout.RoomTypes.SpawnRoom;
import com.mygdx.game.FloorLayout.Type1Floor.GenericFloor;

/**
 * Just a test to see if the floor is nicely generated
 */

public class FloorGenerationTest extends ApplicationAdapter {

    public CameraInputController camController;

    PerspectiveCamera cam;

    Environment environment;

    Array<ModelInstance> instances = new Array<>();

    Array<InGameObject> objectsInstances = new Array<>();

    ModelBatch modelBatch;

    Model model;

    AssetManager assets;

    InGameObject vaisseau;

    btCollisionConfiguration collisionConfig;

    btDispatcher dispatcher;

    btBroadphaseInterface broadphase;

    btCollisionWorld collisionWorld;

    private boolean playerPov =true;

    private int clock;

    @Override
    public void create() {
        Bullet.init();

        modelBatch = new ModelBatch();
        assets = new AssetManager();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(120, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(14f, 6f, /**/20f);
        cam.lookAt(10, 10f, 10);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        modelBuilder.node().id = "box";
        modelBuilder.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GRAY)))
                .box(1f, 1f, 1f);

        model = modelBuilder.end();

        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        collisionWorld = new btCollisionWorld(dispatcher, broadphase, collisionConfig);

        EntityPlayer ship = new EntityPlayer("ship","convertedship.g3db",new btBoxShape(new Vector3(1f, 1f, 1f)),0,0,0);
        this.vaisseau = ship.getInGameObject();
        objectsInstances.add(this.vaisseau);
        collisionWorld.addCollisionObject(vaisseau.body);

        generateFloor();
    }

    /**
     * method that procedurally generates the floor in 3D
     */

    public void generateFloor() {
        btBoxShape shape = new btBoxShape(new Vector3(0.5f, 0.5f, 1f));
        GenericFloor floor = new GenericFloor(100, 8, 4, 9);
        int x = 0;
        int y = 0;
        int z = 0;
        EntityObjects box = new EntityObjects("box",model,shape, 0, 0, 0);
        for (Room room : floor.getRooms()) {
            if (room instanceof EnemyRoom) {
                for (EntityMonster enemy : ((EnemyRoom) room).getEnemies())
                    objectsInstances.add(enemy.getInGameObject());
            }
            if(room instanceof SpawnRoom)
                vaisseau.mooveEntity(new EntityPosition(floor.getRooms().get(0).getCenter().getX(),floor.getRooms().get(0).getCenter().getY(),0));
        }
        for (int i = 0; i < floor.getLayout().length; i++) {
            for (int j = 0; j < floor.getLayout().length; j++) {
                if (floor.getLayout()[i][j].getContent() == ' ') {
                    objectsInstances.add(box.getInGameObject(new EntityPosition(x,y,z)));
                    if (i == 0 || j == 0 || i == floor.getSizeOfFloor() - 1 || j == floor.getSizeOfFloor()-1) {
                        objectsInstances.add(box.getInGameObject(new EntityPosition(x, y + 1, z)));
                    } else {
                        if (floor.getLayout()[i - 1][j].getContent() == 'a') {
                            floor.getLayout()[i - 1][j].setContent('m');
                            objectsInstances.add(box.getInGameObject(new EntityPosition(x - 1, y + 1, z)));
                        }
                        if (floor.getLayout()[i + 1][j].getContent() == 'a') {
                            floor.getLayout()[i + 1][j].setContent('m');
                            objectsInstances.add(box.getInGameObject(new EntityPosition(x + 1, y+ 1, z )));
                        }
                        if (floor.getLayout()[i][j - 1].getContent() == 'a') {
                            floor.getLayout()[i][j-1].setContent('m');
                            objectsInstances.add(box.getInGameObject(new EntityPosition(x, y +1, z -1)));
                        }
                        if (floor.getLayout()[i][j + 1].getContent() == 'a') {
                            floor.getLayout()[i][j + 1].setContent('m');
                            objectsInstances.add(box.getInGameObject(new EntityPosition(x, y + 1, z + 1)));
                        }
                    }
                }
                z = z + 1;
            }
            x = x + 1;
            z = 0;
        }
        floor.printFloor();
    }

    /**
     * make it so the camera constantly follows the player
     */

    private void camFollowPlayer(){
        cam.position.set(new Vector3(vaisseau.transform.getValues()[12]+0.4531824f,vaisseau.transform.getValues()[13]+5.767706f,vaisseau.transform.getValues()[14]+-5.032133f));
        float[] fov = {-0.9991338f,3.6507862E-7f,-0.04161331f,0.14425309f,-0.02119839f,0.8605174f,0.50898004f,-2.485553f,0.035809156f,0.5094212f,-0.85977185f,-7.252268f,0.14425309f,-2.485553f,-7.252268f,1.0f};
        cam.view.set(fov);
        cam.direction.set(-0.047802035f,-0.36853015f,0.9283842f);
    }

    /**
     * make the model assigned to the player character moves with the key pressed
     */

    private void playerDeplacment(){
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            vaisseau.mooveEntity(new EntityPosition(+1f, 0f, 0f));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            vaisseau.mooveEntity(new EntityPosition(-1f, 0f, 0f));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            vaisseau.mooveEntity(new EntityPosition(0f, -1f, 0f));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            vaisseau.mooveEntity(new EntityPosition(-0f, 1f, 0f));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            vaisseau.mooveEntity(new EntityPosition(-0f, -0f, 1f));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.M)) {
            vaisseau.mooveEntity(new EntityPosition(-0f, -0f, -1f));
        }

    }

    @Override
    public void render() {

        playerDeplacment();

        clock += 1; // add the time since the last frame

        if (clock > 10) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
                playerPov = !playerPov;
            }
            clock = 0; // reset your variable to 0
        }

        if (playerPov){
            camFollowPlayer();
            cam.update();
        }
        else{
            camController.update();
        }

        collisionWorld.performDiscreteCollisionDetection();

        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        modelBatch.begin(cam);
        modelBatch.render(objectsInstances, environment);
        modelBatch.render(instances);
        modelBatch.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        instances.clear();
        assets.dispose();
    }

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new FloorGenerationTest(), config);
    }
}
