package xyz.raitaki.AkumaKOTH.Objects;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.raitaki.AkumaKOTH.Utils.Methods;

import java.util.*;

public class KOTHHologram {

    @Getter private KOTHArena arena;
    @Getter private List<Hologram> holograms;
    @Getter private List<Player> showedTo;

    public KOTHHologram(KOTHArena arena) {
        this.arena = arena;
        holograms = new ArrayList<>();
        showedTo = new ArrayList<>();

        putHolos();
    }

    private void putHolos(){
        Location loc = arena.getLoc().clone();
        loc.add(0, 2, 0);

        for(int i = 0; i < 12; i++){
            Hologram hologram = new Hologram("", loc.add(0,0.3,0));
            holograms.add(hologram);
        }

        Collections.reverse(holograms);
        holograms.get(0).updateText("&c&lKOTH Arena " + arena.getName());
        holograms.get(1).updateText("&7Capture time: &a" + Methods.formatTime(arena) + "s");
    }

    public void updateText(){
        Map<Player, Integer> sortedPlayers = Methods.sortPlayers(arena.getPoints());

        for(int i = 2; i < holograms.size(); i++) {
            try {
                Player player = (Player) sortedPlayers.keySet().toArray()[i - 2];
                int id = i - 1;
                int points = sortedPlayers.get(player);
                holograms.get(i).updateText("&7" + id + " - &6" + player.getName() + " &7- &6" + points);
            }catch(Exception ignored){
                holograms.get(i).updateText("");
            }
        }
        holograms.get(1).updateText("&7Capture time: &e" + Methods.formatTime(arena) + "s");
        showToNearby();
    }

    public void showToNearby(){
        for(Player player : arena.getLoc().getWorld().getPlayers()){
            if(player.getLocation().distance(arena.getLoc()) <= 20){
                if(!showedTo.contains(player)){
                    showedTo.add(player);
                    for(Hologram hologram : holograms){
                        hologram.showTo(player);
                    }
                }else{
                    for(Hologram hologram : holograms){
                        hologram.updateTo(player);
                    }
                }
            }else{
                if(showedTo.contains(player)){
                    showedTo.remove(player);
                    for(Hologram hologram : holograms){
                        hologram.hideTo(player);
                    }
                }
            }
        }
    }

    public void hideToEveryone(){
        for(Player player : showedTo){
            for(Hologram hologram : holograms){
                hologram.hideTo(player);
            }
        }
        showedTo.clear();
    }
}
