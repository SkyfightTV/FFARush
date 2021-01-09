package fr.skyfighttv.ffarush.Commands.SubCommands;

import fr.skyfighttv.ffarush.Commands.FFARush;
import fr.skyfighttv.ffarush.Main;
import fr.skyfighttv.ffarush.Utils.FileManager;
import fr.skyfighttv.ffarush.Utils.Files;
import fr.skyfighttv.ffarush.Utils.PlayersManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class FFARushPlay {
    public FFARushPlay(Player player) throws IOException {
        YamlConfiguration spawnConfig = FileManager.getValues().get(Files.Spawn);
        YamlConfiguration kitsConfig = FileManager.getValues().get(Files.Kits);
        YamlConfiguration langConfig = FileManager.getValues().get(Files.Lang);
        //YamlConfiguration config = FileManager.getValues().get(Files.Config);

        if(!FFARush.inGamePlayers.contains(player)) {
            if(spawnConfig.getConfigurationSection("Spawns").getKeys(false).isEmpty()) return;

            Location location = (Location) spawnConfig.get(new ArrayList<>(spawnConfig.getConfigurationSection("Spawns").getKeys(false)).get(new Random().nextInt(spawnConfig.getConfigurationSection("Spawns").getKeys(false).size())));
            assert location != null;

            player.teleport(location);

            if (!FFARush.inGamePlayers.contains(player))
                FFARush.inGamePlayers.add(player);
            if (!FFARush.invinciblePlayers.contains(player))
                FFARush.invinciblePlayers.add(player);

            player.getInventory().clear();
            player.getInventory().setArmorContents(new ItemStack[0]);

            if(PlayersManager.getKit(player).equals("default")) {
                player.getInventory().setContents((ItemStack[]) kitsConfig.get("default.Content"));
                player.getInventory().setArmorContents((ItemStack[]) kitsConfig.get("default.ArmorContent"));
            } else {
                player.getInventory().setContents((ItemStack[]) kitsConfig.get(PlayersManager.getKit(player) + ".Content");
                player.getInventory().setContents((ItemStack[]) kitsConfig.get(PlayersManager.getKit(player) + ".ArmorContent"));
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                FFARush.invinciblePlayers.remove(player);
            }, (config.getInt("Config.Game.TimeInvincibility") * 20));
        } else {
            player.sendMessage(config.getString("Messages.FFARushCantBecauseAlreadyOnGame"));
        }
    }
}
