package fr.skyfighttv.ffarush.Listeners;

import fr.skyfighttv.ffarush.Commands.FFARush;
import fr.skyfighttv.ffarush.Utils.FileManager;
import fr.skyfighttv.ffarush.Utils.Files;
import fr.skyfighttv.ffarush.Utils.PlayersManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.IOException;

public class EntityListeners implements Listener {
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) throws IOException {
        if (event.getEntity() instanceof Player) {
            if (event.getDamager() instanceof TNTPrimed) {
                event.setCancelled(true);
            } else if (event.getDamager() instanceof Player) {
                Player player = (Player) event.getEntity();
                Player damager = (Player) event.getDamager();

                YamlConfiguration config = FileManager.getValues().get(Files.Config);

                if (FFARush.invinciblePlayers.contains(player)
                        || FFARush.invinciblePlayers.contains(damager)) {
                    event.setCancelled(true);
                    return;
                }

                if (player.getHealth() - event.getFinalDamage() < 1) {
                    if (damager.getHealth() > 15.0) {
                        damager.setHealth(20.0);
                    } else {
                        damager.setHealth(damager.getHealth() + 10);
                    }

                    String message = config.getString("Game.KillCommand")
                            .replaceAll("%damager%", damager.getName())
                            .replaceAll("%victim%", player.getName());
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), message);

                    PlayersManager.addKills(damager, 1);
                }
            }
        }
    }
}
