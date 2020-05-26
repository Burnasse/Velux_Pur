package com.mygdx.game.network;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.FloorLayout.Floor;
import com.mygdx.game.gameGeneration.GenerateMultiplayerLevel;
import com.mygdx.game.network.data.DataPlayer;
import com.mygdx.game.network.data.DataPlayerPosition;

import java.io.IOException;

/**
 * The type Client velux.
 */
public class ClientVelux {

    private Client client;
    private Kryo kryo;
    private Floor floor;

    /**
     * The connection state.
     */
    public ConnectionState state = ConnectionState.WAIT;

    /**
     * Instantiates a new Client and handle the data received .
     *
     * @param multiplayerLevel the multiplayer level
     */
    public ClientVelux(final GenerateMultiplayerLevel multiplayerLevel) {
        client = new Client(1000000, 1000000);
        client.start();
        try {
            client.connect(5000, "127.0.0.1", 5000, 5000);
        } catch (IOException e) {
            System.out.println("Unable to connect to server");
            state = ConnectionState.LOST;
            return;
        }

        kryo = client.getKryo();
        kryo.setRegistrationRequired(false);

        client.addListener(new Listener() {
            public void connected(Connection connection) {

            }

            public void received(Connection connection, final Object object) {

                System.out.println(object.getClass());
                if(object instanceof Floor){
                    floor = (Floor) object;
                    synchronized (multiplayerLevel){
                        multiplayerLevel.notify();
                    }
                }

                if(object instanceof DataPlayerPosition){
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            multiplayerLevel.move((DataPlayerPosition)object);
                        }
                    });

                }

                if (object instanceof DataPlayer) {
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            multiplayerLevel.addPlayer(((DataPlayer) object));
                        }
                    });
                }
            }

            public void disconnected(Connection connection) {
                System.out.println("Disconnected");
                state = ConnectionState.LOST;
            }
        });
        state = ConnectionState.ENABLE;

    }

    /**
     * Gets floor.
     *
     * @return the floor
     */
    public Floor getFloor() {
        return floor;
    }

    /**
     * Gets client.
     *
     * @return the client
     */
    public Client getClient() {
        return client;
    }


}
