package xyz.raitaki.AkumaKOTH.Utils;

import org.apache.commons.lang.math.IntRange;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Set;

public class LocationUtil {

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

    public static Location stringToLocation(String loc){
        String[] string = loc.split(",");
        World world   = Bukkit.getWorld(string[0]);
        Double x      = Double.parseDouble(string[1]);
        Double y      = Double.parseDouble(string[2]);
        Double z      = Double.parseDouble(string[3]);
        return new Location(world, x, y, z);
    }

    public static String locationToString(Location loc){
        String world = loc.getWorld().getName();
        String x     = String.valueOf(loc.getX());
        String y     = String.valueOf(loc.getY());
        String z     = String.valueOf(loc.getZ());

        return world + "," + x + "," + y + "," + z;
    }
}
