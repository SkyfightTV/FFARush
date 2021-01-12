package fr.skyfighttv.ffarush.Commands;

import fr.skyfighttv.ffarush.Commands.SubCommands.*;
import fr.skyfighttv.ffarush.Utils.FileManager;
import fr.skyfighttv.ffarush.Utils.Files;
import fr.skyfighttv.ffarush.Utils.PlayersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FFARush implements CommandExecutor {
    public static List<Player> invinciblePlayers = new ArrayList<>();
    public static List<Player> inGamePlayers = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            YamlConfiguration langConfig = FileManager.getValues().get(Files.Lang);
            YamlConfiguration config = FileManager.getValues().get(Files.Config);

            if (args.length == 0) {
                if (player.hasPermission("FFARush.staff")) {
                    player.sendMessage(langConfig.getString("NotFullCommandStaff"));
                } else {
                    player.sendMessage(langConfig.getString("NotFullCommandPlayer"));
                }
                return false;
            }

            if(args[0].equalsIgnoreCase("play")) {
                if (!config.getBoolean("FFARush.Play")) {
                    player.sendMessage(langConfig.getString("CommandDisabled"));
                    return false;
                }

                try {
                    FFARushPlay.init(player);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(args[0].equalsIgnoreCase("setspawn")) {
                if (!player.hasPermission("FFARush.setspawn")) {
                    player.sendMessage(langConfig.getString("NoPermission"));
                    return false;
                }
                if (args.length == 1) {
                    player.sendMessage(langConfig.getString("NotFullCommandSetSpawn"));
                    return false;
                }

                FFARushSetSpawn.init(player, args[1]);
            }
            else if(args[0].equalsIgnoreCase("setkit")) {
                if (!player.hasPermission("FFARush.setkit")) {
                    player.sendMessage(langConfig.getString("NoPermission"));
                    return false;
                }
                if (args.length == 1) {
                    player.sendMessage(langConfig.getString("NotFullCommandSetKit"));
                    return false;
                }

                FFARushSetKit.init(player, args[1]);
            }
            else if (args[0].equalsIgnoreCase("kits")) {
                if (!config.getBoolean("FFARush.Kits")) {
                    player.sendMessage(langConfig.getString("CommandDisabled"));
                    return false;
                }

                FFARushKits.init(player);
            }
            else if (args[0].equalsIgnoreCase("SetLobby")) {
                if (!player.hasPermission("FFARush.setlobby")) {
                    player.sendMessage(langConfig.getString("NoPermission"));
                    return false;
                }
                FFARushSetLobby.init(player);
            }
            else if (args[0].equalsIgnoreCase("reload")) {
                if (!player.hasPermission("FFARush.reload")) {
                    player.sendMessage(langConfig.getString("NoPermission"));
                    return false;
                }

                FileManager.reloadAll();
                try {
                    PlayersManager.saveAll();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                player.sendMessage(langConfig.getString("SuccessReload"));
            }
            else if (args[0].equalsIgnoreCase("stats")) {
                FFARushStats.init(player);
            }
        }
        return false;
    }
}
