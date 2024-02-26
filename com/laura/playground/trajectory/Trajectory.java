package com.laura.playground.trajectory;

import com.laura.playground.utils.LineTrace;
import com.laura.playground.utils.LineTraceResults;
import com.laura.playground.utils.Mathsf;
import com.laura.playground.utils.Tracker;
import com.laura.playground.utils.UniqueList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.SplashPotion;
import org.bukkit.inventory.ItemStack;

public class Trajectory {
    private final Location location;
    private final double gravity;
    private final double velocity;
    private final double yaw;
    private final double pitch;
    private final double minInertia;
    private final double maxInertia;
    private final double pitchRads;
    private final double yawRads;
    private final double pitchCos;
    private final double pitchSin;
    private final double yawCos;
    private final double yawSin;
    private final Set<Entity> ignoreEntities;
    private final Set<Material> ignoreMaterials;
    private static final double DEFAULT_GRAV = 1.333D;

    public static Trajectory trajectory(Location location, Trajectory.ProjectileType projectileType) {
        return new Trajectory(location, projectileType);
    }

    public static Trajectory trajectory(Location location, double gravity, double velocity, double minInertia, double maxInertia) {
        return new Trajectory(location, gravity, velocity, minInertia, maxInertia);
    }

    public static Trajectory trajectory(LivingEntity entity, Trajectory.ProjectileType projectileType) {
        return new Trajectory(entity.getEyeLocation(), projectileType);
    }

    public static Trajectory trajectory(LivingEntity entity, double gravity, double velocity, double minInertia, double maxInertia) {
        return new Trajectory(entity.getEyeLocation(), gravity, velocity, minInertia, maxInertia);
    }

    private Trajectory(Location location, Trajectory.ProjectileType projectileType) {
        this(location, projectileType.getGravity(), projectileType.getMaxVelocity(), projectileType.getMinInertia(), projectileType.getMaxInertia());
    }

    private Trajectory(Location location, double gravity, double velocity, double minInertia, double maxInertia) {
        this.ignoreEntities = new HashSet<Entity>();
        this.ignoreMaterials = EnumSet.noneOf(Material.class);
        this.location = location;
        this.gravity = gravity;
        this.velocity = velocity;
        this.yaw = (double) (-location.getYaw()) + 90.0D;
        this.pitch = (double) location.getPitch();
        this.minInertia = minInertia;
        this.maxInertia = maxInertia;
        this.pitchRads = Math.toRadians(this.pitch);
        this.yawRads = Math.toRadians(this.yaw);
        this.pitchCos = Math.cos(this.pitchRads);
        this.pitchSin = Math.sin(this.pitchRads);
        this.yawCos = Math.cos(this.yawRads);
        this.yawSin = Math.sin(this.yawRads);
    }

    public Trajectory ignoreEntities(Entity... entities) {
        this.ignoreEntities.addAll(Arrays.stream(entities).toList());
        return this;
    }

    public Trajectory ignoreMaterials(Material... materials) {
        this.ignoreMaterials.addAll(Arrays.stream(materials).toList());
        return this;
    }

    public TrajectoryResults trace() {
        return this.trace(64);
    }

    public TrajectoryResults trace(int maxTracePoints) {
        TrajectoryResults.TrajectoryResultsBuilder builder = new TrajectoryResults.TrajectoryResultsBuilder();
        builder.start(this.location.clone());
        List<Location> waypointsList = new ArrayList<Location>();
        List<Location> renderList = new ArrayList<Location>();
        UniqueList<Block> uniqueList = new UniqueList<Location>();
        Location highestPoint = this.location.clone();
        boolean impactFound = false;
        LineTraceResults results = null;

        double points;
        for (int i = 0; i < maxTracePoints; ++i) {
            Location clone = this.location.clone();
            points = this.getX((double) i / 5.0D);
            double y = this.getY((double) i / 5.0D);
            clone.add(points, y, 0.0D);
            Location rotatedPoint = this.rotateY(clone, this.location.clone());
            waypointsList.add(rotatedPoint);
            if (clone.getY() > highestPoint.getY()) {
                highestPoint = rotatedPoint.clone();
            }

            if (i > 0) {
                Location previous = waypointsList.get(i - 1);
                Location current = waypointsList.get(i);
                if (!previous.getBlock().equals(current.getBlock())) {
                    results = LineTrace.lineTrace(previous, current, 8)
                            .track(Tracker.IMPACT, Tracker.BLOCKS)
                            .ignoreEntities(this.ignoreEntities)
                            .ignoreMaterials(this.ignoreMaterials)
                            .trace();
                    uniqueList.addAll(results.getBlocks());
                    Location impactPoint = results.getImpactPoint();
                    if (impactPoint != null) {
                        waypointsList.add(impactPoint);
                        impactFound = true;
                        break;
                    }
                }
            }
        }

        builder.hitAnything(impactFound);
        if (impactFound) {
            builder.impactPoint(results.getImpactPoint());
            builder.impactNormal(results.getImpactNormal());
            builder.impactNormalDirection(results.getImpactNormalDirection());
        } else {
            builder.impactPoint(null);
            builder.impactNormal(null);
            builder.impactNormalDirection(null);
        }

        if (waypointsList.isEmpty()) {
            builder.end(this.location.clone());
        } else {
            builder.end(waypointsList.get(waypointsList.size() - 1));
        }

        builder.blocks(uniqueList.asArrayList());
        builder.roughHighestPoint(highestPoint);
        double roughLength = builder.build().getRoughLength();
        points = Math.max(1.0D, Math.ceil(roughLength));
        if (!waypointsList.isEmpty()) {
            Location lastLoc = waypointsList.get(waypointsList.size() - 1);
            double distance = this.location.clone().distance(lastLoc);

            for (int i = 0; (double) i < points; ++i) {
                double ratio = Mathsf.iLerp(0.0D, points, i);
                double x = Mathsf.lerp(0.0D, distance, ratio);
                double y = this.getYAtX(x);
                Location clone = this.location.clone();
                clone.add(x, y, 0.0D);
                renderList.add(this.rotateY(clone, this.location.clone()));
            }

            builder.tracePoints(renderList);
        } else {
            builder.tracePoints(new ArrayList<Location>());
        }

        return builder.build();
    }

