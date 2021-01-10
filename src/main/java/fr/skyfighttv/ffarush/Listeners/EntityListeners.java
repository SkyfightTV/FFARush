package fr.skyfighttv.ffarush.Listeners;

import fr.skyfighttv.ffarush.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;
import java.io.IOException;

public class EntityListeners implements Listener {
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

}
