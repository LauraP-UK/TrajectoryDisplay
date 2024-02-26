package com.laura.playground.utils.geometry;

import com.laura.playground.utils.Ease;
import com.laura.playground.utils.Mathsf;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Line {
    public static List<Location> pointsAlongALine(Location start, Location end, int points) {
        return pointsAlongALine(start, end, points, Ease.getDefault());
    }

    public static List<Location> pointsAlongALine(Location start, Location end, int points, Ease ease) {
        return pointsAlongALine(start, end, points, ease, -1.0D);
    }

    public static List<Location> pointsAlongALine(Location start, Location end, int points, Ease ease, double power) {
        return pointsAlongALine(start.toVector(), end.toVector(), points, ease, power)
                .stream()
                .map((vector) -> vector.toLocation(start.getWorld()))
                .collect(Collectors.toList());
    }

    public static List<Vector> pointsAlongALine(Vector start, Vector end, int points) {
        return pointsAlongALine(start, end, points, Ease.getDefault());
    }

    public static List<Vector> pointsAlongALine(Vector start, Vector end, int points, Ease ease) {
        return pointsAlongALine(start, end, points, ease, -1.0D);
    }

    public static List<Vector> pointsAlongALine(Vector start, Vector end, int points, Ease ease, double power) {
        List<Vector> pointsList = new ArrayList<Vector>();
        points--;

        for (int i = 0; i <= points; ++i) {
            pointsList.add(lerpVector(start, end, Mathsf.iLerp(0.0D, points, i), ease, power));
        }

        return pointsList;
    }

    public static Vector lerpVector(Vector start, Vector end, double ratio) {
        return lerpVector(start, end, ratio, Ease.getDefault());
    }

    public static Vector lerpVector(Vector start, Vector end, double ratio, Ease ease) {
        return lerpVector(start, end, ratio, ease, -1.0D);
    }

    public static Vector lerpVector(Vector start, Vector end, double ratio, Ease ease, double power) {
        double x;
        double y;
        double z;
        if (power <= 0.0D) {
            if (power != -1.0D) {
                System.out.println("[WARNING] Line.lerpVector() : Power was less than 0 (" + power + ")");
            }

            x = ease.ease(start.getX(), end.getX(), ratio);
            y = ease.ease(start.getY(), end.getY(), ratio);
            z = ease.ease(start.getZ(), end.getZ(), ratio);
        } else {
            x = ease.customEase(start.getX(), end.getX(), ratio, power);
            y = ease.customEase(start.getY(), end.getY(), ratio, power);
            z = ease.customEase(start.getZ(), end.getZ(), ratio, power);
        }

        return new Vector(x, y, z);
    }
}
