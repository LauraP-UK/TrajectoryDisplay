package com.laura.playground.utils.geometry;

import org.bukkit.util.Vector;

public enum Axis {
    X,
    Y,
    Z;

    public static Axis fromNormal(Vector location) {
        Vector normal = location.clone().normalize();
        if (Math.abs(normal.getX()) == 1.0D) {
            return X;
        } else if (Math.abs(normal.getY()) == 1.0D) {
            return Y;
        } else {
            return Math.abs(normal.getZ()) == 1.0D ? Z : Y;
        }
    }
}
