package xyz.raitaki.AkumaKOTH.Utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemUtil {

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

}
