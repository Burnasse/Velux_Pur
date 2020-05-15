package com.mygdx.game.network.data;

/**
 * The type Data position represent the position of an object.
 */
public class DataPosition {

    /**
     * The Id object.
     */
    public int id;

    public int x;
    public int y;
    public int z;

    /**
     * Instantiates a new Data position.
     * This constructor is useful only for kryonet.
     */
    public DataPosition() {
        id = 0;
        x = 0;
        y = 0;
        z = 0;
    }

    /**
     * Instantiates a new Data position.
     *
     * @param id the id
     * @param x  the x
     * @param y  the y
     * @param z  the z
     */
    public DataPosition(int id, int x, int y, int z) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
