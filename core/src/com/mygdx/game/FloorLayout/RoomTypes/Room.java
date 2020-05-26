package com.mygdx.game.FloorLayout.RoomTypes;

import com.mygdx.game.FloorLayout.Position;

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
        this.x1 = Math.min(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.x2 = Math.max(x1, x2);
        this.y2 = Math.max(y1, y2);
        center = new Position((x1 + x2) / 2, (y1 + y2) / 2);
    }

    boolean valueInRange(int value, int min, int max) {
        return (value >= min) && (value <= max);
    }


    /**
     * Checks if this room intersects with the room in parameter
     */

    public boolean intersects(Room room) {
        boolean xOverlap = false;
        boolean yOverlap = false;

        if (valueInRange(getX1(), room.getX1(), room.getX2()) || valueInRange(getX2(), room.getX1(), room.getX2())) {
            xOverlap = true;
        }

        if (valueInRange(getY1(), room.getY1(), room.getY2()) || valueInRange(getY2(), room.getY1(), room.getY2())) {
            yOverlap = true;
        }

        return xOverlap && yOverlap;
    }

    public boolean contains(int x, int y){
        return (getX1()-1<=x && getX2()+1>= x && getY1()-1<=y && getY2()+1>=y);
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
