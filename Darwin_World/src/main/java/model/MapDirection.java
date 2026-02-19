package model;

import java.util.Random;

public enum MapDirection {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;

    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2d(0, 1);
            case NORTH_EAST -> new Vector2d(1, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTH_EAST -> new Vector2d(1, -1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTH_WEST -> new Vector2d(-1, -1);
            case WEST -> new Vector2d(-1, 0);
            case NORTH_WEST -> new Vector2d(-1, 1);
        };
    }

    public MapDirection rotate(int rotation) {
        MapDirection[] values = values();
        int currentIndex = this.ordinal();
        int newIndex = (currentIndex + rotation) % values.length;
        return values[newIndex];
    }

    public MapDirection opposite(){
        return switch (this) {
            case NORTH      -> SOUTH;
            case NORTH_EAST -> SOUTH_WEST;
            case EAST       -> WEST;
            case SOUTH_EAST -> NORTH_WEST;
            case SOUTH      -> NORTH;
            case SOUTH_WEST -> NORTH_EAST;
            case WEST       -> EAST;
            case NORTH_WEST -> SOUTH_EAST;
        };
    }

    public static MapDirection getRandom() {
        return values()[new Random().nextInt(values().length)];
    }
}