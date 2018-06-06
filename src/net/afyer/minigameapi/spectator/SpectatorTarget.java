package net.afyer.minigameapi.spectator;

import net.afyer.minigameapi.MinigameAPI;
import net.afyer.minigameapi.spectator.SpectatorSettings.Option;
import net.afyer.minigameapi.util.TitleUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class SpectatorTarget
{
    private Player player;
    private Player target;

    public SpectatorTarget(Player player, Player target)
    {
        this.player = player;
        this.target = target;
    }

    public Player getPlayer()
    {
        return player;
    }

    public Player getTarget()
    {
        return target;
    }

    public void setTarget(Player target)
    {
        this.target = target;
    }

    public void sendTip()
    {
        if (!check())
        {
            return;
        }
        else if ((player.getPlayer().getSpectatorTarget() != null) && (player.getPlayer()
                .getSpectatorTarget().equals(target)))
        {
            TitleUtil.sendActionbar(player, MinigameAPI.getInstance().getGame()
                    .formatSpectatorTip(player.getPlayer(), target.getPlayer(), true)
                    + "  &a点击左键打开菜单  &c按Shift退出");
            return;
        }
        else if (!player.getPlayer().getWorld().equals(target.getPlayer().getWorld()))
        {
            TitleUtil.sendActionbar(player, "§c§l目标已丢失或不在同一个世界");
            return;
        }
        else
        {
            TitleUtil.sendActionbar(player, MinigameAPI.getInstance().getGame()
                    .formatSpectatorTip(player.getPlayer(), target.getPlayer(), false));
            return;
        }
    }

    public void autoTp()
    {
        if (!check())
        {
            return;
        }
        else if (SpectatorSettings.get(player).getOption(Option.AUTOTP))
        {
            if ((!player.getWorld().equals(target.getWorld())) || (
                    player.getLocation().distance(target.getLocation()) >= 20D))
            {
                player.teleport(target);
                if (SpectatorSettings.get(player).getOption(Option.FIRSTPERSON))
                {
                    TitleUtil.sendFullTitle(player, 0, 20, 0,
                            "&a正在旁观&7" + target.getName(), "&a点击左键打开菜单  &c按Shift键退出");
                    player.getPlayer().setGameMode(GameMode.SPECTATOR);
                    player.setSpectatorTarget(target);
                }
            }
        }
    }

    public void tp()
    {
        if (!check())
        {
            return;
        }
        else if (SpectatorSettings.get(player).getOption(Option.FIRSTPERSON))
        {
            player.teleport(target.getPlayer());
            TitleUtil.sendFullTitle(player, 0, 20, 0, "&a正在旁观&7" + target.getName(),
                    "&a点击左键打开菜单  &c按Shift键退出");
            player.setGameMode(GameMode.SPECTATOR);
            player.setSpectatorTarget(target);
            return;
        }
        else
        {
            player.teleport(target);
            return;
        }
    }

    public boolean check()
    {
        if (target == null)
        {
            return false;
        }
        else if (SpectatorManager.isSpectator(target) || (!target.isOnline()))
        {
            target = null;
            return false;
        }
        return true;
    }
}
