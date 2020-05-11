package com.mygdx.game.FloorGeneration;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Entity.instances.EntityInstance;
import com.mygdx.game.Entity.EntityMonster;
import com.mygdx.game.Entity.EntityObjects;
import com.mygdx.game.Entity.utils.EntityPosition;
import com.mygdx.game.FloorLayout.Floor;
import com.mygdx.game.FloorLayout.RoomTypes.EnemyRoom;
import com.mygdx.game.FloorLayout.RoomTypes.Room;
import com.mygdx.game.FloorLayout.Type1Floor.GenericFloor;
import com.mygdx.game.FloorLayout.Type2Floor.Labyrinth;

/**
 * The type Floor factory generate a floor.
 */
public class FloorFactory {

    /**
     * Create floor and return his data.
     *
     * @param floorType     the floor type
     * @param sizeOfFloor   the size of floor
     * @param numberOfRooms the number of rooms
     * @param minRoomSize   the min room size
     * @param maxRoomSize   the max room size
     * @param model         the model
     * @return the floor data
     */
    public static FloorData create(String floorType, int sizeOfFloor, int numberOfRooms, int minRoomSize, int maxRoomSize, Model model){
        Floor floor;

        if(floorType.equalsIgnoreCase("Labyrinth"))
            floor = new Labyrinth(sizeOfFloor,numberOfRooms,minRoomSize,maxRoomSize);
        else
            floor = new GenericFloor(sizeOfFloor,numberOfRooms,minRoomSize,maxRoomSize);

        btBoxShape shape = new btBoxShape(new Vector3(5f, 5f, 5f));
        Array<EntityInstance> objectsInstances = new Array<>();
        Array<EntityMonster> entityMonsters = new Array<>();
        EntityPosition spawnPosition;

        int x = 0;
        int y = 0;
        int z = 0;

        for (Room room : floor.getRooms()) {
            if (room instanceof EnemyRoom) {
                for (EntityMonster enemy : ((EnemyRoom) room).getEnemies()){
                    entityMonsters.add(enemy);
                    objectsInstances.add(enemy.getEntity());
                }
            }
        }

        for (int i = 0; i < floor.getLayout().length; i++) {
            for (int j = 0; j < floor.getLayout().length; j++) {
                if (floor.getLayout()[i][j].getContent() == ' ') {
                    objectsInstances.add(new EntityObjects("box",model,shape,0f,new EntityPosition(x,y,z)).getEntity());
                    if (i == 0 || j == 0 || i == floor.getSizeOfFloor() - 1 || j == floor.getSizeOfFloor()-1) {
                        objectsInstances.add(new EntityObjects("box",model,shape,0f, new EntityPosition(x,y + 1, z)).getEntity());
                    } else {
                        if (floor.getLayout()[i - 1][j].getContent() == 'a') {
                            floor.getLayout()[i - 1][j].setContent('m');
                            EntityObjects newEntityObject = new EntityObjects("box",model,shape,0f, new EntityPosition(x - 10, y + 10, z));
                            objectsInstances.add(newEntityObject.getEntity());
                        }
                        if (floor.getLayout()[i + 1][j].getContent() == 'a') {
                            floor.getLayout()[i + 1][j].setContent('m');
                            EntityObjects newEntityObject = new EntityObjects("box",model,shape,0f, new EntityPosition(x + 10, y+ 10, z ));
                            objectsInstances.add(newEntityObject.getEntity());
                        }
                        if (floor.getLayout()[i][j - 1].getContent() == 'a') {
                            floor.getLayout()[i][j-1].setContent('m');
                            EntityObjects newEntityObject = new EntityObjects("box",model,shape,0f, new EntityPosition(x, y +10, z -10));
                            objectsInstances.add(newEntityObject.getEntity());
                        }
                        if (floor.getLayout()[i][j + 1].getContent() == 'a') {
                            floor.getLayout()[i][j + 1].setContent('m');
                            EntityObjects newEntityObject = new EntityObjects("box",model,shape,0f,new EntityPosition(x,y+10,z+10));
                            objectsInstances.add(newEntityObject.getEntity());
                        }
                    }

                }
                z = z + 10;
            }
            x = x + 10;
            z = 0;
        }

        spawnPosition = new EntityPosition(floor.getRooms().get(0).getCenter().getX(),30,floor.getRooms().get(0).getCenter().getY());

        return new FloorData(objectsInstances,entityMonsters,spawnPosition);

    }
}
