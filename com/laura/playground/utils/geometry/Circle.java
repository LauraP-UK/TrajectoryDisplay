package com.laura.playground.utils.geometry;

import com.laura.playground.utils.Ease;
import com.laura.playground.utils.Mathsf;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Circle {
    public static List<Vector> circlePointsAxisAligned(double r, int points, Axis axis) {
        return circlePointsAxisAligned(r, points, axis, Ease.getDefault());
    }

    public static List<Vector> circlePointsAxisAligned(double r, int points, Axis axis, Ease ease) {
        return circlePointsAxisAligned(r, points, axis, ease, 0.0D);
    }

    public static List<Vector> circlePointsAxisAligned(double r, int points, Axis axis, Ease ease, double power) {
        return circlePointsAxisAligned(r, points, axis, new Vector(0.0D, 0.0D, 0.0D), ease, power);
    }

    public static List<Vector> circlePointsAxisAligned(double r, int points, Axis axis, double offset) {
        return circlePointsAxisAligned(r, points, axis, offset, Ease.getDefault());
    }

    public static List<Vector> circlePointsAxisAligned(double r, int points, Axis axis, double offset, Ease ease) {
        return circlePointsAxisAligned(r, points, axis, offset, ease, -1.0D);
    }

    public static List<Vector> circlePointsAxisAligned(double r, int points, Axis axis, double offset, Ease ease, double power) {
        return circlePointsAxisAligned(r, points, axis, new Vector(0.0D, 0.0D, 0.0D), offset, ease, power);
    }

    public static List<Location> circlePointsAxisAligned(double r, int points, Axis axis, Location origin) {
        return circlePointsAxisAligned(r, points, axis, origin, Ease.getDefault());
    }

    public static List<Location> circlePointsAxisAligned(double r, int points, Axis axis, Location origin, Ease ease) {
        return circlePointsAxisAligned(r, points, axis, origin, ease, -1.0D);
    }

    public static List<Location> circlePointsAxisAligned(double r, int points, Axis axis, Location origin, Ease ease, double power) {
        return circlePointsAxisAligned(r, points, axis, origin, 0.0D, ease, power);
    }

    public static List<Location> circlePointsAxisAligned(double r, int points, Axis axis, Location origin, double offset) {
        return circlePointsAxisAligned(r, points, axis, origin, offset, Ease.getDefault());
    }

    public static List<Location> circlePointsAxisAligned(double r, int points, Axis axis, Location origin, double offset, Ease ease) {
        return circlePointsAxisAligned(r, points, axis, origin, offset, ease, 0.0D);
    }

    public static List<Location> circlePointsAxisAligned(double r, int points, Axis axis, Location origin, double offset, Ease ease, double power) {
        return circlePointsAxisAligned(r, points, axis, origin.toVector(), offset, ease, power)
                .stream()
                .map((point) -> point.toLocation(origin.getWorld()))
                .collect(Collectors.toList());
    }

    public static List<Vector> circlePointsAxisAligned(double r, int points, Axis axis, Vector origin) {
        return circlePointsAxisAligned(r, points, axis, origin, Ease.getDefault());
    }

    public static List<Vector> circlePointsAxisAligned(double r, int points, Axis axis, Vector origin, Ease ease) {
        return circlePointsAxisAligned(r, points, axis, origin, ease, -1.0D);
    }

    public static List<Vector> circlePointsAxisAligned(double r, int points, Axis axis, Vector origin, Ease ease, double power) {
        return circlePointsAxisAligned(r, points, axis, origin, 0.0D, ease, power);
    }

    public static List<Vector> circlePointsAxisAligned(double r, int points, Axis axis, Vector origin, double offset, Ease ease, double power) {
        List<Vector> returnList = new LinkedList<Vector>();

        for (int i = 0; i < points; ++i) {
            double angle = power <= 0.0D ?
                    ease.ease(0.0D, 360.0D, Mathsf.iLerp(0, points, i)) :
                    ease.customEase(0.0D, 360.0D, Mathsf.iLerp(0, points, i), power);
            returnList.add(parametricEquationCircleAA(origin, r, angle, offset, axis));
        }

        return returnList;
    }

    public static Vector parametricEquationCircleAA(double radius, double angle, double offset, Axis axis) {
        return parametricEquationCircleAA(new Vector(0.0D, 0.0D, 0.0D), radius, angle, offset, axis);
    }

    public static Vector parametricEquationCircleAA(Vector origin, double radius, double angle, double offset, Axis axis) {
        double x = origin.getX() + cosAngle(radius, angle, offset);
        double y = axis == Axis.X ? origin.getY() + cosAngle(radius, angle, offset) : (axis == Axis.Z ? origin.getY() + sinAngle(radius, angle, offset) : 0.0D);
        double z = origin.getZ() + sinAngle(radius, angle, offset);
        Vector returnVec;
        switch (axis) {
            case X:
                returnVec = new Vector(origin.getX(), y, z);
                break;
            case Y:
                returnVec = new Vector(x, origin.getY(), z);
                break;
            case Z:
                returnVec = new Vector(x, y, origin.getZ());
                break;
            default:
                throw new IncompatibleClassChangeError();
        }

        return returnVec;
    }

    private static double sinAngle(double radius, double angle, double offset) {
        return radius * Math.sin(Math.toRadians(angle + offset));
    }

    private static double cosAngle(double radius, double angle, double offset) {
        return radius * Math.cos(Math.toRadians(angle + offset));
    }

    public static Vector randPointRadius(Vector point, double radius) {
        double rand = Math.random();
        double r = radius * Math.sqrt(rand);
        double theta = rand * 2.0D * Math.PI;
        return new Vector(point.getX() + r * Math.cos(theta), point.getY(), point.getZ() + r * Math.sin(theta));
    }
}
