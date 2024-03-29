package xyz.raitaki.AkumaKOTH.Objects.Events;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.raitaki.AkumaKOTH.Objects.KOTHArena;

public class KOTHStartEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final KOTHArena arena;
    private final String name;

    public KOTHStartEvent(KOTHArena arena) {
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

    public KOTHArena getArena() {
        return arena;
    }

    public String getName() {
        return name;
    }
}
