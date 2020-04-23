package com.mygdx.game.classesatrier.Entity;

import java.util.ArrayList;

/**
 * The type Characteristic player.
 */
public class CharacteristicPlayer implements Characteristic{

    private int attackDamage;
    private int health;
    private ArrayList<Item> inventory;

    /**
     * Instantiates a new Characteristic player.
     *
     * @param attackDamage the attack damage
     * @param health       the health
     */
    public CharacteristicPlayer(int attackDamage, int health){
        this.attackDamage = attackDamage;
        this.health = health;
    }
}
