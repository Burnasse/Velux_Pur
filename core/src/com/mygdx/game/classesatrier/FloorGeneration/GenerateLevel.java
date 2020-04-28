package com.mygdx.game.classesatrier.FloorGeneration;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.classesatrier.Entity.EntityMonster;
import com.mygdx.game.classesatrier.Entity.EntityObjects;
import com.mygdx.game.classesatrier.Entity.EntityPlayer;
import com.mygdx.game.classesatrier.FloorLayout.RoomTypes.EnemyRoom;
import com.mygdx.game.classesatrier.FloorLayout.RoomTypes.Room;
import com.mygdx.game.classesatrier.FloorLayout.RoomTypes.SpawnRoom;
import com.mygdx.game.classesatrier.FloorLayout.Type1Floor.GenericFloor;
import com.mygdx.game.classesatrier.FloorLayout.Type2Floor.Labyrinth;

public class GenerateLevel implements ApplicationListener {



    private  ModelBatch modelBatch;
    private  Environment environment;
    private  PerspectiveCamera cam;
    private  CameraInputController camController;

    btGhostPairCallback ghostPairCallback;
    btPairCachingGhostObject ghostObject;
    btConvexShape ghostShape;
    btKinematicCharacterController characterController;
    Matrix4 characterTransform;
    Vector3 characterDirection = new Vector3();
    Vector3 walkDirection = new Vector3();

    private DynamicWorld world;

    NewInGameObject vaisseau;

    Array<NewInGameObject> objectsInstances = new Array<>();

    Model model;
    Model modelPlayer;

    ModelComponent modelComponent;

    private boolean playerPov =true;

    private int clock;

    @Override
    public void create() {
        Bullet.init();

        ghostPairCallback = new btGhostPairCallback();
        world = new DynamicWorld(ghostPairCallback);

        modelBatch = new ModelBatch();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(80, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(14f, 6f, /**/55f);
        cam.lookAt(10f, 0, 30f);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        modelBuilder.node().id = "box";
        MeshPartBuilder builder = modelBuilder.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position
                | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GRAY)));
        BoxShapeBuilder.build(builder,1f,1f,1f);

        model = modelBuilder.end();
/*
        Model modelPlayer = modelBuilder.createBox(.5f,.5f,.5f, new Material(ColorAttribute.createDiffuse(Color.BLUE)),VertexAttributes.Usage.Position
                | VertexAttributes.Usage.Normal);
        EntityPlayer ship = new EntityPlayer("player",modelPlayer,new btBoxShape(new Vector3(0.25f, 0.25f, 0.25f)),0f,10f,4,50f);
        this.vaisseau = ship.getGameObject();
*/




        //contactListener = new MyContactListener();

        //EntityPlayer ship = new EntityPlayer("ship","convertedship.g3db",new btBoxShape(new Vector3(.5f, .5f, .5f)),10f,10f,3,50f);

