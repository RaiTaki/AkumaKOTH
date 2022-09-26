package xyz.raitaki.AkumaKOTH;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.raitaki.AkumaKOTH.Commands.ArenaCommand;
import xyz.raitaki.AkumaKOTH.Commands.Completers.ArenaCompleter;
import xyz.raitaki.AkumaKOTH.Objects.CustomTask;
import xyz.raitaki.AkumaKOTH.Objects.KOTHArena;
import xyz.raitaki.AkumaKOTH.Utils.ConfigManager;

import java.util.TimeZone;

public final class AkumaKOTH extends JavaPlugin {

    private static AkumaKOTH instance;
    private static String prefix = "§8[§cAkumaKOTH§8]§r ";
    private TimeZone timezone = TimeZone.getTimeZone("UTC");

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        Bukkit.getPluginCommand("arena").setExecutor(new ArenaCommand());
        Bukkit.getPluginCommand("arena").setTabCompleter(new ArenaCompleter());

        Bukkit.getPluginManager().registerEvents(new Events(), this);

        loadArenas();
        startTimer();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void startTimer(){
        CustomTask task = new CustomTask(30, 60);
        task.start(runnable -> {
            for(KOTHArena arena : KOTHArena.getArenas().values()){
                if(arena.shouldStart())
                    arena.start();
            }
        }, true);
    }

    public void loadArenas(){
        YamlConfiguration config = ConfigManager.getConfig();

        for(String arenaName : config.getKeys(false)){
            new KOTHArena(arenaName);;
        }
    }

    public static AkumaKOTH getInstance() {
        return instance;
    }

    public static String getPrefix() {
        return prefix;
    }

}
