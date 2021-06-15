package com.tristian.necronbossfight.phases.phase_1;

import com.gmail.filoghost.holograms.api.Hologram;
import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;
import com.tristian.necronbossfight.NecronFightPlugin;
import com.tristian.necronbossfight.mobs.NecronWitherBoss;
import com.tristian.necronbossfight.phases.Phase;
import com.tristian.necronbossfight.utils.TitleAPI;
import com.tristian.necronbossfight.utils.WorldGuardUtils;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PhaseOne extends Phase {


    private List<Player> players; // lol


    public int crystalsActive = 0;
    private int slimeId;
    private List<Entity> thingsToRemove = new CopyOnWriteArrayList<>();

    public BukkitTask task;

    public PhaseOne(World world, Player player, NecronWitherBoss boss) {
        super(player, boss, world);

        entrySetup();
        boss.phase = 1;

//        schedule listener thing
        this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(NecronFightPlugin.getInstance(), new EnergyLaserRunnable(this.boss, this, slimeId), 20L, 20L);


        Bukkit.getPluginManager().registerEvents(new PhaseOneListener(this), NecronFightPlugin.getInstance());
    }

    private void entrySetup() {

        crystalSetup();


    }

    private void crystalSetup() {
        Location crystalOne = WorldGuardUtils.getRegionLocation("crystal_one", world);
        Location crystalTwo = WorldGuardUtils.getRegionLocation("crystal_two", world);

        EnderCrystal crystalOne1 = (EnderCrystal) world.spawnEntity(crystalOne, EntityType.ENDER_CRYSTAL);
        EnderCrystal crystalTwo1 = (EnderCrystal) world.spawnEntity(crystalTwo, EntityType.ENDER_CRYSTAL);
        crystalOne1.setMetadata("catacombsEntityType", new FixedMetadataValue(NecronFightPlugin.getInstance(), "energyCrystal"));
        crystalTwo1.setMetadata("catacombsEntityType", new FixedMetadataValue(NecronFightPlugin.getInstance(), "energyCrystal"));


        this.thingsToRemove.add(crystalOne1);
        this.thingsToRemove.add(crystalTwo1);


        Location slimeOne = WorldGuardUtils.getRegionLocation("slime_crystal_power_one", world);
        Location slimeTwo = WorldGuardUtils.getRegionLocation("slime_crystal_power_two", world);


        Slime slimeEntOne = (Slime) world.spawnEntity(slimeOne, EntityType.SLIME);
        Slime slimeEntTwo = (Slime) world.spawnEntity(slimeTwo, EntityType.SLIME);
        thingsToRemove.add(slimeEntOne);
        thingsToRemove.add(slimeEntTwo);
        slimeEntOne.setSize(5);
        slimeEntTwo.setSize(5);

//        yeah get rid of this and fix holograms ape
        this.slimeId = Bukkit.getScheduler().runTaskTimerAsynchronously(NecronFightPlugin.getInstance(), () -> {

            slimeEntOne.setVelocity(new Vector(0, 0, 0));

            slimeEntTwo.setVelocity(new Vector(0, 0, 0));
        }, 1l, 1L).getTaskId();

        slimeEntOne.setMetadata("catacombsEntityType", new FixedMetadataValue(NecronFightPlugin.getInstance(), "crystalSlime"));
        slimeEntTwo.setMetadata("catacombsEntityType", new FixedMetadataValue(NecronFightPlugin.getInstance(), "crystalSlime"));


    }

    public String getRegionName() {
        return "necron_phase_one";
    }

    public void despawnAll() {
//        clean up clean up everybody clean up
        this.thingsToRemove.forEach(Entity::remove);

    }


    private static class PhaseOneListener implements Listener {

        private PhaseOne parent;

        public PhaseOneListener(PhaseOne parent) {
            this.parent = parent;
        }

        @EventHandler
        public void onCrystalShmack(EntityDamageByEntityEvent e) {
            if (!(e.getEntity() instanceof EnderCrystal))
                return;
            if (!(e.getEntity().hasMetadata("catacombsEntityType")))
                return;
            if (!(e.getDamager() instanceof Player) || e.getEntity().getMetadata("catacombsEntityType").get(0).asString().equals("activeEnergyCrystal")) {
                e.setCancelled(true);
                return;
            }

            e.setCancelled(true);

            Player player = (Player) e.getDamager();

            player.setMetadata("tempItem", new FixedMetadataValue(NecronFightPlugin.getInstance(), player.getInventory().getItem(8) != null ? player.getInventory().getItem(8) : new ItemStack(Material.AIR)));


            ItemStack energyCrystal = new ItemStack(Material.NETHER_STAR);
            ItemMeta meta = energyCrystal.getItemMeta();

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cEnergy Crystal"));

            energyCrystal.setItemMeta(meta);

            player.getInventory().setItem(8, energyCrystal);


            PacketPlayOutEntityDestroy hideCrystal = new PacketPlayOutEntityDestroy(e.getEntity().getEntityId());


            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(hideCrystal);


            this.parent.boss.getTargets().forEach(p -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + player.getName() + " picked up an Energy Crystal!")));


        }

        // todo move this all into one thing
        @EventHandler
        public void onSlimeShmackWithCrystal(EntityDamageByEntityEvent e) {
            if (!(e.getEntity() instanceof Slime))
                return;
            if (!(e.getEntity().hasMetadata("catacombsEntityType")))
                return;
            if (!e.getEntity().getMetadata("catacombsEntityType").get(0).asString().equals("crystalSlime"))
                return;
            if (!(e.getDamager() instanceof Player))
                return;
            Player p = (Player) e.getDamager();
            if (!(hasEnergyCrystal(p)))
                return;


            p.getInventory().setItem(8, p.hasMetadata("tempItem") ? (ItemStack) (p.getMetadata("tempItem").get(0)).value() : new ItemStack(Material.AIR)); // get rid of crystal


            e.setCancelled(true);
            Location former = e.getEntity().getLocation();

            e.getEntity().remove();


            parent.crystalsActive++;
            TitleAPI.sendTimings(p, 1, 60, 1);
            TitleAPI.sendTitle(p, "", TitleAPI.TitleColor.GRAY);
            TitleAPI.sendSubTitle(p, ChatColor.translateAlternateColorCodes('&', "&a" + p.getName() + " has activated an Energy Crystal! &7(&c" + parent.crystalsActive + " &7/ &c2&7)"), TitleAPI.TitleColor.GREEN);
            EnderCrystal crystal = (EnderCrystal) former.getWorld().spawnEntity(former, EntityType.ENDER_CRYSTAL);

            parent.thingsToRemove.add(crystal);

//            todo change me
            crystal.setMetadata("catacombsEntityType", new FixedMetadataValue(NecronFightPlugin.getInstance(), "activeEnergyCrystal"));

            if (parent.crystalsActive == 2) {

                TitleAPI.sendTitle(p, "", TitleAPI.TitleColor.GRAY);

                TitleAPI.sendSubTitle(p, ChatColor.translateAlternateColorCodes('&', "&aThe energy laser is charging up!"), TitleAPI.TitleColor.GRAY);


            }


        }

        private boolean hasEnergyCrystal(Player p) {
            if (p.getInventory().getContents().length == 0)
                return false;
            if (p.getInventory().getContents() == null)
                return false;
            if (p.getInventory().getItem(8) == null || p.getInventory().getItem(8).getType() == Material.AIR)
                return false;
            if (p.getInventory().getItem(8).getItemMeta() == null)
                return false;
            ItemStack item = p.getInventory().getItem(8);
            if (!item.getItemMeta().hasDisplayName())
                return false;
            if (item.getType() != Material.NETHER_STAR)
                return false;
            return item.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&cEnergy Crystal"));
        }

    }

}
