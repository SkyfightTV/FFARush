package fr.skyfighttv.ffarush.Commands.SubCommands;

import fr.skyfighttv.ffarush.Utils.FileManager;
import fr.skyfighttv.ffarush.Utils.Files;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class FFARushSetLobby {
    public FFARushSetLobby(Player player) {
        YamlConfiguration spawnConfig = FileManager.getValues().get(Files.Spawn);
        YamlConfiguration lang = FileManager.getValues().get(Files.Lang);

        spawnConfig.set("Lobby", player.getLocation());

        FileManager.save(Files.Spawn);

        player.sendMessage(config.getString("Messages.FFARushSuccessSetLobby"));
    }
}
