package com.laura.playground.utils;

import com.laura.playground.utils.geometry.Direction;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class LocationUtils {
    public static Location min(Location location1, Location location2) {
        return Vector.getMinimum(location1.toVector(), location2.toVector()).toLocation(location1.getWorld());
    }

    public static Location max(Location location1, Location location2) {
        return Vector.getMaximum(location1.toVector(), location2.toVector()).toLocation(location1.getWorld());
    }

    public static Location maxLocation(List<Location> locations) {
        return compareLocations(locations, false);
    }

    public static Location minLocation(List<Location> locations) {
        return compareLocations(locations, true);
    }

    public static Vector maxVector(List<Vector> vectors) {
        return compareVectors(vectors, false);
    }

    public static Vector minVector(List<Vector> vectors) {
        return compareVectors(vectors, true);
    }

    public static Location centrePointLocation(List<Location> locations) {
        if (locations != null && !locations.isEmpty()) {
            if (locations.size() == 1) {
                return locations.get(0);
            } else {
                double averageX = 0.0D;
                double averageY = 0.0D;
                double averageZ = 0.0D;

                for (Location location : locations) {
                    averageX += location.getX();
                    averageY += location.getY();
                }

                averageX /= locations.size();
                averageY /= locations.size();
                averageZ /= locations.size();
                return new Location(locations.get(0).getWorld(), averageX, averageY, averageZ);
            }
        } else throw new IllegalArgumentException("Locations cannot be empty or null!");
    }

    public static Vector centrePointVector(List<Vector> vectors) {
        if (vectors != null && !vectors.isEmpty()) {
            if (vectors.size() == 1) {
                return vectors.get(0);
            } else {
                double averageX = 0.0D;
                double averageY = 0.0D;
                double averageZ = 0.0D;

                for (Vector vec : vectors) {
                    averageX += vec.getX();
                    averageY += vec.getY();
                }

                averageX /= vectors.size();
                averageY /= vectors.size();
                averageZ /= vectors.size();
                return new Vector(averageX, averageY, averageZ);
            }
        } else throw new IllegalArgumentException("Vectors cannot be empty or null!");
    }

    public static Location compareLocations(List<Location> locations, boolean min) {
        if (locations != null && !locations.isEmpty()) {
            return locations.size() == 1 ?
                    locations.get(0) :
                    locations.stream().max((i, j) -> vectorCompare(i.toVector(), j.toVector(), min)).get();
        } else return null;
    }

    public static Vector compareVectors(List<Vector> vectors, boolean min) {
        if (vectors != null && !vectors.isEmpty()) {
            return vectors.size() == 1 ?
                    vectors.get(0) :
                    vectors.stream().max((i, j) -> vectorCompare(i, j, min)).get();
        } else return null;
    }

    public static int vectorCompare(Vector vector1, Vector vector2, boolean min) {
        if (vector1.getX() > vector2.getX() && vector1.getY() > vector2.getY() && vector1.getZ() > vector2.getZ())
            return min ? -1 : 1;
        else return 0;
    }

    public static Location trim(Location location, int significantDigits) {
        return trim(location.toVector(), significantDigits).toLocation(location.getWorld());
    }

    public static Vector trim(Vector location, int significantDigits) {
        double x = Mathsf.round(location.getX(), significantDigits);
        double y = Mathsf.round(location.getY(), significantDigits);
        double z = Mathsf.round(location.getZ(), significantDigits);
        return new Vector(x, y, z);
    }

    public static Location pointTowards(Location point, Vector towards) {
        return point.clone().setDirection(towards.clone().subtract(point.toVector()).normalize());
    }

    public static Location pointTowards(Location point, Location towards) {
        return point.clone().setDirection(towards.clone().subtract(point).toVector().normalize());
    }

    public static void setPointTowards(Location point, Vector towards) {
        point.setDirection(towards.clone().subtract(point.toVector()).normalize());
    }

    public static void setPointTowards(Location point, Location towards) {
        point.setDirection(towards.clone().subtract(point).toVector().normalize());
    }

    public static void setOppositeDirection(Location location) {
        if (location != null) {
            location.setYaw(oppositeYaw(location));
            location.setPitch(oppositePitch(location));
        }
    }

    public static Location getOppositeDirection(Location location) {
        if (location == null) {
            return null;
        } else {
            Location returnLoc = location.clone();
            returnLoc.setPitch(oppositePitch(location));
            returnLoc.setYaw(oppositeYaw(location));
            return returnLoc;
        }
    }

    public static float oppositeYaw(Location location) {
        return location == null ? 0.0F : oppositeYaw(location.getYaw());
    }

    public static float oppositePitch(Location location) {
        return location == null ? 0.0F : oppositePitch(location.getPitch());
    }

    public static float oppositeYaw(float angle) {
        return (180.0F - Math.abs(angle)) * (float) Mathsf.iSign(angle);
    }

    public static float oppositePitch(float angle) {
        return angle * -1.0F;
    }

    public static Location reflect(Location lineIn, Location impactPoint, Direction direction) {
        if (lineIn != null && direction != null && impactPoint != null) {
            Location impact = lineIn.clone();
            impact.setX(impactPoint.getX());
            impact.setY(impactPoint.getY());
            impact.setZ(impactPoint.getZ());
            Vector n = direction.toVector();
            Location impactPrime = getOppositeDirection(impact);
            Vector impactDirection = impactPrime.getDirection();
            Vector nDot2 = n.clone().multiply(impactDirection.dot(n) * 2.0D);
            Vector reflectionDirection = impactDirection.clone().subtract(nDot2).multiply(-1.0D);
            return impact.clone().setDirection(reflectionDirection);
        } else {
            return null;
        }
    }

    public static Location getPitchPerpendicular(Location location) {
        Location pitchVector = location.clone();
        if (location.getPitch() >= 0.0F) {
            pitchVector.setPitch(location.getPitch() - 90.0F);
        } else {
            pitchVector.setYaw(oppositeYaw(location));
            pitchVector.setPitch(-90.0F - location.getPitch());
        }

        return pitchVector;
    }

    public static Location getYawPerpendicular(Location location) {
        Location yawVector = location.clone();
        float newYaw;
        if (location.getYaw() < 90.0F) newYaw = location.getYaw() + 90.0F;
        else newYaw = -180.0F + Mathsf.remap(90.0F, 180.0F, location.getYaw(), 0.0F, 90.0F);

        if (newYaw == 180.0F) newYaw = -180.0F;

        yawVector.setPitch(0.0F);
        yawVector.setYaw(newYaw);
        return yawVector;
    }

    public static BlockFace getClosestBlockface(Location location, BlockFace... validFaces) {
        return getClosestBlockface(location, Arrays.stream(validFaces).collect(Collectors.toSet()));
    }

    public static BlockFace getClosestBlockface(Location location) {
        return getClosestBlockface(location, (new HashSet<>(Arrays.asList(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN))));
    }

    public static BlockFace getClosestBlockface(Location location, Set<BlockFace> validFaces) {
        if (validFaces != null && !validFaces.isEmpty()) {
            BlockFace closestFace = null;
            double largestDot = Double.NEGATIVE_INFINITY;
            Block locationBlock = location.getBlock();
            Vector centre = locationBlock.getLocation().add(0.5D, 0.5D, 0.5D).toVector();
            Vector locationRemainder = location.toVector().subtract(locationBlock.getLocation().toVector());

            for (BlockFace validFace : validFaces) {
                Direction direction = Direction.getDirection(validFace);
                if (direction != null) {
                    double dot = Math.abs(direction.getOpposite().toVector().add(locationBlock.getLocation().toVector()).dot(location.toVector().subtract(centre)));
                    if (dot > largestDot) {
                        largestDot = dot;
                        closestFace = validFace;
                    }
                }
            }

            if (closestFace == BlockFace.DOWN && locationRemainder.getY() >= 0.5D && validFaces.contains(BlockFace.UP)) {
                closestFace = BlockFace.UP;
            } else if (closestFace == BlockFace.UP && locationRemainder.getY() < 0.5D && validFaces.contains(BlockFace.DOWN)) {
                closestFace = BlockFace.DOWN;
            } else if (closestFace == BlockFace.WEST && locationRemainder.getX() >= 0.5D && validFaces.contains(BlockFace.EAST)) {
                closestFace = BlockFace.EAST;
            } else if (closestFace == BlockFace.EAST && locationRemainder.getX() < 0.5D && validFaces.contains(BlockFace.WEST)) {
                closestFace = BlockFace.WEST;
            } else if (closestFace == BlockFace.NORTH && locationRemainder.getZ() >= 0.5D && validFaces.contains(BlockFace.SOUTH)) {
                closestFace = BlockFace.SOUTH;
            } else if (closestFace == BlockFace.SOUTH && locationRemainder.getZ() < 0.5D && validFaces.contains(BlockFace.NORTH)) {
                closestFace = BlockFace.NORTH;
            }

            return closestFace;
        } else {
            return null;
        }
    }

    /**
     * TODO: Test this thoroughly, high potential for errors
     */
    public static Vector rotateOnVector(Vector axisVector, Vector vector, double angle) {
        double u = axisVector.getX();
        double v = axisVector.getY();
        double w = axisVector.getZ();
        double x = vector.getX();
        double y = vector.getY();
        double z = vector.getZ();
        double product = u * x + v * y + w * z;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double iCos = 1.0D - cos;
        double xPrime = u * product * iCos + x * cos + (-w * y + v * z) * sin;
        double yPrime = v * product * iCos + y * cos + (w * x - u * z) * sin;
        double zPrime = w * product * iCos + z * cos + (-v * x + u * y) * sin;
        return vector.clone().setX(xPrime).setY(yPrime).setZ(zPrime);
    }
}
