package fr.skyfighttv.ffarush.Commands.SubCommands;

import fr.skyfighttv.ffarush.Main;
import fr.skyfighttv.ffarush.Utils.FileManager;
import fr.skyfighttv.ffarush.Utils.Files;
import fr.skyfighttv.ffarush.Utils.PlayersManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FFARushStart {
    public FFARushStart(Player player) {
        File spawnfile = new File(Main.getInstance().getDataFolder() + "/spawn.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(spawnfile);

        File playerfile = new File(Main.getInstance().getDataFolder() + "/Players/" + player.getUniqueId() + ".yml");
        YamlConfiguration yamlConfiguration1 = YamlConfiguration.loadConfiguration(playerfile);

        File kitsfile = new File(Main.getInstance().getDataFolder() + "/kits.yml");
        YamlConfiguration yamlConfiguration2 = YamlConfiguration.loadConfiguration(kitsfile);

        YamlConfiguration spawnConfig = FileManager.getValues().get(Files.Spawn);
        YamlConfiguration playerConfig = PlayersManager.getPlayer(player);
        YamlConfiguration kitsConfig = FileManager.getValues().get(Files.Kits);
        YamlConfiguration langConfig = FileManager.getValues().get(Files.Lang);
        //YamlConfiguration config = FileManager.getValues().get(Files.Config);

        if(!yamlConfiguration1.getBoolean("InGame")) {
            List<Integer> spawn = yamlConfiguration.getIntegerList("Spawn");
            Random random = new Random();
            if(yamlConfiguration.getIntegerList("Spawn").isEmpty()) return false;
            Location location = (Location) yamlConfiguration.get(String.valueOf(random.nextInt(spawn.size() - 1 + 1) + 1));

            player.teleport(location);
            yamlConfiguration1.set("InGame", true);
            yamlConfiguration1.set("Invincibilite", true);
            try {
                yamlConfiguration1.save(playerfile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.getInventory().clear();
            ItemStack[] armor = new ItemStack[0];
            player.getInventory().setArmorContents(armor);

            if(!yamlConfiguration1.getString("CurrentKit").equals("N/A")) {
                int emplacement = 0;
                for (Object kit : yamlConfiguration2.getList(yamlConfiguration1.getString("CurrentKit") + ".Content")) {
                    if(!(kit == null)) {
                        player.getInventory().setItem(emplacement, (ItemStack) kit);
                    }
                    emplacement++;
                }
                final List<ItemStack> itemStackList = new ArrayList<ItemStack>();
                for (Object kit : yamlConfiguration2.getList(yamlConfiguration1.getString("CurrentKit") + ".ArmorContent")) {
                    itemStackList.add((ItemStack) kit);
                }
                ItemStack[] itemStacks = itemStackList.toArray(new ItemStack[0]);
                player.getInventory().setArmorContents(itemStacks);
            } else {
                player.getInventory().setContents((ItemStack[]) yamlConfiguration2.get( yamlConfiguration2.getStringList("Kits").get(0) + ".Content"));
                player.getInventory().setArmorContents((ItemStack[]) yamlConfiguration2.get(yamlConfiguration2.getStringList("Kits").get(0) + ".ArmorContent"));
            }

            Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
                @Override
                public void run() {
                    File playerfile = new File(Main.getInstance().getDataFolder() + "/Players/" + player.getUniqueId() + ".yml");
                    YamlConfiguration yamlConfiguration1 = YamlConfiguration.loadConfiguration(playerfile);
                    yamlConfiguration1.set("Invincibilite", false);
                    try {
                        yamlConfiguration1.save(playerfile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, (config.getInt("Config.Game.TimeInvincibility") * 20));
        } else {
            player.sendMessage(config.getString("Messages.FFARushCantBecauseAlreadyOnGame"));
        }
    }
}
