package com.mygdx.game.Entity;

import com.badlogic.gdx.assets.AssetDescriptor;
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
        Model dodgeModel = assets.manager.get(Assets.playerDodge);
        Model danceModel = assets.manager.get(Assets.playerDance);
        Model chickenModel = assets.manager.get(Assets.playerChicken);
        Model macarenaModel = assets.manager.get(Assets.playerMacarena);
        Model shuffleModel = assets.manager.get(Assets.playerShuffle);
        Model thrillerModel = assets.manager.get(Assets.playerThriller);

        idleModel.animations.get(0).id = "idle";
        runningModel.animations.get(0).id = "running";
        walkModel.animations.get(0).id = "walk";
        jumpModel.animations.get(0).id = "jump";
        dodgeModel.animations.get(0).id = "dodge";
        danceModel.animations.get(0).id = "dance";
        chickenModel.animations.get(0).id = "chicken";
        macarenaModel.animations.get(0).id = "macarena";
        shuffleModel.animations.get(0).id = "shuffle";
        thrillerModel.animations.get(0).id = "thriller";

        player = new EntityPlayer("Player", idleModel, position);

        player.getEntity().copyAnimation(runningModel.animations.get(0));
        player.getEntity().copyAnimation(walkModel.animations.get(0));
        player.getEntity().copyAnimation(jumpModel.animations.get(0));
        player.getEntity().copyAnimation(dodgeModel.animations.get(0));
        player.getEntity().copyAnimation(danceModel.animations.get(0));
        player.getEntity().copyAnimation(chickenModel.animations.get(0));
        player.getEntity().copyAnimation(macarenaModel.animations.get(0));
        player.getEntity().copyAnimation(shuffleModel.animations.get(0));
        player.getEntity().copyAnimation(thrillerModel.animations.get(0));

        return player;
    }

}
