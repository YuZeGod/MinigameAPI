package net.afyer.minigameapi.game;

import net.afyer.minigameapi.MinigameAPI;
import net.afyer.minigameapi.game.event.GameStartEvent;
import net.afyer.minigameapi.location.LocationManager;
import net.afyer.minigameapi.spectator.SpectatorManager;
import net.afyer.minigameapi.util.TitleUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class AGame
{
    private String name;
    private String displayName;
    private int minPlayers;
    private int maxPlayers;
    private int waitSeconds;
    private GameState state;
    private boolean enableHealthDisplay;
    private Location lobbyLocation;
    private List<OfflinePlayer> cachedPlayers;

    public AGame()
    {
        name = "Unkonwn";
        displayName = "未设置";
        waitSeconds = 120;
        state = GameState.LOBBY;
        enableHealthDisplay = false;
        lobbyLocation = LocationManager.getInstance().getLocation("lobby");
        cachedPlayers = new ArrayList<OfflinePlayer>();
    }

    public abstract void onStart();

    public abstract void onStop();

    public abstract void onRejoin(Player player);

    public void forceStart()
    {
        if (!isWaiting())
        {
            return;
        }
        for (Player p : Bukkit.getOnlinePlayers())
        {
            p.setLevel(0);
            p.setExp(0);
        }
        setState(GameState.INGAME);
        MinigameAPI.getInstance().getGame().cachePlayers(
                MinigameAPI.getInstance().getIngamePlayers().toArray(new Player[0]));
        Bukkit.getPluginManager()
                .callEvent(new GameStartEvent(MinigameAPI.getInstance().getGame()));
        onStart();
        MinigameAPI.getInstance().getGameTimer().setTime(3600);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public int getMinPlayers()
    {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers)
    {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers()
    {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers)
    {
        this.maxPlayers = maxPlayers;
    }

    public int getWaitSeconds()
    {
        return waitSeconds;
    }

    public void setWaitSeconds(int waitSeconds)
    {
        this.waitSeconds = waitSeconds;
    }

    public boolean isWaiting()
    {
        return state == GameState.LOBBY;
    }

    public boolean isStarted()
    {
        return state == GameState.INGAME;
    }

    public GameState getState()
    {
        return state;
    }

    public void setState(GameState state)
    {
        this.state = state;
    }

    public boolean isEnableHealthDisplay()
    {
        return enableHealthDisplay;
    }

    public void setEnableHealthDisplay(boolean enableHealthDisplay)
    {
        this.enableHealthDisplay = enableHealthDisplay;
    }

    public Location getLobbyLocation()
    {
        return lobbyLocation;
    }

    public void setLobbyLocation(Location lobbyLocation)
    {
        this.lobbyLocation = lobbyLocation;
    }

    public List<OfflinePlayer> getCachedPlayers()
    {
        return cachedPlayers;
    }

    public void cachePlayers(OfflinePlayer... players)
    {
        for (OfflinePlayer p : players)
        {
            cachedPlayers.add(p);
        }
    }

    public String formatSpectatorTip(Player player, Player target, boolean firstMode)
    {
        if (firstMode)
        {
            return "§f目标: §a§l" + target.getName();
        }
        else
        {
            DecimalFormat df = new DecimalFormat("##.0");
            String distance = df
                    .format(player.getLocation().distance(target.getLocation()));
            return "§f目标: §a§l" + target.getName() + "  §f距离: §a§l" + distance + "米";
        }
    }

    public void openSpectatorInventory(Player player)
    {
        if (!SpectatorManager.isSpectator(player))
        {
            return;
        }
        int players = MinigameAPI.getInstance().getIngamePlayers().size();
        if (players <= 0)
        {
            return;
        }
        int nom = players % 9 == 0 ? 9 : players % 9;
        int size = players + (9 - nom);
        Inventory inv = Bukkit.createInventory(null, size, "§a§l传送器");
        for (Player ingamePlayer : MinigameAPI.getInstance().getIngamePlayers())
        {
            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + ingamePlayer.getDisplayName());
            DecimalFormat df = new DecimalFormat("##.0");
            List<String> lore = new ArrayList<String>();
            lore.add("§f生命值: §a" + df
                    .format(ingamePlayer.getHealth() / ingamePlayer.getMaxHealth())
                    + "%");
            lore.add("§f饱食度: §a" + df.format(ingamePlayer.getFoodLevel() / 20) + "%");
            lore.add(" ");
            lore.add("§7点击传送到该玩家！");
            meta.setLore(lore);
            meta.setOwner(ingamePlayer.getName());
            head.setItemMeta(meta);
            inv.addItem(new ItemStack[] { head });
        }
        player.openInventory(inv);
    }

    public void broadcastMessage(String message)
    {
        broadcastMessage(message, Bukkit.getOnlinePlayers().toArray(new Player[0]));
    }

    public void broadcastMessage(String message, Player... players)
    {
        for (Player p : players)
        {
            if (p != null)
            {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        }
    }

    public void broadcastTitle(String title, String subtitle, int fadeIn, int stay,
            int fadeOut)
    {
        broadcastTitle(title, subtitle, fadeIn, stay, fadeOut,
                Bukkit.getOnlinePlayers().toArray(new Player[0]));
    }

    public void broadcastTitle(String title, String subtitle, int fadeIn, int stay,
            int fadeOut, Player... players)
    {
        for (Player p : players)
        {
            if (p != null)
            {
                TitleUtil.sendTitle(p, fadeIn, stay, fadeOut, title, subtitle);
            }
        }
    }

    public void broadcastSound(Sound sound, float volume, float pitch)
    {
        broadcastSound(sound, volume, pitch,
                Bukkit.getOnlinePlayers().toArray(new Player[0]));
    }

    public void broadcastSound(Sound sound, float volume, float pitch, Player... players)
    {
        for (Player p : players)
        {
            if (p != null)
            {
                p.playSound(p.getLocation(), sound, volume, pitch);
            }
        }
    }
}