    public double getX(double time) {
        double ratio = Mathsf.iLerpClamped(0, 90, this.pitch);
        double inertia = Mathsf.lerp(this.minInertia, this.maxInertia, ratio);
        return this.velocity * Math.pow(time, inertia) * this.pitchCos;
    }

    public double getY(double time) {
        return this.velocity * time * this.pitchSin - Math.pow(this.gravity * time, 2.0D) / 2.0D;
    }

    public double getTime(double x) {
        return x / (this.velocity * this.pitchCos);
    }

    public double getYAtX(double x) {
        return this.getY(this.getTime(x));
    }

    private Location rotateY(Location point, Location origin) {
        Location clone = point.clone().subtract(origin);
        double x = clone.getX();
        double y = clone.getY();
        double z = clone.getZ();
        double xOrigin = origin.getX();
        double yOrigin = origin.getY();
        double zOrigin = origin.getZ();
        double xRotate = x * this.yawCos + z * this.yawSin;
        double zRotate = -x * this.yawSin + z * this.yawCos;
        return new Location(point.getWorld(), xRotate + xOrigin, y + yOrigin, zRotate + zOrigin);
    }

    public enum ProjectileType {
        ARROW(Arrow.class, Material.BOW, 1.333D, 2.0D, 16.75D, 1.0D, 0.89D),
        EGG(Egg.class, Material.EGG, 1.333D, 10.45D, 1.0D, 0.89D),
        ENDER_PEARL(EnderPearl.class, Material.ENDER_PEARL, 1.333D, 10.45D, 1.0D, 0.89D),
        SNOWBALL(Snowball.class, Material.SNOWBALL, 1.333D, 10.45D, 1.0D, 0.89D),
        SPLASH_POTION(SplashPotion.class, Material.SPLASH_POTION, 1.333D, 3.5D, 0.98D, 0.5D);

        private final Class<? extends Entity> entityClass;
        private final Material material;
        private final double gravity;
        private final double minVelocity;
        private final double maxVelocity;
        private final double minInertia;
        private final double maxInertia;

        private ProjectileType(Class<? extends Entity> entityClass, Material material, double gravity, double maxVelocity, double minInertia, double maxInertia) {
            this(entityClass, material, gravity, maxVelocity, maxVelocity, minInertia, maxInertia);
        }

        private ProjectileType(Class<? extends Entity> entityClass, Material material, double gravity, double minVelocity, double maxVelocity, double minInertia, double maxInertia) {
            this.entityClass = entityClass;
            this.material = material;
            this.gravity = gravity;
            this.minVelocity = minVelocity;
            this.maxVelocity = maxVelocity;
            this.minInertia = minInertia;
            this.maxInertia = maxInertia;
        }

        public static Trajectory.ProjectileType getByEntity(Entity entity) {
            for (ProjectileType type : values()) {
                if (type.getEntityClass().isInstance(entity)) {
                    return type;
                }
            }

            String entityClassName = entity == null ? "NULL" : entity.getClass().getName();
            System.out.println("ProjectileType.getByEntity() : No projectile type found for entity " + entityClassName);
            return null;
        }

        public static Trajectory.ProjectileType getByItemStack(ItemStack itemStack) {
            if (itemStack == null) {
                return null;
            } else {
                Material type = itemStack.getType();

                for (ProjectileType projectileType : values()) {
                    if (projectileType.getMaterial() == type) {
                        return projectileType;
                    }
                }

                return null;
            }
        }

        public Class<? extends Entity> getEntityClass() {
            return this.entityClass;
        }

        public Material getMaterial() {
            return this.material;
        }

        public double getGravity() {
            return this.gravity;
        }

        public double getMinVelocity() {
            return this.minVelocity;
        }

        public double getMaxVelocity() {
            return this.maxVelocity;
        }

        public double getMinInertia() {
            return this.minInertia;
        }

        public double getMaxInertia() {
            return this.maxInertia;
        }
    }
}
