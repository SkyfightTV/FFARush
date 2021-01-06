package fr.skyfighttv.ffarush.Commands;

import fr.skyfighttv.ffarush.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FFARush implements CommandExecutor {
    private FileConfiguration config = Main.getInstance().getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length == 0) {
                if (player.hasPermission(config.getString("Permissions.HelpStaff"))) {
                    player.sendMessage(config.getString("Messages.FFARushNotFullCommandStaff"));
                } else {
                    player.sendMessage(config.getString("Messages.FFARushNotFullCommandPlayer"));
                }
                return false;
            }
            if(args[0].equalsIgnoreCase("start")) {
                File spawnfile = new File(Main.getInstance().getDataFolder() + "/spawn.yml");
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(spawnfile);

                File playerfile = new File(Main.getInstance().getDataFolder() + "/Players/" + player.getUniqueId() + ".yml");
                YamlConfiguration yamlConfiguration1 = YamlConfiguration.loadConfiguration(playerfile);

                File kitsfile = new File(Main.getInstance().getDataFolder() + "/kits.yml");
                YamlConfiguration yamlConfiguration2 = YamlConfiguration.loadConfiguration(kitsfile);

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
            else if(args[0].equalsIgnoreCase("setspawn")) {
                if(!player.hasPermission(config.getString("Permissions.SetSpawn"))) {
                    player.sendMessage(config.getString("Messages.FFARushSetSpawnDontHavePermission"));
                    return false;
                }
                if(args.length == 1) {
                    player.sendMessage(config.getString("Messages.FFARushNotFullCommandSetSpawn"));
                    return false;
                }

                File spawnfile = new File(Main.getInstance().getDataFolder() + "/spawn.yml");
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(spawnfile);

                if(!spawnfile.exists()) {
                    try {
                        spawnfile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    yamlConfiguration.createSection("Spawn");
                    try {
                        yamlConfiguration.save(spawnfile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                List<Integer> spawn = yamlConfiguration.getIntegerList("Spawn");
                spawn.add(Integer.valueOf(args[1]));
                yamlConfiguration.set("Spawn", spawn);

                yamlConfiguration.createSection(args[1]);
                yamlConfiguration.set(args[1], player.getLocation());

                try {
                    yamlConfiguration.save(spawnfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                player.sendMessage(config.getString("Messages.FFARushSuccessSetSpawn"));
            }
            else if(args[0].equalsIgnoreCase("setkit")) {
                if(!player.hasPermission(config.getString("Permissions.SetKit"))) {
                    player.sendMessage(config.getString("Messages.FFARushSetKitDontHavePermission"));
                    return false;
                }
                if(args.length == 1) {
                    player.sendMessage(config.getString("Messages.FFARushNotFullCommandSetKit"));
                    return false;
                }

                File kitsfile = new File(Main.getInstance().getDataFolder() + "/kits.yml");
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(kitsfile);

                if(!kitsfile.exists()) {
                    try {
                        kitsfile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    yamlConfiguration.createSection("Kits");
                    try {
                        yamlConfiguration.save(kitsfile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                List<String> kits = yamlConfiguration.getStringList("Kits");
                kits.add(args[1]);
                yamlConfiguration.set("Kits", kits);

                yamlConfiguration.createSection(args[1]);
                yamlConfiguration.createSection(args[1] + ".Content");
                yamlConfiguration.createSection(args[1] + ".ArmorContent");
                yamlConfiguration.set(args[1] + ".Content", player.getInventory().getContents());
                yamlConfiguration.set(args[1] + ".ArmorContent", player.getInventory().getArmorContents());
                yamlConfiguration.createSection(args[1] + ".ItemMenu");
                yamlConfiguration.set(args[1] + ".ItemMenu", "DIAMOND_SWORD");
                yamlConfiguration.createSection(args[1] + ".Lore");
                List<String> lore = yamlConfiguration.getStringList(args[1] + ".Lore");
                yamlConfiguration.set(args[1] + ".Lore", lore);
                yamlConfiguration.createSection(args[1] + ".Permission");
                yamlConfiguration.set(args[1] + ".Permission", "N/A");

                try {
                    yamlConfiguration.save(kitsfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                player.sendMessage(config.getString("Messages.FFARushSuccessSetKit"));
            }
            else if(args[0].equalsIgnoreCase("kits")) {
                File kitsfile = new File(Main.getInstance().getDataFolder() + "/kits.yml");
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(kitsfile);

                if(!kitsfile.exists()) {
                    player.sendMessage(config.getString("Messages.FFARushNoneKits"));
                    return false;
                }

                Inventory inventory = Bukkit.createInventory(null, config.getInt("Config.KitsMenu.Size"), config.getString("Config.KitsMenu.Title"));

                int emplacement = 0;
                for (String kits : yamlConfiguration.getStringList("Kits")) {
                    ItemStack itemStack = new ItemStack(Material.getMaterial(yamlConfiguration.getString(kits + ".ItemMenu")));
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName(kits);
                    itemMeta.setLore(yamlConfiguration.getStringList(kits + ".Lore"));
                    itemStack.setItemMeta(itemMeta);
                    inventory.setItem(emplacement, itemStack);
                    emplacement++;
                }

                player.openInventory(inventory);
            }
            else if(args[0].equalsIgnoreCase("Leave")) {
                File playerfile = new File(Main.getInstance().getDataFolder() + "/Players/" + player.getUniqueId() + ".yml");
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(playerfile);

                File spawnfile = new File(Main.getInstance().getDataFolder() + "/spawn.yml");
                YamlConfiguration yamlConfiguration1 = YamlConfiguration.loadConfiguration(spawnfile);

                yamlConfiguration.set("InGame", false);
                try {
                    yamlConfiguration.save(playerfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                player.getInventory().clear();
                ItemStack[] armor = new ItemStack[0];
                player.getInventory().setArmorContents(armor);

                Main.getInstance().onJoin(player);

                player.teleport((Location) yamlConfiguration1.get("Lobby"));
            }
            else if(args[0].equalsIgnoreCase("SetLobby")) {
                File spawnfile = new File(Main.getInstance().getDataFolder() + "/spawn.yml");
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(spawnfile);

                yamlConfiguration.createSection("Lobby");
                yamlConfiguration.set("Lobby", player.getLocation());
                try {
                    yamlConfiguration.save(spawnfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                player.sendMessage(config.getString("Messages.FFARushSuccessSetLobby"));
            }
        }
        return false;
    }
}
