package com.mygdx.game.Entity;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.mygdx.game.Assets;
import com.mygdx.game.Entity.utils.EntityPosition;

public class MonsterFactory {

    public static EntityMonster create(EntityPosition position, Assets assets, int roomX1, int roomY1, int roomX2, int roomY2){
        EntityMonster monster;

        btBoxShape enemyShape = new btBoxShape(new Vector3(0.3f, 0.3f, 0.3f));

        Model enemyModel = assets.manager.get(Assets.enemyModel);
        Model enemyRunModel = assets.manager.get(Assets.enemyRun);
        Model enemyFireModel = assets.manager.get(Assets.enemyFire);

        enemyRunModel.animations.get(0).id = "run";
        enemyFireModel.animations.get(0).id = "fire";

        monster = new EntityMonster("Monster", enemyModel, enemyShape, 0f, position, "Gunner", roomX1, roomY1, roomX2, roomY2 );

        monster.getEntity().copyAnimation(enemyRunModel.animations.get(0));
        monster.getEntity().copyAnimation(enemyFireModel.animations.get(0));
        monster.getAnimationController().animate("run", -1, 1.0f, null, 0.2f);

        return monster;
    }
}