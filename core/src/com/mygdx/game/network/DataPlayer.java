package com.mygdx.game.network;


/**
 * The type Data player.
 * Useful only for distinguish a new player and a new player position when the server or the client send data
 */
public class DataPlayer {
    /**
     * The Position.
     */
    public DataPlayerPosition position;

    /**
     * Instantiates a new Data player.
     * This constructor is useful only for kryonet.
     */
    public DataPlayer() {
        position = new DataPlayerPosition();
    }

    /**
     * Instantiates a new Data player.
     *
     * @param position the position
     */
    public DataPlayer(DataPlayerPosition position) {
        this.position = position;
    }
}
