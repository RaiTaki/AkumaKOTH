package xyz.raitaki.AkumaKOTH.Objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.raitaki.AkumaKOTH.AkumaKOTH;
import xyz.raitaki.AkumaKOTH.Objects.Events.KOTHStartEvent;
import xyz.raitaki.AkumaKOTH.Objects.Events.KOTHCapEvent;
import xyz.raitaki.AkumaKOTH.Utils.ConfigManager;
import xyz.raitaki.AkumaKOTH.Utils.Methods;

import java.util.*;


public class KOTHArena {

    @Getter @Setter public static HashMap<String, KOTHArena> arenas = new HashMap<>();

    @Getter private HashMap<Player, Integer> points;
    @Getter private KOTHHologram hologram;
    @Getter private List<ItemStack> items;
    @Getter private String name;
    @Getter private Inventory inventory;
    @Getter private Location loc;
    @Getter private Location pos1;
    @Getter private Location pos2;
    @Getter private CustomTask task;

    @Getter private long lastCapture;
    @Getter private int cooldown;
    @Getter private int captureTime;
    @Getter private boolean active;

    public KOTHArena(String name){
        YamlConfiguration config = ConfigManager.getConfig("arenas");
        if(config.getConfigurationSection(name) == null){
            return;
        }

        this.name = name;
        this.loc = Methods.stringToLocation(config.getString(name + ".loc"));
        this.pos1 = Methods.stringToLocation(config.getString(name + ".pos1"));
        this.pos2 = Methods.stringToLocation(config.getString(name + ".pos2"));
        this.points = new HashMap<>();
        this.lastCapture = config.getLong(name + ".lastCapture");
        this.cooldown = config.getInt(name + ".cooldown");
        this.captureTime = config.getInt(name + ".captureTime");
        this.items = Methods.deserializeItems(config.getString(name + ".inventory"));
        active = false;
        setLastCapture();
        arenas.put(name, this);
    }

    public void start(){
        Bukkit.broadcastMessage(AkumaKOTH.getPrefix() + "§aThe KOTH arena " + name + " has started!");
        loadInventory();
        setLastCapture();
        hologram = new KOTHHologram(this);

        active = true;
        task = new CustomTask(0, 20);
        task.start((runnable) ->{
            if(shouldStop()){
                capture();
                runnable.cancel();
                return;
            }
            else{
                for(Player p : loc.getWorld().getPlayers()) {
                    if (Methods.isInArea(p.getLocation(), pos1, pos2))
                        addPoints(p);
                }
            }
            hologram.updateText();

        }, true);

        KOTHStartEvent event = new KOTHStartEvent(this);
        Bukkit.getPluginManager().callEvent(event);
    }

    public void capture(){
        Map<Player, Integer> result = Methods.sortPlayers(points);
        List<Player> players = new ArrayList<>(result.keySet());
        Player p = null;
        Collections.reverse(players);

        active = false;
        hologram.hideToEveryone();
        points.clear();

        for(Player cp : players){
            if(cp.isOnline()){
                p = cp;
                break;
            }
        }
        KOTHCapEvent event = new KOTHCapEvent(this, p);

        if(p == null){
            Bukkit.broadcastMessage(AkumaKOTH.getPrefix() + "§cNobody captured the KOTH arena " + name + "!");
            setLastCapture();
            Bukkit.getPluginManager().callEvent(event);
            return;
        }
        Bukkit.broadcastMessage(AkumaKOTH.getPrefix() + "§a" + p.getName() + " §ecaptured the KOTH arena " + name + "!");
        p.openInventory(inventory);

        Bukkit.getPluginManager().callEvent(event);
        setLastCapture();
    }

    public void setLastCapture(){
        this.lastCapture = System.currentTimeMillis() + (cooldown * 1000L);
        saveArena();
    }

    public void setCooldown(int cooldown){
        this.cooldown = cooldown;
        saveArena();
    }

    public void setLocation(Location loc){
        this.loc = loc;
        saveArena();
    }

    public void setPos1(Location pos1){
        this.pos1 = pos1;
        saveArena();
    }

    public void setPos2(Location pos2){
        this.pos2 = pos2;
        saveArena();
    }

    public void setName(String name){
        deleteArena(this.name);
        saveArenaWithName(name);
        new KOTHArena(name);
    }

    public void setCaptureTime(int captureTime){
        this.captureTime = captureTime;
        saveArena();
    }

    public void setItems(List<ItemStack> items){
        this.items = items;
        saveArena();
    }

    public void addPoints(Player p){
        if(points.containsKey(p)){
            points.put(p, points.get(p) + 1);
        }
        else{
            points.put(p, 1);
        }
    }

    public boolean shouldStop(){
        return System.currentTimeMillis() > lastCapture + (captureTime * 1000L);
    }

    public boolean shouldStart(){
        return System.currentTimeMillis() > lastCapture && !isActive();
    }

    public void loadInventory(){
        YamlConfiguration config = ConfigManager.getConfig("arenas");
        if(config.getConfigurationSection(name) == null){
            return;
        }
        inventory = Bukkit.createInventory(null, 54, "§aKOTH: " + name);
        inventory.setContents(items.toArray(new ItemStack[0]));

    }

    public void saveArena(){
        YamlConfiguration config = ConfigManager.getConfig("arenas");
        config.set(name + ".loc", Methods.locationToString(loc));
        config.set(name + ".pos1", Methods.locationToString(pos1));
        config.set(name + ".pos2", Methods.locationToString(pos2));
        config.set(name + ".lastCapture", lastCapture);
        config.set(name + ".cooldown", cooldown);
        config.set(name + ".captureTime", captureTime);
        config.set(name + ".inventory", Methods.serializeItems(items));
        ConfigManager.saveConfig("arenas");
    }

    public void saveArenaWithName(String name){
        YamlConfiguration config = ConfigManager.getConfig("arenas");
        config.set(name + ".loc", Methods.locationToString(loc));
        config.set(name + ".pos1", Methods.locationToString(pos1));
        config.set(name + ".pos2", Methods.locationToString(pos2));
        config.set(name + ".lastCapture", lastCapture);
        config.set(name + ".cooldown", cooldown);
        config.set(name + ".captureTime", captureTime);
        config.set(name + ".inventory", Methods.serializeItems(items));
        ConfigManager.saveConfig("arenas");
    }

    public static KOTHArena getArena(String name){
        return arenas.get(name);
    }

    public static void createDefaultArena(String name){
        YamlConfiguration config = ConfigManager.getConfig("arenas");

        config.set(name + ".loc", Methods.locationToString(Bukkit.getWorlds().get(0).getSpawnLocation()));
        config.set(name + ".pos1", Methods.locationToString(Bukkit.getWorlds().get(0).getSpawnLocation()));
        config.set(name + ".pos2", Methods.locationToString(Bukkit.getWorlds().get(0).getSpawnLocation()));
        config.set(name + ".lastCapture", System.currentTimeMillis());
        config.set(name + ".cooldown", 60);
        config.set(name + ".captureTime", 60);
        config.set(name + ".inventory", Methods.serializeItems(new ArrayList<>()));
        ConfigManager.saveConfig("arenas");

        new KOTHArena(name);
    }

    public static void deleteArena(String name){
        YamlConfiguration config = ConfigManager.getConfig("arenas");
        KOTHArena arena = getArena(name);
        config.set(name, null);
        ConfigManager.saveConfig("arenas");

        if(arena.getTask() != null)
            arena.getTask().getRunnable().cancel();

        arenas.remove(name);
    }
}
