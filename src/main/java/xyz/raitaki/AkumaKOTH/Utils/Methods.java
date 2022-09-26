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

    public static boolean isNumber(String text){
        try {
            NumberFormat.getInstance().parse(text).doubleValue();
            return true;
        }
        catch (ParseException e){
            return false;
        }
    }

    public static Map<Player, Integer> sortPlayersByPOINTS(Map<Player, Integer> map){
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

    public static void debugParticle(Player p, Location loc){
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldParticles(EnumParticle.FLAME, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0, 0, 0, 0, 1));
    }
}
