package xyz.raitaki.AkumaKOTH.Objects;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import xyz.raitaki.AkumaKOTH.Utils.Methods;

public class Hologram {

    private String text;
    private Location loc;
    private EntityArmorStand armorStand;

    public Hologram(String text, Location loc) {
        this.text = text;
        this.loc = loc;
        build();
    }

    private void build(){
        armorStand = new EntityArmorStand(((CraftWorld)loc.getWorld()).getHandle(), loc.getX(), loc.getY(), loc.getZ());
        armorStand.setCustomName(Methods.replaceColorCode(text));
        armorStand.setCustomNameVisible(true);
        armorStand.setInvisible(true);
        armorStand.setSmall(true);
        armorStand.n(true); //marker
    }

    public void showTo(Player p){
        EntityPlayer ep = ((CraftPlayer)p).getHandle();
        ep.playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(armorStand));
        ep.playerConnection.sendPacket(new PacketPlayOutEntityMetadata(armorStand.getId(), armorStand.getDataWatcher(), true));
    }

    public void hideTo(Player p){
        EntityPlayer ep = ((CraftPlayer)p).getHandle();
        ep.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(armorStand.getId()));
    }

    public void updateText(String text){
        this.text = text;
        if(text.length() < 2)
            armorStand.setCustomNameVisible(false);
        else
            armorStand.setCustomNameVisible(true);
        armorStand.setCustomName(Methods.replaceColorCode(text));
    }

    public void updateTo(Player p){
        if(text.length() < 2) return;
        EntityPlayer ep = ((CraftPlayer)p).getHandle();
        ep.playerConnection.sendPacket(new PacketPlayOutEntityMetadata(armorStand.getId(), armorStand.getDataWatcher(), true));
    }

    public String getText() {
        return text;
    }

    public Location getLoc() {
        return loc;
    }

    public EntityArmorStand getArmorStand() {
        return armorStand;
    }
}

