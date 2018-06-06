package net.afyer.minigameapi.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.afyer.minigameapi.MinigameAPI;
import net.afyer.minigameapi.spectator.SpectatorManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class GameMessageListener implements PluginMessageListener
{
    private String SERVER_NAME;

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message)
    {
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        try
        {
            String subChannel = in.readUTF();
            if (channel.equals("BungeeCord"))
            {
                if (subChannel.equals("GetServer") && (SERVER_NAME == null))
                {
                    SERVER_NAME = in.readUTF();
                    return;
                }
            }
            else if (channel.equals("MinigameAPI"))
            {
                if (subChannel.equals("Rejoin"))
                {
                    String uuid = in.readUTF();
                    if ((MinigameAPI.getInstance().getGame().getCachedPlayers()
                            .contains(UUID.fromString(uuid))) && (!SpectatorManager
                            .isSpectator(Bukkit.getPlayer(UUID.fromString(uuid)))))
                    {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        DataOutputStream dos = new DataOutputStream(baos);
                        dos.writeUTF("RejoinServer");
                        dos.writeUTF(SERVER_NAME);
                        dos.writeUTF(uuid);
                        player.sendPluginMessage(MinigameAPI.getInstance(), "MinigameAPI",
                                baos.toByteArray());
                        return;
                    }
                }
                else if (subChannel.equals("RejoinServer"))
                {
                    String serverName = in.readUTF();
                    String uuid = in.readUTF();
                    Player p = Bukkit.getPlayer(UUID.fromString(uuid));
                    if (p != null)
                    {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        DataOutputStream dos = new DataOutputStream(baos);
                        dos.writeUTF("Connect");
                        dos.writeUTF(serverName);
                        p.sendPluginMessage(MinigameAPI.getInstance(), "BungeeCord",
                                baos.toByteArray());
                        return;
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
