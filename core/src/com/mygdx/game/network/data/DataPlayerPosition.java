package com.mygdx.game.network.data;

import com.badlogic.gdx.math.Matrix4;

/**
 * The type Data player position.
 * Useful only for distinguish a new player and a new player position when the server or the client send data
 */
public class DataPlayerPosition {
    /**
     * The Id player.
     */
    public int id;
    /**
     * The Matrix 4 (transform).
     */
    public Matrix4 matrix4;

    /**
     * Instantiates a new Data player position.
     * This constructor is useful only for kryonet.
     */
    public DataPlayerPosition() {
        id = 0;
        matrix4 = new Matrix4();
    }

    /**
     * Instantiates a new Data player position.
     *
     * @param id      the id
     */
    public DataPlayerPosition(int id) {
        this.id = id;
        matrix4 = new Matrix4();
    }

    /**
     * Instantiates a new Data player position.
     *
     * @param id      the id
     * @param matrix4 the matrix 4
     */
    public DataPlayerPosition(int id, Matrix4 matrix4) {
        this.id = id;
        this.matrix4 = matrix4;
    }

    /**
     * Sets matrix 4.
     *
     * @param matrix4 the matrix 4
     */
    public void setMatrix4(Matrix4 matrix4) {
        this.matrix4 = matrix4;
    }
}