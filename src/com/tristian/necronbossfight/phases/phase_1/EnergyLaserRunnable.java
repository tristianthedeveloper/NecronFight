package com.tristian.necronbossfight.phases.phase_1;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.adapter.BukkitImplAdapter;
import com.sk89q.worldedit.internal.ServerInterfaceAdapter;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.tristian.necronbossfight.NecronFightPlugin;
import com.tristian.necronbossfight.mobs.NecronWitherBoss;
import com.tristian.necronbossfight.phases.phase_2.BatEntityRideable;
import com.tristian.necronbossfight.phases.phase_2.PhaseTwo;
import com.tristian.necronbossfight.utils.WorldGuardUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;

public class EnergyLaserRunnable implements Runnable {

    private NecronWitherBoss boss;
    private PhaseOne parent;

    private BatEntityRideable bat = new BatEntityRideable();

    private ProtectedRegion region;
    private int slimeId;

    //    for damaging stuff
    public EnergyLaserRunnable(NecronWitherBoss boss, PhaseOne parent, int slimeId) {
        this.boss = boss;
        this.parent = parent;
        this.region = WorldGuardUtils.getRegion("laser_beam_phase_1", boss.getLivingEntity().getWorld());
        this.slimeId = slimeId;
    }


    @Override
    public void run() {

        if (parent.crystalsActive == 2) {

            Location loc = boss.getLivingEntity().getLocation();
            if (WorldGuardUtils.getRegion("laser_beam_phase_1", loc.getWorld()).contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {

                boss.setStuck(20 * 5);
                boss.getLivingEntity().damage(boss.getMaxHealth() * 0.25);

                Bukkit.getScheduler().runTaskLater(NecronFightPlugin.getInstance(), () -> {
                    WorldGuardUtils.getPoints("floor_1_break", loc.getWorld()).forEach(pt -> {

//                    yeet the floor
                        Vector v = pt.toBlockVector();
                        Location toBreak = new Location(loc.getWorld(), v.getX(), v.getY(), v.getZ());
                        toBreak.getWorld().playEffect(loc, Effect.STEP_SOUND, loc.getBlock().getType().getId());
                        toBreak.getBlock().breakNaturally(new ItemStack(Material.AIR));

                    });
                    Location endPoint = new Location(boss.getLivingEntity().getWorld(), 273, 222, 236);
                    Location startPoint = new Location(boss.getLivingEntity().getWorld(), 272, 225, 267);
                    Location dest = endPoint;

                    final Blaze b = (Blaze) startPoint.getWorld().spawnEntity(startPoint, EntityType.BLAZE);


                    b.setPassenger(this.parent.player);

                    b.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2000, 200));
                    b.setMetadata("NOTARGET", new FixedMetadataValue(NecronFightPlugin.getInstance(), true));
                    b.setVelocity(new org.bukkit.util.Vector(0, 0, -2));

                    this.parent.boss
                            .getLivingEntity().teleport(dest);
                    this.parent.boss.getLivingEntity().setVelocity(new org.bukkit.util.Vector(0, -10, 0));



                    Bukkit.getScheduler().runTaskLater(NecronFightPlugin.getInstance(), () -> {

                        b.setVelocity(new org.bukkit.util.Vector(0, -10, 0.0));

                        b.eject();
                        Bukkit.getScheduler().runTaskLaterAsynchronously(NecronFightPlugin.getInstance(), () -> {

//                            todo remove me fat
                            b.remove();
                            this.parent.player.setVelocity(new org.bukkit.util.Vector(0, -0.76, 0));


                            this.parent.player.setMetadata("noFallDamageTemp", new FixedMetadataValue(NecronFightPlugin.getInstance(), true));

                            new PhaseTwo(this.parent.player, boss, this.parent.player.getWorld());
                        }, 2L);

                    }, 20L);


                }, 20 * 5);
                parent.despawnAll();

                parent.task.cancel();
                ;

                if (Bukkit.getScheduler().isCurrentlyRunning(slimeId)) Bukkit.getScheduler().cancelTask(slimeId);


            }


        }
    }

    public void breakFloor() {

    }
}
