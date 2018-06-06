package net.afyer.minigameapi.spectator.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class SpectatorJoinEvent extends PlayerEvent
{
    private static final HandlerList handlers = new HandlerList();

    public SpectatorJoinEvent(Player player)
    {
        super(player);
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    @Override public HandlerList getHandlers()
    {
        return handlers;
    }
}
