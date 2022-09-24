package xyz.raitaki.AkumaKOTH.Objects.Events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.raitaki.AkumaKOTH.Objects.KOTHArena;

import javax.annotation.Nullable;

public class KOTHCapEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter private final Player player;
    @Getter private final KOTHArena arena;
    @Getter private final String name;

    public KOTHCapEvent(KOTHArena arena, @Nullable Player player) {
        this.player = player;
        this.arena = arena;
        this.name = arena.getName();
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
