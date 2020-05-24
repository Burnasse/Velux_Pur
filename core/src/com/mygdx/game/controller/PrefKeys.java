package com.mygdx.game.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import java.util.HashMap;

/**
 * The Pref keys class store all keys used in the game or changed by the user.
 */
public class PrefKeys {

    /**
     * The keyMap HashMap for retrieve the KeyName and KeyCode of each key.
     * Useful for display keyBinding (use in ControlsMenu class).
     */
    private static final HashMap<String, Integer> keyMap = new HashMap<>();

    public static int UP_ARR = Input.Keys.UP;
    public static int DOWN_ARR = Input.Keys.DOWN;
    public static int LEFT_ARR = Input.Keys.LEFT;
    public static int RIGHT_ARR = Input.Keys.RIGHT;

    public static int Up = Input.Keys.Z;
    public static int Down = Input.Keys.S;
    public static int Left = Input.Keys.Q;
    public static int Right = Input.Keys.D;

    public static int Interact = Input.Keys.F;

    public static int C = Input.Keys.C;
    public static int LeftClick = Input.Buttons.LEFT;

    /**
     * Init keys for retrieve all keys changed by the user.
     * This method must be used on launch and when the key binding change.
     *
     * @param pref the pref
     */
    public static void initKeys(Preferences pref) {
        Up = pref.getInteger("Up", Up);
        Down = pref.getInteger("Down", Down);
        Left = pref.getInteger("Left", Left);
        Right = pref.getInteger("Right", Right);
        Interact = pref.getInteger("Interact", Interact);

        keyMap.put("Up", Up);
        keyMap.put("Down", Down);
        keyMap.put("Left", Left);
        keyMap.put("Right", Right);
        keyMap.put("Interact", Interact);
    }

    /**
     * Gets key map.
     *
     * @return the key map
     */
    public static HashMap<String, Integer> getKeyMap() {
        return keyMap;
    }


}
