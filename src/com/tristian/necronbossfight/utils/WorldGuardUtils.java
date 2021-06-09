package com.tristian.necronbossfight.utils;

import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.WorldEditAPI;
import com.sk89q.worldedit.internal.LocalWorldAdapter;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.List;
import java.util.stream.Collectors;

public class WorldGuardUtils {


    private static WorldGuardPlugin instance = WorldGuardPlugin.inst();


    public static Polygonal2DRegion getPoints(String region, World world) {

        com.sk89q.worldedit.world.World jesus = WorldEdit.getInstance().getServer().getWorlds().stream().filter(e -> e.getName().equals(world.getName())).findFirst().get();
        ProtectedRegion wgRegion = WorldGuardPlugin.inst().getRegionManager(world).getRegion(region);
        Polygonal2DRegion weRegion = new Polygonal2DRegion(jesus, wgRegion.getPoints(), wgRegion.getMinimumPoint().getBlockY(), wgRegion.getMaximumPoint().getBlockY());
        return weRegion;
    }

    public static Location getRegionLocation(String region, World world) {
//        todo make variable
        return new Location(world, instance.getRegionManager(world).getRegion(region).getMinimumPoint().getX(),  instance.getRegionManager(world).getRegion(region).getMinimumPoint().getY(),  instance.getRegionManager(world).getRegion(region).getMinimumPoint().getZ());
    }

    public static ProtectedRegion getRegion(String region, World world) {
        return instance.getRegionManager(world).getRegion(region);
    }
}
