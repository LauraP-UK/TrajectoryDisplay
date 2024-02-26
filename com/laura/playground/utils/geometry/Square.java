package com.laura.playground.utils.geometry;

import com.laura.playground.utils.LocationUtils;
import com.laura.playground.utils.Randf;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class Square {
    public static List<Location> getCube(Location location, int pointPerEdge) {
        return getCube(location, location, pointPerEdge);
    }

    public static List<Vector> getCube(Vector location, int pointPerEdge) {
        return getCube(location, location, pointPerEdge);
    }

    public static List<Location> getCube(Location centre, double radius, int pointsPerEdge) {
        return getCube(centre, radius, radius, radius, pointsPerEdge);
    }

    public static List<Vector> getCube(Vector centre, double radius, int pointsPerEdge) {
        return getCube(centre, radius, radius, radius, pointsPerEdge);
    }

    public static List<Location> getCube(Location centre, double x, double y, double z, int pointsPerEdge) {
        Vector offset = new Vector(x, y, z);
        return getCube(centre.clone().subtract(offset.clone().multiply(0.5D)), centre.clone().add(offset.clone().multiply(0.5D)), pointsPerEdge);
    }

    public static List<Vector> getCube(Vector centre, double x, double y, double z, int pointsPerEdge) {
        Vector offset = new Vector(x, y, z);
        return getCube(centre.clone().subtract(offset.clone().multiply(0.5D)), centre.clone().add(offset.clone().multiply(0.5D)), pointsPerEdge);
    }

    public static List<Location> getCube(Location min, Location max, int pointsPerEdge) {
        Location minCalc = LocationUtils.min(min, max);
        Location maxCalc = LocationUtils.max(min, max).add(1, 1, 1);
        World world = min.getWorld();
        Location upperNW = new Location(world, minCalc.getX(), maxCalc.getY(), maxCalc.getZ());
        Location upperNE = maxCalc.clone();
        Location upperSW = new Location(world, minCalc.getX(), maxCalc.getY(), minCalc.getZ());
        Location upperSE = new Location(world, maxCalc.getX(), maxCalc.getY(), minCalc.getZ());
        Location lowerNW = new Location(world, minCalc.getX(), minCalc.getY(), maxCalc.getZ());
        Location lowerNE = new Location(world, maxCalc.getX(), minCalc.getY(), maxCalc.getZ());
        Location lowerSW = minCalc.clone();
        Location lowerSE = new Location(world, maxCalc.getX(), minCalc.getY(), minCalc.getZ());
        return getCube(upperNW, upperNE, upperSW, upperSE, lowerNW, lowerNE, lowerSW, lowerSE, pointsPerEdge);
    }

    public static List<Vector> getCube(Vector min, Vector max, int pointsPerEdge) {
        Vector minCalc = Vector.getMinimum(min, max);
        Vector maxCalc = Vector.getMaximum(min, max).add(1, 1, 1);
        Vector upperNW = new Vector(minCalc.getX(), maxCalc.getY(), maxCalc.getZ());
        Vector upperNE = maxCalc.clone();
        Vector upperSW = new Vector(minCalc.getX(), maxCalc.getY(), minCalc.getZ());
        Vector upperSE = new Vector(maxCalc.getX(), maxCalc.getY(), minCalc.getZ());
        Vector lowerNW = new Vector(minCalc.getX(), minCalc.getY(), maxCalc.getZ());
        Vector lowerNE = new Vector(maxCalc.getX(), minCalc.getY(), maxCalc.getZ());
        Vector lowerSW = minCalc.clone();
        Vector lowerSE = new Vector(maxCalc.getX(), minCalc.getY(), minCalc.getZ());
        return getCube(upperNW, upperNE, upperSW, upperSE, lowerNW, lowerNE, lowerSW, lowerSE, pointsPerEdge);
    }

    public static List<Location> getCube(Location upperNW, Location upperNE, Location upperSW, Location upperSE, Location lowerNW, Location lowerNE, Location lowerSW, Location lowerSE, int pointsPerEdge) {
        return getCube(upperNW.toVector(), upperNE.toVector(), upperSW.toVector(), upperSE.toVector(), lowerNW.toVector(), lowerNE.toVector(), lowerSW.toVector(), lowerSE.toVector(), pointsPerEdge)
                .stream()
                .map((point) -> point.toLocation(upperNW.getWorld()))
                .collect(Collectors.toList());
    }

    public static List<Vector> getCube(Vector upperNW, Vector upperNE, Vector upperSW, Vector upperSE, Vector lowerNW, Vector lowerNE, Vector lowerSW, Vector lowerSE, int pointsPerEdge) {
        List<Vector> returnSet = new ArrayList<Vector>();
        returnSet.addAll(Line.pointsAlongALine(upperNW, lowerNW, pointsPerEdge));
        returnSet.addAll(Line.pointsAlongALine(upperNE, lowerNE, pointsPerEdge));
        returnSet.addAll(Line.pointsAlongALine(upperSE, lowerSE, pointsPerEdge));
        returnSet.addAll(Line.pointsAlongALine(upperSW, lowerSW, pointsPerEdge));
        returnSet.addAll(Line.pointsAlongALine(upperNW, upperNE, pointsPerEdge));
        returnSet.addAll(Line.pointsAlongALine(upperSW, upperSE, pointsPerEdge));
        returnSet.addAll(Line.pointsAlongALine(lowerNW, lowerNE, pointsPerEdge));
        returnSet.addAll(Line.pointsAlongALine(lowerSW, lowerSE, pointsPerEdge));
        returnSet.addAll(Line.pointsAlongALine(upperNW, upperSW, pointsPerEdge));
        returnSet.addAll(Line.pointsAlongALine(upperNE, upperSE, pointsPerEdge));
        returnSet.addAll(Line.pointsAlongALine(lowerNW, lowerSW, pointsPerEdge));
        returnSet.addAll(Line.pointsAlongALine(lowerNE, lowerSE, pointsPerEdge));

        for (int i = 0; i < 2; i++) { // Remove duplicate corners
            returnSet.remove(upperNE);
            returnSet.remove(upperNW);
            returnSet.remove(upperSE);
            returnSet.remove(upperSW);
            returnSet.remove(lowerNE);
            returnSet.remove(lowerNW);
            returnSet.remove(lowerSE);
            returnSet.remove(lowerSW);
        }

        return returnSet;
    }

    public static List<Location> getSquare(Location min, Location max, int pointsPerEdge) {
        return getSquare(min.toVector(), max.toVector(), pointsPerEdge)
                .stream()
                .map((point) -> point.toLocation(min.getWorld()))
                .collect(Collectors.toList());
    }

    public static List<Vector> getSquare(Vector min, Vector max, int pointsPerEdge) {
        Vector minCalc = Vector.getMinimum(min, max);
        Vector maxCalc = Vector.getMaximum(min, max);
        Vector maxLeft = new Vector(0, 0, 0);
        Vector maxRight = maxCalc.clone();
        Vector minRight = new Vector(0, 0, 0);
        Vector minLeft = minCalc.clone();
        double z;
        if (minCalc.getX() == maxCalc.getX()) {
            z = minCalc.getX();
            maxLeft.setX(z);
            maxLeft.setY(maxCalc.getY());
            maxLeft.setZ(minCalc.getZ());
            minRight.setX(z);
            minRight.setY(minCalc.getY());
            minRight.setZ(maxCalc.getZ());
        } else if (minCalc.getY() == maxCalc.getY()) {
            z = minCalc.getY();
            maxLeft.setX(minCalc.getX());
            maxLeft.setY(z);
            maxLeft.setZ(minCalc.getZ());
            minRight.setX(maxCalc.getX());
            minRight.setY(z);
            minRight.setZ(maxCalc.getZ());
        } else {
            if (minCalc.getZ() != maxCalc.getZ()) {
                Bukkit.getLogger().warning("[WARN] Geometry.getSquare() : None of the axis of the given vectors matched; " + min + " and " + max);
                return new ArrayList<Vector>();
            }

            z = minCalc.getZ();
            maxLeft.setX(minCalc.getX());
            maxLeft.setY(maxCalc.getY());
            maxLeft.setZ(z);
            minRight.setX(maxCalc.getX());
            minRight.setY(minCalc.getY());
            minRight.setZ(z);
        }

        return getSquare(maxLeft, maxRight, minLeft, minRight, pointsPerEdge);
    }

    public static List<Location> getSquare(Location maxLeft, Location maxRight, Location minLeft, Location minRight, int pointsPerEdge) {
        List<Location> list = new ArrayList<Location>();
        list.addAll(Line.pointsAlongALine(maxLeft, maxRight, pointsPerEdge));
        list.addAll(Line.pointsAlongALine(maxRight, minRight, pointsPerEdge));
        list.addAll(Line.pointsAlongALine(minRight, minLeft, pointsPerEdge));
        list.addAll(Line.pointsAlongALine(minLeft, maxLeft, pointsPerEdge));
        return list;
    }

    public static List<Vector> getSquare(Vector maxLeft, Vector maxRight, Vector minLeft, Vector minRight, int pointsPerEdge) {
        List<Vector> list = new ArrayList<Location>();
        list.addAll(Line.pointsAlongALine(maxLeft, maxRight, pointsPerEdge));
        list.addAll(Line.pointsAlongALine(maxRight, minRight, pointsPerEdge));
        list.addAll(Line.pointsAlongALine(minRight, minLeft, pointsPerEdge));
        list.addAll(Line.pointsAlongALine(minLeft, maxLeft, pointsPerEdge));
        return list;
    }

    public static List<Location> randomPointsInCube(Location min, Location max, int points) {
        return IntStream.range(0, points).mapToObj((i) -> randomPointInCube(min, max)).collect(Collectors.toList());
    }

    public static List<Vector> randomPointsInCube(Vector min, Vector max, int points) {
        return IntStream.range(0, points).mapToObj((i) -> randomPointInCube(min, max)).collect(Collectors.toList());
    }

    public static Location randomPointInCube(Location min, Location max) {
        return new Location(min.getWorld(), Randf.random(min.getX(), max.getX()), Randf.random(min.getY(), max.getY()), Randf.random(min.getZ(), max.getZ()));
    }

    public static Vector randomPointInCube(Vector min, Vector max) {
        return new Vector(Randf.random(min.getX(), max.getX()), Randf.random(min.getY(), max.getY()), Randf.random(min.getZ(), max.getZ()));
    }

    public static List<Location> randomPointsOnBlockSide(Location location, Direction side, int points) {
        return randomPointsOnBlockSide(location, side, points, 1.0D);
    }

    public static List<Vector> randomPointsOnBlockSide(Vector location, Direction side, int points) {
        return randomPointsOnBlockSide(location, side, points, 1.0D);
    }

    public static List<Location> randomPointsOnBlockSide(Location location, Direction side, int points, double height) {
        Location max = location.clone();
        switch (side) {
            case UP:
                max = max.clone().add(new Vector(1.0D, height, 1.0D));
                location = location.clone().add(new Vector(0.0D, height, 0.0D));
                break;
            case DOWN:
                max = max.clone().add(new Vector(1, 0, 1));
                break;
            case NORTH:
                max = max.clone().add(new Vector(1.0D, height, 0.0D));
                break;
            case EAST:
                max = max.clone().add(new Vector(1.0D, height, 1.0D));
                location = location.clone().add(new Vector(1, 0, 0));
                break;
            case SOUTH:
                max = max.clone().add(new Vector(1.0D, height, 1.0D));
                location = location.clone().add(new Vector(0, 0, 1));
                break;
            case WEST:
                max = max.clone().add(new Vector(0.0D, height, 1.0D));
                break;
            default:
                return null;
        }

        return randomPointsInCube(location, max, points);
    }

    public static List<Vector> randomPointsOnBlockSide(Vector location, Direction side, int points, double height) {
        Vector max = location.clone();
        switch (side) {
            case UP:
                max = max.clone().add(new Vector(1.0D, height, 1.0D));
                location = location.clone().add(new Vector(0.0D, height, 0.0D));
                break;
            case DOWN:
                max = max.clone().add(new Vector(1, 0, 1));
                break;
            case NORTH:
                max = max.clone().add(new Vector(1.0D, height, 0.0D));
                break;
            case EAST:
                max = max.clone().add(new Vector(1.0D, height, 1.0D));
                location = location.clone().add(new Vector(1, 0, 0));
                break;
            case SOUTH:
                max = max.clone().add(new Vector(1.0D, height, 1.0D));
                location = location.clone().add(new Vector(0, 0, 1));
                break;
            case WEST:
                max = max.clone().add(new Vector(0.0D, height, 1.0D));
                break;
            default:
                return null;
        }

        return randomPointsInCube(location, max, points);
    }
}
