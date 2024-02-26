package com.laura.playground.utils;

import com.laura.playground.utils.geometry.Direction;
import com.laura.playground.utils.geometry.Line;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class LineTrace {
    private final Location start;
    private Location end;
    private final double distance;
    private final LivingEntity originEntity;
    private final int pointsPerBlock;
    private boolean blocksCheck;
    private boolean entitiesCheck;
    private boolean impactCheck;
    private boolean firstBlockCheck;
    private boolean firstEntityCheck;
    private final EnumSet<Material> ignoreMaterials;
    private final Set<Block> ignoreBlocks;
    private final Set<Entity> ignoreEntities;
    private final EnumSet<Material> getFirstMaterials;

    public static LineTrace lineTrace(Location start, double distance, int pointsPerBlock) {
        return new LineTrace(start, distance, pointsPerBlock, (LivingEntity) null);
    }

    public static LineTrace lineTrace(Location start, Location end, int pointsPerBlock) {
        return new LineTrace(start, end, pointsPerBlock, (LivingEntity) null);
    }

    public static LineTrace lineTrace(LivingEntity livingEntity, double distance, int pointsPerBlock) {
        return new LineTrace(livingEntity.getEyeLocation(), distance, pointsPerBlock, livingEntity);
    }

    public static LineTrace lineTrace(LivingEntity livingEntity, Location end, int pointsPerBlock) {
        return new LineTrace(livingEntity.getEyeLocation(), end, pointsPerBlock, livingEntity);
    }

    private LineTrace(Location start, double distance, int pointsPerBlock, LivingEntity originEntity) {
        this(start, distance, (Location) null, pointsPerBlock, originEntity);
    }

    private LineTrace(Location start, Location end, int pointsPerBlock, LivingEntity originEntity) {
        this(start, start.distance(end), end, pointsPerBlock, originEntity);
    }

    private LineTrace(Location start, double distance, Location end, int pointsPerBlock, LivingEntity originEntity) {
        this.blocksCheck = false;
        this.entitiesCheck = false;
        this.impactCheck = false;
        this.firstBlockCheck = false;
        this.firstEntityCheck = false;
        this.ignoreMaterials = EnumSet.noneOf(Material.class);
        this.ignoreBlocks = new HashSet<Block>();
        this.ignoreEntities = new HashSet<Entity>();
        this.getFirstMaterials = EnumSet.noneOf(Material.class);
        this.start = start;
        this.end = end;
        this.distance = distance;
        this.pointsPerBlock = pointsPerBlock;
        this.originEntity = originEntity;
    }

    public LineTrace track(Tracker... trackers) {
        return this.track(Arrays.stream(trackers).collect(Collectors.toSet()));
    }

    public LineTrace track(Collection<Tracker> trackers) {
        trackers.forEach(this::track);
        return this;
    }

    public LineTrace track(Tracker tracker) {
        switch (tracker) {
            case BLOCKS:
                this.blocksCheck = true;
                break;
            case ENTITIES:
                this.entitiesCheck = true;
                break;
            case IMPACT:
                this.impactCheck = true;
                break;
            case FIRST_BLOCK:
                this.firstBlockCheck = true;
                break;
            case FIRST_ENTITY:
                this.firstEntityCheck = true;
                break;
            case ALL:
                this.blocksCheck = true;
                this.entitiesCheck = true;
                this.impactCheck = true;
                this.firstBlockCheck = true;
                this.firstEntityCheck = true;
        }

        return this;
    }

    public LineTrace ignoreEntities(Entity... entities) {
        return this.ignoreEntities(Arrays.stream(entities).collect(Collectors.toSet()));
    }

    public LineTrace ignoreEntities(Collection<Entity> entities) {
        entities.forEach(this::ignoreEntitiy);
        return this;
    }

    public LineTrace ignoreEntitiy(Entity entity) {
        this.ignoreEntities.add(entity);
        return this;
    }

    public LineTrace ignoreBlocks(Block... blocks) {
        return this.ignoreBlocks(Arrays.stream(blocks).collect(Collectors.toSet()));
    }

    public LineTrace ignoreBlocks(Collection<Block> blocks) {
        blocks.forEach(this::ignoreBlock);
        return this;
    }

    public LineTrace ignoreBlock(Block block) {
        this.ignoreBlocks.add(block);
        return this;
    }

    public LineTrace ignoreMaterials(Material... materials) {
        return this.ignoreMaterials(Arrays.stream(materials).collect(Collectors.toSet()));
    }

    public LineTrace ignoreMaterials(Collection<Material> materials) {
        materials.forEach(this::ignoreMaterial);
        return this;
    }

    public LineTrace ignoreMaterial(Material block) {
        this.ignoreMaterials.add(block);
        return this;
    }

    public LineTrace getFirst(Material... materials) {
        return this.getFirst(Arrays.stream(materials).collect(Collectors.toSet()));
    }

    public LineTrace getFirst(Collection<Material> materials) {
        materials.forEach(this::getFirst);
        return this;
    }

    public LineTrace getFirst(Material material) {
        this.getFirstMaterials.add(material);
        return this;
    }

    public LineTrace ignoreStartBlock() {
        return this.ignoreBlock(this.start.getBlock());
    }

    public LineTrace ignoreOriginEntity() {
        return this.originEntity == null ? this : this.ignoreEntitiy(this.originEntity);
    }

    public LineTraceResults trace() {
        LineTraceResults.LineTraceResultsBuilder builder = LineTraceResults.builder();
        int totalPoints = this.pointsPerBlock * (int) Math.ceil(this.distance);
        if (this.end == null) {
            Vector direction = this.start.getDirection().clone();
            Vector directionMultiplied = direction.multiply(this.distance);
            this.end = this.start.clone().add(directionMultiplied);
        }

        builder.start(this.start);
        builder.end(this.end);
        List<Location> linePoints = Line.pointsAlongALine(this.start, this.end, totalPoints);
        if (!linePoints.isEmpty()) {
            builder.tracePoints(linePoints);
            Block lastBlock = this.start.getBlock();
            Location lastLocation = linePoints.get(0);
            builder.startBlock(this.start.getBlock());
            builder.endBlock(this.end.getBlock());
            List<Entity> entities = new ArrayList<Entity>();
            if (this.entitiesCheck || this.firstEntityCheck) {
                entities = lastLocation.getWorld().getEntities();
            }

            entities.removeAll(this.ignoreEntities);

            List<Block> addedBlocks = new ArrayList();
            List<Entity> addedEntities = new ArrayList();
            boolean firstBlockDone = false;
            boolean setDirection = false;
            boolean hitAnything = false;

            for (Location location : linePoints) {
                Block block = location.getBlock();

                if (block != null) {
                    if ((this.blocksCheck || this.firstBlockCheck && addedBlocks.isEmpty()) && !this.ignoreBlocks.contains(block)) {
                        addedBlocks.add(block);
                        builder.block(block);
                    }

                    if (this.entitiesCheck || this.firstEntityCheck) {

                        for (Entity entity : entities) {
                            if (ignoreEntities.contains(entity)) continue;
                            if (EntityHitBox.hitCheck(location, entity)) {
                                addedEntities.add(entity);
                                builder.entity(entity);
                            }
                        }
                    }

                    if (this.impactCheck) {
                        Material blockMaterial = block.getType();
                        if (!this.ignoreMaterials.contains(blockMaterial) && (this.getFirstMaterials.isEmpty() || this.getFirstMaterials.contains(blockMaterial)) && !this.ignoreBlocks.contains(location.getBlock())) {
                            builder.hitAnything(true);
                            hitAnything = true;
                            if (!firstBlockDone) {
                                builder.firstBlock(block);
                                firstBlockDone = true;
                                if (!this.impactCheck) {
                                    continue;
                                }

                                Direction face = Direction.getRelative(lastLocation.getBlock(), location.getBlock());
                                if (face == null) {
                                    face = Direction.SELF;
                                }

                                double ratio;
                                switch (face) {
                                    case NORTH:
                                        ratio = Mathsf.iLerp(lastLocation.getZ(), location.getZ(), (double) lastLocation.getBlockZ());
                                        break;
                                    case EAST:
                                        ratio = Mathsf.iLerp(lastLocation.getX(), location.getX(), (double) (lastLocation.getBlockX() + 1));
                                        break;
                                    case SOUTH:
                                        ratio = Mathsf.iLerp(lastLocation.getZ(), location.getZ(), (double) (lastLocation.getBlockZ() + 1));
                                        break;
                                    case WEST:
                                        ratio = Mathsf.iLerp(lastLocation.getX(), location.getX(), (double) lastLocation.getBlockX());
                                        break;
                                    case UP:
                                        ratio = Mathsf.iLerp(lastLocation.getY(), location.getY(), (double) (lastLocation.getBlockY() + 1));
                                        break;
                                    case DOWN:
                                        ratio = Mathsf.iLerp(lastLocation.getY(), location.getY(), (double) lastLocation.getBlockY());
                                        break;
                                    default:
                                        ratio = 0.0D;
                                }

                                Location impactPoint = Line.lerpVector(lastLocation.toVector(), location.toVector(), ratio).toLocation(location.getWorld());
                                builder.impactPoint(impactPoint);
                                builder.impactNormal(impactPoint.clone().setDirection(face.getOpposite().toVector()));
                                builder.impactNormalDirection(face.getOpposite());
                            }

                            if (!setDirection && !lastBlock.equals(block)) {
                                setDirection = true;
                                builder.direction(Direction.getRelative(block, lastBlock));
                            }

                            if (!this.blocksCheck && !this.entitiesCheck && !this.firstBlockCheck && !this.firstEntityCheck) {
                                builder.endBlock(block);
                                break;
                            }
                        }
                    }

                    if (hitAnything) {
                        builder.postImpactPoint(location);
                    } else {
                        builder.preImpactPoint(lastLocation);
                    }

                    lastBlock = block;
                    lastLocation = location;
                }
            }

        }
        return builder.build();
    }

    public Location getStart() {
        return this.start;
    }

    public Location getEnd() {
        return this.end;
    }

    public double getDistance() {
        return this.distance;
    }
}
