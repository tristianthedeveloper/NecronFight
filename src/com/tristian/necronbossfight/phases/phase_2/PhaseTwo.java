package com.tristian.necronbossfight.phases.phase_2;

import com.tristian.necronbossfight.NecronFightPlugin;
import com.tristian.necronbossfight.mobs.NecronWitherBoss;
import com.tristian.necronbossfight.phases.Phase;
import com.tristian.necronbossfight.phases.phase_2.pad.PadManager;
import com.tristian.necronbossfight.phases.phase_2.pad.PadRunnable;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitTask;

public class PhaseTwo extends Phase {

    private BukkitTask padTask;

    public PhaseTwo(Player player, NecronWitherBoss boss, World world) {
        super(player, boss, world);


        Bukkit.broadcastMessage("Phase two Started!");

        PadManager.init(world);
        init();


    }


    public void init() {
        boss.phase = 2;

        this.padTask = Bukkit.getScheduler().runTaskTimer(NecronFightPlugin.getInstance(), new PadRunnable(player.getWorld()), 5L, 5L);


    }

    ;


    private static class PhaseTwoFallDamageListener implements Listener {

        @EventHandler
        public void onFallDamage(EntityDamageEvent e) {
            if (e.getCause() != EntityDamageEvent.DamageCause.FALL)
                return;
            if (e.getEntity().hasMetadata("noFallDamageTemp")) {
                e.setCancelled(true);
                e.getEntity().removeMetadata("noFallDamageTemp", NecronFightPlugin.getInstance());
            }
        }

    }

}
