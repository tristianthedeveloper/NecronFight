package com.tristian.necronbossfight.phases.phase_2.pad;

import org.bukkit.World;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PadYellow extends Pad {
    public PadYellow(World world) {
        super("pad_yellow");


//        purple locations but with an x-value of (x) -> x + 50
        this.startLocations = new LinkedList<>();
        this.startLocations.addAll(
                Stream.of(

                        createLocation(302, 196, 263, world),
                        createLocation(302, 196, 264, world),
                        createLocation(302, 196, 265, world),
                        createLocation(301, 196, 266, world),
                        createLocation(300, 196, 267, world),
                        createLocation(299, 196, 267, world),
                        createLocation(298, 196, 267, world),
                        createLocation(297, 196, 266, world),
                        createLocation(296, 196, 265, world),
                        createLocation(296, 196, 264, world),
                        createLocation(296, 196, 263, world),
                        createLocation(297, 196, 262, world),
                        createLocation(298, 196, 261, world),
                        createLocation(299, 196, 261, world),
                        createLocation(300, 196, 261, world),
                        createLocation(301, 196, 262, world),
//                        insides
                        createLocation(301, 196, 263, world),
                        createLocation(301, 196, 264, world),
                        createLocation(301, 196, 265, world),

                        createLocation(300, 196, 263, world),
                        createLocation(300, 196, 264, world),
                        createLocation(300, 196, 265, world),

                        createLocation(299, 196, 263, world),
                        createLocation(299, 196, 264, world),
                        createLocation(299, 196, 265, world),

                        createLocation(298, 196, 263, world),
                        createLocation(298, 196, 264, world),
                        createLocation(298, 196, 265, world),

                        createLocation(298, 196, 262, world),
                        createLocation(299, 196, 262, world),
                        createLocation(300, 196, 262, world),


                        createLocation(298, 196, 266, world),
                        createLocation(299, 196, 266, world),
                        createLocation(300, 196, 266, world),

                        createLocation(296, 196, 266, world),
                        createLocation(297, 196, 267, world),
                        createLocation(298, 196, 268, world),
                        createLocation(299, 196, 268, world),
                        createLocation(300, 196, 268, world),
                        createLocation(301, 196, 267, world),
                        createLocation(302, 196, 266, world),


                        createLocation(297, 196, 263, world),
                        createLocation(297, 196, 264, world),
                        createLocation(297, 196, 265, world)

                ).map(e -> e.clone().add(50, 0, 0)).collect(Collectors.toList()));
    }
}
