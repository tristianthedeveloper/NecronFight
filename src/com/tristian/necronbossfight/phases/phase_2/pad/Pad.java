package com.tristian.necronbossfight.phases.phase_2.pad;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Pad {

    public static HashMap<String, Pad> pads = new LinkedHashMap<>();


    private int lowestBlockPosition;


    private boolean up = false;


    private int addWhenUp = 0;
    private int moveWhenDown = 0;




    private String region;


    //    REALLLLLLLY LAGGY, DO SOMETHIN ABOUT ME
    public List<Location> startLocations;

    public Pad(String region) {
        this.lowestBlockPosition = 194;
        this.up = true;
        pads.put(region, this);

    }


    public String getRegion() {
        return region;
    }

    ;



    public void movePadDown(Pad pad) {

        int temp = lowestBlockPosition;
        int dy = -1;
        for (Location l : startLocations) {
            l.clone().subtract(0, this.moveWhenDown, 0).getBlock().setType(l.getBlock().getType());
            System.out.println(l.getBlock());
        }

        this.moveWhenDown++;

        this.lowestBlockPosition--;

        this.addWhenUp = 0;

        Bukkit.broadcastMessage("moving down");
    }

    public void movePadUp(int amount) {
        System.out.println("moving up");
        List<Location> endPositions = this.startLocations.stream().map(e -> e.clone().subtract(0, (27), 0)).collect(Collectors.toList());

        for (Location l : endPositions) {
//            l.clone().add(0, this.addWhenUp.get(), 0).getBlock().setType(Material.AIR);
            l.add(0, Math.abs(this.addWhenUp), 0).getBlock().setType(Material.AIR);
            System.out.println(l);
        }

        this.addWhenUp++;


        this.lowestBlockPosition++;

        this.moveWhenDown = 0; // make sure nothing bad happens lol
    }

    public void move() {




        if (this.lowestBlockPosition == 166)
            this.up = true;
        if (this.lowestBlockPosition >= 194)
            this.up = false;




        if (up)
            movePadUp(1);
        else movePadDown(this);
        Bukkit.broadcastMessage("add when up: " + this.addWhenUp);
        Bukkit.broadcastMessage("sub when down : " + this.moveWhenDown);
        Bukkit.broadcastMessage("current y pos: " + this.lowestBlockPosition);
    }


    public boolean isUp() {
        return this.up;
    }

    Location createLocation(int x, int y, int z, World world) {
        return new Location(world, x, y, z);
    }
}

