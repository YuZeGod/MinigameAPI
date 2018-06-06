package net.afyer.minigameapi.game.event;

import net.afyer.minigameapi.game.AGame;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameEndEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    private AGame game;

    public GameEndEvent(AGame game)
    {
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
