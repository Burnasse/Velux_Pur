package com.mygdx.game.classesatrier.FloorLayout.RoomTypes;

import com.mygdx.game.classesatrier.Entity.EntityMonster;
import com.mygdx.game.classesatrier.Entity.EntityPlayer;
import com.mygdx.game.classesatrier.Position;

import java.util.Random;

public class SpawnRoom extends Room {
    EntityPlayer player;

    public SpawnRoom(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);
    }
}
