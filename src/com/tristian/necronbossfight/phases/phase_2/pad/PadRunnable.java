package com.tristian.necronbossfight.phases.phase_2.pad;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.tristian.necronbossfight.NecronFightPlugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PadRunnable implements Runnable {

    private World world;

    public PadRunnable(World world) {

    }


    @Override
    public void run() {
        Pad detected;


        List<Player> playersInRegion = new ArrayList<Player>();
        Pad.pads.forEach((k, v) -> {
            ;
            ProtectedRegion region = WorldGuardPlugin.inst().getRegionManager(world).getRegion(k);
            for (Player p : NecronFightPlugin.getInstance().getServer().getOnlinePlayers()) {
                Location loc = p.getLocation();
                if (region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
                    playersInRegion.add(p);
                    break;
                }
            }
        });
        if (!(playersInRegion.isEmpty())) {

        }


    }
}
