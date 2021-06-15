package com.tristian.necronbossfight.phases.phase_2;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.tristian.necronbossfight.NecronFightPlugin;
import com.tristian.necronbossfight.mobs.NecronWitherBoss;
import com.tristian.necronbossfight.mobs.WitherBoss;
import com.tristian.necronbossfight.phases.Phase;
import com.tristian.necronbossfight.phases.phase_2.pad.PadManager;
import com.tristian.necronbossfight.phases.phase_2.pad.PadRunnable;
import com.tristian.necronbossfight.utils.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitTask;

public class PhaseTwo extends Phase {

    private BukkitTask padTask;

    public PhaseTwo(Player player, NecronWitherBoss boss, World world) {
        super(player, boss, world);


        Bukkit.broadcastMessage("Phase two Started!");

        Bukkit.getPluginManager().registerEvents(new PhaseTwoDamageListener(boss), NecronFightPlugin.getInstance());

        PadManager.init(world);
        init();


    }


    public void init() {
        boss.phase = 2;

        this.padTask = Bukkit.getScheduler().runTaskTimer(NecronFightPlugin.getInstance(), new PadRunnable(player.getWorld(), this.boss), 5L, 5L);


    }

    ;


    private static class PhaseTwoDamageListener implements Listener {


        private NecronWitherBoss boss;

        public PhaseTwoDamageListener(NecronWitherBoss boss) {
            this.boss = boss;
        }

        @EventHandler
        public void onFallDamage(EntityDamageEvent e) {
            if (e.getCause() != EntityDamageEvent.DamageCause.FALL)
                return;
            if (e.getEntity().hasMetadata("noFallDamageTemp")) {
                e.setCancelled(true);
                e.getEntity().removeMetadata("noFallDamageTemp", NecronFightPlugin.getInstance());
            }
        }

        @EventHandler
        public void onNecronGetHitByPadEvent(EntityDamageEvent e) {
            if (e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
                if (e.getEntity().hasMetadata("catacombsEntityType") && e.getEntity().getMetadata("catacombsEntityType").get(0).asString().equals("necronWitherBoss")) {

                    PadManager.getPads().forEach(pad -> {

                        Location loc = e.getEntity().getLocation();
                        ApplicableRegionSet region = WorldGuardPlugin.inst().getRegionManager(e.getEntity().getWorld()).getApplicableRegions(
                            new Vector(loc.getX(), loc.getY(), loc.getZ())
                        );

                        region.forEach(protectedRegion -> {
                            if (protectedRegion.getId().contains("_break") && protectedRegion.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {


//                                WorldGuardUtils.getPoints(regionFixed, e.getEntity().getWorld());

                            }
                        });

                    });






                    Bukkit.broadcastMessage("suffocation event.");

                }
            }
        }

    }

}
