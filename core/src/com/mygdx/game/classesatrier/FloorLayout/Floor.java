package com.mygdx.game.classesatrier.FloorLayout;

import com.mygdx.game.classesatrier.FloorLayout.RoomTypes.EnemyRoom;
import com.mygdx.game.classesatrier.FloorLayout.RoomTypes.Room;
import com.mygdx.game.classesatrier.FloorLayout.RoomTypes.SpawnRoom;
import com.mygdx.game.classesatrier.Position;

import java.util.ArrayList;
import java.util.Random;

public abstract class Floor {
    protected Position[][] layout;
    protected int sizeOfFloor;
    protected ArrayList<Room> rooms = new ArrayList<>();

    public void printFloor() {
        for (int i = 0; i < sizeOfFloor; i++) {
            String floor = "";
            for (int j = 0; j < sizeOfFloor; j++) {
                floor = (floor + "  " + layout[i][j].getContent());
            }
            System.out.println(floor);
        }
    }

    public int getSizeOfFloor() {
        return sizeOfFloor;
    }

    public Position[][] getLayout() {
        return layout;
    }

    public void GenerateRooms(int numberOfRooms, int minRoomSize, int maxRoomSize) {
        int width;
        int height;
        int x;
        int y;
        Random rand = new Random();
        int count = 0;

        while (rooms.size() < numberOfRooms) {

            width = rand.nextInt(maxRoomSize - minRoomSize + 1) + minRoomSize;
            height = rand.nextInt(maxRoomSize - minRoomSize + 1) + minRoomSize;

            x = rand.nextInt(sizeOfFloor - maxRoomSize + 1);
            y = rand.nextInt(sizeOfFloor - maxRoomSize + 1);

            if (rooms.isEmpty())
                rooms.add(new SpawnRoom(x, y, x + width, y + height));

            if (!rooms.isEmpty()) {
                Room newRoom = new EnemyRoom(x, y, x + width, y + height, 2);
                for (Room room : rooms) {
                    if (newRoom.intersects(room)) {
                        count = count + 1;
                        if (count == 30) {
                            count = 0;
                            rooms.clear();
                        }
                        break;
                    }
                }
                rooms.add(newRoom);
            }
        }
    }
}
