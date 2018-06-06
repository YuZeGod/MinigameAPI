package net.afyer.minigameapi.achievement.event;

import net.afyer.minigameapi.achievement.Achievement;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerAwardAchievementEvent extends PlayerEvent
{
    private static final HandlerList handlers = new HandlerList();

    private Achievement achievement;

    public PlayerAwardAchievementEvent(Player player, Achievement achievement)
    {
        super(player);
        this.achievement = achievement;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    public Achievement getAchievement()
    {
        return achievement;
    }

    @Override public HandlerList getHandlers()
    {
        return handlers;
    }
}
