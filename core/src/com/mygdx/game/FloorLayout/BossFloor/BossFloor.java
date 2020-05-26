package com.mygdx.game.FloorLayout.BossFloor;

import com.mygdx.game.FloorLayout.Floor;
import com.mygdx.game.FloorLayout.Position;
import com.mygdx.game.FloorLayout.RoomTypes.EnemyRoom;
import com.mygdx.game.FloorLayout.RoomTypes.SpawnRoom;

public class BossFloor extends Floor {

    public BossFloor() {

        sizeOfFloor = 50;
        layout = new Position[sizeOfFloor][sizeOfFloor];
        for (int i = 0; i < sizeOfFloor; i++) {
            for (int j = 0; j < sizeOfFloor; j++) {
                layout[i][j] = new Position(i, j, 'a');
            }
        }

        rooms.add(new SpawnRoom(15, 35, 35, 45));
        rooms.add(new EnemyRoom(10, 5, 40, 20));
        createRooms();

        for (int i = rooms.get(1).getCenter().getY(); i < rooms.get(0).getCenter().getY(); i++) {
            layout[rooms.get(0).getCenter().getX()][i].setContent(' ');
        }
    }

    public static void main(String[] args) {
        BossFloor boss = new BossFloor();
        boss.printFloor();
    }
}