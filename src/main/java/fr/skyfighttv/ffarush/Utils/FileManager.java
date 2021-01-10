package fr.skyfighttv.ffarush.Utils;

import fr.skyfighttv.ffarush.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.HashMap;

public class FileManager {
    private static HashMap<Files, YamlConfiguration> values;
    //private static HashMap<String, File> files;

    public FileManager() throws IOException {
        values = new HashMap<>();

        for (Files files : Files.values()) {
            File fileRessource = new File(Main.getInstance().getDataFolder() + "/" + files.getName() + ".yml");
            if (!fileRessource.exists()) {
                InputStream fileStream = Main.getInstance().getResource(files.getName() + ".yml");
                assert fileStream != null;
                byte[] buffer = new byte[fileStream.available()];
                fileStream.read(buffer);

                fileRessource.createNewFile();
                OutputStream outStream = new FileOutputStream(fileRessource);
                outStream.write(buffer);
            }
            values.put(files, YamlConfiguration.loadConfiguration(fileRessource));
        }
    }

    /*public static void writeInFile(File file, String text) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedWriter.write(text);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Main.sendDebug(file.getName() + " was modify");
    }*/

    public static void save(Files files) {
        File file = new File(Main.getInstance().getDataFolder() + "/" + files.name() + ".yml");
        if (!file.exists()) {
            System.out.println("ERROR : File " + files.name() + " not found !");
            return;
        }
        try {
            values.get(files).save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        values.put(files, YamlConfiguration.loadConfiguration(file));
    }

    public static void saveAll() {
        for (Files files : Files.values())
            save(files);
    }

    public static void reload(Files files) {
        File file = new File(Main.getInstance().getDataFolder() + "/" + files.name() + ".yml");

        if (!file.exists()) {
            System.out.println("ERROR : File " + files.name() + " not found !");
            return;
        }

        values.put(files, YamlConfiguration.loadConfiguration(file));
    }

    public static void reloadAll() {
        for (Files files : Files.values())
            reload(files);
    }

    public static HashMap<Files, YamlConfiguration> getValues() {
        return values;
    }

    /*public static HashMap<String, File> getFiles() {
        return files;
    }*/
}
