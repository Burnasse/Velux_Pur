package com.mygdx.game.classesatrier.FloorLayout;

import java.util.ArrayList;
import java.util.Random;

public class Floor {

    ArrayList<Room> rooms = new ArrayList<>();
    int sizeOfFloor = 30;
    int numberOfRooms = 6;
    int minRoomSize = 3;
    int maxRoomSize = 10;
    char[][] layout;

    /**
     * method that creates the rooms and corridors connecting them in a 2D grid
     *
     * @param maxRoomSize : the biggest the width/height of the room can get
     * @param minRoomSize : the smallest the width/height of the room can get
     * @param sizeOfFloor : the number of cases we have in a floor
     * @param numberOfRooms : the number of rooms in a floor
     */
    public Floor(int sizeOfFloor, int numberOfRooms, int minRoomSize, int maxRoomSize) {

        this.sizeOfFloor = sizeOfFloor;
        this.numberOfRooms = numberOfRooms;
        this.minRoomSize = minRoomSize;
        this.maxRoomSize = maxRoomSize;
        layout = new char[sizeOfFloor][sizeOfFloor];

        int width;
        int height;
        int x;
        int y;
        Random rand = new Random();
        int i = 0;
        while (rooms.size() < numberOfRooms) {
            i++;
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
                        if (i == 30){
                            i = 0;
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
                        hCorridor(prevCenter.x, center.x, prevCenter.y);
                        vCorridor(prevCenter.y, center.y, center.x);
                    }
                    else{
                        vCorridor(prevCenter.y, center.y, prevCenter.x);
                        hCorridor(prevCenter.x, center.x, center.y);
                    }
                }
            } else
                rooms.add(newRoom);
        }

    }

    public int getSizeOfFloor() {
        return sizeOfFloor;
    }

    /**
     * method that generates the floor in a 2D grid
     */

    public void generateFloor() {
        for (int i = 0; i < sizeOfFloor; i++) {
            for (int j = 0; j < sizeOfFloor; j++) {
                if (layout[i][j] != ' ')
                    layout[i][j] = 'a';
            }
        }
        for (Room room : rooms) {
            for (int i = room.getX1(); i < room.getX2(); i++) {
                for (int j = room.getY1(); j < room.getY2(); j++)
                    layout[i][j] = ' ';
            }
        }
    }

    /**
     * method that renders the floor in the console
     */

    public void printFloor() {

        for (int i = 0; i < sizeOfFloor; i++) {
            String floor = "";
            for (int j = 0; j < sizeOfFloor; j++) {
                floor = (floor + "  " + layout[i][j]);
            }
            System.out.println(floor);
        }
    }

    public char[][] getLayout() {
        return layout;
    }


    /**
     * horizontal corridor between the center of two rooms
     */

    public void hCorridor(int x1, int x2, int y) {
        for (int i = Math.min(x1, x2); i < Math.max(x1, x2); i++) {
            layout[i][y] = ' ';
        }
    }

    /**
     * vertical corridor between the center of two rooms
     */
    public void vCorridor(int y1, int y2, int x) {
        for (int i = Math.min(y1, y2); i < Math.max(y1, y2); i++) {
            layout[x][i] = ' ';
        }
    }
}
