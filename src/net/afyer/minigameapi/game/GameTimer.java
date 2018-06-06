package net.afyer.minigameapi.game;

import net.afyer.minigameapi.MinigameAPI;
import net.afyer.minigameapi.game.event.GameEndEvent;
import net.afyer.minigameapi.game.event.GameStartEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("deprecation") public class GameTimer implements Runnable
{
    private final List<Updatable> updates = new ArrayList<Updatable>();
    private int time = -1;
    private int waitSecond = 0;

    @Override public void run()
    {
        for (Updatable u : updates)
        {
            try
            {
                u.onUpdate();
            }
            catch (Exception e)
            {

            }
        }
        if (time == -1)
        {
            return;
        }
        time -= 1;
        AGame game = MinigameAPI.getInstance().getGame();
        if (game.isWaiting())
        {
            if (Bukkit.getOnlinePlayers().size() < game.getMinPlayers())
            {
                for (Player p : Bukkit.getOnlinePlayers())
                {
                    p.setExp(0F);
                    p.setLevel(0);
                }
                if (waitSecond >= 60)
                {
                    waitSecond = 0;
                }
                else
                {
                    waitSecond += 1;
                }
            }
            else
            {
                for (Player p : Bukkit.getOnlinePlayers())
                {
                    p.setExp(0F);
                    p.setLevel(time);
                }
            }
        }
        else if (game.isStarted() && game.isEnableHealthDisplay())
        {
            for (Player p : Bukkit.getOnlinePlayers())
            {
                for (Player player : Bukkit.getOnlinePlayers())
                {
                    int health = (int) player.getHealth();
                    Objective obj = p.getScoreboard()
                            .getObjective(DisplaySlot.BELOW_NAME);
                    if (obj == null)
                    {
                        obj = p.getScoreboard().registerNewObjective(String.valueOf(
                                UUID.randomUUID().toString().substring(0, 6)), "health");
                        obj.setDisplaySlot(DisplaySlot.BELOW_NAME);
                    }
                    obj.setDisplayName(ChatColor.RED + "❤");
                    obj.getScore(player).setScore(health);
                }
            }
        }
        if (time == 0)
        {
            time = -1;
            finishCountdown();
        }
        else
        {
            broadcastState(time);
        }
    }

    public void broadcastState(int time)
    {
        AGame game = MinigameAPI.getInstance().getGame();
        switch (game.getState())
        {
            case LOBBY:
                if (Bukkit.getOnlinePlayers().size() < game.getMinPlayers())
                {
                    int diff = game.getMinPlayers() - Bukkit.getOnlinePlayers().size();
                    if (waitSecond == 0)
                    {
                        if (diff == 1)
                        {
                            Bukkit.broadcastMessage("§e还需要一名玩家加入,即可开始游戏.");
                        }
                        else
                        {
                            Bukkit.broadcastMessage(
                                    "§e还需要 §b" + diff + " §e名玩家加入,游戏即可开始.");
                        }
                        playSound(Sound.CHICKEN_EGG_POP);
                    }
                    return;
                }
                if (time % 60 == 0 && time != 60)
                {
                    Bukkit.broadcastMessage("§e游戏将在 §c" + (time / 60) + " §e分钟后开始！");
                    playSound(Sound.WOOD_CLICK);
                }
                else if (time == 60 || time == 30 || time == 10 || time == 5 || time == 4
                        || time == 3 || time == 2 || time == 1)
                {
                    Bukkit.broadcastMessage("§e游戏将在 §c" + time + " §e秒后开始！");
                    game.broadcastTitle("§c§l" + time, "§e准备战斗吧！", 0, 20, 0,
                            Bukkit.getOnlinePlayers().toArray(new Player[0]));
                    playSound(Sound.WOOD_CLICK);
                }
                break;
            case INGAME:
                if (time == 15 || time <= 3)
                {
                    Bukkit.broadcastMessage("§e游戏将在 §c" + time + " §e秒后结束！");
                    playSound(Sound.CLICK);
                }
                break;
            default:
                break;
        }
    }

    public void finishCountdown()
    {
        switch (MinigameAPI.getInstance().getGame().getState())
        {
            case INGAME:
                Bukkit.getPluginManager()
                        .callEvent(new GameEndEvent(MinigameAPI.getInstance().getGame()));
                MinigameAPI.getInstance().getGame().onStop();
                setTime(15);
                break;
            case LOBBY:
                if (MinigameAPI.getInstance().getIngamePlayers().size() >= MinigameAPI
                        .getInstance().getGame().getMinPlayers())
                {
                    MinigameAPI.getInstance().getGame().cachePlayers(
                            MinigameAPI.getInstance().getIngamePlayers()
                                    .toArray(new Player[0]));
                    Bukkit.getPluginManager().callEvent(
                            new GameStartEvent(MinigameAPI.getInstance().getGame()));
                    MinigameAPI.getInstance().getGame().onStart();
                    setTime(3600);
                }
                else
                {
                    setTime(120);
                }
                break;
            default:
                break;
        }
    }

    private void playSound(Sound sound)
    {
        for (Player p : Bukkit.getOnlinePlayers())
        {
            p.playSound(p.getLocation(), sound, 1F, 1F);
        }
    }

    public int getTime()
    {
        return time;
    }

    public void setTime(int time)
    {
        this.time = time;
    }

    public void addUpdatable(Updatable u)
    {
        updates.add(u);
    }

    public void removeUpdatable(Updatable u)
    {
        updates.remove(u);
    }
}
