package com.laura.playground.trajectory;

import com.laura.playground.utils.geometry.Direction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class TrajectoryResults {
    private final Location start;
    private final Location end;
    private final Location roughHighestPoint;
    private final Location impactPoint;
    private final Location impactNormal;
    private final Direction impactNormalDirection;
    private final List<Location> tracePoints;
    private final List<Block> blocks;
    private final boolean hitAnything;

    public Location getStart() {
        return this.start.clone();
    }

    public Location getEnd() {
        return this.end.clone();
    }

    public Location getRoughHighestPoint() {
        return this.roughHighestPoint.clone();
    }

    public boolean hitAnything() {
        return this.hitAnything;
    }

    public Location getImpactPoint() {
        return this.impactPoint == null ? null : this.impactPoint.clone();
    }

    public Location getImpactPointOrEnd() {
        return this.impactPoint == null ? this.end : this.impactPoint.clone();
    }

    public Location getImpactNormal() {
        return this.impactNormal == null ? null : this.impactNormal.clone();
    }

    public Direction getImpactNormalDirection() {
        return this.impactNormalDirection;
    }

    public List<Location> getTracePoints() {
        return new ArrayList<Location>(this.tracePoints);
    }

    public List<Block> getBlocks() {
        return new ArrayList<Location>(this.blocks);
    }

    public double getRoughLength() {
        Location startPoint = this.getStart();
        Location endPoint = this.getEnd();
        Location midPoint = this.getRoughHighestPoint();
        if (startPoint != null && endPoint != null && midPoint != null) {
            double lengthA = startPoint.distance(midPoint);
            double lengthB = midPoint.distance(endPoint);
            return lengthA + lengthB;
        } else {
            return 0.0D;
        }
    }

    public static TrajectoryResults.TrajectoryResultsBuilder builder() {
        return new TrajectoryResults.TrajectoryResultsBuilder();
    }

    private TrajectoryResults(Location start, Location end, Location roughHighestPoint, Location impactPoint, Location impactNormal, Direction impactNormalDirection, List<Location> tracePoints, List<Block> blocks, boolean hitAnything) {
        this.start = start;
        this.end = end;
        this.roughHighestPoint = roughHighestPoint;
        this.impactPoint = impactPoint;
        this.impactNormal = impactNormal;
        this.impactNormalDirection = impactNormalDirection;
        this.tracePoints = tracePoints;
        this.blocks = blocks;
        this.hitAnything = hitAnything;
    }

    public static class TrajectoryResultsBuilder {
        private Location start;
        private Location end;
        private Location roughHighestPoint;
        private Location impactPoint;
        private Location impactNormal;
        private Direction impactNormalDirection;
        private ArrayList<Location> tracePoints;
        private ArrayList<Block> blocks;
        private boolean hitAnything;

        TrajectoryResultsBuilder() {
        }

        public TrajectoryResults.TrajectoryResultsBuilder start(Location start) {
            this.start = start;
            return this;
        }

        public TrajectoryResults.TrajectoryResultsBuilder end(Location end) {
            this.end = end;
            return this;
        }

        public TrajectoryResults.TrajectoryResultsBuilder roughHighestPoint(Location roughHighestPoint) {
            this.roughHighestPoint = roughHighestPoint;
            return this;
        }

        public TrajectoryResults.TrajectoryResultsBuilder impactPoint(Location impactPoint) {
            this.impactPoint = impactPoint;
            return this;
        }

        public TrajectoryResults.TrajectoryResultsBuilder impactNormal(Location impactNormal) {
            this.impactNormal = impactNormal;
            return this;
        }

        public TrajectoryResults.TrajectoryResultsBuilder impactNormalDirection(Direction impactNormalDirection) {
            this.impactNormalDirection = impactNormalDirection;
            return this;
        }

        public TrajectoryResults.TrajectoryResultsBuilder tracePoint(Location tracePoint) {
            if (this.tracePoints == null) {
                this.tracePoints = new ArrayList<Location>();
            }

            this.tracePoints.add(tracePoint);
            return this;
        }

        public TrajectoryResults.TrajectoryResultsBuilder tracePoints(Collection<? extends Location> tracePoints) {
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

        public TrajectoryResults.TrajectoryResultsBuilder clearTracePoints() {
            if (this.tracePoints != null) {
                this.tracePoints.clear();
            }

            return this;
        }

        public TrajectoryResults.TrajectoryResultsBuilder block(Block block) {
            if (this.blocks == null) {
                this.blocks = new ArrayList<Block>();
            }

            this.blocks.add(block);
            return this;
        }

        public TrajectoryResults.TrajectoryResultsBuilder blocks(Collection<? extends Block> blocks) {
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

        public TrajectoryResults.TrajectoryResultsBuilder clearBlocks() {
            if (this.blocks != null) {
                this.blocks.clear();
            }

            return this;
        }

        public TrajectoryResults.TrajectoryResultsBuilder hitAnything(boolean hitAnything) {
            this.hitAnything = hitAnything;
            return this;
        }

        public TrajectoryResults build() {
            List<Location> tracePoints;
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

            List<Block> blocks;
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

            return new TrajectoryResults(this.start, this.end, this.roughHighestPoint, this.impactPoint, this.impactNormal, this.impactNormalDirection, tracePoints, blocks, this.hitAnything);
        }

        public String toString() {
            return "TrajectoryResults.TrajectoryResultsBuilder(start=" + this.start + ", end=" + this.end + ", roughHighestPoint=" + this.roughHighestPoint + ", impactPoint=" + this.impactPoint + ", impactNormal=" + this.impactNormal + ", impactNormalDirection=" + this.impactNormalDirection + ", tracePoints=" + this.tracePoints + ", blocks=" + this.blocks + ", hitAnything=" + this.hitAnything + ")";
        }
    }
}
