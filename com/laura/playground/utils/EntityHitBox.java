package com.laura.playground.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Boat;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Illusioner;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Item;
import org.bukkit.entity.Llama;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Stray;
import org.bukkit.entity.Vex;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Vindicator;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.ZombieHorse;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.util.Vector;


/**
 * NOTE: This entire class can be essentially removed with the latest versions of Minecraft and integrating PaperMC Entity HitBox API.
 */
public enum EntityHitBox {
    ARMOR_STAND(ArmorStand.class, 1.975D, 0.5D),
    BAT(Bat.class, 0.9D, 0.5D),
    BLAZE(Blaze.class, 1.8D, 0.6D),
    BOAT(Boat.class, 0.455D, 1.375D),
    CAVE_SPIDER(CaveSpider.class, 0.5D, 0.7D),
    CHICKEN(Chicken.class, 0.7D, 0.4D),
    COW(Cow.class, 1.4D, 0.9D),
    CREEPER(Creeper.class, 1.7D, 0.6D),
    DONKEY(Donkey.class, 1.6D, 1.39D),
    ELDER_GUARDIAN(ElderGuardian.class, 2.0D, 2.0D),
    ENDER_CRYSTAL(EnderCrystal.class, 2.0D, 2.0D),
    ENDER_DRAGON(EnderDragon.class, 8.0D, 16.0D),
    ENDERMAN(Enderman.class, 2.9D, 0.6D),
    ENDERMITE(Endermite.class, 0.3D, 0.4D),
    EVOKER(Evoker.class, 1.95D, 0.6D),
    FALLING_BLOCK(FallingBlock.class, 0.98D, 0.98D),
    GHAST(Ghast.class, 4.0D, 4.0D),
    GUARDIAN(Guardian.class, 0.85D, 0.85D),
    HORSE(Horse.class, 1.6D, 1.39D),
    HUSK(Husk.class, 1.95D, 0.6D),
    ILLUSIONER(Illusioner.class, 1.95D, 0.6D),
    IRON_GOLEM(IronGolem.class, 2.7D, 1.4D),
    ITEM(Item.class, 0.25D, 0.25D),
    SLIME(Slime.class, 0.51D, 0.51D),
    LLAMA(Llama.class, 1.875D, 0.9D),
    MAGMACUBE(MagmaCube.class, 0.51D, 0.51D),
    MINECART(Minecart.class, 0.7D, 0.98D),
    OCELOT(Ocelot.class, 0.7D, 0.6D),
    PARROT(Parrot.class, 0.9D, 0.5D),
    PIG(Pig.class, 0.9D, 0.9D),
    PIGMAN(PigZombie.class, 1.95D, 0.6D),
    PLAYER(Player.class, 1.8D, 0.6D),
    RABBIT(Rabbit.class, 0.5D, 0.4D),
    SHEEP(Sheep.class, 1.3D, 0.9D),
    SHULKER(Shulker.class, 1.0D, 1.0D),
    SILVERFISH(Silverfish.class, 0.3D, 0.4D),
    SKELETON(Skeleton.class, 1.99D, 0.6D),
    SNOW_GOLEM(Snowman.class, 1.9D, 0.7D),
    SPIDER(Spider.class, 0.9D, 1.4D),
    SQUID(Squid.class, 0.8D, 0.8D),
    STRAY(Stray.class, 1.99D, 0.6D),
    VEX(Vex.class, 0.8D, 0.4D),
    VILLAGER(Villager.class, 1.95D, 0.6D),
    VINDICATOR(Vindicator.class, 1.95D, 0.6D),
    WITCH(Witch.class, 1.95D, 0.6D),
    WITHER(Wither.class, 3.5D, 0.9D),
    WITHER_SKELETON(WitherSkeleton.class, 2.4D, 0.7D),
    WOLF(Wolf.class, 0.85D, 0.6D),
    XP_ORB(ExperienceOrb.class, 0.5D, 0.5D),
    ZOMBIE(Zombie.class, 1.95D, 0.6D),
    ZOMBIE_HORSE(ZombieHorse.class, 1.6D, 1.39D),
    ZOMBIE_VILLAGER(ZombieVillager.class, 1.95D, 0.6D);

    private final Class<? extends Entity> entity;
    private final double height;
    private final double width;
    private final double length;

    private EntityHitBox(Class<? extends Entity> entity, double height, double widthLength) {
        this(entity, height, widthLength, widthLength);
    }

    private EntityHitBox(Class<? extends Entity> entity, double height, double width, double length) {
        this.entity = entity;
        this.height = height;
        this.width = width;
        this.length = length;
    }

    public static boolean hitCheck(Location location, Entity entity) {
        EntityHitBox.BoundingBox boundingBox = parseEntity(entity);
        if (boundingBox != null && location != null && location.getWorld() != null) {
            Location entityLocation = entity.getLocation();
            Location maxLoc = boundingBox.getMax().toLocation(location.getWorld()).add(entityLocation);
            Location minLoc = boundingBox.getMin().toLocation(location.getWorld()).add(entityLocation);
            if (maxLoc.getX() < location.getX()) {
                return false;
            } else if (maxLoc.getY() < location.getY()) {
                return false;
            } else if (maxLoc.getZ() < location.getZ()) {
                return false;
            } else if (minLoc.getX() > location.getX()) {
                return false;
            } else if (minLoc.getY() > location.getY()) {
                return false;
            } else {
                return !(minLoc.getZ() > location.getZ());
            }
        } else {
            return false;
        }
    }

