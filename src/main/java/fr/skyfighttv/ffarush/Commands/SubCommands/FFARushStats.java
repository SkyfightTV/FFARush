package fr.skyfighttv.ffarush.Commands.SubCommands;

import fr.skyfighttv.ffarush.Utils.FileManager;
import fr.skyfighttv.ffarush.Utils.Files;
import fr.skyfighttv.ffarush.Utils.PlayersManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class FFARushStats {
    public static void init(Player player) {
        YamlConfiguration langConfig = FileManager.getValues().get(Files.Lang);
        StringBuilder message = new StringBuilder();

        for (String msg : langConfig.getStringList("StatsMessage")) {
            message.append(msg
                    .replaceAll("%death%", PlayersManager.getDeaths(player) + "")
                    .replaceAll("%kill%", PlayersManager.getKills(player) + "")
                    .replaceAll("%ratio%", String.valueOf((PlayersManager.getKills(player) + 1) / (PlayersManager.getDeaths(player) + 1))));
            message.append("\n");
        }

        String messageSend = message.toString();
        player.sendMessage(messageSend.replace("\n", ""));
    }
}
