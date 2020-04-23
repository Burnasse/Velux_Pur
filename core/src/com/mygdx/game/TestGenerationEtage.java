package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.classesatrier.Entity.EntityObjects;
import com.mygdx.game.classesatrier.Entity.InGameObject;
import com.mygdx.game.classesatrier.FloorLayout.Type1Floor.GenericFloor;
import com.mygdx.game.classesatrier.FloorLayout.Type2Floor.Labyrinth;

public class TestGenerationEtage extends ApplicationAdapter {

    public CameraInputController camController;
    PerspectiveCamera cam;
    Environment environment;
    Array<ModelInstance> instances = new Array<>();
    Array<InGameObject> objectsInstances = new Array<>();
    ModelBatch modelBatch;
    Model model;
    AssetManager assets;
    boolean loading;


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
        cam.position.set(0, 0, 0);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        modelBuilder.node().id = "box";
        modelBuilder.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.WHITE)))
                .box(1f, 1f, 1f);
        model = modelBuilder.end();

        generateFloor(40, 3, 3, 5);
    }

    /**
     * method that procedurally generates the floor in 3D
     */

    public void generateFloor(int sizeOfFloor, int numberOfRooms, int minRoomSize, int maxRoomSize) {
        Labyrinth genericFloor = new Labyrinth(sizeOfFloor, numberOfRooms, minRoomSize, maxRoomSize);
        genericFloor.printFloor();
        int x = 0;
        int y = 0;
        int z = 0;
        EntityObjects box = new EntityObjects("box", "dd", 0, 0, 0);
        for (int i = 0; i < genericFloor.getLayout().length; i++) {
            for (int j = 0; j < genericFloor.getLayout().length; j++) {
                if (genericFloor.getLayout()[i][j].getContent() == ' ') {
                    objectsInstances.add(box.createObjectFromModel("box", model, new btBoxShape(new Vector3(0.5f, 0.5f, 1f))));
                    objectsInstances.get(objectsInstances.size - 1).transform.trn(x, y, z);

                    if (i == 0 || j == 0 || i == genericFloor.getSizeOfFloor() - 1 || j == genericFloor.getSizeOfFloor() - 1) {
                        objectsInstances.add(box.createObjectFromModel("box", model, new btBoxShape(new Vector3(0.5f, 0.5f, 0.5f))));
                        objectsInstances.get(objectsInstances.size - 1).transform.trn(x, y + 1, z);
                    } else {
                        if (genericFloor.getLayout()[i - 1][j].getContent() == 'a') {
                            objectsInstances.add(box.createObjectFromModel("box", model, new btBoxShape(new Vector3(0.5f, 0.5f, 0.5f))));
                            objectsInstances.get(objectsInstances.size - 1).transform.trn(x - 1, y + 1, z);
                        }

                        if (genericFloor.getLayout()[i + 1][j].getContent() == 'a') {
                            objectsInstances.add(box.createObjectFromModel("box", model, new btBoxShape(new Vector3(0.5f, 0.5f, 0.5f))));
                            objectsInstances.get(objectsInstances.size - 1).transform.trn(x + 1, y + 1, z);
                        }

                        if (genericFloor.getLayout()[i][j - 1].getContent() == 'a') {
                            objectsInstances.add(box.createObjectFromModel("box", model, new btBoxShape(new Vector3(0.5f, 0.5f, 0.5f))));
                            objectsInstances.get(objectsInstances.size - 1).transform.trn(x, y + 1, z - 1);
                        }

                        if (genericFloor.getLayout()[i][j + 1].getContent() == 'a') {
                            objectsInstances.add(box.createObjectFromModel("box", model, new btBoxShape(new Vector3(0.5f, 0.5f, 0.5f))));
                            objectsInstances.get(objectsInstances.size - 1).transform.trn(x, y + 1, z + 1);
                        }
                    }


                }
                z = z + 1;

            }
            x = x + 1;
            z = 0;
        }
        genericFloor.printFloor();
    }

    @Override
    public void render() {

        camController.update();

        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        modelBatch.begin(cam);
        modelBatch.render(objectsInstances, environment);
        modelBatch.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        instances.clear();
        assets.dispose();
    }
}
