package xyz.raitaki.AkumaKOTH.Utils;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.raitaki.AkumaKOTH.AkumaKOTH;

import java.io.File;
import java.util.HashMap;

public class ConfigManager {

    private static YamlConfiguration config = null;

    @SneakyThrows
    public static void loadConfig(String name) {
        File file = new File(AkumaKOTH.getInstance().getDataFolder(), name + ".yml");

        if(!file.exists()) {
            if(!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static YamlConfiguration getConfig() {
        if(config == null) {
            loadConfig("arenas");
        }
        return config;
    }

    public static void setData(String name, String path, Object value) {
        config.set(path, value);
        saveConfig(name);
    }

    @SneakyThrows
    public static void saveConfig(String name) {
        config.save(new File(AkumaKOTH.getInstance().getDataFolder(), name + ".yml"));
    }

    public static void reloadConfig(String name) {
        loadConfig(name);
    }

}
