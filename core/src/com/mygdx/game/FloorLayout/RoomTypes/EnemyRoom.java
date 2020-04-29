package com.mygdx.game.FloorLayout.RoomTypes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.mygdx.game.Entity.EntityMonster;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.mygdx.game.Entity.EntityPosition;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class EnemyRoom extends Room {

    private ArrayList<EntityMonster> enemies = new ArrayList<>();

    /**
     * generate a room with monsters
     *
     * @param x1 x coordinate of the first point
     * @param y1 y coordinate of the first point
     * @param x2 x coordinate of the second point
     * @param y2 y coordinate of the second point
     * @param numberOfMonsters number of Monsters in the room
     */

    public EnemyRoom(int x1, int y1, int x2, int y2, int numberOfMonsters) {
        super(x1, y1, x2, y2);

        int currentMonsterXPosition;
        int currentMonsterZPosition;

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        modelBuilder.node().id = "box";
        modelBuilder.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.RED)))
                .box(0.3f, 0.3f, 0.3f);
        Model model = modelBuilder.end();

        Bullet.init();
        btBoxShape btBoxShape = new btBoxShape(new Vector3(0.3f,0.3f,0.3f));

        for (int i = 0; i < numberOfMonsters; i++) {

            currentMonsterXPosition = ThreadLocalRandom.current().nextInt(x1, x2);
            currentMonsterZPosition = ThreadLocalRandom.current().nextInt(y1, y2); //In libgdx Z is the depth

            //enemies.add(new EntityMonster("méchant monsieur", model, btBoxShape, currentMonsterXPosition, 1, currentMonsterZPosition));
            enemies.add(new EntityMonster("méchant monsieur", model, btBoxShape, 0.1f, new EntityPosition(currentMonsterXPosition, 1 , currentMonsterZPosition)));
        }
    }

    public ArrayList<EntityMonster> getEnemies() {
        return enemies;
    }
}
