package com.tristian.necronbossfight.phases.phase_2;

import com.tristian.necronbossfight.mobs.NecronWitherBoss;
import com.tristian.necronbossfight.phases.Phase;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PhaseTwo extends Phase {
    public PhaseTwo(Player player, NecronWitherBoss boss, World world) {
        super(player, boss, world);

        init();

    }

    public void init() {
        boss.phase = 2;
    }

    ;

}
