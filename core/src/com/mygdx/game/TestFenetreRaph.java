package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.btree.leaf.Wait;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.mygdx.game.classesatrier.FloorLayout.Floor;


/**
 * The type Test fenetre raph.
 */
public class TestFenetreRaph extends ApplicationAdapter {

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
        cam.position.set(0f, 0f, /**/0f);
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

        for (InGameObject obj: objectsInstances){
            collisionWorld.addCollisionObject(obj.body);
        }

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
        Floor floor = new Floor(30, 5, 3, 7);
        floor.generateFloor();
        int x = 0;
        int y = 0;
        int z = 0;
        EntityObjects box = new EntityObjects("box",model,shape, 0, 0, 0);
        for (int i = 0; i < floor.getLayout().length; i++) {
            for (int j = 0; j < floor.getLayout().length; j++) {
                if (floor.getLayout()[i][j] == ' ') {
                    objectsInstances.add(box.getInGameObject(new EntityPosition(x,y,z)));
                    if (i == 0 || j == 0 || i == floor.getSizeOfFloor() - 1 || j == floor.getSizeOfFloor()-1) {
                        objectsInstances.add(box.getInGameObject(new EntityPosition(x, y, z + 1)));
                    } else {
                        if (floor.getLayout()[i - 1][j] == 'a') {
                            objectsInstances.add(box.getInGameObject(new EntityPosition(x - 1, y, z + 1)));
                        }
                        if (floor.getLayout()[i + 1][j] == 'a') {
                            objectsInstances.add(box.getInGameObject(new EntityPosition(x + 1, y, z + 1)));
                        }
                        if (floor.getLayout()[i][j - 1] == 'a') {
                            objectsInstances.add(box.getInGameObject(new EntityPosition(x, y - 1, z + 1)));
                        }
                        if (floor.getLayout()[i][j + 1] == 'a') {
                            objectsInstances.add(box.getInGameObject(new EntityPosition(x, y + 1, z + 1)));
                        }

                    }
                }
                y = y + 1;
            }
            x = x + 1;
            y = 0;
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


    @Override
    public void render() {
        camController.update();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            vaisseau.mooveEntity(new EntityPosition(-0.1f, 0f, 0f));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            vaisseau.mooveEntity(new EntityPosition(+0.1f, 0f, 0f));
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
