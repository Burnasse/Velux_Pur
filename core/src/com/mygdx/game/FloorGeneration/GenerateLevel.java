package com.mygdx.game.FloorGeneration;

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
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Entity.*;
import com.mygdx.game.FloorLayout.RoomTypes.EnemyRoom;
import com.mygdx.game.FloorLayout.RoomTypes.Room;
import com.mygdx.game.FloorLayout.Type2Floor.Labyrinth;

public class GenerateLevel implements ApplicationListener {

    private  ModelBatch modelBatch;
    private  Environment environment;
    private  PerspectiveCamera cam;
    private  CameraInputController camController;

    EntityPlayer player;
    Vector3 walkDirection = new Vector3();

    private DynamicWorld world;

    Array<EntityInstance> objectsInstances = new Array<>();
    Model model;

    private boolean playerPov =true;
    private int clock;

    @Override
    public void create() {
        Bullet.init();

        world = new DynamicWorld();

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


        ModelBuilder modelBuilder1 = new ModelBuilder();
        Model model1 = modelBuilder1.createCapsule(0.1f,0.5f,16, new Material(ColorAttribute.createDiffuse(Color.BLUE)),VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        player = new EntityPlayer("Player", model1, new EntityPosition(10,10,10));
        world.getDynamicsWorld().addCollisionObject(player.getEntity().getGhostObject(),(short)btBroadphaseProxy.CollisionFilterGroups.CharacterFilter,(short) btBroadphaseProxy.CollisionFilterGroups.AllFilter);
        world.getDynamicsWorld().addAction(player.getEntity().getController());
        generateFloor();

        for(Entity obj : objectsInstances){
            world.addRigidBody((btRigidBody) obj.getBody());
        }
    }

    public void generateFloor() {
        btBoxShape shape = new btBoxShape(new Vector3(0.5f, 0.5f, 0.5f));
        Labyrinth floor = new Labyrinth(100, 3, 15, 20);
        int x = 0;
        int y = 0;
        int z = 0;

        for (Room room : floor.getRooms()) {
            if (room instanceof EnemyRoom) {
                for (EntityMonster enemy : ((EnemyRoom) room).getEnemies())
                    objectsInstances.add(enemy.getEntity());
            }
        }

        for (int i = 0; i < floor.getLayout().length; i++) {
            for (int j = 0; j < floor.getLayout().length; j++) {
                if (floor.getLayout()[i][j].getContent() == ' ') {
                    objectsInstances.add(new EntityObjects("box",model,new btBoxShape(new Vector3(0.5f, 0.5f, 0.5f)),0f,new EntityPosition(x,y,z)).getEntity());
                    if (i == 0 || j == 0 || i == floor.getSizeOfFloor() - 1 || j == floor.getSizeOfFloor()-1) {
                        objectsInstances.add(new EntityObjects("box",model,shape,0f, new EntityPosition(x,y + 1, z)).getEntity());
                    } else {
                        if (floor.getLayout()[i - 1][j].getContent() == 'a') {
                            floor.getLayout()[i - 1][j].setContent('m');
                            EntityObjects newEntityObject = new EntityObjects("box",model,shape,0f, new EntityPosition(x - 1, y + 1, z));
                            objectsInstances.add(newEntityObject.getEntity());
                        }
                        if (floor.getLayout()[i + 1][j].getContent() == 'a') {
                            floor.getLayout()[i + 1][j].setContent('m');
                            EntityObjects newEntityObject = new EntityObjects("box",model,shape,0f, new EntityPosition(x + 1, y+ 1, z ));
                            objectsInstances.add(newEntityObject.getEntity());
                        }
                        if (floor.getLayout()[i][j - 1].getContent() == 'a') {
                            floor.getLayout()[i][j-1].setContent('m');
                            EntityObjects newEntityObject = new EntityObjects("box",model,shape,0f, new EntityPosition(x, y +1, z -1));
                            objectsInstances.add(newEntityObject.getEntity());
                        }
                        if (floor.getLayout()[i][j + 1].getContent() == 'a') {
                            floor.getLayout()[i][j + 1].setContent('m');
                            EntityObjects newEntityObject = new EntityObjects("box",model,shape,0f,new EntityPosition(x,y+1,z+1));
                            objectsInstances.add(newEntityObject.getEntity());
                        }
                    }

                }
                z = z + 1;
            }
            x = x + 1;
            z = 0;
        }

        player.getEntity().transform.set(new EntityPosition(floor.getRooms().get(0).getCenter().getX(),1,floor.getRooms().get(0).getCenter().getY()), new Quaternion());
    }

    private void camFollowPlayer(){
        cam.position.set(new Vector3(player.getEntity().transform.getValues()[12]+0.4531824f,player.getEntity().transform.getValues()[13]+5.767706f,player.getEntity().transform.getValues()[14]+-5.032133f));
        float[] fov = {-0.9991338f,3.6507862E-7f,-0.04161331f,0.14425309f,-0.02119839f,0.8605174f,0.50898004f,-2.485553f,0.035809156f,0.5094212f,-0.85977185f,-7.252268f,0.14425309f,-2.485553f,-7.252268f,1.0f};
        cam.view.set(fov);
        cam.direction.set(-0.047802035f,-0.36853015f,0.9283842f);
    }

    /**
     * make the model assigned to the player character moves with the key pressed
     */

    private void playerDeplacment(){

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            walkDirection.add(1,0,0);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            walkDirection.add(-1,0,0);
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            walkDirection.add(0,0,1);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            walkDirection.add(0,0,-1);

        walkDirection.scl(4f * Gdx.graphics.getDeltaTime());
        player.getEntity().getController().setWalkDirection(walkDirection);

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
        modelBatch.render(player.getEntity(), environment);
        modelBatch.end();

        player.getEntity().getGhostObject().getWorldTransform(player.getEntity().transform);
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        for(Entity obj : objectsInstances)
            obj.dispose();

        objectsInstances.clear();

        world.dispose();
        //contactListener.dispose();

        modelBatch.dispose();
        model.dispose();

    }
}
