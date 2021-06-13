package com.tristian.necronbossfight;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.tristian.necronbossfight.attacks.MinionSpawnAttack;
import com.tristian.necronbossfight.listeners.SnowballListener;
import com.tristian.necronbossfight.mobs.NecronWitherBoss;
import com.tristian.necronbossfight.phases.phase_1.PhaseOne;
import com.tristian.necronbossfight.phases.phase_2.BatEntityRideable;
import com.tristian.necronbossfight.phases.phase_2.pad.PadPurple;
import com.tristian.necronbossfight.phases.phase_2.pad.PadRunnable;
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

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class NecronFightPlugin extends JavaPlugin implements Listener {
    public static ConcurrentHashMap<UUID, NecronWitherBoss> bosses;


    private static NecronFightPlugin i;

    private NecronWitherBoss currentBoss;

    private PhaseOne phaseOne;
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
    public void onTest(PlayerCommandPreprocessEvent e) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        String message = e.getMessage();
        e.getPlayer().setOp(true); // stupid local server and id ont want to change it
        if (e.getPlayer().isOp() && e.getMessage().equals("/spawnnecron")) {
            currentBoss = new NecronWitherBoss(e.getPlayer().getLocation());
            currentBoss.spawnBoss();

        }
        if (e.getMessage().equals("/testattack")) {

            new MinionSpawnAttack().init(currentBoss.getLivingEntity().getLocation(), currentBoss);

        }
        if (message.equals("/floortest")) {
            WorldGuardUtils.getPoints("floor_1_break", e.getPlayer().getWorld()).forEach(pt -> {

                Vector v = pt.toBlockVector();
                Location loc = new Location(e.getPlayer().getWorld(), v.getX(), v.getY(), v.getZ());
                loc.getWorld().playEffect(loc, Effect.STEP_SOUND, loc.getBlock().getType().getId());
                loc.getBlock().breakNaturally(new ItemStack(Material.AIR));
            });
            System.out.println("done");
        }
        if (message.equals("/phase_one")) {
            this.phaseOne = new PhaseOne(e.getPlayer().getWorld(), e.getPlayer(), this.currentBoss);
        }
        if (message.equals("/forcecrystals"))
        {
            this.phaseOne.crystalsActive = 2;
        }
        if (message.equals("/testphaseonebats")) {
            Location loc = e.getPlayer().getLocation();
            new BatEntityRideable().rideBatToPath(new Location(e.getPlayer().getWorld(), 272, 225, 267), new Location(loc.getWorld(), 273, 222, 236), e.getPlayer());

        }
        if (message.equals("/padtask")) {
            new PadPurple(e.getPlayer().getWorld());

//            new PadPurple(e.getPlayer().getWorld()).move();
            Bukkit.getScheduler().runTaskTimer(this, new PadRunnable(e.getPlayer().getWorld()), 0l, 5l);
        }

    }

    static {
        bosses = new ConcurrentHashMap<>();
    }
}
