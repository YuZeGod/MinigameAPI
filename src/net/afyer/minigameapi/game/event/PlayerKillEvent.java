package net.afyer.minigameapi.game.event;

import net.afyer.minigameapi.game.AGame;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerKillEvent extends PlayerEvent
{
    private static final HandlerList handlers = new HandlerList();
    private AGame game;
    private Player killer;

    public PlayerKillEvent(AGame game, Player player, Player killer)
    {
        super(player);
        this.game = game;
        this.killer = killer;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    public AGame getGame()
    {
        return game;
    }

    public Player getKiller()
    {
        return killer;
    }

    @Override public HandlerList getHandlers()
    {
        return handlers;
    }
}
