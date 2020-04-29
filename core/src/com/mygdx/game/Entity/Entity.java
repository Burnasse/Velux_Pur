package com.mygdx.game.Entity;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;

public interface Entity {

    btCollisionObject getBody();

    void dispose();
}
