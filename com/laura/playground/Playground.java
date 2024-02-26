package com.laura.playground;

import com.laura.playground.trajectory.Trajectory;
import com.laura.playground.trajectory.TrajectoryResults;
import com.laura.playground.utils.Ease;
import com.laura.playground.utils.LineTrace;
import com.laura.playground.utils.LocationUtils;
import com.laura.playground.utils.Mathsf;
import com.laura.playground.utils.ParticleUtils;
import com.laura.playground.utils.geometry.Axis;
import com.laura.playground.utils.geometry.Circle;
import com.laura.playground.utils.geometry.Direction;
import com.laura.playground.utils.geometry.Line;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Playground extends JavaPlugin implements Listener {
    private static final Set<Player> players = new HashSet<Player>();
    private static final Map<Player, Integer> drawnBows = new HashMap<Player, Integer>();
    private static BukkitRunnable runnable = null;
    private static long ticks = 0L;

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        if (runnable == null) {
            runnable = new BukkitRunnable() {
                public void run() {

                    for (Player player : Playground.players) {
                        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
                        Trajectory.ProjectileType projectile = Trajectory.ProjectileType.getByItemStack(itemInMainHand);
                        if (projectile != null && projectile != Trajectory.ProjectileType.ARROW) {
                            Playground.renderTrajectory(player, projectile);
                        }
                    }

                    Map<Player, Integer> newDrawnBows = new HashMap<Player, Integer>();

                    for (Entry<Player, Integer> playerIntegerEntry : Playground.drawnBows.entrySet()) {
                        Player player = (Player) playerIntegerEntry.getKey();
                        if (player.getInventory().getItemInMainHand().getType() == Material.BOW) {
                            int draw = playerIntegerEntry.getValue() == null ? 0 : playerIntegerEntry.getValue();
                            Trajectory.ProjectileType arrow = Trajectory.ProjectileType.ARROW;
                            double power = Mathsf.remap(0.0D, 20.0D, draw, arrow.getMinVelocity(), arrow.getMaxVelocity());
                            Playground.renderTrajectory(player, arrow, power);
                            newDrawnBows.put(player, Math.min(draw + 1, 20));
                        }
                    }

                    Playground.drawnBows.clear();
                    Playground.drawnBows.putAll(newDrawnBows);
                    if (Playground.ticks == Long.MAX_VALUE) Playground.ticks = 0L;
                    else Playground.ticks++;

                }
            };
            runnable.runTaskTimer(this, 1L, 1L);
        }

    }

    public void onDisable() {
        if (runnable != null) {
            runnable.cancel();
            runnable = null;
            players.clear();
            ticks = 0L;
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        players.add(player);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        players.remove(player);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer(); // TODO: Fix issue where quickly tapping bow causes trajectory arc to persist
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        if (itemInMainHand.getType() == Material.BOW && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK))
            drawnBows.put(player, 0);
        else drawnBows.remove(player);
    }

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
            if (itemInMainHand.getType() != Material.BOW) return;

            drawnBows.remove(player);
        }

    }

    private static void renderTrajectory(Player player, Trajectory.ProjectileType projectile) {
        renderTrajectory(player, projectile, projectile.getMaxVelocity());
    }

    private static void renderTrajectory(Player player, Trajectory.ProjectileType projectile, double power) {
        Location eyeLocation = player.getEyeLocation().clone();
        Location yawPerpendicular = LocationUtils.getYawPerpendicular(eyeLocation);
        Location offsetStart = LineTrace.lineTrace((Location) yawPerpendicular, 0.5D, 2).trace().getEnd();

        offsetStart.setYaw(LocationUtils.oppositeYaw(eyeLocation.getYaw()));
        offsetStart.setPitch(LocationUtils.oppositePitch(eyeLocation.getPitch()));
        eyeLocation.setYaw(LocationUtils.oppositeYaw(eyeLocation.getYaw()));
        eyeLocation.setPitch(LocationUtils.oppositePitch(eyeLocation.getPitch()));

        double gravity = projectile.getGravity();
        double minInertia = projectile.getMinInertia();
        double maxInertia = projectile.getMaxInertia();

        TrajectoryResults primeTrajectoryResults = Trajectory.trajectory(eyeLocation, gravity, power, minInertia, maxInertia).ignoreEntities(player).ignoreMaterials(Material.AIR).trace();
        TrajectoryResults offsetTrajectoryResults = Trajectory.trajectory(offsetStart, gravity, power, minInertia, maxInertia).ignoreEntities(player).ignoreMaterials(Material.AIR).trace();

        Location impactPoint = primeTrajectoryResults.getImpactPoint();

        if (impactPoint != null) {
            Direction impactNormalDirection = primeTrajectoryResults.getImpactNormalDirection();
            renderImpactTarget(impactPoint, impactNormalDirection);
        }

        List<Location> primeTracePoints = primeTrajectoryResults.getTracePoints();
        List<Location> offsetTracePoints = offsetTrajectoryResults.getTracePoints();

        for (int i = 1; i < primeTracePoints.size(); i++) {
            Location primeLoc = primeTracePoints.get(i);
            if (i >= offsetTracePoints.size()) {
                ParticleUtils.spawnParticle(primeLoc, 100, 255, 100);
            } else {
                Location offsetLoc = offsetTracePoints.get(i);
                double ratio = Mathsf.iLerp(0, primeTracePoints.size(), i);
                Location location = Line.lerpVector(offsetLoc.toVector(), primeLoc.toVector(), ratio).toLocation(primeLoc.getWorld());
                ParticleUtils.spawnParticle(location, 100, 255, 100);
            }
        }

    }

    public static void renderImpactTarget(Location impact, Direction direction) {
        if (ticks % 2L != 0L) {
            Location renderPoint = impact.clone().add(direction.toVector().multiply(0.05D));
            long mod40 = ticks % 40L;
            long mod100 = ticks % 100L;
            long localTicks = mod40;
            if (mod40 > 20L) localTicks = 40L - mod40;

            double ratio = Mathsf.iLerpClamped(0.0D, 20.0D, (double) localTicks);
            double radius = Ease.IN_OUT.ease(0.55D, 1.0D, ratio);
            double offset = Mathsf.remap(0.0D, 100.0D, (double) mod100, 0.0D, 360.0D);
            Axis axis = Axis.fromNormal(direction.toVector());
            List<Location> crossPoints = Circle.circlePointsAxisAligned(radius, 4, axis, renderPoint, offset);
            List<Location> line1 = Line.pointsAlongALine(crossPoints.get(0), crossPoints.get(2), 11);
            List<Location> line2 = Line.pointsAlongALine(crossPoints.get(1), crossPoints.get(3), 11);
            line1.addAll(line2);
            List<Location> circle = Circle.circlePointsAxisAligned(radius, 21, axis, (Location) renderPoint);
            circle.addAll(line1);
            ParticleUtils.renderLine(circle, Color.RED);
        }
    }
}
