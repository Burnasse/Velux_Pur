package com.mygdx.game.classesatrier.FloorLayout;

public abstract class Floor {
     protected Point[][] layout;
     protected int sizeOfFloor;

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

    public Point[][] getLayout() {
        return layout;
    }
}
