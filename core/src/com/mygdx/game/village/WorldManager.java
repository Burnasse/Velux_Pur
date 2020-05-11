package com.mygdx.game.village;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
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
import com.mygdx.game.FloorGeneration.DynamicWorld;

import static com.mygdx.game.village.CallbackFlags.GROUND_FLAG;
import static com.mygdx.game.village.CallbackFlags.TRIGGER_FLAG;

public class WorldManager {

    private DynamicWorld world;
    private Array<EntityInstance> objectsInstance;

    public WorldManager(){
        world = new DynamicWorld();
        objectsInstance = new Array<>();
    }

    public WorldManager(DebugDrawer debugDrawer){
        world = new DynamicWorld(debugDrawer);
        objectsInstance = new Array<>();
    }

    public void createTrigger(EntityPosition position, float width, float height, float depth){
        btBoxShape triggerShape = new btBoxShape(new Vector3(width,height,depth));
        EntityObjects trigger = new EntityObjects("trigger",new Model(), triggerShape, 0f, position);

        objectsInstance.add(trigger.getEntity());
        trigger.getEntity().getBody().setUserValue(objectsInstance.indexOf(trigger.getEntity(), false));
        trigger.getEntity().getBody().setCollisionFlags(trigger.getEntity().getBody().getCollisionFlags()
                | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE
                | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        trigger.getEntity().getBody().setContactCallbackFlag(TRIGGER_FLAG);
        world.addRigidBody((btRigidBody) trigger.getEntity().getBody());

    }

    public void createHouse(float xPosition, float zPosition, float width, float height, float depth){

        ModelBuilder modelBuilder = new ModelBuilder();
        Model houseModel = modelBuilder.createBox(width,height,depth,
                new Material(ColorAttribute.createDiffuse(Color.GRAY)),
                VertexAttributes.Usage.Position
                        | VertexAttributes.Usage.Normal);

        btBoxShape houseShape = new btBoxShape(new Vector3(width/2,height/2,depth/2));
        EntityObjects house = new EntityObjects("ground",houseModel, houseShape,0f, new EntityPosition(xPosition,height/2,zPosition));
        addObjectInWorld(house);
    }

    public void createGround(Model model, EntityPosition position){
        btBoxShape invisibleWallShape = new btBoxShape(new Vector3(.1f,1,.5f));
        btBoxShape groundShape = new btBoxShape(new Vector3(5f,.25f,.5f));
        EntityObjects ground = new EntityObjects("ground",model, groundShape,0f, position);
        EntityObjects invisibleWallLeft = new EntityObjects("wall",new Model(),invisibleWallShape,0f,new EntityPosition(position.x+5f,position.y,position.z));
        EntityObjects invisibleWallRight = new EntityObjects("wall",new Model(),invisibleWallShape,0f,new EntityPosition(position.x-5f,position.y,position.z));

        addObjectInWorld(ground);
        addObjectInWorld(invisibleWallLeft);
        addObjectInWorld(invisibleWallRight);
    }

    private void addObjectInWorld(EntityObjects obj){
        objectsInstance.add(obj.getEntity());
        obj.getEntity().getBody().setUserValue(objectsInstance.indexOf(obj.getEntity(), false)+1);
        obj.getEntity().getBody().setCollisionFlags(obj.getEntity().getBody().getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        obj.getEntity().getBody().setContactCallbackFlag(GROUND_FLAG);
        world.addRigidBody((btRigidBody) obj.getEntity().getBody());
    }

    public btDynamicsWorld getWorld() {
        return world.getDynamicsWorld();
    }

    public Array<EntityInstance> getObjectsInstance() {
        return objectsInstance;
    }

    public void dispose(){
        world.dispose();
        for(EntityInstance instance : objectsInstance)
            instance.dispose();
    }
}
