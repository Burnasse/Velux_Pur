package com.mygdx.game.Entity;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.mygdx.game.Assets;
import com.mygdx.game.Entity.utils.EntityPosition;

import java.util.Random;

public class MonsterFactory {

    public static EntityMonster create(EntityPosition position, Assets assets, int roomX1, int roomY1, int roomX2, int roomY2){
        Random rand = new Random();
        String typeOfMonster = "tt";
        EntityMonster monster;

        btBoxShape enemyShape = new btBoxShape(new Vector3(0.3f, 0.6f, 0.3f));

        Model enemyModel = assets.manager.get(Assets.enemyModel);
        Model enemyRunModel = assets.manager.get(Assets.enemyRun);
        Model enemyFireModel = assets.manager.get(Assets.enemyFire);

        enemyRunModel.animations.get(0).id = "run";
        enemyFireModel.animations.get(0).id = "fire";

        if(rand.nextInt(2) == 1)
            typeOfMonster = "Gunner";

        monster = new EntityMonster("Monster", enemyModel, enemyShape, 0f, position, typeOfMonster, roomX1, roomY1, roomX2, roomY2,assets );

        monster.getEntity().copyAnimation(enemyRunModel.animations.get(0));
        monster.getEntity().copyAnimation(enemyFireModel.animations.get(0));
        monster.getAnimationController().animate("run", -1, 1.0f, null, 0.2f);

        return monster;
    }
}