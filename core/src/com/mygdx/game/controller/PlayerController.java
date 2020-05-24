package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Entity.EntityPlayer;

/**
 * The type Player controller.
 */
public class PlayerController implements InputProcessor, ControllerListener {

    private EntityPlayer player;
    private Vector3 walkDirection = new Vector3();
    private boolean playerPov;
    private PerspectiveCamera camera;

    /**
     * Instantiates a new Player controller.
     *
     * @param player the player
     */
    public PlayerController(EntityPlayer player, PerspectiveCamera camera) {
        this.player = player;
        this.camera = camera;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (Gdx.input.isKeyPressed(PrefKeys.LEFT_ARR))
            walkDirection.add(1, 0, 0);
        if (Gdx.input.isKeyPressed(PrefKeys.RIGHT_ARR))
            walkDirection.add(-1, 0, 0);
        if (Gdx.input.isKeyPressed(PrefKeys.UP_ARR))
            walkDirection.add(0, 0, 1);
        if (Gdx.input.isKeyPressed(PrefKeys.DOWN_ARR))
            walkDirection.add(0, 0, -1);
        if (Gdx.input.isKeyPressed(PrefKeys.C))
            System.out.println(Gdx.input.getX() + " " + Gdx.input.getY());

        setMovement();

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (!Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            walkDirection.set(0, 0, 0);
            setMovement();
        }

        return true;
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

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {

        Vector2 player2DPos = new Vector2(camera.project(player.getPosition()).x, camera.project(player.getPosition()).y);

        Vector2 cursor2DPos = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);

        float newOrientation = (float) Math.atan2(-(cursor2DPos.x - player2DPos.x), cursor2DPos.y - player2DPos.y) * MathUtils.radiansToDegrees;

        player.getEntity().transform.set(player.getPosition(), new Quaternion(new Vector3(0, 1, 0), newOrientation));

        player.getEntity().getBody().setWorldTransform(player.getEntity().transform);

        return true;
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
    private void setMovement() {
        walkDirection.scl(4f * Gdx.graphics.getDeltaTime());
        player.getEntity().getController().setWalkDirection(walkDirection);

    }

}
