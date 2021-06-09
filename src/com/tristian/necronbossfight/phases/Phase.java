package com.tristian.necronbossfight.phases;

import com.tristian.necronbossfight.mobs.NecronWitherBoss;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class Phase {

    public Player player;

    public List<Player> players; // lol

    public  NecronWitherBoss boss;

    public World world;


    public Phase(Player player, NecronWitherBoss boss, World world) {
        this.player = player;
        this.boss = boss;
        this.world = world;
    }

}
