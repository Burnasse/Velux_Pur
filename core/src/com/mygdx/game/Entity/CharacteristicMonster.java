package com.mygdx.game.Entity;

/**
 * The type Characteristic monster.
 */
public class CharacteristicMonster implements Characteristic {

    private int attackDamage;
    private int health;

    /**
     * Instantiates a new Characteristic monster.
     *
     * @param attackDamage the attack damage
     * @param health       the health
     */
    public CharacteristicMonster(int attackDamage, int health) {
        this.attackDamage = attackDamage;
        this.health = health;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

}
