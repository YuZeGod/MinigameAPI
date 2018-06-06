package net.afyer.minigameapi.listener;

import net.afyer.minigameapi.MinigameAPI;
import net.afyer.minigameapi.game.AGame;
import net.afyer.minigameapi.spectator.SpectatorManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;

public class BlockListener extends BaseListener
{
    private static AGame game = MinigameAPI.getInstance().getGame();

    public BlockListener(MinigameAPI plugin)
    {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent e)
    {
        if (SpectatorManager.isSpectator(e.getPlayer()) || !game.isStarted())
        {
            e.setCancelled(true);
            return;
        }
        getPlugin().getEventHandler().onBlockBreak(e);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent e)
    {
        if (SpectatorManager.isSpectator(e.getPlayer()) || !game.isStarted())
        {
            e.setCancelled(true);
            return;
        }
        getPlugin().getEventHandler().onBlockPlace(e);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHangingBreakByEntity(HangingBreakByEntityEvent e)
    {
        if (!(e.getRemover() instanceof Player))
        {
            return;
        }
        else if (SpectatorManager.isSpectator((Player) e.getRemover()) || !game
                .isStarted())
            e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHangingPlace(HangingPlaceEvent e)
    {
        if (!(e.getEntity() instanceof Player))
        {
            return;
        }
        else if (SpectatorManager.isSpectator(e.getPlayer()) || !game.isStarted())
        {
            e.setCancelled(true);
        }
    }
}
