package com.mygdx.game.classesatrier.FloorLayout;

public class Room {

    //Coordonnées des points en haut à droite et en bas à gauche de la pièce
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    //Le centre
    private Point center;

    public Point getCenter() {
        return center;
    }

    public Room(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        center = new Point((x1 + x2) / 2, (y1 + y2) / 2);
    }

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
