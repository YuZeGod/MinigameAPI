package net.afyer.minigameapi.cache;

import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;

public class CacheManager
{
    private static final Map<OfflinePlayer, PlayerData> CACHES = new HashMap<OfflinePlayer, PlayerData>();

    public static void cache(OfflinePlayer player)
    {
        CACHES.put(player, new PlayerData(player));
    }

    public static PlayerData getCache(OfflinePlayer player)
    {
        return CACHES.getOrDefault(player, null);
    }

    public static void removeCache(OfflinePlayer player)
    {
        if (!CACHES.containsKey(player))
        {
            return;
        }
        CACHES.remove(player);
    }
}
