package com.mygdx.game.FloorLayout.Type2Floor;

import com.mygdx.game.FloorLayout.Floor;
import com.mygdx.game.FloorLayout.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Labyrinth extends Floor {

    /**
     * generate this floor as a labyrinth made following Prim's algorithm
     *
     * @param sizeOfFloor size of the grid
     * @param numberOfRooms number of rooms we will put in our labyrinth
     * @param maxRoomSize the biggest the width/height of the room can get
     * @param minRoomSize the smallest the width/height of the room can get
     */

    public Labyrinth(int sizeOfFloor, int numberOfRooms, int minRoomSize, int maxRoomSize) {
        Random rand = new Random();

        this.sizeOfFloor = sizeOfFloor;
        layout = new Position[sizeOfFloor][sizeOfFloor];

        for (int i = 0; i < sizeOfFloor; i++) {
            for (int j = 0; j < sizeOfFloor; j++) {
                layout[i][j] = new Position(i, j, 'a');
            }
        }

        int startingCaseX = rand.nextInt(sizeOfFloor);
        int startingCaseY = rand.nextInt(sizeOfFloor);

        layout[startingCaseX][startingCaseY].setContent(' ');

        ArrayList<Position> walls = new ArrayList<>();
        checkForWalls(startingCaseX, startingCaseY, walls);

        HashSet<Position> visited = new HashSet<>();
        visited.add(layout[startingCaseX][startingCaseY]);

        while (!walls.isEmpty()) {

            int currentWall = rand.nextInt(walls.size());

            int x = walls.get(currentWall).getX();
            int y = walls.get(currentWall).getY();

            if (x == 0)         //to avoid an OutOfBounds exception
                x = x + 1;
            if (x == sizeOfFloor - 1)
                x = x - 1;
            if (y == 0)
                y = y + 1;
            if (y == sizeOfFloor - 1)
                y = y - 1;

            if (!(visited.contains(layout[x - 1][y])) && (visited.contains(layout[x + 1][y]))) {
                layout[x][y].setContent(' ');
                layout[x - 1][y].setContent(' ');

                visited.add(layout[x - 1][y]);
                checkForWalls(x - 1, y, walls);
            }
            if ((visited.contains(layout[x - 1][y])) && !(visited.contains(layout[x + 1][y]))) {
                layout[x][y].setContent(' ');
                layout[x + 1][y].setContent(' ');

                visited.add(layout[x + 1][y]);
                checkForWalls(x + 1, y, walls);
            }
            if (!(visited.contains(layout[x][y - 1])) && visited.contains(layout[x][y + 1])) {
                layout[x][y].setContent(' ');
                layout[x][y - 1].setContent(' ');

                visited.add(layout[x][y - 1]);
                checkForWalls(x, y - 1, walls);
            }
            if (visited.contains(layout[x][y - 1]) && !(visited.contains(layout[x][y + 1]))) {
                layout[x][y].setContent(' ');
                layout[x][y + 1].setContent(' ');

                visited.add(layout[x][y + 1]);
                checkForWalls(x, y + 1, walls);
            }
            walls.remove(currentWall);
        }
        GenerateRooms(numberOfRooms, minRoomSize, maxRoomSize);
    }

    /**
     * checks the point in position x y for walls around it
     * @param x x coordinate of our point
     * @param y y coordinate of our point
     * @param walls the list we will fill with the walls
     */

    private void checkForWalls(int x, int y, ArrayList<Position> walls) {
        int x1 = x - 1;
        int x2 = x + 2;
        int y1 = y - 1;
        int y2 = y + 2;

        if (x == 0)                 // We want to avoid an OutOfBounds exception
            x1 = x;
        if (x == sizeOfFloor - 1)
            x2 = sizeOfFloor;
        if (y == 0)
            y1 = y;
        if (y == sizeOfFloor - 1)
            y2 = sizeOfFloor;


        for (int i = x1; i < x2; i++) {
            for (int j = y1; j < y2; j++) {
                if (layout[i][j].getContent() == 'a')
                    walls.add(layout[i][j]);
            }
        }
    }
}