    private static EntityHitBox.BoundingBox parseEntity(Entity entity) {
        if (entity == null) {
            return null;
        } else {
            EntityHitBox entityHitBox = getFromEntity(entity);
            if (entityHitBox == null) {
                return null;
            } else {
                double height = entityHitBox.getHeight();
                double width = entityHitBox.getWidth();
                double length = entityHitBox.getLength();
                Ageable ageable = entity instanceof Ageable ? (Ageable) entity : null;
                if (ageable != null && !ageable.isAdult()) {
                    if (entity instanceof Chicken) {
                        height = 0.35D;
                        width = 0.2D;
                    } else if (entity instanceof Cow) {
                        height = 0.7D;
                        width = 0.45D;
                    } else if (!(entity instanceof Horse) && !(entity instanceof Donkey)) {
                        if (entity instanceof Llama) {
                            height = 0.94D;
                            width = 0.45D;
                        } else if (entity instanceof Ocelot) {
                            height = 0.35D;
                            width = 0.3D;
                        } else if (entity instanceof Pig) {
                            height = 0.45D;
                            width = 0.45D;
                        } else if (entity instanceof Rabbit) {
                            height = 0.25D;
                            width = 0.2D;
                        } else if (entity instanceof Sheep) {
                            height = 0.675D;
                            width = 0.45D;
                        } else if (entity instanceof Wolf) {
                            height = 0.425D;
                            width = 0.3D;
                        } else if (entity instanceof Zombie) {
                            height = 0.975D;
                            width = 0.3D;
                        }
                    } else {
                        height = 0.8D;
                        width = 0.7D;
                    }
                }

                Block block;
                if (entity instanceof Boat) {
                    block = entity.getLocation().getBlock();
                    if (block.getType().equals(Material.WATER)) {
                        height = 0.56D;
                    }
                } else if (entity instanceof ElderGuardian) {
                    block = entity.getLocation().getBlock();
                    if (block.getType().equals(Material.WATER)) {
                        height = 0.99D;
                        width = 0.99D;
                    }
                } else if (entity instanceof Slime) {
                    Slime slime = (Slime) entity;
                    switch (slime.getSize()) {
                        case 0:
                            height = 0.51D;
                            width = 0.51D;
                            break;
                        case 1:
                            height = 1.02D;
                            width = 1.02D;
                            break;
                        case 2:
                            height = 2.04D;
                            width = 2.04D;
                    }
                } else if (entity instanceof Player) {
                    Player player = (Player) entity;
                    if (player.isSneaking()) {
                        height = 1.5D;
                    }
                }

                return new EntityHitBox.BoundingBox(height, width, width);
            }
        }
    }

    public static EntityHitBox getFromEntity(Entity entity) {
        for (int i = 0; i < values().length; i) {
            EntityHitBox value = values()[i];
            if (entity.getClass().isInstance(value.getEntity()) || value.entity.equals(entity.getClass())) {
                return value;
            }

            if (entity instanceof Player && value == PLAYER) {
                return value;
            }
        }

        return null;
    }

    public Class<? extends Entity> getEntity() {
        return this.entity;
    }

    public double getHeight() {
        return this.height;
    }

    public double getWidth() {
        return this.width;
    }

    public double getLength() {
        return this.length;
    }

    public static class BoundingBox {
        private final Vector upperNW = new Vector(0, 0, 0);
        private final Vector upperNE = new Vector(0, 0, 0);
        private final Vector upperSE = new Vector(0, 0, 0);
        private final Vector upperSW = new Vector(0, 0, 0);
        private final Vector lowerNW = new Vector(0, 0, 0);
        private final Vector lowerNE = new Vector(0, 0, 0);
        private final Vector lowerSE = new Vector(0, 0, 0);
        private final Vector lowerSW = new Vector(0, 0, 0);

        public BoundingBox(double height, double width, double length) {
            double halfWidth = width / 2.0D;
            double halfLength = length / 2.0D;
            this.upperNW.add(new Vector(-halfWidth, height, -halfLength));
            this.upperNE.add(new Vector(halfWidth, height, -halfLength));
            this.upperSE.add(new Vector(halfWidth, height, halfLength));
            this.upperSW.add(new Vector(-halfWidth, height, halfLength));
            this.lowerNW.add(new Vector(-halfWidth, 0.0D, -halfLength));
            this.lowerNE.add(new Vector(halfWidth, 0.0D, -halfLength));
            this.lowerSE.add(new Vector(halfWidth, 0.0D, halfLength));
            this.lowerSW.add(new Vector(-halfWidth, 0.0D, halfLength));
        }

        public Vector getMax() {
            return this.upperSE.clone();
        }

        public Vector getMin() {
            return this.lowerNW.clone();
        }
    }
}
