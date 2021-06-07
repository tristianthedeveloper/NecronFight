package com.tristian.necronbossfight.phases.phase_1;

import com.tristian.necronbossfight.NecronFightPlugin;
import com.tristian.necronbossfight.mobs.NecronWitherBoss;
import com.tristian.necronbossfight.utils.WorldGuardUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;

public class PhaseOne {

    private Player player;

    private List<Player> players; // lol

    private NecronWitherBoss boss;

    private World world;

    private EnderCrystal crystalOne;
    private EnderCrystal crystalTwo;

    private int crystalsActive = 0;

    public PhaseOne(World world, Player player) {
        this.world = world;
        this.player = player;
        entrySetup();
        Bukkit.getPluginManager().registerEvents(new PhaseOneListener(this), NecronFightPlugin.getInstance());
    }

    private void entrySetup() {

        crystalSetup();


    }

    private void crystalSetup() {
        Location crystalOne = WorldGuardUtils.getRegionLocation("crystal_one", world);
        Location crystalTwo = WorldGuardUtils.getRegionLocation("crystal_two", world);

        this.crystalOne = (EnderCrystal) world.spawnEntity(crystalOne, EntityType.ENDER_CRYSTAL);
        this.crystalTwo = (EnderCrystal) world.spawnEntity(crystalTwo, EntityType.ENDER_CRYSTAL);
        this.crystalOne.setMetadata("catacombsEntityType", new FixedMetadataValue(NecronFightPlugin.getInstance(), "energyCrystal"));
        this.crystalTwo.setMetadata("catacombsEntityType", new FixedMetadataValue(NecronFightPlugin.getInstance(), "energyCrystal"));

        Location slimeOne = WorldGuardUtils.getRegionLocation("slime_crystal_power_one", world);
        Location slimeTwo = WorldGuardUtils.getRegionLocation("slime_crystal_power_one", world);


        Slime slimeEntOne = (Slime) world.spawnEntity(slimeOne, EntityType.SLIME);
        Slime slimeEntTwo = (Slime) world.spawnEntity(slimeTwo, EntityType.SLIME);
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(slimeEntOne.getEntityId());

        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
        packet = new PacketPlayOutEntityDestroy(slimeEntTwo.getEntityId());
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
        slimeEntOne.setCustomName(ChatColor.translateAlternateColorCodes('&', "&cMissing Energy Crystal &7(Click Here)"));
        slimeEntTwo.setCustomName(ChatColor.translateAlternateColorCodes('&', "&cMissing Energy Crystal &7(Click Here)"));

        slimeEntOne.setCustomNameVisible(true);
        slimeEntTwo.setCustomNameVisible(true);



    }

    public String getRegionName() {
        return "necron_phase_one";
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
            if (!(e.getDamager() instanceof Player) || e.getEntity().hasMetadata("activeEnergyCRystal")) {
                e.setCancelled(true);
                return;
            }
            // todo add title and stuff like announcement
            Player player = (Player)e.getDamager();
            player.setMetadata("tempItem", new FixedMetadataValue(NecronFightPlugin.getInstance(), player.getInventory().getItem(8).getType() != Material.AIR ? player.getInventory().getItem(8) : new ItemStack(Material.AIR)));

            ItemStack energyCrystal = new ItemStack(Material.NETHER_STAR);
            ItemMeta meta = energyCrystal.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cEnergy Crystal"));
            energyCrystal.setItemMeta(meta);

            player.getInventory().setItem(8, energyCrystal);

        }
        // todo move this all into one thing
        @EventHandler
        public void onSlimeShmackWithCrystal(EntityDamageByEntityEvent e) {
            if (!(e.getEntity() instanceof Slime))
                return;
            if (!(e.getEntity().hasMetadata("catacombsEntityType")))
                return;
            if (!e.getEntity().getMetadata("catacombsEntityType").get(0).equals("crystalSlime"))
                return;
            if (!(e.getDamager() instanceof Player))
                return;
            Player p = (Player) e.getDamager();
            if (!(hasEnergyCrystal(p)))
                return;


            p.getInventory().setItem(8, p.hasMetadata("tempItem") ? (ItemStack) (p.getMetadata("tempItem").get(0)) : new ItemStack(Material.AIR)); // get rid of crystal


            e.setCancelled(true);
            Location former = e.getEntity().getLocation();
            e.getEntity().remove();

            former.getWorld().spawnEntity(former, EntityType.ENDER_CRYSTAL);

            parent.crystalsActive++;




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
            return !item.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&cEnergy Crystal"));
        }

    }

}
