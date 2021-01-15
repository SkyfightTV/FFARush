package fr.skyfighttv.ffarush.Listeners;

import fr.ChadOW.cinventory.citem.ItemCreator;
import fr.skyfighttv.ffarush.Commands.FFARush;
import fr.skyfighttv.ffarush.Utils.FileManager;
import fr.skyfighttv.ffarush.Utils.Files;
import fr.skyfighttv.ffarush.Utils.PlayersManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class PlayerListeners implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws IOException {
        Player player = event.getPlayer();

        PlayersManager.create(player);

        clearInv(player);
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        if(event.getItem() == null) return;

        if(event.getItem().getType().equals(Material.FLINT_AND_STEEL) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if(event.getClickedBlock().getType().equals(Material.TNT)) {
                event.getClickedBlock().setType(Material.AIR);

                Entity entity = event.getPlayer().getWorld().spawn(event.getClickedBlock().getLocation().add(0.5, 0.0, 0.5), TNTPrimed.class);
                FileManager.getValues().get(Files.Config);
                entity.setFireTicks(FileManager.getValues().get(Files.Config).getInt("Tnt.Time"));
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void FoodChange(FoodLevelChangeEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            Player player = (Player)event.getEntity();
            player.setFoodLevel(20);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) throws IOException {
        Player player = event.getEntity();

        if (FFARush.inGamePlayers.contains(player)) {

            PlayersManager.addDeath(player, 1);
            FFARush.inGamePlayers.remove(player);

            player.spigot().respawn();

            event.getDrops().clear();

            clearInv(player);

            if (!FileManager.getValues().get(Files.Spawn).contains("Lobby")
                    || FileManager.getValues().get(Files.Spawn).get("Lobby") == null){
                player.sendMessage("Please set a lobby : /ffarush setlobby");
                return;
            }

            player.teleport((Location) FileManager.getValues().get(Files.Spawn).get("Lobby"));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        FFARush.inGamePlayers.remove(player);

        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[0]);

        player.teleport((Location) FileManager.getValues().get(Files.Spawn).get("Lobby"));
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (FFARush.inGamePlayers.contains(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (!FFARush.inGamePlayers.contains(event.getPlayer())){
            if(!event.getPlayer().isOp()) {
                event.setCancelled(true);
            }
            if(event.getItem() == null
                    || event.getItem().getType().equals(Material.AIR)
                    || !event.getItem().hasItemMeta()) {
                return;
            }

            YamlConfiguration config = FileManager.getValues().get(Files.Config);

            for (String items : config.getConfigurationSection("LobbyItems").getKeys(false)) {
                if (event.getItem().getItemMeta().getLore().equals(config.getStringList("LobbyItems." + items + ".Lore"))
                        && event.getItem().getItemMeta().getDisplayName().equals(config.getString("LobbyItems." + items + ".Title"))) {
                    Bukkit.dispatchCommand(event.getPlayer(), config.getString("LobbyItems." + items + ".Command"));
                }
            }
        }
    }

    private void clearInv(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[0]);

        YamlConfiguration config = FileManager.getValues().get(Files.Config);

        for (String items : config.getConfigurationSection("LobbyItems").getKeys(false)) {
            player.getInventory().setItem(config.getInt("LobbyItems." + items + ".Location"),
                    new ItemCreator(Material.getMaterial(config.getString("LobbyItems." + items + ".Material")), 0)
                            .setName(config.getString("LobbyItems." + items + ".Title"))
                            .setLores(config.getStringList("LobbyItems." + items + ".Lore"))
                            .getItem());
        }
    }
}
