package xyz.raitaki.AkumaKOTH.Utils;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.apache.commons.lang.math.IntRange;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;
import xyz.raitaki.AkumaKOTH.Objects.KOTHArena;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Methods {

    public static String replaceColorCode(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void saveDefaultConfig() {
        ConfigManager.loadConfig("config");
        YamlConfiguration config = ConfigManager.getConfig("config");
        config.options().copyDefaults(true);
        config.setDefaults(getDefaults());
        ConfigManager.saveConfig("config");
    }

    private static MemoryConfiguration getDefaults(){
        MemoryConfiguration defaults = new MemoryConfiguration();
        defaults.set("test", "blah");
        defaults.set("timezone", "Europe/Istanbul");

        return defaults;
    }

    public static Location stringToLocation(String loc){
        String[] string = loc.split(",");
        World world   = Bukkit.getWorld(string[0]);
        Double x      = Double.parseDouble(string[1]);
        Double y      = Double.parseDouble(string[2]);
        Double z      = Double.parseDouble(string[3]);
        return new Location(world, x, y, z);
    }

    public static String serializeItems(List<ItemStack> items) {
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("c", items.stream().map((is) -> {
            return is == null ? null : is.serialize();
        }).collect(Collectors.toList()));
        return yaml.saveToString();
    }

    public static List<ItemStack> deserializeItems(String listOfItems) {
        YamlConfiguration yaml = new YamlConfiguration();
        if(listOfItems == null){
            return new ArrayList<>();
        }
        try {
            yaml.loadFromString(listOfItems);
            if (!yaml.isList("c")) {
                return new ArrayList<>();
            } else {
                return yaml.getList("c").stream().map((ent) -> {
                    return (Map)ent;
                }).map((ent) -> {
                    return ent == null ? null : ItemStack.deserialize(ent);
                }).collect(Collectors.toList());
            }
        } catch (InvalidConfigurationException var3) {
            return new ArrayList<>();
        }
    }

    public static String locationToString(Location loc){
        String world = loc.getWorld().getName();
        String x     = String.valueOf(loc.getX());
        String y     = String.valueOf(loc.getY());
        String z     = String.valueOf(loc.getZ());

        return world + "," + x + "," + y + "," + z;
    }

    public static boolean isNumber(String text){
        try {
            NumberFormat.getInstance().parse(text).doubleValue();
            return true;
        }
        catch (ParseException e){
            return false;
        }
    }

    public static List<ItemStack> arrayToList(ItemStack[] array){
        List<ItemStack> list = new ArrayList<>();
        for(ItemStack item : array){
            if(item == null){
                continue;
            }
            list.add(item);
        }
        return list;
    }

    public static Map<Player, Integer> sortPlayers(Map<Player, Integer> map){
        return map.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public static String formatTime(KOTHArena arena){
        long time = (arena.getCaptureTime()*1000L + arena.getLastCapture()) - System.currentTimeMillis();
        String pattern = "mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return simpleDateFormat.format(new Date(time));
    }

    public static boolean isInArea(Location origin, Location l2, Location l1){
        return new IntRange(l1.getX(), l2.getX()).containsDouble(origin.getX())
                && new IntRange(l1.getY(), l2.getY()).containsDouble(origin.getY())
                &&  new IntRange(l1.getZ(), l2.getZ()).containsDouble(origin.getZ());
    }

    public static Block getTargetBlock(Player p, int range) {
        Block block = p.getTargetBlock((Set<Material>) null, range);
        if(block.getType().isSolid()){
            return block;
        }
        return null;
    }

    public static void debugParticle(Player p, Location loc){
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldParticles(EnumParticle.FLAME, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0, 0, 0, 0, 1));
    }
}