/*
        objectsInstances.add(vaisseau);
        world.addRigidBody(vaisseau.body);
*/
        //characterTransform = vaisseau.transform;
        ModelBuilder modelBuilder1 = new ModelBuilder();
        Model model1 = modelBuilder1.createCapsule(0.1f,0.5f,16, new Material(ColorAttribute.createDiffuse(Color.BLUE)),VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        modelComponent = new ModelComponent(model1, 10,10,10);
        modelComponent.transform.rotate(Vector3.X, 90);
        ghostObject = new btPairCachingGhostObject();
        ghostObject.setWorldTransform(modelComponent.transform);
        ghostShape = new btCapsuleShape(0.1f, .5f);
        ghostObject.setCollisionShape(ghostShape);
        ghostObject.setCollisionFlags(btCollisionObject.CollisionFlags.CF_CHARACTER_OBJECT);
        characterController = new btKinematicCharacterController(ghostObject, ghostShape, .35f, Vector3.Y);

/*
        world.getDynamicsWorld().addCollisionObject(ghostObject,
                (short)btBroadphaseProxy.CollisionFilterGroups.CharacterFilter,
                (short)(btBroadphaseProxy.CollisionFilterGroups.StaticFilter | btBroadphaseProxy.CollisionFilterGroups.DefaultFilter));
        ((world.getDynamicsWorld())).addAction(characterController);
*/

        world.getDynamicsWorld().addCollisionObject(ghostObject,(short)btBroadphaseProxy.CollisionFilterGroups.CharacterFilter,(short) btBroadphaseProxy.CollisionFilterGroups.AllFilter);
        world.getDynamicsWorld().addAction(characterController);
        generateFloor();

    }

    public void generateFloor() {
        btBoxShape shape = new btBoxShape(new Vector3(0.5f, 0.5f, 0.5f));
        GenericFloor floor = new GenericFloor(20, 3, 4, 10);
        int x = 0;
        int y = 0;
        int z = 0;
        //EntityObjects box = new EntityObjects("box",model,shape,0f, 0, 0, 0);
        for (Room room : floor.getRooms()) {
            if (room instanceof EnemyRoom) {
                for (EntityMonster enemy : ((EnemyRoom) room).getEnemies())
                    objectsInstances.add(enemy.getGameObject());
            }
            if(room instanceof SpawnRoom) modelComponent.transform.set(((SpawnRoom) room).getPlayer().getGameObject().transform);//objectsInstances.add(((SpawnRoom) room).getPlayer().getGameObject());
        }

        for (int i = 0; i < floor.getLayout().length; i++) {
            for (int j = 0; j < floor.getLayout().length; j++) {
                if (floor.getLayout()[i][j].getContent() == ' ') {
                    objectsInstances.add(new EntityObjects("box",model,new btBoxShape(new Vector3(0.5f, 0.5f, 0.5f)),0f,x,y,z).getGameObject());
                    if (i == 0 || j == 0 || i == floor.getSizeOfFloor() - 1 || j == floor.getSizeOfFloor()-1) {
                        objectsInstances.add(new EntityObjects("box",model,shape,0f,x, y + 1, z).getGameObject());
                        //objectsInstances.add(box.getGameObject(new EntityPosition(x, y + 1, z)));
                    } else {
                        if (floor.getLayout()[i - 1][j].getContent() == 'a') {
                            floor.getLayout()[i - 1][j].setContent('m');
                            objectsInstances.add(new EntityObjects("box",model,shape,0f,x - 1, y + 1, z).getGameObject());
                            //objectsInstances.add(box.getGameObject(new EntityPosition(x - 1, y + 1, z)));
                        }
                        if (floor.getLayout()[i + 1][j].getContent() == 'a') {
                            floor.getLayout()[i + 1][j].setContent('m');
                            objectsInstances.add(new EntityObjects("box",model,shape,0f,x + 1, y+ 1, z ).getGameObject());
                            //objectsInstances.add(box.getGameObject(new EntityPosition(x + 1, y+ 1, z )));
                        }
                        if (floor.getLayout()[i][j - 1].getContent() == 'a') {
                            floor.getLayout()[i][j-1].setContent('m');
                            objectsInstances.add(new EntityObjects("box",model,shape,0f,x, y +1, z -1).getGameObject());
                            //objectsInstances.add(box.getGameObject(new EntityPosition(x, y +1, z -1)));
                        }
                        if (floor.getLayout()[i][j + 1].getContent() == 'a') {
                            floor.getLayout()[i][j + 1].setContent('m');
                            objectsInstances.add(new EntityObjects("box",model,shape,0f,x, y + 1, z + 1).getGameObject());
                            //objectsInstances.add(box.getGameObject(new EntityPosition(x, y + 1, z + 1)));
                        }
                    }

                }
                z = z + 1;
            }
            x = x + 1;
            z = 0;
        }
        for(NewInGameObject obj : objectsInstances){
            world.addRigidBody(obj.body);
        }
        //floor.printFloor();
    }

    private void camFollowPlayer(){
        cam.position.set(new Vector3(modelComponent.transform.getValues()[12]+0.4531824f,modelComponent.transform.getValues()[13]+5.767706f,modelComponent.transform.getValues()[14]+-5.032133f));
        float[] fov = {-0.9991338f,3.6507862E-7f,-0.04161331f,0.14425309f,-0.02119839f,0.8605174f,0.50898004f,-2.485553f,0.035809156f,0.5094212f,-0.85977185f,-7.252268f,0.14425309f,-2.485553f,-7.252268f,1.0f};
        cam.view.set(fov);
        cam.direction.set(-0.047802035f,-0.36853015f,0.9283842f);
    }

    /**
     * make the model assigned to the player character moves with the key pressed
     */

    private void playerDeplacment(){

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            modelComponent.transform.rotate(0, 1, 0, 5f);
            ghostObject.setWorldTransform(modelComponent.transform);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            modelComponent.transform.rotate(0, 1, 0, -5f);
            ghostObject.setWorldTransform(modelComponent.transform);
        }
        // Fetch which direction the character is facing now
        characterDirection.set(-1,0,0).rot(modelComponent.transform).nor();
        // Set the walking direction accordingly (either forward or backward)
        walkDirection.set(0,0,0);
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            walkDirection.add(characterDirection);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            walkDirection.add(-characterDirection.x, -characterDirection.y, -characterDirection.z);
        walkDirection.scl(4f * Gdx.graphics.getDeltaTime());
        // And update the character controller
        characterController.setWalkDirection(walkDirection);

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        final float delta = Math.min(1f/30f, Gdx.graphics.getDeltaTime());

        world.getDynamicsWorld().stepSimulation(delta, 5, 1f/60f);

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



        Gdx.gl.glClearColor(0.2f, 0.6f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        modelBatch.render(objectsInstances, environment);
        modelBatch.render(modelComponent);
        modelBatch.end();

        ghostObject.getWorldTransform(modelComponent.transform);
    }
    private final Vector3 tmp = new Vector3();
    public void updateMove(){
        float deltaX = -Gdx.input.getDeltaX() * .5f;
        float deltaY = -Gdx.input.getDeltaY() * .5f;
        tmp.set(0,0,0);
        cam.rotate(cam.up, deltaX);
        tmp.set(cam.direction).crs(cam.up).nor();
        cam.direction.rotate(tmp,deltaY);
        //Move
        Matrix4 ghost = new Matrix4();
        Vector3 translation = new Vector3();
        ghostObject.getWorldTransform(ghost);
        ghost.getTranslation(translation);
        modelComponent.transform.set(translation.x,translation.y,translation.z, cam.direction.x, cam.direction.y, cam.direction.z,0);
        cam.position.set(translation.x,translation.y,translation.z);
        cam.update(true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        for(NewInGameObject obj : objectsInstances)
            obj.dispose();

        objectsInstances.clear();

        //groundObject.dispose();
        //groundShape.dispose();

        //ballObject.dispose();
        //ballShape.dispose();

        world.dispose();
        //contactListener.dispose();

        modelBatch.dispose();
        model.dispose();
        characterController.dispose();
        ghostObject.dispose();
        ghostShape.dispose();
        ghostPairCallback.dispose();
    }
}
