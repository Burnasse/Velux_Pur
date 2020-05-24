package com.mygdx.game.FloorLayout.Type3Floor;

import com.mygdx.game.FloorLayout.Position;
import com.mygdx.game.FloorLayout.Type2Floor.Labyrinth;

public class Mixed extends Labyrinth {

    /**
     * Generate a floor that is a Prim labyrinth without the dead ends
     *
     * @param sizeOfFloor   size of the grid
     * @param numberOfRooms number of rooms we will put in our labyrinth
     * @param maxRoomSize   the biggest the width/height of the room can get
     * @param minRoomSize   the smallest the width/height of the room can get
     */

    public Mixed(int sizeOfFloor, int numberOfRooms, int minRoomSize, int maxRoomSize) {
        super(sizeOfFloor, numberOfRooms, minRoomSize, maxRoomSize);

        layout = new Position[sizeOfFloor][sizeOfFloor];

        for (int i = 0; i < sizeOfFloor; i++) {
            for (int j = 0; j < sizeOfFloor; j++) {
                layout[i][j] = new Position(i, j, 'a');
            }
        }


        GenerateRooms(numberOfRooms, minRoomSize, maxRoomSize);
        generatePrimLabyrinth();
        EraseDeadEnds();
    }

    /**
     * Erase the dead ends of the floor
     */

    private void EraseDeadEnds() {
        for (int i = 0; i < sizeOfFloor; i++) {
            layout[i][sizeOfFloor - 1].setContent('a');
            layout[i][0].setContent('a');
            layout[0][i].setContent('a');
            layout[sizeOfFloor - 1][i].setContent('a');
        }


        for (int i = 1; i < sizeOfFloor - 1; i++) {
            for (int j = 1; j < sizeOfFloor - 1; j++) {
                checkForDeadEnds(layout[i][j]);
            }
        }

    }

    /**
     * check if the position is a dead end and recursively erases all the corridor
     *
     * @param position the corridor's potential dead end
     */

    private void checkForDeadEnds(Position position) {

        int wallCount = 0;
        Position nextDeadEnd = null;

        int x1 = position.getX() - 1;
        int x2 = position.getX() + 1;
        int y1 = position.getY() - 1;
        int y2 = position.getY() + 1;


        if (layout[x1][position.getY()].getContent() == 'a') {
            wallCount = wallCount + 1;
        } else nextDeadEnd = layout[x1][position.getY()];

        if (layout[x2][position.getY()].getContent() == 'a') {
            wallCount = wallCount + 1;
        } else nextDeadEnd = layout[x2][position.getY()];

        if (layout[position.getX()][y1].getContent() == 'a') {
            wallCount = wallCount + 1;
        } else nextDeadEnd = layout[position.getX()][y1];

        if (layout[position.getX()][y2].getContent() == 'a') {
            wallCount = wallCount + 1;
        } else nextDeadEnd = layout[position.getX()][y2];


        if (wallCount == 3) {
            layout[position.getX()][position.getY()].setContent('a');
            checkForDeadEnds(nextDeadEnd);
        }


    }

    public static void main(String[] args) {
        Mixed mixed = new Mixed(30, 8, 3, 5);
        mixed.printFloor();
    }
}
