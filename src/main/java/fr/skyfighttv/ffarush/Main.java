package fr.skyfighttv.ffarush;

import fr.skyfighttv.ffarush.Commands.FFARush;
import fr.skyfighttv.ffarush.Commands.FFARushTab;
import fr.skyfighttv.ffarush.Listeners.BlockListeners;
import fr.skyfighttv.ffarush.Listeners.EntityListeners;
import fr.skyfighttv.ffarush.Listeners.PlayerListeners;
import fr.skyfighttv.ffarush.Listeners.Tnt;
import fr.skyfighttv.ffarush.Utils.FileManager;
import fr.skyfighttv.ffarush.Utils.PlayersManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
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
            new BlockListeners(),
            new PlayerListeners(),
            new EntityListeners(),
            new Tnt()
    ));

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

        System.out.println(ANSI_YELLOW + " _____ _____ _    " + ANSI_BLUE + "____  _   _ ____  _   _" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "|  ___|  ___/ \\  " + ANSI_BLUE + "|  _ \\| | | / ___|| | | |" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "| |_  | |_ / _ \\ " + ANSI_BLUE + "| |_) | | | \\___ \\| |_| |" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "|  _| |  _/ ___ \\" + ANSI_BLUE + "|  _ <| |_| |___) |  _  |" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "|_|   |_|/_/   \\_\\" + ANSI_BLUE + "_| \\_\\\\___/|____/|_| |_|" + ANSI_RESET);
        System.out.println(" ");
        System.out.println(ANSI_CYAN + "Loading current player data ..." + ANSI_RESET);
        new PlayersManager();

        System.out.println(ANSI_CYAN + "Loading configuration files in progress ..." + ANSI_RESET);
        try {
            new FileManager();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(ANSI_CYAN + "Finalization of the loading of the plugin in progress ..." + ANSI_RESET);
        getCommand("ffarush").setExecutor(new FFARush());
        getCommand("ffarush").setTabCompleter(new FFARushTab());

        for (Listener listener : listeners)
            getServer().getPluginManager().registerEvents(listener, this);
        System.out.println(ANSI_CYAN + "Loading of the finalized plugin." + ANSI_RESET);
        System.out.println(" ");
    }

    @Override
    public void onDisable() {
        try {
            PlayersManager.saveAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendDebug(String message) {
        Bukkit.getLogger().info(message);
    }

    public static Main getInstance() {
        return Instance;
    }
}
