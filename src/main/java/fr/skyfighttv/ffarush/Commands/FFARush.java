package fr.skyfighttv.ffarush.Commands;

import fr.skyfighttv.ffarush.Commands.SubCommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
            if (args.length == 0) {
                if (player.hasPermission(config.getString("Permissions.HelpStaff"))) {
                    player.sendMessage(config.getString("Messages.FFARushNotFullCommandStaff"));
                } else {
                    player.sendMessage(config.getString("Messages.FFARushNotFullCommandPlayer"));
                }
                return false;
            }

            if(args[0].equalsIgnoreCase("play")) {
                try {
                    new FFARushPlay(player);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(args[0].equalsIgnoreCase("setspawn")) {
                if(!player.hasPermission(config.getString("Permissions.SetSpawn"))) {
                    player.sendMessage(config.getString("Messages.FFARushSetSpawnDontHavePermission"));
                    return false;
                }
                if(args.length == 1) {
                    player.sendMessage(config.getString("Messages.FFARushNotFullCommandSetSpawn"));
                    return false;
                }

                new FFARushSetSpawn(player, args[0]);
            }
            else if(args[0].equalsIgnoreCase("setkit")) {
                if(!player.hasPermission(config.getString("Permissions.SetKit"))) {
                    player.sendMessage(config.getString("Messages.FFARushSetKitDontHavePermission"));
                    return false;
                }
                if(args.length == 1) {
                    player.sendMessage(config.getString("Messages.FFARushNotFullCommandSetKit"));
                    return false;
                }

                new FFARushSetKit(player, args[1]);
            }
            else if(args[0].equalsIgnoreCase("kits")) {
                new FFARushKits(player);
            }
            else if(args[0].equalsIgnoreCase("SetLobby")) {
                new FFARushSetLobby(player);
            }
        }
        return false;
    }
}
