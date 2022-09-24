package xyz.raitaki.AkumaKOTH.Utils;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.raitaki.AkumaKOTH.AkumaKOTH;

import java.io.File;
import java.util.HashMap;

public class ConfigManager {
    @Getter private static HashMap<String, YamlConfiguration> configs = new HashMap<>();

    @SneakyThrows
    public static void loadConfig(String name) {
        File file = new File(AkumaKOTH.getInstance().getDataFolder(), name + ".yml");

        if(!file.exists()) {
            if(!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        configs.put(name, config);
    }

    public static YamlConfiguration getConfig(String name) {
        if(!configs.containsKey(name)) {
            loadConfig(name);
        }
        return configs.get(name);
    }

    public static void setData(String name, String path, Object value) {
        getConfig(name).set(path, value);
        saveConfig(name);
    }

    @SneakyThrows
    public static void saveConfig(String name) {
        getConfig(name).save(new File(AkumaKOTH.getInstance().getDataFolder(), name + ".yml"));
    }

    public static void reloadConfig(String name) {
        loadConfig(name);
    }

    public static void reloadAllConfigs() {
        for(String name : configs.keySet()) {
            reloadConfig(name);
        }
    }

    public static void saveAllConfigs() {
        for(String name : configs.keySet()) {
            saveConfig(name);
        }
    }
}
