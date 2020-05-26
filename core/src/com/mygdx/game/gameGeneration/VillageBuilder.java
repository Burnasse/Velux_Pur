package com.mygdx.game.gameGeneration;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Assets;
import com.mygdx.game.Entity.EntityObjects;
import com.mygdx.game.Entity.instances.EntityInstance;
import com.mygdx.game.Entity.utils.EntityPosition;
import com.mygdx.game.physics.DynamicWorld;

import static com.mygdx.game.physics.CallbackFlags.GROUND_FLAG;
import static com.mygdx.game.physics.CallbackFlags.TRIGGER_FLAG;

/**
 * The type Village builder.
 */
public class VillageBuilder {

    private Assets assets;

    private DynamicWorld world;
    private Array<EntityInstance> objectsInstance;
    private Array<ModelInstance> boxLightInstance;

    private ModelInstance sky;
    private ModelInstance background;

    private Model groundModel;
    private Model boxLightModel;

    /**
     * Instantiates a new Village builder.
     */
    public VillageBuilder(Assets assets) {
        world = new DynamicWorld();
        this.assets = assets;
        init();
    }

    /**
     * Instantiates a new Village builder.
     *
     * @param debugDrawer the debug drawer
     */
    public VillageBuilder(Assets assets, DebugDrawer debugDrawer) {
        world = new DynamicWorld(debugDrawer);
        this.assets = assets;
        init();
    }

    private void init() {
        objectsInstance = new Array<>();
        boxLightInstance = new Array<>();

        Model skyBox = assets.manager.get(Assets.skyBoxVillage);
        sky = new ModelInstance(skyBox);

        Model backgroundModel = assets.manager.get(Assets.groundVillage);
        background = new ModelInstance(backgroundModel);
        background.transform.trn(5, -0.5f, 5);

        ModelBuilder modelBuilder = new ModelBuilder();
        groundModel = modelBuilder.createBox(10f, .5f, 1f,
                new Material(ColorAttribute.createDiffuse(Color.GRAY)),
                VertexAttributes.Usage.Position
                        | VertexAttributes.Usage.Normal);

        boxLightModel = modelBuilder.createBox(0.25f,0.25f,0.25f,new Material(new ColorAttribute(ColorAttribute.Emissive,new Color(0.9f,0.3f,0.3f,1))), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    }

    /**
     * Create house.
     *
     * @param xPosition the x position
     * @param zPosition the z position
     * @param width     the width
     * @param height    the height
     * @param depth     the depth
     */
    public void createHouse(float xPosition, float zPosition, float width, float height, float depth) {

        ModelBuilder modelBuilder = new ModelBuilder();
        Model houseModel = modelBuilder.createBox(width, height, depth,
                new Material(ColorAttribute.createDiffuse(Color.GRAY)),
                VertexAttributes.Usage.Position
                        | VertexAttributes.Usage.Normal);

        btBoxShape houseShape = new btBoxShape(new Vector3(width / 2, height / 2, depth / 2));
        EntityObjects house = new EntityObjects("ground", houseModel, houseShape, 0f, new EntityPosition(xPosition, height / 2, zPosition));
        addObjectInWorld(house);
    }

    /**
     * Create ground.
     *
     * @param x     the x
     * @param y     the y
     * @param z     the z
     */
    public void createGround(float x, float y, float z) {
        btBoxShape invisibleWallShape = new btBoxShape(new Vector3(.1f, 1, .5f));
        btBoxShape groundShape = new btBoxShape(new Vector3(5f, .25f, .5f));
        EntityObjects ground = new EntityObjects("ground", groundModel, groundShape, 0f, new EntityPosition(x, y, z));
        EntityObjects invisibleWallLeft = new EntityObjects("wall", new Model(), invisibleWallShape, 0f, new EntityPosition(x + 5f, y, z));
        EntityObjects invisibleWallRight = new EntityObjects("wall", new Model(), invisibleWallShape, 0f, new EntityPosition(x - 5f, y, z));

        addObjectInWorld(ground);
        addObjectInWorld(invisibleWallLeft);
        addObjectInWorld(invisibleWallRight);
    }

    public void createLightBox(Environment environment, float x, float y, float z, float rotation){
        ModelInstance cubeLight = new ModelInstance(boxLightModel);
        cubeLight.transform.trn(new Vector3(x,y,z));
        if(rotation != 0) cubeLight.transform.rotate(Vector3.Y,rotation);
        boxLightInstance.add(cubeLight);
        environment.add(new PointLight().set(new Color(0.9f,0.3f,0.1f,1),new Vector3(x,y,z),10));
    }

    private void addObjectInWorld(EntityObjects obj) {
        objectsInstance.add(obj.getEntity());
        obj.getEntity().getBody().setUserValue(objectsInstance.indexOf(obj.getEntity(), false) + 1);
        obj.getEntity().getBody().setCollisionFlags(obj.getEntity().getBody().getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        obj.getEntity().getBody().setContactCallbackFlag(GROUND_FLAG);
        world.addRigidBody(obj.getEntity().getBody());
    }

    /**
     * Gets world.
     *
     * @return the world
     */
    public btDynamicsWorld getWorld() {
        return world.getDynamicsWorld();
    }

    /**
     * Gets objects instance.
     *
     * @return the objects instance
     */
    public Array<EntityInstance> getObjectsInstance() {
        return objectsInstance;
    }


    /**
     * Gets box light instance.
     *
     * @return the box light instance
     */
    public Array<ModelInstance> getBoxLightInstance() {
        return boxLightInstance;
    }

    /**
     * Gets sky.
     *
     * @return the sky
     */
    public ModelInstance getSky() {
        return sky;
    }

    /**
     * Gets background.
     *
     * @return the background
     */
    public ModelInstance getBackground() {
        return background;
    }

    /**
     * Dispose.
     */
    public void dispose() {
        for (EntityInstance instance : objectsInstance) {
            world.getDynamicsWorld().removeRigidBody((btRigidBody) instance.getBody());
            instance.dispose();
        }

        world.dispose();
    }

}
