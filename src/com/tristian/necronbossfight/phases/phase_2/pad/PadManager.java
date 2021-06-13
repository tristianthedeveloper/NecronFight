package com.tristian.necronbossfight.phases.phase_2.pad;

import org.bukkit.World;

public class PadManager {


    public static PadPurple purple;
    public static PadYellow yellow;
    public static PadRed red;
    public static PadGreen green;




    public static void init(World world) {

        purple = new PadPurple(world);
        yellow = new PadYellow(world);

    }


}
