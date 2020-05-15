package com.mygdx.game.network.data;

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
    public float[] matrix4;

    /**
     * Instantiates a new Data player position.
     * This constructor is useful only for kryonet.
     */
    public DataPlayerPosition() {
        id = 0;
        matrix4 = new float[16];
    }

    /**
     * Instantiates a new Data player position.
     *
     * @param id      the id
     * @param matrix4 the matrix 4
     */
    public DataPlayerPosition(int id, float[] matrix4) {
        this.id = id;
        this.matrix4 = matrix4;
    }

    /**
     * Sets matrix 4.
     *
     * @param matrix4 the matrix 4
     */
    public void setMatrix4(float[] matrix4) {
        this.matrix4 = matrix4;
    }
}
