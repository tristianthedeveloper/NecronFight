package com.tristian.necronbossfight.phases.phase_2.pad;

import org.bukkit.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PadManager {


    public static PadPurple purple;
    public static PadYellow yellow;
    public static PadRed red;
    public static PadGreen green;

    private static ArrayList<Pad> pads = new ArrayList<>();




    public static void init(World world) {

        purple = new PadPurple(world);
        yellow = new PadYellow(world);

        pads.add(yellow);
        pads.add(purple);
//        pads.add(red);
//        pads.add(green);
    }


    public static ArrayList<Pad> getPads() {
        return pads;
    }


}
