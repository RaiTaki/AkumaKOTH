package xyz.raitaki.AkumaKOTH.Objects;

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
import xyz.raitaki.AkumaKOTH.Utils.ItemUtil;
import xyz.raitaki.AkumaKOTH.Utils.LocationUtil;
import xyz.raitaki.AkumaKOTH.Utils.Methods;

import java.util.*;


public class KOTHArena {

    public static List<KOTHArena> arenas = new ArrayList<>();

    private HashMap<Player, Integer> points;
    private KOTHHologram hologram;
    private List<ItemStack> items;
    private String name;

    private Inventory inventory;
    private Location loc;
    private Location pos1;
    private Location pos2;
    private CustomTask task;

    private long lastCapture;
    private int cooldown;
    private int captureTime;
    private boolean active;

    public KOTHArena(String name){
        YamlConfiguration config = ConfigManager.getConfig();
        if(config.getConfigurationSection(name) == null){
            return;
        }

        this.name = name;
        this.loc = LocationUtil.stringToLocation(config.getString(name + ".loc"));
        this.pos1 = LocationUtil.stringToLocation(config.getString(name + ".pos1"));
        this.pos2 = LocationUtil.stringToLocation(config.getString(name + ".pos2"));
        this.points = new HashMap<>();
        this.lastCapture = config.getLong(name + ".lastCapture");
        this.cooldown = config.getInt(name + ".cooldown");
        this.captureTime = config.getInt(name + ".captureTime");
        this.items = ItemUtil.deserializeItems(config.getString(name + ".inventory"));
        active = false;
        setLastCapture();
        arenas.add(this);
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
                    if (LocationUtil.isInArea(p.getLocation(), pos1, pos2))
                        addPoints(p);
                }
            }
            hologram.updateText();

        }, true);

        KOTHStartEvent event = new KOTHStartEvent(this);
        Bukkit.getPluginManager().callEvent(event);
    }

    public void capture(){
        Map<Player, Integer> result = Methods.sortPlayersByPOINTS(points);
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
        YamlConfiguration config = ConfigManager.getConfig();
        if(config.getConfigurationSection(name) == null){
            return;
        }
        inventory = Bukkit.createInventory(null, 54, "§aKOTH: " + name);
        inventory.setContents(items.toArray(new ItemStack[0]));

    }

    public void saveArena(){
        YamlConfiguration config = ConfigManager.getConfig();
        config.set(name + ".loc", LocationUtil.locationToString(loc));
        config.set(name + ".pos1", LocationUtil.locationToString(pos1));
        config.set(name + ".pos2", LocationUtil.locationToString(pos2));
        config.set(name + ".lastCapture", lastCapture);
        config.set(name + ".cooldown", cooldown);
        config.set(name + ".captureTime", captureTime);
        config.set(name + ".inventory", ItemUtil.serializeItems(items));
        ConfigManager.saveConfig("arenas");
    }

    public void saveArenaWithName(String name){
        YamlConfiguration config = ConfigManager.getConfig();
        config.set(name + ".loc", LocationUtil.locationToString(loc));
        config.set(name + ".pos1", LocationUtil.locationToString(pos1));
        config.set(name + ".pos2", LocationUtil.locationToString(pos2));
        config.set(name + ".lastCapture", lastCapture);
        config.set(name + ".cooldown", cooldown);
        config.set(name + ".captureTime", captureTime);
        config.set(name + ".inventory", ItemUtil.serializeItems(items));
        ConfigManager.saveConfig("arenas");
    }

    public static KOTHArena getArena(String name){
        for(KOTHArena arena : arenas){
            if(arena.getName().equalsIgnoreCase(name)){
                return arena;
            }
        }
        return null;
    }

    public static void createDefaultArena(String name){
        YamlConfiguration config = ConfigManager.getConfig();

        config.set(name + ".loc", LocationUtil.locationToString(Bukkit.getWorlds().get(0).getSpawnLocation()));
        config.set(name + ".pos1", LocationUtil.locationToString(Bukkit.getWorlds().get(0).getSpawnLocation()));
        config.set(name + ".pos2", LocationUtil.locationToString(Bukkit.getWorlds().get(0).getSpawnLocation()));
        config.set(name + ".lastCapture", System.currentTimeMillis());
        config.set(name + ".cooldown", 60);
        config.set(name + ".captureTime", 60);
        config.set(name + ".inventory", ItemUtil.serializeItems(new ArrayList<>()));
        ConfigManager.saveConfig("arenas");

        new KOTHArena(name);
    }

    public static void deleteArena(String name){
        YamlConfiguration config = ConfigManager.getConfig();
        KOTHArena arena = getArena(name);
        config.set(name, null);
        ConfigManager.saveConfig("arenas");

        if(arena.getTask() != null)
            arena.getTask().getRunnable().cancel();

        arenas.remove(name);
    }

    public HashMap<Player, Integer> getPoints() {
        return points;
    }

    public KOTHHologram getHologram() {
        return hologram;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public String getName() {
        return name;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Location getLoc() {
        return loc;
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public CustomTask getTask() {
        return task;
    }

    public long getLastCapture() {
        return lastCapture;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getCaptureTime() {
        return captureTime;
    }

    public boolean isActive() {
        return active;
    }

    public static List<KOTHArena> getArenas() {
        return arenas;
    }
}
