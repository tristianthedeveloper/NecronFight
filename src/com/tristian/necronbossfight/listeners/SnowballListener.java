package com.tristian.necronbossfight.listeners;

import com.tristian.necronbossfight.utils.WorldUtils;
import net.minecraft.server.v1_7_R4.EntitySnowball;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

public class SnowballListener implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager().hasMetadata("noDamage")) {
            if (e.getDamager() instanceof Snowball || e.getDamager() instanceof EntitySnowball)
                e.getDamager().remove();
            e.setCancelled(true);

        }
    }

    @EventHandler
    public void onSnowballLand(ProjectileHitEvent e) {
        if ((e.getEntity() instanceof EntitySnowball || e.getEntity() instanceof Snowball) && e.getEntity().hasMetadata("necronSnowball")) {


            for (int i = 0; i < 15; i++) {
                e.getEntity().getWorld().spawnEntity(WorldUtils.getNearbyLocation(e.getEntity().getLocation(), 5, 2, 0), EntityType.SKELETON);
            }
            e.getEntity().remove();;

        }
    }

}
