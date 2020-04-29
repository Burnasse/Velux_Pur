package com.mygdx.game.FloorLayout.RoomTypes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.mygdx.game.Entity.EntityPlayer;
import java.util.concurrent.ThreadLocalRandom;

public class SpawnRoom extends Room {

    /**
     * Room in which we put the player at the beginning of a level
     * @param x1 x position of the up-left extremity of the room
     * @param y1 y position of the up-left extremity of the room
     * @param x2 x position of the down-right extremity of the room
     * @param y2 y position of the down-right extremity of the room
     */

    public SpawnRoom(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);

        int xPosition = ThreadLocalRandom.current().nextInt(x1, x2);
        int zPosition =ThreadLocalRandom.current().nextInt(y1, y2); //Z position because z is the depth in libgdx


        Bullet.init();
        btBoxShape btBoxShape = new btBoxShape(new Vector3(0.3f,0.3f,0.3f));

    }

    public SpawnRoom(int x1, int y1, int x2, int y2, EntityPlayer player){
        super(x1, y1, x2, y2);
    }

}