package xyz.raitaki.AkumaKOTH;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import xyz.raitaki.AkumaKOTH.Objects.KOTHArena;
import xyz.raitaki.AkumaKOTH.Utils.ItemUtil;
import xyz.raitaki.AkumaKOTH.Utils.Methods;

import java.util.List;

public class Events implements Listener {

    @EventHandler
    public void inventoryCloseEvent(InventoryCloseEvent event) {
        String title = event.getView().getTitle();
        title = title.replace(Methods.replaceColorCode("§aEdit loot for arena "), "");
        if (KOTHArena.getArena(title) == null) {
            return;
        }
        List<ItemStack> items = ItemUtil.arrayToList(event.getInventory().getContents());
        KOTHArena.getArena(title).setItems(items);
    }

    @EventHandler
    public void entityDamageByEntityEvent(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player p){
            String title = p.getOpenInventory().getTitle();
            if(title.contains(Methods.replaceColorCode("&aKOTH"))){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void playerQuitEvent(PlayerQuitEvent event){
        Player player = event.getPlayer();
        KOTHArena.getArenas().values().forEach(arena -> {
            arena.getPoints().remove(player);
            arena.getHologram().getShowedTo().remove(player);
        });
    }
}
