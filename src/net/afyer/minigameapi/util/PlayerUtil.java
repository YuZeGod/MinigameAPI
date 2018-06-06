package net.afyer.minigameapi.util;

import net.afyer.minigameapi.MinigameAPI;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerUtil
{
    public static void respawn(Player player)
    {
        Bukkit.getScheduler().runTaskLater(MinigameAPI.getInstance(), new Runnable()
        {
            @Override public void run()
            {
                PacketPlayInClientCommand packet = new PacketPlayInClientCommand(
                        PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN);
                ((CraftPlayer) player).getHandle().playerConnection.a(packet);
            }
        }, 1L);
    }
}
