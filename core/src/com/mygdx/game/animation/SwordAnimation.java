package com.mygdx.game.animation;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.mygdx.game.Entity.EntityPlayer;
import com.mygdx.game.Entity.utils.AttackPosition;
import com.mygdx.game.Entity.utils.EntityPosition;

public class SwordAnimation {
    public static int animationduration = 60;
    private EntityPlayer player;
    private Vector3 swordPlayerPos;

    /**
     * Construct a new AnimationController.
     */
    public SwordAnimation(EntityPlayer player,Vector3 swordPlayerPos) {
        this.player = player;
        this.swordPlayerPos = swordPlayerPos;
    }


    public void swordAnimation(){
        Vector3 playerpos = player.getPosition();

        float newOrientation = (float) Math.atan2(-(player.cursor2DPos.x - player.player2DPos.x), player.cursor2DPos.y - player.player2DPos.y) * MathUtils.radiansToDegrees;

        AttackPosition.AttackDirection attackDirection = AttackPosition.getAttackDirection(newOrientation);
        if (player.cdAttack<=30) {
            player.cdAttack++;
            player.getWeapon().getEntity().getBody().setCollisionFlags(btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
            player.cdColisionWeaponEnnemy++;
        }
        if (player.cdAttack<SwordAnimation.animationduration && player.cdAttack>30){
            player.getWeapon().getEntity().getBody().setCollisionFlags(btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
            player.cdAttack++;
            player.cdColisionWeaponEnnemy++;
        }

        if (player.cdAttack == SwordAnimation.animationduration){
            player.getWeapon().getEntity().getBody().setCollisionFlags(55);
            player.isAttacking = false;
        }

        if (player.cdAttack == SwordAnimation.animationduration){
            swordPlayerPos = new Vector3(0.75f,-0.15f,0.15f);
        }

        if (player.cdAttack<SwordAnimation.animationduration) {
            if (attackDirection == AttackPosition.AttackDirection.EAST) {
                if (player.cdAttack == 1) {
                    swordPlayerPos = new Vector3(-1.6f, 0.2f, -0.5f);
                }
                if (player.cdAttack > 2 && player.cdAttack < 25) {
                    swordPlayerPos.z += 0.04;
                }
            }
            if (attackDirection == AttackPosition.AttackDirection.NORTH) {
                if (player.cdAttack == 1) {
                    swordPlayerPos = new Vector3(-0.5f, 0.2f, 1.6f);
                }
                if (player.cdAttack > 2 && player.cdAttack < 25) {
                    swordPlayerPos.x += 0.04;

                }
            }
            if (attackDirection == AttackPosition.AttackDirection.WEST) {
                if (player.cdAttack == 1) {
                    swordPlayerPos = new Vector3(1.6f, 0.2f, 0.5f);
                }
                if (player.cdAttack > 2 && player.cdAttack < 25) {

                    swordPlayerPos.z -= 0.04;
                }
            }
            if (attackDirection == AttackPosition.AttackDirection.SOUTH) {
                if (player.cdAttack == 1) {
                    swordPlayerPos = new Vector3(0.5f, 0.2f, -1.3f);
                }
                if (player.cdAttack > 2 && player.cdAttack < 25) {
                    swordPlayerPos.x -= 0.04;
                }
            }
            if (attackDirection == AttackPosition.AttackDirection.NE) {
                if (player.cdAttack == 1) {
                    swordPlayerPos = new Vector3(-1.9f, 0.2f, 0.7f);
                }
                if (player.cdAttack > 2 && player.cdAttack < 25) {
                    swordPlayerPos.x += 0.02;
                    swordPlayerPos.z += 0.02;
                }
            }
            if (attackDirection == AttackPosition.AttackDirection.NW) {
                if (player.cdAttack == 1) {
                    swordPlayerPos = new Vector3(0.7f, 0.2f, 1.9f);
                }
                if (player.cdAttack > 2 && player.cdAttack < 25) {
                    swordPlayerPos.x += 0.03;
                    swordPlayerPos.z += -0.03;
                }
            }
            if (attackDirection == AttackPosition.AttackDirection.SE) {
                if (player.cdAttack == 1) {
                    swordPlayerPos = new Vector3(-0.7f, 0.2f, -1.9f);
                }
                if (player.cdAttack > 2 && player.cdAttack < 25) {
                    swordPlayerPos.x += -0.03;
                    swordPlayerPos.z -= -0.03;
                }
            }
            if (attackDirection == AttackPosition.AttackDirection.SW) {
                if (player.cdAttack == 1) {
                    swordPlayerPos = new Vector3(1.9f, 0.2f, -0.7f);
                }
                if (player.cdAttack > 2 && player.cdAttack < 25) {
                    swordPlayerPos.x -= 0.02;
                    swordPlayerPos.z -= 0.02;
                }
            }
        }

        player.getWeapon().getEntity().transform.set(player.getPosition(), new Quaternion(new Vector3(0, 1, 0), newOrientation-67.5f));

        player.getWeapon().getEntity().getBody().setWorldTransform(player.getEntity().transform);
       /* if(player.cdAttack == 2) {
            swordPlayerPos = new Vector3(0.50f,0.2f,0.35f);
        }
        if (player.cdAttack > 10 && player.cdAttack < 30 ){
            swordPlayerPos.x += 0.001;
            swordPlayerPos.y += 0.01;
            swordPlayerPos.z += 0;
        }
        if (player.cdAttack > 30 && player.cdAttack < 50 ){
            swordPlayerPos.x += 0.05;
            swordPlayerPos.y += 0.0;
            swordPlayerPos.z += 0;
        }
        if (player.cdAttack > 50 && player.cdAttack < 60 ){
            swordPlayerPos.x += 0.008;
            swordPlayerPos.y += 0.0;
            swordPlayerPos.z -= 0.06;
        }

        if (player.cdAttack > 60 && player.cdAttack < 70 ){
            swordPlayerPos.x -= 0.1;
            swordPlayerPos.y += 0.0;
            swordPlayerPos.z -= 0.07;
        }*/

        player.getWeapon().getEntity().move(new EntityPosition(playerpos.x+swordPlayerPos.x,playerpos.y+swordPlayerPos.y,playerpos.z+swordPlayerPos.z));
    }
}
