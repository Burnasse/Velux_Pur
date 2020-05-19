package com.mygdx.game.FloorGeneration;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.mygdx.game.Entity.EntityMonster;
import com.mygdx.game.Entity.EntityObjects;
import com.mygdx.game.Entity.instances.EntityInstance;
import com.mygdx.game.Entity.utils.EntityPosition;
import com.mygdx.game.FloorLayout.Floor;
import com.mygdx.game.FloorLayout.RoomTypes.EnemyRoom;
import com.mygdx.game.FloorLayout.RoomTypes.Room;
import com.mygdx.game.FloorLayout.Type1Floor.GenericFloor;
import com.mygdx.game.FloorLayout.Type2Floor.Labyrinth;
import com.mygdx.game.ui.Minimap;

import java.util.ArrayList;

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
    public static FloorData create(String floorType, int sizeOfFloor, int numberOfRooms, int minRoomSize, int maxRoomSize) {

        Floor floor;

        if (floorType.equalsIgnoreCase("Labyrinth"))
            floor = new Labyrinth(sizeOfFloor, numberOfRooms, minRoomSize, maxRoomSize);
        else
            floor = new GenericFloor(sizeOfFloor, numberOfRooms, minRoomSize, maxRoomSize);

        Minimap minimap = new Minimap(floor.getLayout());

        ArrayList<EntityInstance> objectsInstances = new ArrayList<>();
        ArrayList<EntityMonster> entityMonsters = new ArrayList<>();

        int x = 0;
        int y = 0;
        int z = 0;

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        modelBuilder.node().id = "box";
        MeshPartBuilder builder = modelBuilder.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position |VertexAttributes.Usage.Generic, new Material(ColorAttribute.createDiffuse(Color.RED)));
        BoxShapeBuilder.build(builder, 0.3f, 0.3f, 0.3f);
        Model enemyModel = modelBuilder.end();

        btBoxShape enemyShape = new btBoxShape(new Vector3(0.3f, 0.3f, 0.3f));

        for (Room room : floor.getRooms()) {
            if (room instanceof EnemyRoom) {
                for (EntityPosition enemyPosition : ((EnemyRoom) room).getEnemies()) {
                    enemyPosition.x *= blockSize;
                    enemyPosition.z *= blockSize;
                    EntityMonster newMonster = new EntityMonster("méchant monsieur", enemyModel, enemyShape, 15f, enemyPosition);
                    entityMonsters.add(newMonster);
                    objectsInstances.add(newMonster.getEntity());
                }
            }
        }

        AssetManager assetManager = new AssetManager();
        assetManager.load("wallG3D.g3db", Model.class);
        assetManager.finishLoading();
        Model wallModel = assetManager.get("wallG3D.g3db", Model.class);

        assetManager.load("ground.g3db", Model.class);
        assetManager.finishLoading();
        Model ground = assetManager.get("ground.g3db", Model.class);

        btBoxShape wallShape = new btBoxShape(new Vector3(blockSize / 2f, blockSize / 2f, blockSize / 2f));
        btBoxShape groundShape = new btBoxShape(new Vector3( blockSize/2, blockSize / 4, blockSize/2));

        for (int i = 0; i < sizeOfFloor; i++) {
            for (int j = 0; j < sizeOfFloor; j++) {
                if (floor.getLayout()[i][j].getContent() == ' ') {
                    objectsInstances.add(new EntityObjects("box",ground,groundShape,0f,new EntityPosition(x,y+blockSize/2,z)).getEntity());
                    if (i == 0 || j == 0 || i == sizeOfFloor - 1 || j == sizeOfFloor - 1) {
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

        //objectsInstances.add(new EntityObjects("box", ground, groundShape, 0f, new EntityPosition((sizeOfFloor * blockSize / 2) - blockSize / 2, blockSize / 4, (sizeOfFloor * blockSize / 2) - blockSize / 2)).getEntity());

        EntityPosition spawnPosition = new EntityPosition(floor.getRooms().get(0).getCenter().getX() * blockSize, 5f, floor.getRooms().get(0).getCenter().getY() * blockSize);

        return new FloorData(objectsInstances, entityMonsters, spawnPosition, minimap);

    }
}
