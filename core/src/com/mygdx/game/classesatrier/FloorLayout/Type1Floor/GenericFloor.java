package com.mygdx.game.classesatrier.FloorLayout.Type1Floor;

import com.mygdx.game.classesatrier.FloorLayout.Floor;
import com.mygdx.game.classesatrier.FloorLayout.Point;
import com.mygdx.game.classesatrier.FloorLayout.Room;

import java.util.ArrayList;
import java.util.Random;

public class GenericFloor extends Floor {

    /**
     * method that creates the rooms and corridors connecting them in a 2D grid
     *
     * @param maxRoomSize   : the biggest the width/height of the room can get
     * @param minRoomSize   : the smallest the width/height of the room can get
     * @param sizeOfFloor   : the number of cases we have in a floor
     * @param numberOfRooms : the number of rooms in a floor
     */

    public GenericFloor(int sizeOfFloor, int numberOfRooms, int minRoomSize, int maxRoomSize) {

        this.sizeOfFloor = sizeOfFloor;
        layout = new Point[sizeOfFloor][sizeOfFloor];

        for (int i = 0; i < sizeOfFloor; i++) {
            for (int j = 0; j < sizeOfFloor; j++) {
                layout[i][j] = new Point(i, j);
            }
        }

        int width;
        int height;
        int x;
        int y;
        Random rand = new Random();
        int count = 0;
        ArrayList<Room> rooms = new ArrayList<>();
        while (rooms.size() < numberOfRooms) {
            count = count + 1;
            width = rand.nextInt(maxRoomSize - minRoomSize + 1) + minRoomSize;
            height = rand.nextInt(maxRoomSize - minRoomSize + 1) + minRoomSize;

            x = rand.nextInt(sizeOfFloor - maxRoomSize + 1);
            y = rand.nextInt(sizeOfFloor - maxRoomSize + 1);

            Room newRoom = new Room(x, y, x + width, y + height);

            if (!rooms.isEmpty()) {
                boolean intersects = false;
                for (Room room : rooms) {
                    if (newRoom.intersects(room)) {
                        intersects = true;
                        if (count == 30) {
                            count = 0;
                            rooms.clear();
                        }
                        break;
                    }
                }
                if (!intersects) {
                    Point prevCenter = rooms.get(rooms.size() - 1).getCenter();
                    rooms.add(newRoom);
                    Point center = rooms.get(rooms.size() - 1).getCenter();
                    if (rand.nextInt(1) == 0) {
                        hCorridor(prevCenter.getX(), center.getX(), prevCenter.getY());
                        vCorridor(prevCenter.getY(), center.getY(), center.getX());
                    } else {
                        vCorridor(prevCenter.getY(), center.getY(), prevCenter.getX());
                        hCorridor(prevCenter.getX(), center.getX(), center.getY());
                    }
                }
            } else
                rooms.add(newRoom);
        }

        for (int i = 0; i < sizeOfFloor; i++) {
            for (int j = 0; j < sizeOfFloor; j++) {
                if (layout[i][j].getContent() != ' ')
                    layout[i][j].setContent('a');
            }
        }
        for (Room room : rooms) {
            for (int i = room.getX1(); i < room.getX2(); i++) {
                for (int j = room.getY1(); j < room.getY2(); j++)
                    layout[i][j].setContent(' ');
            }
        }

    }

    /**
     * horizontal corridor between the center of two rooms
     */

    public void hCorridor(int x1, int x2, int y) {
        for (int i = Math.min(x1, x2); i < Math.max(x1, x2); i++) {
            layout[i][y].setContent(' ');
        }
    }

    /**
     * vertical corridor between the center of two rooms
     */

    public void vCorridor(int y1, int y2, int x) {
        for (int i = Math.min(y1, y2); i < Math.max(y1, y2); i++) {
            layout[x][i].setContent(' ');
        }
    }
}
