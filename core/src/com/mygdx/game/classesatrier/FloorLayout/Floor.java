package com.mygdx.game.classesatrier.FloorLayout;

import com.mygdx.game.classesatrier.FloorLayout.RoomTypes.EnemyRoom;
import com.mygdx.game.classesatrier.FloorLayout.RoomTypes.Room;
import com.mygdx.game.classesatrier.FloorLayout.RoomTypes.SpawnRoom;
import com.mygdx.game.classesatrier.Position;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

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

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public Position[][] getLayout() {
        return layout;
    }

    public void GenerateRooms(int numberOfRooms, int minRoomSize, int maxRoomSize) {
        int width;
        int height;
        int x;
        int y;
        boolean intersect = false;
        int count = 0;

        while (rooms.size() < numberOfRooms) {

            width = ThreadLocalRandom.current().nextInt(minRoomSize, maxRoomSize);
            height = ThreadLocalRandom.current().nextInt(minRoomSize, maxRoomSize);

            x = ThreadLocalRandom.current().nextInt(0, sizeOfFloor - width + 1);
            y = ThreadLocalRandom.current().nextInt(0, sizeOfFloor - height + 1);

            if (!rooms.isEmpty()) {
                Room newRoom = new EnemyRoom(x, y, x + width, y + height, 2);
                for (Room room : rooms) {
                    if (newRoom.intersects(room)) {
                        count = count + 1;
                        intersect = true;
                        break;
                    }
                }
                if (!intersect) rooms.add(newRoom);

            } else {
                rooms.add(new SpawnRoom(x, y, x + width, y + height));
            }
            if(count == 3000) rooms.clear();
        }

        for (Room room : rooms) {


            for (int i = room.getX1(); i < room.getX2(); i++) {
                for (int j = room.getY1(); j < room.getY2(); j++)
                    layout[i][j].setContent(' ');
            }
        }
    }
}
