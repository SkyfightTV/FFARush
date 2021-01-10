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
import java.util.List;
import java.util.Random;

public class FFARushPlay {
    public FFARushPlay(Player player) throws IOException {
        YamlConfiguration spawnConfig = FileManager.getValues().get(Files.Spawn);
        FileManager.reload(Files.Kits);
        YamlConfiguration kitsConfig = FileManager.getValues().get(Files.Kits);
        YamlConfiguration langConfig = FileManager.getValues().get(Files.Lang);
        YamlConfiguration config = FileManager.getValues().get(Files.Config);

        if(!FFARush.inGamePlayers.contains(player)) {
            if(spawnConfig.getConfigurationSection("Spawns").getKeys(false).isEmpty()) return;

            int random = new Random().nextInt(spawnConfig.getConfigurationSection("Spawns").getKeys(false).size());
            Location location = (Location) spawnConfig.get("Spawns." + new ArrayList<>(spawnConfig.getConfigurationSection("Spawns").getKeys(false)).get(random));
            assert location != null;

            player.teleport(location);

            if (!FFARush.inGamePlayers.contains(player))
                FFARush.inGamePlayers.add(player);
            if (!FFARush.invinciblePlayers.contains(player))
                FFARush.invinciblePlayers.add(player);

            player.getInventory().clear();
            player.getInventory().setArmorContents(new ItemStack[0]);

            System.out.println(PlayersManager.getKit(player) + " / " + PlayersManager.getPlayer(player).getString("Kit"));

            int emplacement = 0;
            for (Object kit : kitsConfig.getList(PlayersManager.getKit(player) + ".Content")) {
                if(!(kit == null)) {
                    player.getInventory().setItem(emplacement, (ItemStack) kit);
                }
                emplacement++;
            }
            final List<ItemStack> itemStackList = new ArrayList<ItemStack>();
            for (Object kit : kitsConfig.getList(PlayersManager.getKit(player) + ".ArmorContent")) {
                itemStackList.add((ItemStack) kit);
            }
            ItemStack[] itemStacks = itemStackList.toArray(new ItemStack[0]);
            player.getInventory().setArmorContents(itemStacks);

            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> FFARush.invinciblePlayers.remove(player), (config.getInt("Game.Invincibility") * 20));
        } else {
            player.sendMessage(langConfig.getString("AlreadyOnGame"));
        }
    }
}
