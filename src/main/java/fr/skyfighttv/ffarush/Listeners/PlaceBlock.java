package fr.skyfighttv.ffarush.Listeners;

import fr.skyfighttv.ffarush.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.io.File;

public class PlaceBlock implements Listener {

    @EventHandler
    public void onPlace(final BlockPlaceEvent event) {

        File playerfile = new File(Main.getInstance().getDataFolder() + "/Players/" + event.getPlayer().getUniqueId() + ".yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(playerfile);

        if(yamlConfiguration.getBoolean("InGame")) {
            if (Main.getInstance().getConfig().getIntegerList("Config.AutoDelete.Blocks").contains(event.getBlock().getType().getId())) {
                event.setCancelled(true);
                Block block = event.getBlockPlaced().getLocation().getBlock();
                block.setType(event.getBlock().getType());
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        event.getBlockPlaced().setType(Material.AIR);
                    }
                }, (Main.getInstance().getConfig().getInt("Config.AutoDelete.Time") * 20));
            }
        } else {
            if(!event.getPlayer().isOp()) {
                event.setCancelled(true);
            }
        }
    }
}
