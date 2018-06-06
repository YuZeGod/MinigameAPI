package net.afyer.minigameapi.game.event;

import net.afyer.minigameapi.game.AGame;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerJoinGameEvent extends PlayerEvent
{
    private static final HandlerList handlers = new HandlerList();
    private AGame game;

    public PlayerJoinGameEvent(AGame game, Player player)
    {
        super(player);
        this.game = game;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    public AGame getGame()
    {
        return game;
    }

    @Override public HandlerList getHandlers()
    {
        return handlers;
    }
}
