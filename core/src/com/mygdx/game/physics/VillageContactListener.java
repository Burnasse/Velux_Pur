package com.mygdx.game.physics;

import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.mygdx.game.controller.VillageController;

/**
 * The type Village contact listener is called when there is a collision.
 */
public class VillageContactListener extends ContactListener {

    private VillageController controller;

    public VillageContactListener(VillageController controller) {
        this.controller = controller;
    }

    @Override
    public boolean onContactAdded(int userValue0, int partId0, int index0, boolean match0, int userValue1, int partId1,
                                  int index1, boolean match1) {

        if (match1) {
            if (controller.waitTrigger && userValue1 != controller.userValue)
                controller.notifyTrigger();
            if (userValue0 == 1)
                controller.setInteractTrigger("Exit");
            if (userValue0 == 2)
                controller.setInteractTrigger("Trader");
            if (userValue0 == 3)
                System.out.println();
            if (userValue0 == 4 && !controller.waitTrigger)
                controller.setCanChangeLayout(userValue0, true);
            if (userValue0 == 5 && !controller.waitTrigger)
                controller.setCanChangeLayout(userValue0, false);
            if (userValue0 == 6 && !controller.waitTrigger)
                controller.setCanChangeLayout(userValue0, true);
            if (userValue0 == 7 && !controller.waitTrigger)
                controller.setCanChangeLayout(userValue0, false);
        }
        return true;
    }

    @Override
    public void onContactEnded(int userValue0, boolean match0, int userValue1, boolean match1) {
        if (match1) {
            if (userValue0 == 2)
                controller.setInteractTrigger("");
            if (userValue0 == 3)
                controller.setInteractTrigger("");
            if (userValue0 == 4)
                controller.canChangeLayout = false;
            if (userValue0 == 5)
                controller.canChangeLayout = false;
            if (userValue0 == 6)
                controller.canChangeLayout = false;
            if (userValue0 == 7)
                controller.canChangeLayout = false;
        }
    }
}
