package fr.skyfighttv.ffarush.Listeners;

import fr.skyfighttv.ffarush.Addons.ScoreboardSign;
import fr.skyfighttv.ffarush.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
public class PlayerListeners implements Listener {

    private FileConfiguration config = Main.getInstance().getConfig();
    private HashMap<Player, ScoreboardSign> scoreboard = new HashMap<Player, ScoreboardSign>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws IOException {
        final Player player = event.getPlayer();

        File playerstorage = new File(Main.getInstance().getDataFolder() + "/Players/");

        File playerfile = new File(Main.getInstance().getDataFolder() + "/Players/" + player.getUniqueId() + ".yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(playerfile);

        if(!playerstorage.exists()) {
            playerstorage.mkdirs();
        }

        if(!playerfile.exists()) {
            playerfile.createNewFile();

            yamlConfiguration = YamlConfiguration.loadConfiguration(playerfile);

            yamlConfiguration.createSection("Pseudo");
            yamlConfiguration.set("Pseudo", player.getDisplayName());
            yamlConfiguration.createSection("InGame");
            yamlConfiguration.set("InGame", false);
            yamlConfiguration.createSection("Invincibilite");
            yamlConfiguration.set("Invincibilite", false);
            yamlConfiguration.createSection("Kills");
            yamlConfiguration.set("Kills", 0);
            yamlConfiguration.createSection("Morts");
            yamlConfiguration.set("Morts", 0);
            yamlConfiguration.createSection("CurrentKit");
            yamlConfiguration.set("CurrentKit", "N/A");

            yamlConfiguration.save(playerfile);
        }

        player.getInventory().clear();
        ItemStack[] armor = new ItemStack[0];
        player.getInventory().setArmorContents(armor);

        Main.getInstance().onJoin(player);

        scoreboard.remove(player);
        scoreboard.put(player, new ScoreboardSign(player, config.getString("Config.Scoreboard.Title")));
        scoreboard.get(player).create();
        List<String> lines = config.getStringList("Config.Scoreboard.Lignes");
        for(int i = 0; i != lines.size(); i++) {
            lines.set(i, lines.get(i).replaceAll("%KILLS%", yamlConfiguration.getInt("Kills") + ""));
            lines.set(i, lines.get(i).replaceAll("%MORTS%", yamlConfiguration.getInt("Morts") + ""));
            if(yamlConfiguration.getInt("Kills") == 0 || yamlConfiguration.getInt("Morts") == 0) {
                lines.set(i, lines.get(i).replaceAll("%RATIO%", "0.00"));
            } else {
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
            lines.set(i, lines.get(i).replaceAll("%RATIO%", decimalFormat.format(yamlConfiguration.getDouble("Kills") / yamlConfiguration.getDouble("Morts")) + ""));
            }
        }
        for(int i = 0; i != lines.size(); i++) {
            scoreboard.get(player).setLine(i, lines.get(i));
        }

        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                if(!player.isOnline()) return;
                if(!scoreboard.containsKey(player)) {
                    return;
                }

                File playerfile = new File(Main.getInstance().getDataFolder() + "/Players/" + player.getUniqueId() + ".yml");
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(playerfile);

                List<String> lines = config.getStringList("Config.Scoreboard.Lignes");
                for(int i = 0; i != lines.size(); i++) {
                    lines.set(i, lines.get(i).replaceAll("%KILLS%", yamlConfiguration.getInt("Kills") + ""));
                    lines.set(i, lines.get(i).replaceAll("%MORTS%", yamlConfiguration.getInt("Morts") + ""));
                    if(yamlConfiguration.getInt("Kills") == 0 || yamlConfiguration.getInt("Morts") == 0) {
                        lines.set(i, lines.get(i).replaceAll("%RATIO%", "0.00"));
                    } else {
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
                        lines.set(i, lines.get(i).replaceAll("%RATIO%", decimalFormat.format(yamlConfiguration.getDouble("Kills") / yamlConfiguration.getDouble("Morts")) + ""));
                    }
                }
                scoreboard.get(player).destroy();
                scoreboard.get(player).create();

                for(int i = 0; i != lines.size(); i++) {
                    scoreboard.get(player).setLine(i, lines.get(i));
                }
            }
        }, 0, (config.getInt("Config.Scoreboard.Update") * 20));

    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        if(event.getItem() == null || event.getAction() == null) return;
        if(event.getItem().getType().equals(Material.FLINT_AND_STEEL) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if(event.getClickedBlock().getType().equals(Material.TNT)) {
                event.getClickedBlock().setType(Material.AIR);
                Entity entity = event.getPlayer().getWorld().spawn(event.getClickedBlock().getLocation().add(0.5,0.0,0.5), TNTPrimed.class);
                entity.setFireTicks(config.getInt("Config.Tnt.Time"));
                event.setCancelled(true);
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) throws IOException {
        if(event.getEntity() instanceof Player) {
            File playerfile = new File(Main.getInstance().getDataFolder() + "/Players/" + event.getEntity().getUniqueId() + ".yml");
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(playerfile);

            if (!yamlConfiguration.getBoolean("InGame")) {
                if (!event.getEntity().isOp()) {
                    event.setCancelled(true);
                }
                return;
            }
        }
        if(event.getEntity() instanceof Player && event.getDamager() instanceof TNTPrimed) {
            event.setCancelled(true);
        } else if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = Bukkit.getPlayer(((Player) event.getEntity()).getDisplayName());
            Player damager = Bukkit.getPlayer(((Player) event.getDamager()).getDisplayName());

            File playerfile = new File(Main.getInstance().getDataFolder() + "/Players/" + damager.getUniqueId() + ".yml");
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(playerfile);

            if(yamlConfiguration.getBoolean("Invincibilite")) {
                event.setCancelled(true);
                return;
            }

            if(player.getHealth() - event.getFinalDamage() < 1) {
                if(damager.getHealth() > 15.0) {
                    damager.setHealth(20.0);
                } else {
                    damager.setHealth(damager.getHealth() + 10);
                }
                String message = config.getString("Config.Kill.Command");
                message = message.replaceAll("%PLAYER%", damager.getDisplayName());
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), message);

                yamlConfiguration.set("Kills", yamlConfiguration.getInt("Kills") + 1);

                yamlConfiguration.save(playerfile);

            }
        }
    }

    @EventHandler
    public void FallDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player && event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void FoodChange(FoodLevelChangeEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            Player player = (Player)event.getEntity();
            event.setCancelled(true);
            if (player.getFoodLevel() < 19.0D) {
                player.setFoodLevel(20);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) throws IOException {
        Player player = event.getEntity();

        File playerfile = new File(Main.getInstance().getDataFolder() + "/Players/" + player.getUniqueId() + ".yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(playerfile);

        File spawnfile = new File(Main.getInstance().getDataFolder() + "/spawn.yml");
        YamlConfiguration yamlConfiguration1 = YamlConfiguration.loadConfiguration(spawnfile);

        yamlConfiguration.set("Morts", yamlConfiguration.getInt("Morts") + 1);
        yamlConfiguration.set("InGame", false);
        yamlConfiguration.save(playerfile);
        
        player.spigot().respawn();

        player.getInventory().clear();
        ItemStack[] armor = new ItemStack[0];
        player.getInventory().setArmorContents(armor);

        Main.getInstance().onJoin(player);

        player.teleport((Location) yamlConfiguration1.get("Lobby"));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) throws IOException {
        Player player = event.getPlayer();

        File playerfile = new File(Main.getInstance().getDataFolder() + "/Players/" + player.getUniqueId() + ".yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(playerfile);

        File spawnfile = new File(Main.getInstance().getDataFolder() + "/spawn.yml");
        YamlConfiguration yamlConfiguration1 = YamlConfiguration.loadConfiguration(spawnfile);

        yamlConfiguration.set("InGame", false);
        yamlConfiguration.save(playerfile);

        player.getInventory().clear();
        ItemStack[] armor = new ItemStack[0];
        player.getInventory().setArmorContents(armor);

        if(!(scoreboard.get(player) == null)) {
            scoreboard.get(player).destroy();
            scoreboard.remove(player);
        }

        player.teleport((Location) yamlConfiguration1.get("Lobby"));
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) throws IOException {
        InventoryView inventoryView = event.getView();

        File playerfile = new File(Main.getInstance().getDataFolder() + "/Players/" + event.getWhoClicked().getUniqueId() + ".yml");
        YamlConfiguration yamlConfiguration1 = YamlConfiguration.loadConfiguration(playerfile);

        if(yamlConfiguration1.getBoolean("InGame")) {
            event.setCancelled(true);
        }
        if(inventoryView.getTitle().equals(config.getString("Config.KitsMenu.Title"))) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack current = event.getCurrentItem();

            File kitsfile = new File(Main.getInstance().getDataFolder() + "/kits.yml");
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(kitsfile);

            for (String kits : yamlConfiguration.getStringList("Kits")) {
                if (current.getItemMeta().getDisplayName().equals(kits)) {
                    if(yamlConfiguration.getString(kits + ".Permission").equals("N/A") || player.hasPermission(yamlConfiguration.getString(kits + ".Permission"))) {
                        yamlConfiguration1.set("CurrentKit", kits);
                        yamlConfiguration1.save(playerfile);

                        player.sendMessage(config.getString("Messages.FFARushSuccessSelectKit"));
                        player.closeInventory();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDrop (PlayerDropItemEvent event) {
        if(!event.getPlayer().isOp()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {

        File playerfile = new File(Main.getInstance().getDataFolder() + "/Players/" + event.getPlayer().getUniqueId() + ".yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(playerfile);

        if(!yamlConfiguration.getBoolean("InGame")) {
            if(!event.getPlayer().isOp()) {
                event.setCancelled(true);
            }
            if(!event.getItem().hasItemMeta()) {
                return;
            }
            if(event.getItem().getType().equals(Material.AIR) || event.getItem() == null)  {
                return;
            }
            if(event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(config.getString("Config.Lobby.StartItem.Title"))) {
                Bukkit.dispatchCommand(event.getPlayer(), config.getString("Config.Lobby.StartItem.Command"));
            } else if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(config.getString("Config.Lobby.KitsItem.Title"))) {
                Bukkit.dispatchCommand(event.getPlayer(), config.getString("Config.Lobby.KitsItem.Command"));
            }
        }
    }
}
