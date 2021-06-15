package com.tristian.necronbossfight.mobs;

import com.tristian.necronbossfight.NecronFightPlugin;
import com.tristian.necronbossfight.utils.BossUtils;
import com.tristian.necronbossfight.utils.CustomEntity;
import com.tristian.necronbossfight.utils.WorldGuardUtils;
import net.minecraft.server.v1_7_R4.IRangedEntity;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public class NecronWitherBoss {


    private Location location;
    private UUID uuid;
    public int phase = 0;

    public NecronWitherBoss(Location location) {
        this.location = location;
    }

    private IRangedEntity ent;
    private LivingEntity livingEnt;

    public static boolean insidePillar(WitherBoss witherBoss) {

        Location loc = new Location(witherBoss.world.getWorld(), witherBoss.locX, witherBoss.locY, witherBoss.locZ);

        return WorldGuardUtils.applicableRegions(loc) != null && Objects.requireNonNull(WorldGuardUtils.applicableRegions(loc)).stream().anyMatch(e -> e.getId().contains("_pillar"));

    }

    public static boolean insidePillar(Location loc) {


        return WorldGuardUtils.applicableRegions(loc) != null && Objects.requireNonNull(WorldGuardUtils.applicableRegions(loc)).stream().anyMatch(e -> e.getId().contains("_pillar"));

    }
    public IRangedEntity getEntity() {
        return ent;
    }

    public EntityType getEntityType() {
        return EntityType.WITHER;
    }

    public LivingEntity spawnBoss() {
        final WitherBoss boss = (WitherBoss) BossUtils.createCustomEntity(CustomEntity.WITHER, this, this.location);
        final LivingEntity e = (LivingEntity) boss.getBukkitEntity();
        this.uuid = e.getUniqueId();
        e.setMetadata("do_not_clear", new FixedMetadataValue(NecronFightPlugin.getInstance(), true));
        e.setMetadata("boss", new FixedMetadataValue(NecronFightPlugin.getInstance(), this));
        e.setMetadata("catacombsEntityType", new FixedMetadataValue(NecronFightPlugin.getInstance(), "necronWitherBoss"));
        e.setMaxHealth((double) this.getMaxHealth());
        e.setHealth(e.getMaxHealth());
        this.setEnt(e);
        e.setRemoveWhenFarAway(false);
        e.setCustomName(ChatColor.translateAlternateColorCodes('&', "&4&lNecron"));
        e.setCustomNameVisible(true);
        this.livingEnt = e;
        NecronFightPlugin.bosses.put(e.getUniqueId(), this);
        boss.setBoss(this);
        return e;
    }

    private void setEnt(LivingEntity e) {
        this.livingEnt = e;
    }


    //    will be 1 billion later lol
    public double getMaxHealth() {

        return 500;

    }

    public void setStuck(long time) {
        this.getLivingEntity().setMetadata("necronStuck", new FixedMetadataValue(NecronFightPlugin.getInstance(), true));

        Bukkit.getScheduler().runTaskLaterAsynchronously(NecronFightPlugin.getInstance(), () -> this.getLivingEntity().removeMetadata("necronStuck", NecronFightPlugin.getInstance()), time);
    }

    public void setStuck() {
        this.getLivingEntity().setMetadata("necronStuck", new FixedMetadataValue(NecronFightPlugin.getInstance(), true));

    }

    public void removeStuckMeta() {
        this.getLivingEntity().removeMetadata("necronStuck", NecronFightPlugin.getInstance());
    }


    public LivingEntity getLivingEntity() {
        return this.livingEnt;
    }


    public List<Player> getTargets() {
        return Arrays.asList(Bukkit.getServer().getOnlinePlayers());
    }

    public boolean isStuck() {
        return this.getLivingEntity().hasMetadata("necronStuck");
    }


    public void sendMessage(String message) {

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4&lNecron: &c" + message));


    }

    public void hitByPillar() {
        this.setStuck(5 * 20L);
        this.getLivingEntity().damage(this.getMaxHealth() * .25);

    }
}
