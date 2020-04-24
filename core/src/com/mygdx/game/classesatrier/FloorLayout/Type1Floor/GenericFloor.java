package com.mygdx.game.classesatrier.FloorLayout.Type1Floor;

import com.mygdx.game.classesatrier.FloorLayout.Floor;
import com.mygdx.game.classesatrier.FloorLayout.Position;

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
        layout = new Position[sizeOfFloor][sizeOfFloor];

        for (int i = 0; i < sizeOfFloor; i++) {
            for (int j = 0; j < sizeOfFloor; j++) {
                layout[i][j] = new Position(i, j);
            }
        }

        GenerateRooms(numberOfRooms, minRoomSize, maxRoomSize);

        Random rand = new Random();

        for (int i = 1; i < numberOfRooms; i++) {
            Position prevCenter = rooms.get(i - 1).getCenter();
            Position center = rooms.get(i).getCenter();
            if (rand.nextInt(1) == 0) {
                hCorridor(prevCenter.getX(), center.getX(), prevCenter.getY());
                vCorridor(prevCenter.getY(), center.getY(), center.getX());
            } else {
                vCorridor(prevCenter.getY(), center.getY(), prevCenter.getX());
                hCorridor(prevCenter.getX(), center.getX(), center.getY());
            }
        }
        for (int i = 0; i < sizeOfFloor; i++) {
            for (int j = 0; j < sizeOfFloor; j++) {
                if (layout[i][j].getContent() != ' ')
                    layout[i][j].setContent('a');
            }
        }

    }

    /**
     * horizontal corridor between the center of two rooms
     */

    public void hCorridor(int x1, int x2, int y) {
        for (int i = Math.min(x1, x2); i < Math.max(x1, x2) + 1; i++) {
            layout[i][y].setContent(' ');
        }
    }

    /**
     * vertical corridor between the center of two rooms
     */

    public void vCorridor(int y1, int y2, int x) {
        for (int i = Math.min(y1, y2); i < Math.max(y1, y2) + 1; i++) {
            layout[x][i].setContent(' ');
        }
    }
}
