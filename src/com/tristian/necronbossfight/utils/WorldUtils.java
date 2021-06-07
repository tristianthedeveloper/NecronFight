package com.tristian.necronbossfight.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.Random;

public class WorldUtils {

    public static Location getNearbyLocation(final Location l, final int radius, final int minDistance, final int y_boost) {
        final Random rand = new Random();
        final Location rand_loc = l.clone();
        rand_loc.add((double)((rand.nextBoolean() ? 1 : -1) * (rand.nextInt(radius) + minDistance)), 0.0, (double)((rand.nextBoolean() ? 1 : -1) * (rand.nextInt(radius) + minDistance)));
        rand_loc.add(0.5, (double)y_boost, 0.5);
        final Block b = rand_loc.getWorld().getHighestBlockAt(rand_loc).getLocation().getBlock();
        if (b.getY() < rand_loc.getY()) {
            final Location location = b.getLocation();
            return location;
        }
        final Location location = rand_loc;
        return location;
    }
}
