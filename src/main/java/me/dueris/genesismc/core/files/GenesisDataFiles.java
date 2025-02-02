package me.dueris.genesismc.core.files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Set;

import static org.bukkit.Bukkit.getLogger;

public class GenesisDataFiles {

    public static FileConfiguration mainConfig;
    public static File mainConfigFile;
    public static FileConfiguration orbconfig;
    public static File orbConfigFile;
    public static File lang;
    public static FileConfiguration englishLang;
    public static File englishLangFile;
    public static FileConfiguration germanLang;
    public static File germanLangFile;
    public static FileConfiguration russianLang;
    public static File russianLangFile;
    public static FileConfiguration chineseLang;
    public static File chineseLangFile;

    public static void setup() {
        File lang = new File(Bukkit.getServer().getPluginManager().getPlugin("GenesisMC").getDataFolder(), "lang");
        orbConfigFile = new File(Bukkit.getServer().getPluginManager().getPlugin("GenesisMC").getDataFolder(), "orboforigins.yml");
        mainConfigFile = new File(Bukkit.getServer().getPluginManager().getPlugin("GenesisMC").getDataFolder(), "origin-server.yml");
        englishLangFile = new File(Bukkit.getServer().getPluginManager().getPlugin("GenesisMC").getDataFolder() + File.separator + "lang", "english.yml");
        germanLangFile = new File(Bukkit.getServer().getPluginManager().getPlugin("GenesisMC").getDataFolder() + File.separator + "lang", "german.yml");
        russianLangFile = new File(Bukkit.getServer().getPluginManager().getPlugin("GenesisMC").getDataFolder() + File.separator + "lang", "russian.yml");
        chineseLangFile = new File(Bukkit.getServer().getPluginManager().getPlugin("GenesisMC").getDataFolder() + File.separator + "lang", "trad-chinese.yml");
        if (!lang.exists()) {
            lang.mkdirs();
        }


        if (!mainConfigFile.exists()) {
            try {
                mainConfigFile.createNewFile();
            } catch (IOException var3) {
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Unable to create origin-server.yml. Please reload or restart server. If that doesn't work, contact Dueris on her Discord server");
            }
        }

        if (!orbConfigFile.exists()) {
            try {
                orbConfigFile.createNewFile();
            } catch (IOException var2) {
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Unable to create orboforigins.yml. Please reload or restart server. If that doesn't work, contact Dueris on her Discord server");
            }
        }

        if (!englishLangFile.exists()) {
            try {
                englishLangFile.createNewFile();
            } catch (IOException var21) {
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Unable to create english.yml. Please reload or restart server. If that doesn't work, contact Dueris on her Discord server");
            }
        }

        if (!germanLangFile.exists()) {
            try {
                germanLangFile.createNewFile();
            } catch (IOException var28) {
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Unable to create german.yml. Please reload or restart server. If that doesn't work, contact Dueris on her Discord server");
            }
        }

        if (!russianLangFile.exists()) {
            try {
                russianLangFile.createNewFile();
            } catch (IOException var7) {
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Unable to create russian.yml. Please reload or restart server. If that doesn't work, contact Dueris on her Discord server");
            }
        }
        if (!chineseLangFile.exists()) {
            try {
                chineseLangFile.createNewFile();
            } catch (IOException var6) {
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Unable to create trad-chinese.yml. Please reload or restart server. If that doesn't work, contact Dueris on her Discord server");
            }
        }

    }

    public static void save() {
        try {
            mainConfig.save(mainConfigFile);
        } catch (IOException var2) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Couldn't save yml file: " + var2.getMessage());
        }

