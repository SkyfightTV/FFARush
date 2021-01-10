package fr.skyfighttv.ffarush.Listeners;

import fr.skyfighttv.ffarush.Commands.FFARush;
import fr.skyfighttv.ffarush.Main;
import fr.skyfighttv.ffarush.Utils.FileManager;
import fr.skyfighttv.ffarush.Utils.Files;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListeners implements Listener {
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if(FFARush.inGamePlayers.contains(event.getPlayer())) {
            YamlConfiguration config = FileManager.getValues().get(Files.Config);

            if (config.getStringList("AutoDelete.Blocks").contains(event.getBlock().getType().name())) {
                event.setCancelled(true);
                Block block = event.getBlockPlaced().getLocation().getBlock();
                block.setType(event.getBlock().getType());
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> event.getBlockPlaced().setType(Material.AIR), (config.getInt("AutoDelete.Time") * 20));
            }
        }
    }
}
