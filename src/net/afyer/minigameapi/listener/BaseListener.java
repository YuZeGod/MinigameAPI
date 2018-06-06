package net.afyer.minigameapi.listener;

import net.afyer.minigameapi.MinigameAPI;
import org.bukkit.event.Listener;

public class BaseListener implements Listener
{
    private final MinigameAPI plugin;

    public BaseListener(MinigameAPI plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public MinigameAPI getPlugin()
    {
        return plugin;
    }
}