        try {
            orbconfig.save(orbConfigFile);
        } catch (IOException var1) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Couldn't save yml file: " + var1.getMessage());
        }

        try {
            englishLang.save(englishLangFile);
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Couldn't save yml file: " + e.getMessage());
        }

        try {
            russianLang.save(russianLangFile);
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Couldn't save yml file: " + e.getMessage());
        }

        try {
            germanLang.save(germanLangFile);
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Couldn't save yml file: " + e.getMessage());
        }
        try {
            chineseLang.save(chineseLangFile);
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Couldn't save yml file: " + e.getMessage());
        }

    }

    private static void addMissingLines(File configFile, String filePath) {
        try {
            File tempConfigFile = new File(Bukkit.getServer().getPluginManager().getPlugin("GenesisMC").getDataFolder(), "tempfile.yml");
            InputStream inputStream = Bukkit.getServer().getPluginManager().getPlugin("GenesisMC").getResource(filePath);
            Files.copy(inputStream, tempConfigFile.toPath());

            FileConfiguration tempConfig = YamlConfiguration.loadConfiguration(tempConfigFile);
            FileConfiguration sourceConfig = YamlConfiguration.loadConfiguration(configFile);
            Set<String> tempKeys = tempConfig.getKeys(true);
            Set<String> sourceKeys = sourceConfig.getKeys(true);

            if (sourceKeys.equals(tempKeys)) {
                tempConfigFile.delete();
                return;
            }
            for (String key : sourceKeys) tempKeys.remove(key);
            for (String key : tempKeys) sourceConfig.set(key, tempConfig.get(key));

            sourceConfig.save(configFile);
            tempConfigFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void loadMainConfig() {
        mainConfigFile = new File(Bukkit.getServer().getPluginManager().getPlugin("GenesisMC").getDataFolder(), "origin-server.yml");
        if (!mainConfigFile.exists()) {
            mainConfigFile.getParentFile().mkdirs();
            try (InputStream inputStream = Bukkit.getServer().getPluginManager().getPlugin("GenesisMC").getResource("origin-server.yml")) {
                Files.copy(inputStream, mainConfigFile.toPath());
            } catch (Exception e) {
                getLogger().severe("Failed to create custom configuration file: " + e.getMessage());
            }
        }

        // Load the custom configuration file
        addMissingLines(mainConfigFile, "origin-server.yml");
        mainConfig = YamlConfiguration.loadConfiguration(mainConfigFile);
    }

    public static void loadOrbConfig() {
        orbConfigFile = new File(Bukkit.getServer().getPluginManager().getPlugin("GenesisMC").getDataFolder(), "orboforigins.yml");
        if (!orbConfigFile.exists()) {
            orbConfigFile.getParentFile().mkdirs();
            try (InputStream inputStream = Bukkit.getServer().getPluginManager().getPlugin("GenesisMC").getResource("orboforigins.yml")) {
                Files.copy(inputStream, orbConfigFile.toPath());
            } catch (Exception e) {
                getLogger().severe("Failed to create custom configuration file: " + e.getMessage());
            }
        }

        // Load the custom configuration file
        addMissingLines(orbConfigFile, "orboforigins.yml");
        orbconfig = YamlConfiguration.loadConfiguration(orbConfigFile);
    }

    public static void loadLangConfig() {
        englishLangFile = new File(Bukkit.getServer().getPluginManager().getPlugin("GenesisMC").getDataFolder() + File.separator + "/lang/" + File.separator, "english.yml");
        if (!englishLangFile.exists()) {
            englishLangFile.getParentFile().mkdirs();
            try (InputStream inputStream = Bukkit.getServer().getPluginManager().getPlugin("GenesisMC").getResource("langFiles/english.yml")) {
                Files.copy(inputStream, englishLangFile.toPath());
            } catch (Exception e) {
                getLogger().severe("Failed to create custom configuration file: " + e.getMessage());
            }
        }

        // Load the custom configuration file
        addMissingLines(englishLangFile, "langFiles/english.yml");
        englishLang = YamlConfiguration.loadConfiguration(englishLangFile);

        russianLangFile = new File(Bukkit.getServer().getPluginManager().getPlugin("GenesisMC").getDataFolder() + File.separator + "/lang/" + File.separator, "russian.yml");
        if (!russianLangFile.exists()) {
            russianLangFile.getParentFile().mkdirs();
            try (InputStream inputStream = Bukkit.getServer().getPluginManager().getPlugin("GenesisMC").getResource("langFiles/russian.yml")) {
                Files.copy(inputStream, russianLangFile.toPath());
            } catch (Exception e) {
                getLogger().severe("Failed to create custom configuration file: " + e.getMessage());
            }
        }

        // Load the custom configuration file
        addMissingLines(russianLangFile, "langFiles/russian.yml");
        russianLang = YamlConfiguration.loadConfiguration(russianLangFile);

        germanLangFile = new File(Bukkit.getServer().getPluginManager().getPlugin("GenesisMC").getDataFolder() + File.separator + "/lang/" + File.separator, "german.yml");
        if (!germanLangFile.exists()) {
            germanLangFile.getParentFile().mkdirs();
            try (InputStream inputStream = Bukkit.getServer().getPluginManager().getPlugin("GenesisMC").getResource("langFiles/german.yml")) {
                Files.copy(inputStream, germanLangFile.toPath());
            } catch (Exception e) {
                getLogger().severe("Failed to create custom configuration file: " + e.getMessage());
            }
        }

        // Load the custom configuration file
        addMissingLines(germanLangFile, "langFiles/german.yml");
        germanLang = YamlConfiguration.loadConfiguration(germanLangFile);

        chineseLangFile = new File(Bukkit.getServer().getPluginManager().getPlugin("GenesisMC").getDataFolder() + File.separator + "/lang/" + File.separator, "trad-chinese.yml");
        if (!chineseLangFile.exists()) {
            chineseLangFile.getParentFile().mkdirs();
            try (InputStream inputStream = Bukkit.getServer().getPluginManager().getPlugin("GenesisMC").getResource("langFiles/trad-chinese.yml")) {
                Files.copy(inputStream, chineseLangFile.toPath());
            } catch (Exception e) {
                getLogger().severe("Failed to create custom configuration file: " + e.getMessage());
            }
        }

        // Load the custom configuration file
        addMissingLines(chineseLangFile, "langFiles/trad-chinese.yml");
        chineseLang = YamlConfiguration.loadConfiguration(chineseLangFile);
    }

    public static FileConfiguration getMainConfig() {
        return mainConfig;
    }

    public static FileConfiguration getOrbCon() {
        return orbconfig;
    }

    public static FileConfiguration getEnglishLang() {
        return englishLang;
    }

    public static FileConfiguration getGermanLang() {
        return germanLang;
    }

    public static FileConfiguration getRussianLang() {
        return russianLang;
    }

    public static FileConfiguration getChineseLang() {
        return chineseLang;
    }

}
