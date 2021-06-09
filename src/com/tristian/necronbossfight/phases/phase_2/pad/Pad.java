package com.tristian.necronbossfight.phases.phase_2.pad;

import com.tristian.necronbossfight.NecronFightPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public abstract class Pad {



    private int lowestBlockPosition;

    private String region;


    //    REALLLLLLLY LAGGY, DO SOMETHIN ABOUT ME
    public List<Location> startLocations;

    public Pad(String region) {
        this.lowestBlockPosition = 169;
    }


    public String getRegion() {
        return region;
    }

    ;


    public boolean goUp(Pad pad) {
//        return false if to go down.
        return pad.lowestBlockPosition == 169;
    }

    public void movePadDown(Pad pad) {

        int temp = lowestBlockPosition;
        int dy = -1;
        AtomicInteger i = new AtomicInteger(0);
        Bukkit.getScheduler().runTaskTimer(NecronFightPlugin.getInstance(), () -> {
            if (i.get() >= 28)
                return;
            for (Location l : startLocations) {
                l.clone().subtract(0, i.get(), 0).getBlock().setType(l.getBlock().getType());
            }
            i.incrementAndGet();
            this.lowestBlockPosition--;
        }, 1, 5L);
    }

    public void movePadUp() {
        List<Location> endPositions = this.startLocations.stream().map(e -> e.subtract(0, 27, 0)).collect(Collectors.toList());

        System.out.println(endPositions);
        AtomicInteger i = new AtomicInteger(0);
        Bukkit.getScheduler().runTaskTimer(NecronFightPlugin.getInstance(), () -> {
            if (i.get() >= 28)
                return;
            for (Location l : endPositions) {
                l.clone().add(0, i.get(), 0).getBlock().setType(Material.AIR);
            }
            i.incrementAndGet();
            this.lowestBlockPosition++;
        }, 1, 5L);
    }

    public void move() {
        if (goUp(this))
            movePadUp();
        else movePadDown(this);
    }

}

