package fr.skyfighttv.ffarush.Commands.SubCommands;

import fr.skyfighttv.ffarush.Utils.FileManager;
import fr.skyfighttv.ffarush.Utils.Files;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FFARushSetKit {
    public FFARushSetKit(Player player, String kit) {
        YamlConfiguration kitsConfig = FileManager.getValues().get(Files.Kits);
        YamlConfiguration langConfig = FileManager.getValues().get(Files.Lang);

        List<ItemStack> content = new ArrayList<>(Arrays.asList(player.getInventory().getContents()));
        for (int i = 0; i < 4; i ++) {
            content.remove(content.size() - 1);
        }

        kitsConfig.set(kit + ".Content", content);
        kitsConfig.set(kit + ".ArmorContent", player.getInventory().getArmorContents());
        kitsConfig.set(kit + ".ItemMenu", "DIAMOND_SWORD");
        kitsConfig.set(kit + ".Lore", new ArrayList<>());
        kitsConfig.set(kit + ".Permission", "N/A");

        FileManager.save(Files.Kits);

        player.sendMessage(langConfig.getString("SuccessSetKit"));
    }
}
