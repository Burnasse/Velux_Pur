package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Entity.EntityPlayer;

/**
 * The type Player controller.
 */
public class PlayerController implements InputProcessor, ControllerListener {

    private EntityPlayer player;
    private AnimationController animation;
    private Vector3 walkDirection = new Vector3();
    private boolean playerPov;
    private float speed = 0;

    /**
     * If the player is turned left.
     */
    private boolean lookLeft = true;

    /**
     * If the player is turned left.
     */
    private boolean lookRight = false;

    /**
     * If the player is turned left.
     */
    private boolean lookUp = false;

    /**
     * If the player is turned left.
     */
    private boolean lookDown = false;

    /**
     * If the player sprint
     */
    private boolean isSprint = false;


    /**
     * Instantiates a new Player controller.
     *
     * @param player the player
     */
    public PlayerController(EntityPlayer player,AnimationController animationController) {
        this.player = player;
        this.animation = animationController;
    }
    public boolean keyDown(int keycode) {

        speed = 0;

        if (Gdx.input.isKeyPressed(PrefKeys.LEFT_ARR) || Gdx.input.isKeyPressed(PrefKeys.Left)) {
            moveLeft(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT));
        }

        if (Gdx.input.isKeyPressed(PrefKeys.RIGHT_ARR) || Gdx.input.isKeyPressed(PrefKeys.Right)) {
            moveRight(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT));
        }

        if ((Gdx.input.isKeyPressed(PrefKeys.UP_ARR) || Gdx.input.isKeyPressed(PrefKeys.Up))) {
            moveUp(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT));
        }

        if ((Gdx.input.isKeyPressed(PrefKeys.DOWN_ARR) || Gdx.input.isKeyPressed(PrefKeys.Down))) {
            moveDown(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT));
        }

        setMovement(speed);

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (!player.getEntity().getController().onGround())
            return false;
        if (!Gdx.input.isKeyPressed(PrefKeys.LEFT_ARR) && !Gdx.input.isKeyPressed(PrefKeys.Left)
                && !Gdx.input.isKeyPressed(PrefKeys.RIGHT_ARR) && !Gdx.input.isKeyPressed(PrefKeys.Right)
                && !Gdx.input.isKeyPressed(PrefKeys.DOWN_ARR) && !Gdx.input.isKeyPressed(PrefKeys.Down)
                && !Gdx.input.isKeyPressed(PrefKeys.UP_ARR) && !Gdx.input.isKeyPressed(PrefKeys.Up)) {
            walkDirection.set(0, 0, 0);
            animation.animate("idle", -1, 1.0f, null, 0.2f);
            setMovement(0);
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        player.attack();
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == PrefKeys.LeftClick)
            System.out.println("AHAHAHAHAH");
        if (button == Input.Buttons.RIGHT)
            System.out.println("OUHOUHOUHOUH");
        return true;
    }
    private void moveLeft(boolean sprint) {
        if (!lookLeft)
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), 180);
        player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
        walkDirection.add(1, 0, 0);

        if (sprint || isSprint) {
            animation.animate("running", -1, 1.0f, null, 0.2f);
            speed = 3f;
        } else {
            animation.animate("walk", -1, 1.0f, null, 0.2f);
            speed = 1.5f;
        }

        if (lookRight)
            lookLeft = true;
        lookRight = false;
        player.getEntity().transform.rotate(new Vector3(0, 1, 0), 180);
        player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
        walkDirection.add(1, 0, 0);

        if (sprint || isSprint) {
            animation.animate("running", -1, 1.0f, null, 0.2f);
            speed = 3f;
        } else {
            animation.animate("walk", -1, 1.0f, null, 0.2f);
            speed = 1.5f;
        }

        if (lookUp)
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), 90);
        lookLeft = true;
        lookUp = false;
        player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
        walkDirection.add(1, 0, 0);

        if (sprint || isSprint) {
            animation.animate("running", -1, 1.0f, null, 0.2f);
            speed = 3f;
        } else {
            animation.animate("walk", -1, 1.0f, null, 0.2f);
            speed = 1.5f;
        }

        if (lookDown)
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), -90);
        lookLeft = true;
        lookDown= false;
        player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
        walkDirection.add(1, 0, 0);

        if (sprint || isSprint) {
            animation.animate("running", -1, 1.0f, null, 0.2f);
            speed = 3f;
        } else {
            animation.animate("walk", -1, 1.0f, null, 0.2f);
            speed = 1.5f;
        }

    }

    private void moveRight(boolean sprint) {
        if (lookLeft)
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), 180);
        lookLeft = false;
        lookRight = true;
        player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
        walkDirection.add(-1, 0, 0);

        if (sprint || isSprint) {
            animation.animate("running", -1, 1.0f, null, 0.2f);
            speed = 3f;
        } else {
            animation.animate("walk", -1, 1.0f, null, 0.2f);
            speed = 1.5f;
        }

        if (lookRight)
        lookLeft = false;
        lookRight = true;
        player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
        walkDirection.add(-1, 0, 0);

        if (sprint || isSprint) {
            animation.animate("running", -1, 1.0f, null, 0.2f);
            speed = 3f;
        } else {
            animation.animate("walk", -1, 1.0f, null, 0.2f);
            speed = 1.5f;
        }

        if (lookUp)
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), -90);
        lookLeft = false;
        lookUp = false;
        lookRight = true;
        player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
        walkDirection.add(-1, 0, 0);

        if (sprint || isSprint) {
            animation.animate("running", -1, 1.0f, null, 0.2f);
            speed = 3f;
        } else {
            animation.animate("walk", -1, 1.0f, null, 0.2f);
            speed = 1.5f;
        }

        if (lookDown)
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), 90);
        lookLeft = false;
        lookDown= false;
        lookRight = true;
        player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
        walkDirection.add(-1, 0, 0);

        if (sprint || isSprint) {
            animation.animate("running", -1, 1.0f, null, 0.2f);
            speed = 3f;
        } else {
            animation.animate("walk", -1, 1.0f, null, 0.2f);
            speed = 1.5f;
        }
    }


    private void moveUp(boolean sprint) {
        if (lookLeft)
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), -90);
        lookLeft = false;
        lookUp = true;
        player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
        walkDirection.add(0, 0, 1);

        if (sprint || isSprint) {
            animation.animate("running", -1, 1.0f, null, 0.2f);
            speed = 3f;
        } else {
            animation.animate("walk", -1, 1.0f, null, 0.2f);
            speed = 1.5f;
        }

        if (lookRight)
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), 90);
        lookLeft = false;
        lookRight = false;
        lookUp = true;
        player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
        walkDirection.add(1, 0, 1);

        if (sprint || isSprint) {
            animation.animate("running", -1, 1.0f, null, 0.2f);
            speed = 3f;
        } else {
            animation.animate("walk", -1, 1.0f, null, 0.2f);
            speed = 1.5f;
        }

        if (lookUp)
        lookLeft = false;
        lookUp = true;
        player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
        walkDirection.add(0, 0, 1);

        if (sprint || isSprint) {
            animation.animate("running", -1, 1.0f, null, 0.2f);
            speed = 3f;
        } else {
            animation.animate("walk", -1, 1.0f, null, 0.2f);
            speed = 1.5f;
        }

        if (lookDown)
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), 180);
        lookLeft = false;
        lookDown= false;
        lookUp = true;
        player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
        walkDirection.add(0, 0, 1);

        if (sprint || isSprint) {
            animation.animate("running", -1, 1.0f, null, 0.2f);
            speed = 3f;
        } else {
            animation.animate("walk", -1, 1.0f, null, 0.2f);
            speed = 1.5f;
        }
    }

    private void moveDown(boolean sprint) {
        if (lookLeft)
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), 90);
        lookLeft = false;
        lookDown= true;
        player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
        walkDirection.add(0, 0, -1);

        if (sprint || isSprint) {
            animation.animate("running", -1, 1.0f, null, 0.2f);
            speed = 3f;
        } else {
            animation.animate("walk", -1, 1.0f, null, 0.2f);
            speed = 1.5f;
        }

        if (lookRight)
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), -90);
        lookRight = false;
        lookDown=true;
        player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
        walkDirection.add(0, 0, -1);

        if (sprint || isSprint) {
            animation.animate("running", -1, 1.0f, null, 0.2f);
            speed = 3f;
        } else {
            animation.animate("walk", -1, 1.0f, null, 0.2f);
            speed = 1.5f;
        }

        if (lookUp)
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), -180);
        lookUp = false;
        lookDown = true;
        player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
        walkDirection.add(0, 0, -1);

        if (sprint || isSprint) {
            animation.animate("running", -1, 1.0f, null, 0.2f);
            speed = 3f;
        } else {
            animation.animate("walk", -1, 1.0f, null, 0.2f);
            speed = 1.5f;
        }

        if (lookDown)
        lookDown= true;
        player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
        walkDirection.add(0, 0, -1);

        if (sprint || isSprint) {
            animation.animate("running", -1, 1.0f, null, 0.2f);
            speed = 3f;
        } else {
            animation.animate("walk", -1, 1.0f, null, 0.2f);
            speed = 1.5f;
        }
    }

    private void dodge() {
        player.getEntity().getController().jump(new Vector3(3, 0, 0));
        animation.animate("dodge", 1, 1.0f, new AnimationController.AnimationListener() {
            @Override
            public void onEnd(AnimationController.AnimationDesc anim) {
                if (speed == 3)
                    animation.animate("running", -1, 1.0f, null, 0.2f);
                else if (speed == 1.5f)
                    animation.animate("walk", -1, 1.0f, null, 0.2f);
                else
                    animation.animate("idle", -1, 1.0f, null, 0.2f);
            }

            @Override
            public void onLoop(AnimationController.AnimationDesc animation) {
            }
        }, 0.2f);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void connected(Controller controller) {

    }

    @Override
    public void disconnected(Controller controller) {

    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {

        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }

    /**
     * Set movement
     */
    private void setMovement(float speed) {
        walkDirection.scl(speed * Gdx.graphics.getDeltaTime());
        player.getEntity().getController().setWalkDirection(walkDirection);
    }

}
