package com.laura.playground.utils.geometry;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public enum Direction {
    SELF(0, 0, 0),
    NORTH(0, 0, -1),
    EAST(1, 0, 0),
    SOUTH(0, 0, 1),
    WEST(-1, 0, 0),
    UP(0, 1, 0),
    DOWN(0, -1, 0),
    NORTH_WEST(NORTH, WEST),
    NORTH_EAST(NORTH, EAST),
    SOUTH_WEST(SOUTH, WEST),
    SOUTH_EAST(SOUTH, EAST),
    NORTH_UP(NORTH, UP),
    NORTH_DOWN(NORTH, DOWN),
    EAST_UP(EAST, UP),
    EAST_DOWN(EAST, DOWN),
    SOUTH_UP(SOUTH, UP),
    SOUTH_DOWN(SOUTH, DOWN),
    WEST_UP(WEST, UP),
    WEST_DOWN(WEST, DOWN),
    NORTH_WEST_UP(NORTH_WEST, UP),
    NORTH_WEST_DOWN(NORTH_WEST, DOWN),
    NORTH_EAST_UP(NORTH_EAST, UP),
    NORTH_EAST_DOWN(NORTH_EAST, DOWN),
    SOUTH_WEST_UP(SOUTH_WEST, UP),
    SOUTH_WEST_DOWN(SOUTH_WEST, DOWN),
    SOUTH_EAST_UP(SOUTH_EAST, UP),
    SOUTH_EAST_DOWN(SOUTH_EAST, DOWN),
    WEST_NORTH_WEST(WEST, NORTH_WEST),
    NORTH_NORTH_WEST(NORTH, NORTH_WEST),
    NORTH_NORTH_EAST(NORTH, NORTH_EAST),
    EAST_NORTH_EAST(EAST, NORTH_EAST),
    EAST_SOUTH_EAST(EAST, SOUTH_EAST),
    SOUTH_SOUTH_EAST(SOUTH, SOUTH_EAST),
    SOUTH_SOUTH_WEST(SOUTH, SOUTH_WEST),
    WEST_SOUTH_WEST(WEST, SOUTH_WEST);

    private final int x;
    private final int y;
    private final int z;
    private final BlockFace blockFace;
    private static final Direction[] BLOCK_FACE_LOOKUP;
    public static final Direction[] CARDINALS = new Direction[]{NORTH, EAST, SOUTH, WEST};
    public static final Direction[] CARDINALS_AND_DIAGS = new Direction[]{NORTH, EAST, SOUTH, WEST, NORTH_WEST, NORTH_EAST, SOUTH_WEST, SOUTH_EAST};
    public static final Direction[] ADJACENTS = new Direction[]{NORTH, EAST, SOUTH, WEST, UP, DOWN};

    static {
        BlockFace[] values = BlockFace.values();
        Direction[] blockFaceLookup = new Direction[values.length];

        for (int i = 0; i < values.length; i++) {
            try {
                blockFaceLookup[i] = valueOf(values[i].name());
            } catch (IllegalArgumentException e) {
                blockFaceLookup[i] = null;
            }
        }

        BLOCK_FACE_LOOKUP = blockFaceLookup;
    }

    private Direction(Direction first, Direction offset) {
        this(first.getX() + offset.getX(), first.getY() + offset.getY(), first.getZ() + offset.getZ());
    }

    private Direction(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;

        BlockFace face;
        try {
            face = BlockFace.valueOf(this.name());
        } catch (IllegalArgumentException e) {
            face = null;
        }

        this.blockFace = face;
    }

    public Location getRelative(Location location) {
        return getRelative(location, this);
    }

    public Vector getRelative(Vector location) {
        return getRelative(location, this);
    }

    public Block getRelative(Block block) {
        return getRelative(block, this);
    }

    public Location toLocation(World world) {
        return this.toVector().toLocation(world);
    }

    public boolean isAdjacent() {
        for (Direction adjacent : ADJACENTS) {
            if (this.equals(adjacent)) {
                return true;
            }
        }

        return false;
    }

    public Direction getOpposite() {
        return getOpposite(this);
    }

    public Vector toVector() {
        return toVector(this);
    }

    public String asString() {
        return this.toVector().toString();
    }

    public static Vector toVector(Direction direction) {
        return new Vector(direction.getX(), direction.getY(), direction.getZ());
    }

    public static BlockFace getBlockFace(Direction direction) {
        return direction.getBlockFace();
    }

    public static Direction getDirection(BlockFace blockFace) {
        return BLOCK_FACE_LOOKUP[blockFace.ordinal()];
    }

    public static Location getRelative(Location location, Direction direction) {
        return getRelative(location.toVector(), direction).toLocation(location.getWorld());
    }

    public static Block getRelative(Block block, Direction direction) {
        return getRelative(block.getLocation(), direction).getBlock();
    }

    public static Vector getRelative(Vector location, Direction direction) {
        return location.clone().add(direction.toVector());
    }

    public static Direction getRelative(Block block1, Block block2) {
        return getRelative(block1.getLocation(), block2.getLocation());
    }

    public static Direction getRelative(Location location1, Location location2) {
        return getRelative(location1.toVector(), location2.toVector());
    }

    public static Direction getRelative(Vector location1, Vector location2) {
        if (location1 != null && location2 != null) {
            Direction[] directions = values();

            for (Direction direction : directions) {
                if (location1.clone().add(direction.toVector()).equals(location2)) {
                    return direction;
                }
            }

            return null;
        } else {
            return null;
        }
    }

    public static boolean isAdjacent(Location location1, Location location2) {
        if (location1 != null && location2 != null) {
            for (Direction adjacent : ADJACENTS) {
                if (location1.clone().add(adjacent.toVector()).equals(location2)) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public static Direction getOpposite(Direction direction) {
        if (direction == null) {
            return null;
        } else {
            switch (direction) {
                case NORTH:
                    return SOUTH;
                case EAST:
                    return WEST;
                case SOUTH:
                    return NORTH;
                case WEST:
                    return EAST;
                case NORTH_WEST:
                    return SOUTH_EAST;
                case NORTH_EAST:
                    return SOUTH_WEST;
                case SOUTH_WEST:
                    return NORTH_EAST;
                case SOUTH_EAST:
                    return NORTH_WEST;
                case UP:
                    return DOWN;
                case DOWN:
                    return UP;
                case NORTH_UP:
                    return SOUTH_DOWN;
                case NORTH_DOWN:
                    return SOUTH_UP;
                case EAST_UP:
                    return WEST_DOWN;
                case EAST_DOWN:
                    return WEST_UP;
                case SOUTH_UP:
                    return NORTH_DOWN;
                case SOUTH_DOWN:
                    return NORTH_UP;
                case WEST_UP:
                    return EAST_DOWN;
                case WEST_DOWN:
                    return EAST_UP;
                case NORTH_WEST_UP:
                    return SOUTH_EAST_DOWN;
                case NORTH_WEST_DOWN:
                    return SOUTH_EAST_UP;
                case NORTH_EAST_UP:
                    return SOUTH_WEST_DOWN;
                case NORTH_EAST_DOWN:
                    return SOUTH_WEST_UP;
                case SOUTH_WEST_UP:
                    return NORTH_EAST_DOWN;
                case SOUTH_WEST_DOWN:
                    return NORTH_EAST_UP;
                case SOUTH_EAST_UP:
                    return NORTH_WEST_DOWN;
                case SOUTH_EAST_DOWN:
                    return NORTH_WEST_UP;
                case WEST_NORTH_WEST:
                    return EAST_SOUTH_EAST;
                case NORTH_NORTH_WEST:
                    return SOUTH_SOUTH_EAST;
                case NORTH_NORTH_EAST:
                    return SOUTH_SOUTH_WEST;
                case EAST_NORTH_EAST:
                    return WEST_SOUTH_WEST;
                case EAST_SOUTH_EAST:
                    return WEST_NORTH_WEST;
                case SOUTH_SOUTH_EAST:
                    return NORTH_NORTH_WEST;
                case SOUTH_SOUTH_WEST:
                    return NORTH_NORTH_EAST;
                case WEST_SOUTH_WEST:
                    return EAST_NORTH_EAST;
                case SELF:
                default:
                    return SELF;
            }
        }
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public BlockFace getBlockFace() {
        return this.blockFace;
    }
}
