package com.tristian.necronbossfight.mobs;

import com.tristian.necronbossfight.NecronFightPlugin;
import com.tristian.necronbossfight.utils.BossUtils;
import com.tristian.necronbossfight.utils.CustomEntity;
import net.minecraft.server.v1_7_R4.IRangedEntity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class NecronWitherBoss {


    private Location location;
    private UUID uuid;

    public NecronWitherBoss(Location location) {
        this.location = location;
    }

    private IRangedEntity ent;
    private LivingEntity livingEnt;

    public IRangedEntity getEntity() {
        return ent;
    }

    public EntityType getEntityType() {
        return EntityType.WITHER;
    }

    public LivingEntity spawnBoss() {
        final WitherBoss boss = (WitherBoss) BossUtils.createCustomEntity(CustomEntity.WITHER, this, this.location);
        boss.setBoss(this);
        final LivingEntity e = (LivingEntity)boss.getBukkitEntity();
        this.uuid = e.getUniqueId();
        e.setMetadata("do_not_clear", new FixedMetadataValue(NecronFightPlugin.getInstance(), true));
        e.setMetadata("boss", new FixedMetadataValue(NecronFightPlugin.getInstance(), this));
        e.setMaxHealth((double)this.getMaxHealth());
        e.setHealth(e.getMaxHealth());
        this.setEnt(e);
        e.setRemoveWhenFarAway(false);
        e.setCustomName(ChatColor.translateAlternateColorCodes('&', "&4&lNecron"));
        e.setCustomNameVisible(true);
        this.livingEnt = e;
        NecronFightPlugin.bosses.put(e.getUniqueId(), this);

        return e;
    }

    private void setEnt(LivingEntity e) {
        this.livingEnt = e;
    }


    //    will be 1 billion later lol
    public double getMaxHealth() {

        return 100;

    }


    public LivingEntity getLivingEntity() {
        return this.livingEnt;
    }

    public List<Player> getTargets() {
        return Arrays.asList(Bukkit.getServer().getOnlinePlayers());
    }
}
