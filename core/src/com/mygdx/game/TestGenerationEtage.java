package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.classesatrier.Entity.EntityObjects;
import com.mygdx.game.classesatrier.Entity.EntityPlayer;
import com.mygdx.game.classesatrier.Entity.InGameObject;
import com.mygdx.game.classesatrier.EntityPosition;
import com.mygdx.game.classesatrier.FloorLayout.Type1Floor.GenericFloor;


/**
 * The type Test fenetre raph.
 */
public class TestGenerationEtage extends ApplicationAdapter {

    class MyContactListener extends ContactListener {
        @Override
        public boolean onContactAdded (int userValue0, int partId0, int index0, int userValue1, int partId1, int index1) {
            System.out.println("eeeeeeee");
            return true;
        }
    }

    public CameraInputController camController;

    PerspectiveCamera cam;

    Environment environment;

    Array<ModelInstance> instances = new Array<ModelInstance>();

    Array<InGameObject> objectsInstances = new Array<InGameObject>();

    ModelBatch modelBatch;

    Model model;

    AssetManager assets;

    boolean loading;

    InGameObject vaisseau;

    btCollisionConfiguration collisionConfig;

    btDispatcher dispatcher;

    MyContactListener contactListener;

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

        /**
         * gestion de la camera
         */

        cam = new PerspectiveCamera(120, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(14f, 6f, /**/20f);
        cam.lookAt(10, 10f, 10);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        /**
         * on creer les objets ici
         */

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
		/*modelBuilder.node().id = "sphere";
		modelBuilder.part("sphere", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GREEN)))
				.sphere(1f, 1f, 1f, 10, 10);*/
        modelBuilder.node().id = "box";
        modelBuilder.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GRAY)))
                .box(1f, 1f, 1f);
		/*modelBuilder.node().id = "cone";
		modelBuilder.part("cone", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.YELLOW)))
				.cone(1f, 2f, 1f, 10);
		modelBuilder.node().id = "capsule";
		modelBuilder.part("capsule", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.CYAN)))
				.capsule(0.5f, 2f, 10);
		modelBuilder.node().id = "cylinder";
		modelBuilder.part("cylinder", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.MAGENTA))).cylinder(1f, 2f, 1f, 10);*/

        model = modelBuilder.end();
        generateFloor();

        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        collisionWorld = new btCollisionWorld(dispatcher, broadphase, collisionConfig);
        contactListener = new MyContactListener();

        /*for (InGameObject obj: objectsInstances){
            collisionWorld.addCollisionObject(obj.body);
        }*/

        EntityPlayer ship = new EntityPlayer("ship","convertedship.g3db",new btBoxShape(new Vector3(1f, 1f, 1f)),0,0,0);
        this.vaisseau = ship.getInGameObject();
        objectsInstances.add(this.vaisseau);
        collisionWorld.addCollisionObject(vaisseau.body);
    }

    /**
     * method that procedurally generates the floor in 3D
     */
    public void generateFloor() {
        btBoxShape shape = new btBoxShape(new Vector3(0.5f, 0.5f, 1f));
        GenericFloor floor = new GenericFloor(30, 5, 3, 7);
        int x = 0;
        int y = 0;
        int z = 0;
        EntityObjects box = new EntityObjects("box",model,shape, 0, 0, 0);
        for (int i = 0; i < floor.getLayout().length; i++) {
            for (int j = 0; j < floor.getLayout().length; j++) {
                if (floor.getLayout()[i][j].getContent() == ' ') {
                    objectsInstances.add(box.getInGameObject(new EntityPosition(x,y,z)));
                    if (i == 0 || j == 0 || i == floor.getSizeOfFloor() - 1 || j == floor.getSizeOfFloor()-1) {
                        objectsInstances.add(box.getInGameObject(new EntityPosition(x, y + 1, z)));
                    } else {
                        if (floor.getLayout()[i - 1][j].getContent() == 'a') {
                            objectsInstances.add(box.getInGameObject(new EntityPosition(x - 1, y + 1, z)));
                        }
                        if (floor.getLayout()[i + 1][j].getContent() == 'a') {
                            objectsInstances.add(box.getInGameObject(new EntityPosition(x + 1, y+ 1, z )));
                        }
                        if (floor.getLayout()[i][j - 1].getContent() == 'a') {
                            objectsInstances.add(box.getInGameObject(new EntityPosition(x, y +1, z -1)));
                        }
                        if (floor.getLayout()[i][j + 1].getContent() == 'a') {
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
     * pour l'instant totalement inutile
     */
    private void doneLoading() {
        Model model = assets.get("block.obj", Model.class);
        ModelInstance block = new ModelInstance(model);
        instances.add(block);
        loading = false;
    }

    /**
     * position the cam behind the player
     */
    private void camFollowPlayer(){
        cam.position.set(new Vector3(vaisseau.transform.getValues()[12]+0.4531824f,vaisseau.transform.getValues()[13]+5.767706f,vaisseau.transform.getValues()[14]+-5.032133f));
        float champdevision[] = {-0.9991338f,3.6507862E-7f,-0.04161331f,0.14425309f,-0.02119839f,0.8605174f,0.50898004f,-2.485553f,0.035809156f,0.5094212f,-0.85977185f,-7.252268f,0.14425309f,-2.485553f,-7.252268f,1.0f};
        cam.view.set(champdevision);
        cam.direction.set(-0.047802035f,-0.36853015f,0.9283842f);
    }

    /**
     * everything is said in the methode title
     */
    private void playerDeplacment(){
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            vaisseau.mooveEntity(new EntityPosition(+0.1f, 0f, 0f));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            vaisseau.mooveEntity(new EntityPosition(-0.1f, 0f, 0f));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            vaisseau.mooveEntity(new EntityPosition(0f, -0.1f, 0f));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            vaisseau.mooveEntity(new EntityPosition(-0f, 0.1f, 0f));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            vaisseau.mooveEntity(new EntityPosition(-0f, -0f, 0.1f));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.M)) {
            vaisseau.mooveEntity(new EntityPosition(-0f, -0f, -0.1f));
        }

    }

    @Override
    public void render() {

        playerDeplacment();

        clock += 1; // add the time since the last frame

        if (clock > 10) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
                if (playerPov)
                    playerPov = false;
                else
                    playerPov = true;
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

        /**
         * cam info
         *
        System.out.println("cam pos :" +cam.position);
        System.out.println("cam look at :" + cam.view);
        System.out.println("X : "+vaisseau.transform.getValues()[12] + "Y = "+ vaisseau.transform.getValues()[13]+ "Z = "+vaisseau.transform.getValues()[14]);
        System.out.println("cam look at :" + cam.direction);
**/

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
}
