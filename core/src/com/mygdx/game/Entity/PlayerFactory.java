package com.mygdx.game.Entity;

import com.badlogic.gdx.graphics.g3d.Model;
import com.mygdx.game.Assets;
import com.mygdx.game.Entity.utils.EntityPosition;

/**
 * The type Player factory.
 */
public class PlayerFactory {

    /**
     * Create entity player.
     *
     * @param assets the assets
     * @param position the position
     * @return the entity player
     */
    public static EntityPlayer create(EntityPosition position, Assets assets) {
        EntityPlayer player;

        Model idleModel = assets.manager.get(Assets.playerIdleModel);
        Model runningModel = assets.manager.get(Assets.playerRunningModel);
        Model walkModel = assets.manager.get(Assets.playerWalkModel);
        Model jumpModel = assets.manager.get(Assets.playerJumpModel);

        idleModel.animations.get(0).id = "idle";
        runningModel.animations.get(0).id = "running";
        walkModel.animations.get(0).id = "walk";
        jumpModel.animations.get(0).id = "jump";

        player = new EntityPlayer("Player", idleModel, position);

        player.getEntity().copyAnimation(runningModel.animations.get(0));
        player.getEntity().copyAnimation(walkModel.animations.get(0));
        player.getEntity().copyAnimation(jumpModel.animations.get(0));

        return player;
    }

}
