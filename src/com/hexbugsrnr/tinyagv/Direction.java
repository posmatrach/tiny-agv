package com.hexbugsrnr.tinyagv;

import java.util.Random;

/**
 * Created by null on 9/06/16.
 */
public enum Direction
{
    NORTH(0, 1),
    SOUTH(0, -1),
    WEST(-1, 0),
    EAST(1, 0),
    NORTHEAST(1, 1),
    NORTHWEST(-1, 1),
    SOUTHEAST(1, -1),
    SOUTHWEST(-1, -1);

    static final Random RANDOM = new Random();

    static public Direction getRandomDirection()
    {
        int i = RANDOM.nextInt(8);
        switch (i)
        {
            case 0: return Direction.NORTH;
            case 1: return Direction.NORTHEAST;
            case 2: return Direction.EAST;
            case 3: return Direction.SOUTHEAST;
            case 4: return Direction.SOUTH;
            case 5: return Direction.SOUTHWEST;
            case 6: return Direction.WEST;
            case 7: return Direction.NORTHWEST;
            default: return Direction.NORTH;
        }
    }

    protected final int dx;

    protected final int dy;

    Direction(int dx, int dy)
    {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx()
    {
        return dx;
    }

    public int getDy()
    {
        return dy;
    }
}
