package com.mygdx.game.gameGeneration;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Array;
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

    private DynamicWorld world;
    private Array<EntityInstance> objectsInstance;
    private ModelInstance sky;
    private ModelInstance background;

    /**
     * Instantiates a new Village builder.
     */
    public VillageBuilder() {
        world = new DynamicWorld();
        init();
    }

    /**
     * Instantiates a new Village builder.
     *
     * @param debugDrawer the debug drawer
     */
    public VillageBuilder(DebugDrawer debugDrawer) {
        world = new DynamicWorld(debugDrawer);
        init();
    }

    private void init() {
        objectsInstance = new Array<>();

        AssetManager assetManager = new AssetManager();
        assetManager.load("skyBox.g3db", Model.class);
        assetManager.finishLoading();
        Model skyBox = assetManager.get("skyBox.g3db", Model.class);
        sky = new ModelInstance(skyBox);

        assetManager.load("groundG3D.g3db", Model.class);
        assetManager.finishLoading();
        Model backgroundModel = assetManager.get("groundG3D.g3db", Model.class);
        background = new ModelInstance(backgroundModel);
        background.transform.trn(5, -0.5f, 5);
    }

    /**
     * Create trigger.
     *
     * @param position the position
     * @param width    the width
     * @param height   the height
     * @param depth    the depth
     */
    public void createTrigger(EntityPosition position, float width, float height, float depth) {
        btBoxShape triggerShape = new btBoxShape(new Vector3(width, height, depth));
        EntityObjects trigger = new EntityObjects("trigger", new Model(), triggerShape, 0f, position);

        objectsInstance.add(trigger.getEntity());
        trigger.getEntity().getBody().setUserValue(objectsInstance.indexOf(trigger.getEntity(), false) + 1);
        trigger.getEntity().getBody().setCollisionFlags(trigger.getEntity().getBody().getCollisionFlags()
                | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE
                | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        trigger.getEntity().getBody().setContactCallbackFlag(TRIGGER_FLAG);
        world.addRigidBody((btRigidBody) trigger.getEntity().getBody());
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
     * @param model the model
     * @param x     the x
     * @param y     the y
     * @param z     the z
     */
    public void createGround(Model model, float x, float y, float z) {
        btBoxShape invisibleWallShape = new btBoxShape(new Vector3(.1f, 1, .5f));
        btBoxShape groundShape = new btBoxShape(new Vector3(5f, .25f, .5f));
        EntityObjects ground = new EntityObjects("ground", model, groundShape, 0f, new EntityPosition(x, y, z));
        EntityObjects invisibleWallLeft = new EntityObjects("wall", new Model(), invisibleWallShape, 0f, new EntityPosition(x + 5f, y, z));
        EntityObjects invisibleWallRight = new EntityObjects("wall", new Model(), invisibleWallShape, 0f, new EntityPosition(x - 5f, y, z));

        addObjectInWorld(ground);
        addObjectInWorld(invisibleWallLeft);
        addObjectInWorld(invisibleWallRight);
    }

    private void addObjectInWorld(EntityObjects obj) {
        objectsInstance.add(obj.getEntity());
        obj.getEntity().getBody().setUserValue(objectsInstance.indexOf(obj.getEntity(), false) + 1);
        obj.getEntity().getBody().setCollisionFlags(obj.getEntity().getBody().getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        obj.getEntity().getBody().setContactCallbackFlag(GROUND_FLAG);
        world.addRigidBody((btRigidBody) obj.getEntity().getBody());
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
