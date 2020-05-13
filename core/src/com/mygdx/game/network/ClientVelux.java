package com.mygdx.game.network;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The type Client velux.
 */
public class ClientVelux {

    private Client client;
    private Kryo kryo;
    private ArrayList<DataPosition> instancePosition;
    private DataPosition spawnPosition;

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
        client = new Client(16384, 8192);
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
                if (object instanceof ArrayList) {
                    instancePosition = (ArrayList<DataPosition>) object;
                }

                if (object instanceof DataPosition) {
                    spawnPosition = (DataPosition) object;
                }

                if (object instanceof DataPlayerPosition) {
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            multiplayerLevel.movePlayer((DataPlayerPosition) object);
                        }
                    });
                }

                if (object instanceof DataPlayer) {
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            multiplayerLevel.addPlayer(((DataPlayer) object).position);
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
     * Gets instance pos.
     *
     * @return the instance position
     */
    public ArrayList<DataPosition> getInstancePosition() {
        return instancePosition;
    }

    /**
     * Gets spawn position.
     *
     * @return the spawn position
     */
    public DataPosition getSpawnPosition() {
        return spawnPosition;
    }

    /**
     * Gets client.
     *
     * @return the client
     */
    public Client getClient() {
        return client;
    }

    /**
     * Wait connection to the server.
     * return true if the connection is enable
     * else return false
     *
     * @return the boolean
     */
    public Boolean enableConnection() {
        while (true) {
            if (state.equals(ConnectionState.WAIT))
                continue;
            if (state.equals(ConnectionState.ENABLE))
                return true;
            if (state.equals(ConnectionState.LOST)) {
                return false;
            }
        }
    }

}
