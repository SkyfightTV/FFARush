package fr.skyfighttv.ffarush.Commands.SubCommands;

import fr.skyfighttv.ffarush.Utils.FileManager;
import fr.skyfighttv.ffarush.Utils.Files;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class FFARushSetKit {
    public FFARushSetKit(Player player, String kit) {
        YamlConfiguration kitsConfig = FileManager.getValues().get(Files.Kits);
        YamlConfiguration langConfig = FileManager.getValues().get(Files.Lang);

        kitsConfig.set(kit + ".Content", player.getInventory().getContents());
        kitsConfig.set(kit + ".ArmorContent", player.getInventory().getArmorContents());
        kitsConfig.set(kit + ".ItemMenu", "DIAMOND_SWORD");
        kitsConfig.set(kit + ".Lore", new ArrayList<>());
        kitsConfig.set(kit + ".Permission", "N/A");

        FileManager.save(Files.Kits);

        player.sendMessage(langConfig.getString("SuccessSetKit"));
    }
}
