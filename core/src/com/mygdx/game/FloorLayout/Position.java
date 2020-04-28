package com.mygdx.game.FloorLayout;

public class Position {

    private int x;
    private int y;

    private char content;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(int x, int y, char character) {
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