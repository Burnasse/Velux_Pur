package com.mygdx.game.Entity;

/**
 * The type Characteristic player.
 */
public class CharacteristicPlayer implements Characteristic {

    private int attackDamage;
    private int health;

    /**
     * Instantiates a new Characteristic player.
     *
     * @param attackDamage the attack damage
     * @param health       the health
     */
    public CharacteristicPlayer(int attackDamage, int health) {
        this.attackDamage = attackDamage;
        this.health = health;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getHealth() {
        return health;
    }

    public void getsAttacked(float dammage){
        this.health -= dammage;
    }
}
