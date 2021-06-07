package com.tristian.necronbossfight.utils;

import com.tristian.necronbossfight.mobs.NecronWitherBoss;
import com.tristian.necronbossfight.mobs.WitherBoss;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class BossUtils {
    public static net.minecraft.server.v1_7_R4.Entity createCustomEntity(final CustomEntity type, final NecronWitherBoss boss, final Location location) {
        final CraftWorld world = (CraftWorld)location.getWorld();
        try {
            final net.minecraft.server.v1_7_R4.Entity entity = type.getCustomClass().getDeclaredConstructor(net.minecraft.server.v1_7_R4.World.class).newInstance(world.getHandle());
            entity.setPosition(location.getX(), location.getY(), location.getZ());
            world.getHandle().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
            if (entity instanceof WitherBoss) {
                ((WitherBoss)entity).setBoss(boss);
            }
            return entity;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
