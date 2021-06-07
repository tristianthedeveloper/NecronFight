package com.tristian.necronbossfight;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.tristian.necronbossfight.attacks.MinionSpawnAttack;
import com.tristian.necronbossfight.listeners.SnowballListener;
import com.tristian.necronbossfight.mobs.NecronWitherBoss;
import com.tristian.necronbossfight.phases.phase_1.PhaseOne;
import com.tristian.necronbossfight.utils.CustomEntity;
import com.tristian.necronbossfight.utils.WorldGuardUtils;
import javafx.beans.value.ObservableBooleanValue;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class NecronFightPlugin extends JavaPlugin  implements Listener  {
    public static ConcurrentHashMap<UUID, NecronWitherBoss> bosses;


    private static NecronFightPlugin i;

    private NecronWitherBoss currentBoss;

    public static Plugin getInstance() {
        return i;
    }


    @Override
    public void onEnable() {

        i = this;
        System.out.println("hopefully loaded");

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new SnowballListener(), this);

        CustomEntity.registerEntities();

    }

    @EventHandler
    public void onTest(PlayerCommandPreprocessEvent e) {
        String message = e.getMessage();
        if (e.getPlayer().isOp() && e.getMessage().equals("/spawnnecron"))
        {
            currentBoss = new NecronWitherBoss(e.getPlayer().getLocation());
            currentBoss.spawnBoss();

        }
        if (e.getMessage().equals("/testattack"))
        {

            new MinionSpawnAttack().init(currentBoss.getLivingEntity().getLocation(), currentBoss);

        }
        if (message.equals("/floortest")) {
            WorldGuardUtils.getPoints("floor_1_break", e.getPlayer().getWorld()).forEach(pt -> {

                Vector v = pt.toBlockVector();
               Location loc = new Location(e.getPlayer().getWorld(), v.getX(), v.getY(),v.getZ());
                loc.getWorld().playEffect(loc, Effect.STEP_SOUND, loc.getBlock().getType().getId());
                loc.getBlock().breakNaturally(new ItemStack(Material.AIR));
            });
            System.out.println("done");
        }
        if (message.equals("/phase_one"))
        {
            new PhaseOne(e.getPlayer().getWorld(), e.getPlayer());
        }
    }
    static {
        bosses = new ConcurrentHashMap<>();
    }
}
