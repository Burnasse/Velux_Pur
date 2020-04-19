package com.mygdx.game.classesatrier.Entity;

public class CharacteristicMonster implements Characteristic{

    private int attackDamage;
    private int health;

    public CharacteristicMonster(int attackDamage, int health){
        this.attackDamage = attackDamage;
        this.health = health;
    }
}
