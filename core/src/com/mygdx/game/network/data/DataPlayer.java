package com.mygdx.game.network.data;

/**
 * The type Data player.
 * Useful only for distinguish a new player and a new player position when the server or the client send data
 */
public class DataPlayer {
    /**
     * The Position.
     */
    public DataPlayerPosition player;

    public int id;

    /**
     * Instantiates a new Data player.
     * This constructor is useful only for kryonet.
     */
    public DataPlayer() {
        player = new DataPlayerPosition();
        id = 0;
    }

    /**
     * Instantiates a new Data player.
     *
     * @param player the position
     */
    public DataPlayer(DataPlayerPosition player, int id) {
        this.player = player;
        this.id = id;
    }
}