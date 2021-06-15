package com.tristian.necronbossfight.phases.phase_2.pad;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.tristian.necronbossfight.NecronFightPlugin;
import com.tristian.necronbossfight.mobs.NecronWitherBoss;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PadRunnable implements Runnable {

    private World world;
    private NecronWitherBoss boss;
    private LivingEntity necronEnt;

    public PadRunnable(World world, NecronWitherBoss boss) {

        this.world = world;
        this.boss = boss;
        this.necronEnt = boss.getLivingEntity();
    }


    @Override
    public void run() {
        AtomicReference<Pad> detected = new AtomicReference<>();


        List<Player> playersInRegion = new ArrayList<Player>();
        Pad.pads.forEach((k, v) -> {
            ProtectedRegion region = WorldGuardPlugin.inst().getRegionManager(world).getRegion(k);
            for (Player p : NecronFightPlugin.getInstance().getServer().getOnlinePlayers()) {
                Location loc = p.getLocation();
                if (region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
                    System.out.println("detected");
                    detected.set(v);
                    break;
                }
            }
            
        });
        if (detected.get() != null) {
            detected.get().move();
        }

    }
}
