package com.laura.playground.utils;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;

public class ParticleUtils {
    public static Color cycleRainbow(double ratio) {
        double v = Mathsf.lerp(0.0D, 6.0D, Mathsf.clamp(0.0D, 1.0D, ratio));
        int stage = (int) Math.floor(v);
        double r = 0.0D;
        double g = 0.0D;
        double b = 0.0D;
        double slidingVal = Mathsf.remap(stage, stage + 1, v, 0.0D, 255.0D);
        switch (stage) {
            case 0:
                r = 255.0D;
                g = slidingVal;
                break;
            case 1:
                r = 255.0D - slidingVal;
                g = 255.0D;
                break;
            case 2:
                g = 255.0D;
                b = slidingVal;
                break;
            case 3:
                g = 255.0D - slidingVal;
                b = 255.0D;
                break;
            case 4:
                r = slidingVal;
                b = 255.0D;
                break;
            case 5:
                r = 255.0D;
                b = 255.0D - slidingVal;
                break;
            default:
                r = 255.0D;
        }

        return Color.fromRGB((int) Math.floor(r), (int) Math.floor(g), (int) Math.floor(b));
    }

    public static void spawnParticle(Location location, int r, int g, int b) {
        if (location != null && location.getWorld() != null) {
            DustOptions effect = createParticle(r, g, b);
            location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, effect);
        }
    }

    public static void spawnParticle(Location location, Color color) {
        if (location != null && location.getWorld() != null) {
            DustOptions effect = createParticle(color);
            location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, effect);
        }
    }

    public static DustOptions createParticle(Color color) {
        return createParticle(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static DustOptions createParticle(int r, int g, int b) {
        return new DustOptions(Color.fromRGB(r, g, b), 1.0F);
    }

    public static void renderLine(List<Location> locations, Color color) {
        renderLine(locations, color.getRed(), color.getGreen(), color.getBlue());
    }

    public static void renderLine(List<Location> locations, int r, int g, int b) {
        renderLine(locations, r, g, b, r, g, b);
    }

    public static void renderLine(List<Location> locations, Color start, Color end) {
        renderLine(locations, start.getRed(), start.getGreen(), start.getBlue(), end.getRed(), end.getGreen(), end.getBlue());
    }

    public static void renderLine(List<Location> locations, int startR, int startG, int startB, int endR, int endG, int endB) {
        if (locations != null) {
            for (int i = 0; i < locations.size(); ++i) {
                int curR = startR == endR ? startR : Mathsf.remap(0, locations.size(), i, startR, endR);
                int curG = startG == endG ? startG : Mathsf.remap(0, locations.size(), i, startG, endG);
                int curB = startB == endB ? startB : Mathsf.remap(0, locations.size(), i, startB, endB);
                spawnParticle(locations.get(i), curR, curG, curB);
            }
        }
    }
}
