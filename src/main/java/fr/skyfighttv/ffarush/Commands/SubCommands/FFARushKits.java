package fr.skyfighttv.ffarush.Commands.SubCommands;

import fr.ChadOW.cinventory.cinventory.CInventory;
import fr.ChadOW.cinventory.citem.CItem;
import fr.ChadOW.cinventory.citem.ItemCreator;
import fr.skyfighttv.ffarush.Utils.FileManager;
import fr.skyfighttv.ffarush.Utils.Files;
import fr.skyfighttv.ffarush.Utils.PlayersManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class FFARushKits {
    public FFARushKits(Player player) {
        YamlConfiguration kitsConfig = FileManager.getValues().get(Files.Kits);
        YamlConfiguration config = FileManager.getValues().get(Files.Config);

        CInventory inventory = new CInventory(27, "Kit menu");

        for (String kits : kitsConfig.getKeys(false)) {
            CItem item = new CItem(new ItemCreator(Material.getMaterial(kitsConfig.getString(kits + ".ItemMenu")), 0)
                    .setName(kits)
                    .setLores(kitsConfig.getStringList(kits + ".Lore")));
            item.addEvent((cInventory, cItem, player1, clickContext) -> {
                if (kitsConfig.getKeys(false).contains(cItem.getName())) {
                    if (kitsConfig.getString(cItem.getName() + ".Permission").equals("N/A")
                            || player.hasPermission(kitsConfig.getString(cItem.getName() + ".Permission"))) {
                        try {
                            PlayersManager.setKit(player, cItem.getName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        player.sendMessage(FileManager.getValues().get(Files.Lang).getString("SuccessSelectKit"));

                        inventory.close(player);
                    }
                }
            });
            inventory.addElement(item);
        }
        inventory.open(player);
    }
}
