package com.mygdx.game.classesatrier.Entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;

public class OutGameEntity {
    private Model model;
    private btCollisionShape shape;

    public OutGameEntity(String filename,btCollisionShape shape){
        AssetManager assets = new AssetManager();
        assets.load(filename,Model.class);
    }
}
