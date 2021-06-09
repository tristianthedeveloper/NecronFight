package com.tristian.necronbossfight.phases.phase_2;

import com.tristian.necronbossfight.NecronFightPlugin;
import com.tristian.necronbossfight.mobs.NecronWitherBoss;
import net.minecraft.server.v1_7_R4.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

// 272 225 240 after phase 1.
public class BatEntityRideable implements Listener {

    public BatEntityRideable() {
        Bukkit.getPluginManager().registerEvents(this, NecronFightPlugin.getInstance());
    }

    public void rideBatToPath(Location startPoint, Location endPoint, Entity toSend) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {

        Location dest = endPoint;

        final Blaze b = (Blaze) startPoint.getWorld().spawnEntity(startPoint, EntityType.BLAZE);

        b.setMetadata("NOTARGET", new FixedMetadataValue(NecronFightPlugin.getInstance(), true));

        final Zombie z = (Zombie) startPoint.getWorld().spawnEntity(new Location(startPoint.getWorld(), 272, 224, 239), EntityType.ZOMBIE);


        final EntityInsentient c = ((EntityInsentient) ((CraftEntity) b).getHandle());
        b.setVelocity(new Vector(0, 0, -2));

        b.setPassenger(toSend);
    }

    /*
     * Intellij for some reason hates getOnlinePlayers
     *
     * @return
     */
    public List<Player> getOnlinePlayers() {
        return Arrays.asList(Bukkit.getServer().getOnlinePlayers());
    }

    @EventHandler
    public void cancelTarget(EntityTargetEvent e) {
        if (e.getEntity().hasMetadata("NOTARGET"))
            e.setCancelled(true);
    }


}
