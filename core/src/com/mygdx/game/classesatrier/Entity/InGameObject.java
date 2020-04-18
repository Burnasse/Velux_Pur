package com.mygdx.game.classesatrier.Entity;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.Disposable;

/*
classe repris du tuto bullet, qui etend ModelInstance; donc est une instance contient :
- une HitBox de type btCollisionObject
- un boolean qui dit si l'objet bouge ou pas
 */

public class InGameObject extends ModelInstance implements Disposable {
    static class Constructor implements Disposable {

        public final Model model;
        public final String node;
        public final btCollisionShape shape;

        public Constructor(Model model, String node, btCollisionShape shape) {
            this.model = model;
            this.node = node;
            this.shape = shape;
        }

        public InGameObject construct() {
            return new InGameObject(model, node, shape);
        }

        @Override
        public void dispose () {
                shape.dispose();
            }
        }

        public final btCollisionObject body;
        public boolean moving;

        public InGameObject(Model model, String node, btCollisionShape shape) {
            super(model, node);
            body = new btCollisionObject();
            body.setCollisionShape(shape);
        }

        @Override
        public void dispose () {
            body.dispose();
        }
    }

