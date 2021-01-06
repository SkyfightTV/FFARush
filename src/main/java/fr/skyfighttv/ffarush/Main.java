package fr.skyfighttv.ffarush;

import fr.skyfighttv.ffarush.Commands.FFARush;
import fr.skyfighttv.ffarush.Listeners.PlaceBlock;
import fr.skyfighttv.ffarush.Listeners.PlayerListeners;
import fr.skyfighttv.ffarush.Listeners.TntDamage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main Instance;

    public static Main getInstance() {
        return Instance;
    }

    public void onJoin(Player player) {
        player.getInventory().clear();
        ItemStack[] armor = new ItemStack[0];
        player.getInventory().setArmorContents(armor);

        ItemStack itemStack = new ItemStack(Material.getMaterial(getConfig().getString("Config.Lobby.StartItem.Material")));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(getConfig().getString("Config.Lobby.StartItem.Title"));
        itemMeta.setLore(getConfig().getStringList("Config.Lobby.StartItem.Lore"));
        itemStack.setItemMeta(itemMeta);

        ItemStack itemStack1 = new ItemStack(Material.getMaterial(getConfig().getString("Config.Lobby.KitsItem.Material")));
        ItemMeta itemMeta1 = itemStack1.getItemMeta();
        itemMeta1.setDisplayName(getConfig().getString("Config.Lobby.KitsItem.Title"));
        itemMeta1.setLore(getConfig().getStringList("Config.Lobby.KitsItem.Lore"));
        itemStack1.setItemMeta(itemMeta1);

        player.getInventory().setItem(getConfig().getInt("Config.Lobby.StartItem.Location"), itemStack);
        player.getInventory().setItem(getConfig().getInt("Config.Lobby.KitsItem.Location"), itemStack1);
    }

    @Override
    public void onEnable() {
        Main.Instance = this;

        getCommand("ffarush").setExecutor(new FFARush());

        getServer().getPluginManager().registerEvents(new TntDamage(), this);
        getServer().getPluginManager().registerEvents(new PlaceBlock(), this);
        getServer().getPluginManager().registerEvents(new PlayerListeners(), this);

        saveDefaultConfig();
    }

    @Override
    public void onDisable() {

    }
}
