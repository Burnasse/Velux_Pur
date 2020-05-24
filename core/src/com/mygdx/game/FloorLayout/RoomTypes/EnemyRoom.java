package com.mygdx.game.FloorLayout.RoomTypes;

import com.mygdx.game.Entity.utils.EntityPosition;
import com.mygdx.game.FloorGeneration.FloorFactory;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class EnemyRoom extends Room {

    private ArrayList<EntityPosition> enemiesPosition = new ArrayList<>();

    /**
     * generate a room with monsters
     *
     * @param x1               x coordinate of the first point
     * @param y1               y coordinate of the first point
     * @param x2               x coordinate of the second point
     * @param y2               y coordinate of the second point
     * @param numberOfMonsters number of Monsters in the room
     */

    public EnemyRoom(int x1, int y1, int x2, int y2, int numberOfMonsters) {
        super(x1, y1, x2, y2);

        int currentMonsterXPosition;
        int currentMonsterZPosition;

        for (int i = 0; i < numberOfMonsters; i++) {
            currentMonsterXPosition = ThreadLocalRandom.current().nextInt(x1 + 1, x2);
            currentMonsterZPosition = ThreadLocalRandom.current().nextInt(y1 + 1, y2); //In libgdx Z is the depth
            enemiesPosition.add(new EntityPosition(currentMonsterXPosition, 3, currentMonsterZPosition));
        }
    }

    public ArrayList<EntityPosition> getEnemies() {
        return enemiesPosition;
    }
}
