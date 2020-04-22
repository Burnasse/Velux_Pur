package com.mygdx.game.classesatrier.FloorLayout.RoomTypes;

import com.mygdx.game.classesatrier.Position;

public abstract class Room {


    private int x1;
    private int y1;
    private int x2;
    private int y2;

    private Position center;

    public Position getCenter() {
        return center;
    }

    /**
     * generate a room
     *
     * @param x1 x coordinate of the first point
     * @param y1 y coordinate of the first point
     * @param x2 x coordinate of the second point
     * @param y2 y coordinate of the second point
     */

    public Room(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        center = new Position((x1 + x2) / 2, (y1 + y2) / 2);
    }

    /**
     * Checks if this room intersects with the room in parameter
     */

    public boolean intersects(Room room) {
        return (x1 <= room.x2 && x2 >= room.x1 &&
                y1 <= room.y2 && room.y2 >= room.y1);
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }


}
