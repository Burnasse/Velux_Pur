package com.mygdx.game.classesatrier.FloorLayout;

public class Point {
    private int x;
    private int y;
    private char content;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y, char character) {
        this.x = x;
        this.y = y;
        content = character;
    }

    public void setContent(char content) {
        this.content = content;
    }

    public char getContent() {
        return content;
    }
}
