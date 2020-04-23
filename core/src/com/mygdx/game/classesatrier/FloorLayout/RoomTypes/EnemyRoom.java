package com.mygdx.game.classesatrier.FloorLayout.RoomTypes;

import com.badlogic.gdx.graphics.g3d.Model;
import com.mygdx.game.classesatrier.Entity.EntityMonster;
import com.mygdx.game.classesatrier.FloorLayout.RoomTypes.Room;

import java.util.ArrayList;
import java.util.Random;

public class EnemyRoom extends Room {

    private ArrayList<EntityMonster> enemies = new ArrayList<>();

    /**
     * generate a room
     *
     * @param x1 x coordinate of the first point
     * @param y1 y coordinate of the first point
     * @param x2 x coordinate of the second point
     * @param y2 y coordinate of the second point
     */

    public EnemyRoom(int x1, int y1, int x2, int y2, int numberOfMonsters) {
        super(x1, y1, x2, y2);
        Random rand = new Random();
        int currentMonsterXPosition;
        int currentMonsterYPosition;
        Model model = new Model();
        for (int i = 0; i < numberOfMonsters; i++) {
            currentMonsterXPosition = rand.nextInt(x2 - x1 + 1) + x1;
            currentMonsterYPosition = rand.nextInt(y2 - y1 + 1) + y1;
            enemies.add(new EntityMonster("e", ));
        }
    }

    public ArrayList<EntityMonster> getEnemies() {
        return enemies;
    }
}
