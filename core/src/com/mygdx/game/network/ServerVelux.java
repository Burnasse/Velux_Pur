package com.mygdx.game.network;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.FloorLayout.Type2Floor.Labyrinth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Server velux.
 */
public class ServerVelux {

    private Server server;
    private Kryo kryo;
    private HashMap<Integer, Connection> clientList = new HashMap<>();
    private ArrayList<DataPosition> instancesPosition = new ArrayList<>();
    private DataPosition spawnPosition;
    private HashMap<Integer, DataPlayer> playersPositions = new HashMap<>();

    /**
     * Instantiates a new Server velux.
     * The server generate a new level and send it to all client
     *
     * @throws IOException the io exception
     */
    public ServerVelux() throws IOException {
        generateLevel();

        server = new Server();
        server.bind(5000, 5000);
        server.start();

        kryo = server.getKryo();
        kryo.setRegistrationRequired(false);

        server.addListener(new Listener() {

            public void connected(Connection connection) {
                clientList.putIfAbsent(connection.getID(), connection);
                //for(DataPosition data : instancesPosition)
                //connection.sendTCP(data);
                connection.sendTCP(instancesPosition);
                connection.sendTCP(spawnPosition);

                if (!playersPositions.isEmpty()) {
                    for (DataPlayer dataPlayer : playersPositions.values())
                        connection.sendTCP(dataPlayer);
                }
                Matrix4 matrix4 = new Matrix4();
                matrix4.set(new Vector3(spawnPosition.x, spawnPosition.y, spawnPosition.z), new Quaternion());
                DataPlayerPosition newDataPlayer = new DataPlayerPosition(connection.getID(), matrix4.val);

                playersPositions.putIfAbsent(connection.getID(), new DataPlayer(newDataPlayer));
                sendToAll(connection.getID(), new DataPlayer(newDataPlayer));

            }

            public void received(Connection connection, Object object) {

                if (object instanceof DataPlayerPosition) {
                    System.out.println(Arrays.toString(((DataPlayerPosition) object).matrix4));
                    playersPositions.get(connection.getID()).position.setMatrix4(((DataPlayerPosition) object).matrix4);
                    sendToAll(connection.getID(), playersPositions.get(connection.getID()).position);
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
     * Generate the position of all object
     */
    private void generateLevel(){
        Labyrinth floor = new Labyrinth(25, 3, 4, 6);

        int x = 0;
        int y = 0;
        int z = 0;

        for (int i = 0; i < floor.getLayout().length; i++) {
            for (int j = 0; j < floor.getLayout().length; j++) {
                if (floor.getLayout()[i][j].getContent() == ' ') {
                    instancesPosition.add(new DataPosition(0, x, y, z));

                    if (i == 0 || j == 0 || i == floor.getSizeOfFloor() - 1 || j == floor.getSizeOfFloor() - 1) {
                        instancesPosition.add(new DataPosition(0, x, y + 1, z));
                    } else {
                        if (floor.getLayout()[i - 1][j].getContent() == 'a') {
                            floor.getLayout()[i - 1][j].setContent('m');
                            instancesPosition.add(new DataPosition(0, x - 1, y + 1, z));
                        }
                        if (floor.getLayout()[i + 1][j].getContent() == 'a') {
                            floor.getLayout()[i + 1][j].setContent('m');
                            instancesPosition.add(new DataPosition(0, x + 1, y + 1, z));
                        }
                        if (floor.getLayout()[i][j - 1].getContent() == 'a') {
                            floor.getLayout()[i][j - 1].setContent('m');
                            instancesPosition.add(new DataPosition(0, x, y + 1, z - 1));
                        }
                        if (floor.getLayout()[i][j + 1].getContent() == 'a') {
                            floor.getLayout()[i][j + 1].setContent('m');
                            instancesPosition.add(new DataPosition(0, x, y + 1, z + 1));
                        }
                    }

                }
                z = z + 1;
            }
            x = x + 1;
            z = 0;
        }

        spawnPosition = new DataPosition(-1, floor.getRooms().get(0).getCenter().getX(), 1, floor.getRooms().get(0).getCenter().getY());

        System.out.println("Level generate");
        System.out.println(spawnPosition.x + " - " + spawnPosition.y + " - " + spawnPosition.z);
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


