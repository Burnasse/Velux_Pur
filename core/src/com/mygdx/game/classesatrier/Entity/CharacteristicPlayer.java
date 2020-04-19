package com.mygdx.game.classesatrier.Entity;

import java.util.ArrayList;

public class CharacteristicPlayer implements Characteristic{

    private int attackDamage;
    private int health;
    private ArrayList<Item> inventory;

    public CharacteristicPlayer(int attackDamage, int health){
        this.attackDamage = attackDamage;
        this.health = health;
    }
}
