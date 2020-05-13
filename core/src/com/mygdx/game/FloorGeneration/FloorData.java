package com.mygdx.game.FloorGeneration;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Entity.instances.EntityInstance;
import com.mygdx.game.Entity.EntityMonster;
import com.mygdx.game.Entity.utils.EntityPosition;

import java.util.ArrayList;

/**
 * The type Floor data stores all data relative of the floor.
 */
public class FloorData {

    /**
     * The list of all entity in the floor (except player).
     */
    public ArrayList<EntityInstance> objectsInstances;
    /**
     * The list of all monster in the floor.
     */
    public ArrayList<EntityMonster> entityMonsters;
    /**
     * The Player spawn position.
     */
    public EntityPosition playerSpawnPosition;

    /**
     * Instantiates a new Floor data.
     *
     * @param objectsInstances    the objects instances
     * @param entityMonsters      the entity monsters
     * @param playerSpawnPosition the player spawn position
     */
    public FloorData(ArrayList<EntityInstance> objectsInstances, ArrayList<EntityMonster> entityMonsters, EntityPosition playerSpawnPosition) {
        this.objectsInstances = objectsInstances;
        this.entityMonsters = entityMonsters;
        this.playerSpawnPosition = playerSpawnPosition;
    }
}
