package com.mygdx.game.classesatrier.Entity;

/**
 * The type Characteristic monster.
 */
public class CharacteristicMonster implements Characteristic{

    private int attackDamage;
    private int health;

    /**
     * Instantiates a new Characteristic monster.
     *
     * @param attackDamage the attack damage
     * @param health       the health
     */
    public CharacteristicMonster(int attackDamage, int health){
        this.attackDamage = attackDamage;
        this.health = health;
    }
}
