package com.laura.playground.utils;

import com.laura.playground.utils.geometry.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;

public final class LineTraceResults {
    private final List<Block> blocks;
    private final List<Entity> entities;
    private final List<Location> tracePoints;
    private final List<Location> preImpactPoints;
    private final List<Location> postImpactPoints;
    private final Block firstBlock;
    private final Block startBlock;
    private final Block endBlock;
    private final Direction direction;
    private final Location impactPoint;
    private final Location impactNormal;
    private final Direction impactNormalDirection;
    private final Location end;
    private final Location start;
    private final boolean hitAnything;

    public Location getImpactPoint() {
        return this.impactPoint == null ? null : this.impactPoint.clone();
    }

    public Location getImpactPointOrEnd() {
        return this.impactPoint == null ? this.end : this.impactPoint.clone();
    }

    public Location getImpactNormal() {
        return this.impactNormal == null ? null : this.impactNormal.clone();
    }

    public Material hitMaterial() {
        return this.firstBlock == null ? Material.AIR : this.firstBlock.getType();
    }

    public List<Block> getNonAirBlocks() {
        return this.getBlocksFilterOut(Material.AIR);
    }

    public List<Block> getBlocksFiltered(Material... filter) {
        return this.getBlocksFiltered(Arrays.stream(filter).collect(Collectors.toSet()));
    }

    public List<Block> getBlocksFiltered(Set<Material> filter) {
        return this.getBlocks().stream().filter(block -> filter.contains(block.getType())).collect(Collectors.toList());
    }

    public List<Block> getBlocksFilterOut(Material... filter) {
        return this.getBlocksFilterOut(Arrays.stream(filter).collect(Collectors.toSet()));
    }

    public List<Block> getBlocksFilterOut(Set<Material> filter) {
        return this.getBlocks().stream().filter((block) -> !filter.contains(block.getType())).collect(Collectors.toList());
    }

    public BlockFace getBlockFace() {
        return this.getDirection().getBlockFace();
    }

    public Location getReflection() {
        return LocationUtils.reflect(this.start, this.getImpactPoint(), this.getDirection());
    }

    public List<Block> getBlocks() {
        return this.blocks;
    }

    public List<Entity> getEntities() {
        return this.entities;
    }

    public List<Location> getTracePoints() {
        return this.tracePoints;
    }

    public List<Location> getPreImpactPoints() {
        return this.preImpactPoints;
    }

    public List<Location> getPostImpactPoints() {
        return this.postImpactPoints;
    }

    public Block getFirstBlock() {
        return this.firstBlock;
    }

    public Block getStartBlock() {
        return this.startBlock;
    }

