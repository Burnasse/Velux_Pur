package com.mygdx.game.FloorGeneration;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.mygdx.game.Assets;
import com.mygdx.game.Entity.EntityMonster;
import com.mygdx.game.Entity.EntityObjects;
import com.mygdx.game.Entity.MonsterFactory;
import com.mygdx.game.Entity.instances.EntityInstance;
import com.mygdx.game.Entity.utils.EntityPosition;
import com.mygdx.game.FloorLayout.Floor;
import com.mygdx.game.FloorLayout.RoomTypes.EnemyRoom;
import com.mygdx.game.FloorLayout.RoomTypes.Room;
import com.mygdx.game.FloorLayout.Type1Floor.GenericFloor;
import com.mygdx.game.FloorLayout.Type2Floor.Labyrinth;
import com.mygdx.game.FloorLayout.Type3Floor.Mixed;
import com.mygdx.game.ui.Minimap;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The type Floor factory generate a floor.
 */
public class FloorFactory {

    public static float blockSize = 5;

    /**
     * Create floor and return his data.
     *
     * @param floorType     the floor type
     * @param sizeOfFloor   the size of floor
     * @param numberOfRooms the number of rooms
     * @param minRoomSize   the min room size
     * @param maxRoomSize   the max room size
     * @return the floor data
     */
    public static FloorData create(String floorType, int sizeOfFloor, int numberOfRooms, int minRoomSize, int maxRoomSize, Assets assets){
        Floor floor;

        if (floorType.equalsIgnoreCase("Labyrinth"))
            floor = new Labyrinth(sizeOfFloor, numberOfRooms, minRoomSize, maxRoomSize);
        if (floorType.equalsIgnoreCase("Mixed"))
            floor = new Mixed(sizeOfFloor, numberOfRooms, minRoomSize, maxRoomSize);
        else
            floor = new GenericFloor(sizeOfFloor, numberOfRooms, minRoomSize, maxRoomSize);

        return buildFloor(floor,assets);
    }

    public static FloorData create(Floor floor, Assets assets){
        return buildFloor(floor,assets);
    }

    private static FloorData buildFloor(Floor floor, Assets assets) {

        ArrayList<EntityInstance> objectsInstances = new ArrayList<>();
        ArrayList<EntityMonster> entityMonsters = new ArrayList<>();

        int x = 0;
        int y = 0;
        int z = 0;

        EntityPosition exitPosition = null;

        for (Room room : floor.getRooms()) {
            if (room instanceof EnemyRoom) {
                for (EntityPosition enemyPosition : ((EnemyRoom) room).getEnemies()) {
                    enemyPosition.x *= blockSize;
                    enemyPosition.z *= blockSize;
                    EntityMonster newMonster = MonsterFactory.create(enemyPosition, assets, room.getX1(),room.getY1(),room.getX2(),room.getY2());
                    newMonster.getBehavior().adaptToFloor((int) blockSize);
                    entityMonsters.add(newMonster);
                    objectsInstances.add(newMonster.getEntity());
                }

                if(exitPosition == null){
                    exitPosition = new EntityPosition(
                            ThreadLocalRandom.current().nextInt(room.getX1()+1, room.getX2()-1)*blockSize,
                            blockSize,
                            ThreadLocalRandom.current().nextInt(room.getY1()+1, room.getY2()-1)*blockSize);
                }
            }
        }

        Minimap minimap = new Minimap(floor.getLayout(), exitPosition);

        Model wallModel = assets.manager.get(Assets.wallLevel);
        Model ground = assets.manager.get(Assets.groundLevel);

        btBoxShape wallShape = new btBoxShape(new Vector3(blockSize / 2f, blockSize / 2f, blockSize / 2f));
        btBoxShape groundShape = new btBoxShape(new Vector3(blockSize / 2, blockSize / 6, blockSize / 2));

        for (int i = 0; i < floor.getSizeOfFloor(); i++) {
            for (int j = 0; j < floor.getSizeOfFloor(); j++) {
                if (floor.getLayout()[i][j].getContent() == ' ') {
                    objectsInstances.add(new EntityObjects("box", ground, groundShape, 0f, new EntityPosition(x, y + blockSize / 2, z)).getEntity());
                    if (i == 0 || j == 0 || i == floor.getSizeOfFloor() - 1 || j == floor.getSizeOfFloor() - 1) {
                        EntityObjects s = new EntityObjects("box", wallModel, wallShape, 0f, new EntityPosition(x, y + blockSize, z));
                        objectsInstances.add(s.getEntity());
                    } else {
                        if (floor.getLayout()[i - 1][j].getContent() == 'a') {
                            floor.getLayout()[i - 1][j].setContent('m');
                            EntityObjects newEntityObject = new EntityObjects("box", wallModel, wallShape, 0f, new EntityPosition(x - blockSize, y + blockSize, z));
                            objectsInstances.add(newEntityObject.getEntity());
                        }
                        if (floor.getLayout()[i + 1][j].getContent() == 'a') {
                            floor.getLayout()[i + 1][j].setContent('m');
                            EntityObjects newEntityObject = new EntityObjects("box", wallModel, wallShape, 0f, new EntityPosition(x + blockSize, y + blockSize, z));
                            objectsInstances.add(newEntityObject.getEntity());
                        }
                        if (floor.getLayout()[i][j - 1].getContent() == 'a') {
                            floor.getLayout()[i][j - 1].setContent('m');
                            EntityObjects newEntityObject = new EntityObjects("box", wallModel, wallShape, 0f, new EntityPosition(x, y + blockSize, z - blockSize));
                            objectsInstances.add(newEntityObject.getEntity());
                        }
                        if (floor.getLayout()[i][j + 1].getContent() == 'a') {
                            floor.getLayout()[i][j + 1].setContent('m');
                            EntityObjects newEntityObject = new EntityObjects("box", wallModel, wallShape, 0f, new EntityPosition(x, y + blockSize, z + blockSize));
                            objectsInstances.add(newEntityObject.getEntity());
                        }
                    }

                }
                z = (int) (z + blockSize);
            }
            x = (int) (x + blockSize);
            z = 0;
        }

        EntityPosition spawnPosition = new EntityPosition(floor.getRooms().get(0).getCenter().getX() * blockSize, 5f, floor.getRooms().get(0).getCenter().getY() * blockSize);

        return new FloorData(objectsInstances, entityMonsters, spawnPosition, exitPosition, minimap);

    }
}
