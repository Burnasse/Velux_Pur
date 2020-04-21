package com.mygdx.game.classesatrier.FloorLayout.Type2Floor;

import com.mygdx.game.classesatrier.FloorLayout.Point;

import java.util.ArrayList;
import java.util.Random;

public class Labyrinth {
    private Point[][] layout;
    private int sizeOfFloor;
    private ArrayList<Point> visited = new ArrayList<>();
    private ArrayList<Point> walls = new ArrayList<>();

    //Trop lourd pour ce que c'est, à optimiser

    public Labyrinth(int sizeOfFloor) {

        Random rand = new Random();

        this.sizeOfFloor = sizeOfFloor;
        layout = new Point[sizeOfFloor][sizeOfFloor];

        for (int i = 0; i < sizeOfFloor; i++) {
            for (int j = 0; j < sizeOfFloor; j++) {
                layout[i][j] = new Point(i, j, 'a');

            }
        }

        int startingCaseX = rand.nextInt(sizeOfFloor);
        int startingCaseY = rand.nextInt(sizeOfFloor);

        int endingCaseX = rand.nextInt(sizeOfFloor);
        int endingCaseY = rand.nextInt(sizeOfFloor);

        layout[startingCaseX][startingCaseY].setContent(' ');
        checkForWalls(startingCaseX, startingCaseY);

        visited.add(layout[startingCaseX][startingCaseY]);

        //très sale en-dessous de ce commentaire, à nettoyer
        while (!walls.isEmpty()) {

            int currentWall = rand.nextInt(walls.size());

            int x = walls.get(currentWall).getX();
            int y = walls.get(currentWall).getY();

            if (x == 0)
                x = x + 1;
            if (x == sizeOfFloor - 1)
                x = x - 1;
            if (y == 0)
                y = y + 1;
            if (y == sizeOfFloor - 1)
                y = y - 1;

            int direction = rand.nextInt(2);
            if (direction == 0) {


                if (!(visited.contains(layout[x - 1][y])) && (visited.contains(layout[x + 1][y]))) {
                    System.out.println("a");
                    layout[x][y].setContent(' ');
                    layout[x - 1][y].setContent(' ');
                    visited.add(layout[x][y]);
                    visited.add(layout[x - 1][y]);
                    checkForWalls(x - 1, y);

                }
                if ((visited.contains(layout[x - 1][y])) && !(visited.contains(layout[x + 1][y]))) {
                    System.out.println("a");
                    layout[x][y].setContent(' ');
                    layout[x + 1][y].setContent(' ');
                    visited.add(layout[x][y]);
                    visited.add(layout[x + 1][y]);
                    checkForWalls(x + 1, y);

                }
            } else {
                if (!(visited.contains(layout[x][y - 1])) && visited.contains(layout[x][y + 1])) {
                    layout[x][y].setContent(' ');
                    layout[x][y - 1].setContent(' ');
                    visited.add(layout[x][y]);
                    visited.add(layout[x][y - 1]);
                    checkForWalls(x, y - 1);
                }
                if (visited.contains(layout[x][y - 1]) && !(visited.contains(layout[x][y + 1]))) {
                    layout[x][y].setContent(' ');
                    layout[x][y + 1].setContent(' ');
                    visited.add(layout[x][y]);
                    visited.add(layout[x][y + 1]);
                    checkForWalls(x, y + 1);
                }
            }
            if (layout[endingCaseX][endingCaseY].getContent() == ' ') {
                System.out.println("normalement c'est fini");
                break;
            }
        }
    }

    private void checkForWalls(int x, int y) {
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
                    walls.add(new Point(i, j));
            }
        }
    }

    public void printFloor() {

        for (int i = 0; i < sizeOfFloor; i++) {
            String floor = "";
            for (int j = 0; j < sizeOfFloor; j++) {
                floor = (floor + "  " + layout[i][j].getContent());
            }
            System.out.println(floor);
        }
    }
}
