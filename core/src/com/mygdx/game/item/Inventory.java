package com.mygdx.game.item;

import java.util.HashSet;

public class Inventory {

    private int numberOfPotionsAvailable;

    private HashSet<Item> items;

    public Inventory(){
        this.items = new HashSet<>();
    }

    public void usePotion(){
        numberOfPotionsAvailable--;
    }

    public void getNewPotion(){
        numberOfPotionsAvailable++;
    }

    public void addItemInInventory(Item item){
        items.add(item);
    }

}
