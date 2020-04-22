package com.mygdx.game.classesatrier.FloorLayout.RoomTypes;

import com.mygdx.game.classesatrier.Entity.EntityMonster;
import com.mygdx.game.classesatrier.Entity.EntityPlayer;

import java.util.Random;

public class SpawnRoom extends Room {
    EntityPlayer player;
    public SpawnRoom(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);
        Random rand = new Random();
        int playerPositionX = rand.nextInt(x2 - x1 + 1) + x1;
        int playerPositionY = rand.nextInt(y2 - y1 + 1) + y1;
        player.setPosition(playerPositionX, playerPositionY);
    }
}
