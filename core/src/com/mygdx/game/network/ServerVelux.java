package com.mygdx.game.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.FloorLayout.Floor;
import com.mygdx.game.FloorLayout.Type2Floor.Labyrinth;
import com.mygdx.game.network.data.DataPlayer;
import com.mygdx.game.network.data.DataPlayerPosition;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Server velux.
 */
public class ServerVelux {

    private Server server;
    private Kryo kryo;
    private HashMap<Integer, Connection> clientList = new HashMap<>();
    private HashMap<Integer, DataPlayer> players = new HashMap<>();
    private Floor floor;

    /**
     * Instantiates a new Server velux.
     * The server generate a new level and send it to all client
     *
     * @throws IOException the io exception
     */
    public ServerVelux() throws IOException {
        floor = new Labyrinth(30,4,4,8);
        System.out.println("Level generate");

        server = new Server(1000000,1000000);
        server.bind(5000, 5000);
        server.start();

        kryo = server.getKryo();
        kryo.setRegistrationRequired(false);

        server.addListener(new Listener() {

            public void connected(Connection connection) {
                clientList.putIfAbsent(connection.getID(), connection);
                connection.sendTCP(floor);

                System.out.println(players.size());
                for(DataPlayer player : players.values())
                    connection.sendTCP(player);


                DataPlayer newDataPlayer = new DataPlayer(new DataPlayerPosition(connection.getID()),connection.getID());

                players.putIfAbsent(connection.getID(), newDataPlayer);
                sendToAll(connection.getID(), newDataPlayer);

            }

            public void received(Connection connection, Object object) {

                if (object instanceof DataPlayerPosition) {
                    players.get(((DataPlayerPosition)object).id).player.matrix4.set(((DataPlayerPosition)object).matrix4);
                    sendToAll(connection.getID(), players.get(connection.getID()).player);
                }
            }

            public void disconnected(Connection c) {
                System.out.println("AH !");
                clientList.remove(c.getID());
            }
        });

    }

    /**
     * Send the specified object to all client except for the exceptId client
     * @param exceptId the client id
     * @param object the object
     */
    private void sendToAll(int exceptId, Object object) {
        for (Map.Entry<Integer, Connection> set : clientList.entrySet()) {
            if (set.getKey() != exceptId) {
                set.getValue().sendTCP(object);
            }
        }
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {
        new ServerVelux();
    }
}
