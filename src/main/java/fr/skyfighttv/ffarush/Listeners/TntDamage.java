package fr.skyfighttv.ffarush.Listeners;

import fr.skyfighttv.ffarush.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class TntDamage implements Listener {

    private FileConfiguration config = Main.getInstance().getConfig();

    @EventHandler
    public void onTnt(EntityExplodeEvent event) {
        if(event.getEntity().getType().equals(EntityType.PRIMED_TNT)) {
            for(Block block : event.blockList()) {
                if(block.getType().equals(Material.TNT)) {
                    block.setType(Material.AIR);
                    Entity entity = block.getWorld().spawn(block.getLocation(), TNTPrimed.class);
                    entity.setFireTicks(config.getInt("Config.Tnt.Time"));
                }
                if(config.getIntegerList("Config.Tnt.CanBreakBlocks").contains(block.getType().getId())) {
                    event.getEntity().getWorld().dropItem(block.getLocation().add(0.5,0.0,0.5), new ItemStack(block.getType()));
                    block.setType(Material.AIR);
                }
            }
            for(Player player : event.getEntity().getWorld().getPlayers()) {
                if(event.getEntity().getLocation().getX() + 3 > player.getLocation().getX() && event.getEntity().getLocation().getX() - 3 < player.getLocation().getX() && event.getEntity().getLocation().getZ() + 3 > player.getLocation().getZ() && event.getEntity().getLocation().getZ() - 3 < player.getLocation().getZ() && event.getEntity().getLocation().getY() + 3 > player.getLocation().getY() && event.getEntity().getLocation().getY() - 3 < player.getLocation().getY()) {
                    double X = player.getLocation().getX() - event.getLocation().getX();
                    double Y = player.getLocation().getY() - event.getLocation().getY();
                    double Z = player.getLocation().getZ() - event.getLocation().getZ();
                    Vector vector = player.getVelocity();
                    Vector vector1 = new Vector(X, Y, Z).multiply(config.getDouble("Config.TntFly.MultiplyForceVelocity"));
                    vector.add(vector1);
                    player.setVelocity(vector);
                }
            }
            event.setCancelled(true);
        } else {
            event.setCancelled(true);
        }
    }
}
