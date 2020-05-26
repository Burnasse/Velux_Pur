package com.mygdx.game.Entity.utils;

import com.badlogic.gdx.math.Vector3;

public class EntityPosition extends Vector3 {

    public EntityPosition(float x, float y, float z) {
        super(x,y,z);
    }

    /**
     * Create new EntityPosition
     * Mainly used in multiplayer
     * */
    public EntityPosition(){
        super(0,0,0);
    }
}