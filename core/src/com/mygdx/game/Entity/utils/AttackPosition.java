package com.mygdx.game.Entity.utils;

public class AttackPosition {
    public static enum AttackDirection{
        NORTH,
        SOUTH,
        EAST,
        WEST,
        NW,
        NE,
        SE,
        SW
    }

    public static AttackDirection getAttackDirection(float angleAttackFromPlayer){
        if (angleAttackFromPlayer <22.5 &&  angleAttackFromPlayer > -22.5)
            return AttackDirection.NORTH;
        if (angleAttackFromPlayer <-22.5 &&  angleAttackFromPlayer > -67.5)
            return AttackDirection.NE;
        if (angleAttackFromPlayer <-67.5 &&  angleAttackFromPlayer > -112.5)
            return AttackDirection.EAST;
        if (angleAttackFromPlayer <-112.5 &&  angleAttackFromPlayer > -157.5)
            return AttackDirection.SE;
        if (angleAttackFromPlayer > 157.5 || (angleAttackFromPlayer > -180 && angleAttackFromPlayer < -157.5))
            return AttackDirection.SOUTH;
        if (angleAttackFromPlayer > 112.5 &&  angleAttackFromPlayer < 157.5)
            return AttackDirection.SW;
        if (angleAttackFromPlayer > 67.5 &&  angleAttackFromPlayer < 112.5)
            return AttackDirection.WEST;
        if (angleAttackFromPlayer > 22.5 &&  angleAttackFromPlayer < 67.5)
            return AttackDirection.NW;
        return AttackDirection.NORTH;
    }
}
