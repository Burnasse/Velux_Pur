package com.mygdx.game.classesatrier.Entity;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.classesatrier.EntityPosition;

/**
 * The type In game object.
 */
public class InGameObject extends ModelInstance implements Disposable {
    /**
     * the constructor is used so we can create only one constructor to instanciate in a game a multitude of entity with the same model and hitbox.
     */
    static class Constructor implements Disposable {
        /**
         * the node is the id of the model we want, its set at "a12345a" to recognize if a change has been made to the node attribute
         */
        private String node = "a12345a";
        private final Model model;
        private final btCollisionShape shape;

        /**
         * Instantiates a new Constructor with a model which has more than 1 model inside
         *
         * @param models the model
         * @param node   the node of the item that we wants
         * @param shape  the shape
         */
        public Constructor(Model models, String node, btCollisionShape shape) {
            this.model = models;
            this.node = node;
            this.shape = shape;
        }

        /**
         * Instantiates a new Constructor with a single model
         *
         * @param model the model
         * @param shape the shape
         */
        public Constructor(Model model, btCollisionShape shape) {
            this.model = model;
            this.shape = shape;
        }

        /**
         * Construct in game object.
         *
         * @return the in game object
         */
        public InGameObject construct() {
            if (node.equals("a12345a"))
                return new InGameObject(model, shape);
            else
                return new InGameObject(model,node,shape);
        }

        /**
         * empties the shape when not needed anymore. use it when you are done using the constructor
         */
        @Override
        public void dispose () {
                shape.dispose();
            }
        }


    /**
     * The Body.
     */
    public final btCollisionObject body;
    /**
     * object is moving or not
     */
    public boolean moving;

    /**
     * Instantiates a new In game object with a multi model.
     *
     * @param model the model
     * @param node  the node
     * @param shape the shape
     */
    public InGameObject(Model model, String node, btCollisionShape shape) {
            super(model, node);
            body = new btCollisionObject();
            body.setCollisionShape(shape);
        }

    /**
     * Instantiates a new In game object with a single model.
     *
     * @param model the model
     * @param shape the shape
     */
    public InGameObject(Model model, btCollisionShape shape) {
        super(model);
        body = new btCollisionObject();
        body.setCollisionShape(shape);
    }

    /**
     * use it when you are done using your in game instance
     */
        @Override
        public void dispose () {
            body.dispose();
        }

    /**
     * Moove entity.
     *
     * @param pos the pos
     */
    public void mooveEntity(EntityPosition pos){
            super.transform.trn(pos);
        }
    }

