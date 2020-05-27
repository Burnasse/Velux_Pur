package com.mygdx.game.FloorGeneration;

import com.mygdx.game.Entity.CharacteristicMonster;

public class ComputingFloorDifficulty {

    public int sizeOfFloor = 5;
    public int numberOfRooms = 1;
    public int minRoomSize = 3;
    public int maxRoomSize = 6;
    public int baseAttackDammages = 2;
    public int baselife = 5;
    public CharacteristicMonster characteristicMonster;

    public ComputingFloorDifficulty(int currentFloor) {
        if(currentFloor%5 == 0){
            this.characteristicMonster = new CharacteristicMonster(baseAttackDammages * currentFloor*5, baselife * currentFloor*5);
            return;
        }
        this.characteristicMonster = new CharacteristicMonster(baseAttackDammages * currentFloor, baselife * currentFloor);
        this.numberOfRooms = currentFloor;
        this.sizeOfFloor = currentFloor * 5;
    }
}
