package com.tristian.necronbossfight.attacks;

import com.tristian.necronbossfight.NecronFightPlugin;
import com.tristian.necronbossfight.mobs.NecronWitherBoss;
import com.tristian.necronbossfight.utils.WorldUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Snowball;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MinionSpawnAttack {


    public void init(Location startOfNecron, NecronWitherBoss necron) {


        LivingEntity necronLiving = necron.getLivingEntity();


        Location head = necronLiving.getLocation().add(0, 3, 0);

        for (int i = 0; i < 3; i++) {

            Snowball snowball = (Snowball) head.getWorld().spawnEntity(WorldUtils.getNearbyLocation(head, 2, 1, 1).add(0, necron.getLivingEntity().getEyeHeight(), 0), EntityType.SNOWBALL);

            PacketPlayOutEntityDestroy snowballRemover = new PacketPlayOutEntityDestroy(snowball.getEntityId());

            necron.getTargets().forEach(e -> ((CraftPlayer) e).getHandle().playerConnection.sendPacket(snowballRemover));


            int dx = Math.random() < 0.5 ? 1 : -1;
            int dz = Math.random() < 0.5 ? 1 : -1;

            snowball.setVelocity(new Vector(0.03 * dx, 0.2, 0.03 * dz));
            snowball.setMetadata("noDamage", new FixedMetadataValue(NecronFightPlugin.getInstance(), true));
            snowball.setMetadata("necronSnowball", new FixedMetadataValue(NecronFightPlugin.getInstance(), true));


            Bukkit.getScheduler().runTaskTimerAsynchronously(NecronFightPlugin.getInstance(), new BukkitRunnable() {
                @Override
                public void run() {

                    if (snowball.isDead() || snowball.isOnGround())
                        cancel();

                    Location pos = snowball.getLocation();

                    snowball.getWorld().playEffect(pos, Effect.COLOURED_DUST, 0);
                }
            }, 1, 1);
        }




    }
}
