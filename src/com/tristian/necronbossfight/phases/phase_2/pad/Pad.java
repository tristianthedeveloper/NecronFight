package com.tristian.necronbossfight.phases.phase_2.pad;

import com.tristian.necronbossfight.NecronFightPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public abstract class Pad {

    public static HashMap<String, Pad> pads = new LinkedHashMap<>();


    private int lowestBlockPosition;

    private AtomicInteger addWhenUp = new AtomicInteger(0);

    private AtomicInteger moveWhenDown = new AtomicInteger(0);


    private String region;


    //    REALLLLLLLY LAGGY, DO SOMETHIN ABOUT ME
    public List<Location> startLocations;

    public Pad(String region) {
        this.lowestBlockPosition = 169;

        pads.put(region, this);

    }


    public String getRegion() {
        return region;
    }

    ;


    public boolean goUp(Pad pad) {
//        return false if to go down.
        return pad.lowestBlockPosition == 169;
    }

    public void movePadDown(Pad pad, int amount) {

        int temp = lowestBlockPosition;
        int dy = -1;
        AtomicInteger i = new AtomicInteger(0);
        Bukkit.getScheduler().runTaskTimer(NecronFightPlugin.getInstance(), () -> {
            if (i.get() >= amount)
                return;
            for (Location l : startLocations) {
                l.clone().subtract(0, this.moveWhenDown.getAndIncrement(), 0).getBlock().setType(l.getBlock().getType());
            }
            i.incrementAndGet();
            this.lowestBlockPosition--;
        }, 1, 5L);
    }

    public void movePadUp(int amount) {
        List<Location> endPositions = this.startLocations.stream().map(e -> e.subtract(0, 27, 0)).collect(Collectors.toList());

        System.out.println(endPositions);
        AtomicInteger i = new AtomicInteger(0);
        Bukkit.getScheduler().runTaskTimer(NecronFightPlugin.getInstance(), () -> {
            if (i.get() >= amount)
                return;
            for (Location l : endPositions) {
                l.clone().add(0, this.addWhenUp.getAndIncrement(), 0).getBlock().setType(Material.AIR);
            }
            i.incrementAndGet();
            this.lowestBlockPosition++;
        }, 1, 5L);
    }

    public void move() {
        if (goUp(this))
            movePadUp(1);
        else movePadDown(this, 1);
    }

}

