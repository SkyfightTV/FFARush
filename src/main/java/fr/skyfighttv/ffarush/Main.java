package fr.skyfighttv.ffarush;

import fr.skyfighttv.ffarush.Commands.FFARush;
import fr.skyfighttv.ffarush.Listeners.PlaceBlock;
import fr.skyfighttv.ffarush.Utils.FileManager;
import fr.skyfighttv.ffarush.Utils.PlayersManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends JavaPlugin {
    public static String ANSI_RESET = "";
    public static String ANSI_BLACK = "";
    public static String ANSI_RED = "";
    public static String ANSI_GREEN = "";
    public static String ANSI_YELLOW = "";
    public static String ANSI_BLUE = "";
    public static String ANSI_PURPLE = "";
    public static String ANSI_CYAN = "";
    public static String ANSI_WHITE = "";
    private static Main Instance;

    private List<Listener> listeners = new ArrayList<>(Arrays.asList(
            new PlaceBlock()
    ));

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
        Instance = this;
        saveDefaultConfig();

        if (getConfig().getBoolean("ColorConsole")) {
            ANSI_RESET = "\u001B[0m";
            ANSI_BLACK = "\u001B[30m";
            ANSI_RED = "\u001B[31m";
            ANSI_GREEN = "\u001B[32m";
            ANSI_YELLOW = "\u001B[33m";
            ANSI_BLUE = "\u001B[34m";
            ANSI_PURPLE = "\u001B[35m";
            ANSI_CYAN = "\u001B[36m";
            ANSI_WHITE = "\u001B[37m";
        }

        try {
            new FileManager();
        } catch (IOException e) {
            e.printStackTrace();
        }

        getCommand("ffarush").setExecutor(new FFARush());

        for (Listener listener : listeners)
            getServer().getPluginManager().registerEvents(listener, this);

    }

    @Override
    public void onDisable() {
        try {
            PlayersManager.saveAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Main getInstance() {
        return Instance;
    }
}