    public Block getEndBlock() {
        return this.endBlock;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public Direction getImpactNormalDirection() {
        return this.impactNormalDirection;
    }

    public Location getEnd() {
        return this.end;
    }

    public Location getStart() {
        return this.start;
    }

    public boolean isHitAnything() {
        return this.hitAnything;
    }

    public static LineTraceResults.LineTraceResultsBuilder builder() {
        return new LineTraceResults.LineTraceResultsBuilder();
    }

    private LineTraceResults(List<Block> blocks, List<Entity> entities, List<Location> tracePoints, List<Location> preImpactPoints, List<Location> postImpactPoints, Block firstBlock, Block startBlock, Block endBlock, Direction direction, Location impactPoint, Location impactNormal, Direction impactNormalDirection, Location end, Location start, boolean hitAnything) {
        this.blocks = blocks;
        this.entities = entities;
        this.tracePoints = tracePoints;
        this.preImpactPoints = preImpactPoints;
        this.postImpactPoints = postImpactPoints;
        this.firstBlock = firstBlock;
        this.startBlock = startBlock;
        this.endBlock = endBlock;
        this.direction = direction;
        this.impactPoint = impactPoint;
        this.impactNormal = impactNormal;
        this.impactNormalDirection = impactNormalDirection;
        this.end = end;
        this.start = start;
        this.hitAnything = hitAnything;
    }

    public static class LineTraceResultsBuilder {
        private ArrayList<Block> blocks;
        private ArrayList<Entity> entities;
        private ArrayList<Location> tracePoints;
        private ArrayList<Location> preImpactPoints;
        private ArrayList<Location> postImpactPoints;
        private Block firstBlock;
        private Block startBlock;
        private Block endBlock;
        private Direction direction;
        private Location impactPoint;
        private Location impactNormal;
        private Direction impactNormalDirection;
        private Location end;
        private Location start;
        private boolean hitAnything;

        LineTraceResultsBuilder() {
        }

        public LineTraceResults.LineTraceResultsBuilder block(Block block) {
            if (this.blocks == null) {
                this.blocks = new ArrayList<Block>();
            }

            this.blocks.add(block);
            return this;
        }

        public LineTraceResults.LineTraceResultsBuilder blocks(Collection<? extends Block> blocks) {
            if (blocks == null) {
                throw new NullPointerException("blocks cannot be null");
            } else {
                if (this.blocks == null) {
                    this.blocks = new ArrayList<Block>();
                }

                this.blocks.addAll(blocks);
                return this;
            }
        }

        public LineTraceResults.LineTraceResultsBuilder clearBlocks() {
            if (this.blocks != null) {
                this.blocks.clear();
            }

            return this;
        }

        public LineTraceResults.LineTraceResultsBuilder entity(Entity entity) {
            if (this.entities == null) {
                this.entities = new ArrayList<Entity>();
            }

            this.entities.add(entity);
            return this;
        }

        public LineTraceResults.LineTraceResultsBuilder entities(Collection<? extends Entity> entities) {
            if (entities == null) {
                throw new NullPointerException("entities cannot be null");
            } else {
                if (this.entities == null) {
                    this.entities = new ArrayList<Entity>();
                }

                this.entities.addAll(entities);
                return this;
            }
        }

        public LineTraceResults.LineTraceResultsBuilder clearEntities() {
            if (this.entities != null) {
                this.entities.clear();
            }

            return this;
        }

        public LineTraceResults.LineTraceResultsBuilder tracePoint(Location tracePoint) {
            if (this.tracePoints == null) {
                this.tracePoints = new ArrayList<Location>();
            }

            this.tracePoints.add(tracePoint);
            return this;
        }

        public LineTraceResults.LineTraceResultsBuilder tracePoints(Collection<? extends Location> tracePoints) {
            if (tracePoints == null) {
                throw new NullPointerException("tracePoints cannot be null");
            } else {
                if (this.tracePoints == null) {
                    this.tracePoints = new ArrayList<Location>();
                }

                this.tracePoints.addAll(tracePoints);
                return this;
            }
        }

        public LineTraceResults.LineTraceResultsBuilder clearTracePoints() {
            if (this.tracePoints != null) {
                this.tracePoints.clear();
            }

            return this;
        }

        public LineTraceResults.LineTraceResultsBuilder preImpactPoint(Location preImpactPoint) {
            if (this.preImpactPoints == null) {
                this.preImpactPoints = new ArrayList<Location>();
            }

            this.preImpactPoints.add(preImpactPoint);
            return this;
        }

        public LineTraceResults.LineTraceResultsBuilder preImpactPoints(Collection<? extends Location> preImpactPoints) {
            if (preImpactPoints == null) {
                throw new NullPointerException("preImpactPoints cannot be null");
            } else {
                if (this.preImpactPoints == null) {
                    this.preImpactPoints = new ArrayList<Location>();
                }

                this.preImpactPoints.addAll(preImpactPoints);
                return this;
            }
        }

        public LineTraceResults.LineTraceResultsBuilder clearPreImpactPoints() {
            if (this.preImpactPoints != null) {
                this.preImpactPoints.clear();
            }

            return this;
        }

        public LineTraceResults.LineTraceResultsBuilder postImpactPoint(Location postImpactPoint) {
            if (this.postImpactPoints == null) {
                this.postImpactPoints = new ArrayList<Location>();
            }

            this.postImpactPoints.add(postImpactPoint);
            return this;
        }

        public LineTraceResults.LineTraceResultsBuilder postImpactPoints(Collection<? extends Location> postImpactPoints) {
            if (postImpactPoints == null) {
                throw new NullPointerException("postImpactPoints cannot be null");
            } else {
                if (this.postImpactPoints == null) {
                    this.postImpactPoints = new ArrayList<Location>();
                }

                this.postImpactPoints.addAll(postImpactPoints);
                return this;
            }
        }

        public LineTraceResults.LineTraceResultsBuilder clearPostImpactPoints() {
            if (this.postImpactPoints != null) {
                this.postImpactPoints.clear();
            }

            return this;
        }

        public LineTraceResults.LineTraceResultsBuilder firstBlock(Block firstBlock) {
            this.firstBlock = firstBlock;
            return this;
        }

        public LineTraceResults.LineTraceResultsBuilder startBlock(Block startBlock) {
            this.startBlock = startBlock;
            return this;
        }

        public LineTraceResults.LineTraceResultsBuilder endBlock(Block endBlock) {
            this.endBlock = endBlock;
            return this;
        }

        public LineTraceResults.LineTraceResultsBuilder direction(Direction direction) {
            this.direction = direction;
            return this;
        }

        public LineTraceResults.LineTraceResultsBuilder impactPoint(Location impactPoint) {
            this.impactPoint = impactPoint;
            return this;
        }

        public LineTraceResults.LineTraceResultsBuilder impactNormal(Location impactNormal) {
            this.impactNormal = impactNormal;
            return this;
        }

        public LineTraceResults.LineTraceResultsBuilder impactNormalDirection(Direction impactNormalDirection) {
            this.impactNormalDirection = impactNormalDirection;
            return this;
        }

        public LineTraceResults.LineTraceResultsBuilder end(Location end) {
            this.end = end;
            return this;
        }

        public LineTraceResults.LineTraceResultsBuilder start(Location start) {
            this.start = start;
            return this;
        }

        public LineTraceResults.LineTraceResultsBuilder hitAnything(boolean hitAnything) {
            this.hitAnything = hitAnything;
            return this;
        }

        public LineTraceResults build() {
            List blocks;
            switch (this.blocks == null ? 0 : this.blocks.size()) {
                case 0:
                    blocks = Collections.emptyList();
                    break;
                case 1:
                    blocks = Collections.singletonList(this.blocks.get(0));
                    break;
                default:
                    blocks = Collections.unmodifiableList(new ArrayList<Block>(this.blocks));
            }

            List entities;
            switch (this.entities == null ? 0 : this.entities.size()) {
                case 0:
                    entities = Collections.emptyList();
                    break;
                case 1:
                    entities = Collections.singletonList(this.entities.get(0));
                    break;
                default:
                    entities = Collections.unmodifiableList(new ArrayList<Entity>(this.entities));
            }

            List tracePoints;
            switch (this.tracePoints == null ? 0 : this.tracePoints.size()) {
                case 0:
                    tracePoints = Collections.emptyList();
                    break;
                case 1:
                    tracePoints = Collections.singletonList(this.tracePoints.get(0));
                    break;
                default:
                    tracePoints = Collections.unmodifiableList(new ArrayList<Location>(this.tracePoints));
            }

            List preImpactPoints;
            switch (this.preImpactPoints == null ? 0 : this.preImpactPoints.size()) {
                case 0:
                    preImpactPoints = Collections.emptyList();
                    break;
                case 1:
                    preImpactPoints = Collections.singletonList(this.preImpactPoints.get(0));
                    break;
                default:
                    preImpactPoints = Collections.unmodifiableList(new ArrayList<Location>(this.preImpactPoints));
            }

            List postImpactPoints;
            switch (this.postImpactPoints == null ? 0 : this.postImpactPoints.size()) {
                case 0:
                    postImpactPoints = Collections.emptyList();
                    break;
                case 1:
                    postImpactPoints = Collections.singletonList(this.postImpactPoints.get(0));
                    break;
                default:
                    postImpactPoints = Collections.unmodifiableList(new ArrayList<Location>(this.postImpactPoints));
            }

            return new LineTraceResults(blocks, entities, tracePoints, preImpactPoints, postImpactPoints, this.firstBlock, this.startBlock, this.endBlock, this.direction, this.impactPoint, this.impactNormal, this.impactNormalDirection, this.end, this.start, this.hitAnything);
        }

        public String toString() {
            return "LineTraceResults.LineTraceResultsBuilder(blocks=" + this.blocks + ", entities=" + this.entities + ", tracePoints=" + this.tracePoints + ", preImpactPoints=" + this.preImpactPoints + ", postImpactPoints=" + this.postImpactPoints + ", firstBlock=" + this.firstBlock + ", startBlock=" + this.startBlock + ", endBlock=" + this.endBlock + ", direction=" + this.direction + ", impactPoint=" + this.impactPoint + ", impactNormal=" + this.impactNormal + ", impactNormalDirection=" + this.impactNormalDirection + ", end=" + this.end + ", start=" + this.start + ", hitAnything=" + this.hitAnything + ")";
        }
    }
}
