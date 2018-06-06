package net.afyer.minigameapi.tellraw;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tellraw implements Cloneable
{
    private List<MessagePart> messageParts = new ArrayList<MessagePart>();
    private String cache;

    public Tellraw(String text)
    {
        messageParts.add(new MessagePart(text));
    }

    public static Tellraw create()
    {
        return create("");
    }

    public static Tellraw create(String text)
    {
        return new Tellraw(text);
    }

    public static Tellraw create(String text, Object... objects)
    {
        return new Tellraw(String.format(text, objects));
    }

    public void broadcast()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            send(player);
        }
    }

    public Tellraw cmd_tip(String command, String... tip)
    {
        return command(command).tip(tip);
    }

    public Tellraw command(String command)
    {
        return onClick("run_command", command);
    }

    public Tellraw file(String path)
    {
        return onClick("open_file", path);
    }

    public Tellraw insertion(String data)
    {
        latest().insertionData = data;
        return this;
    }

    public Tellraw item(ItemStack item)
    {
        return item(ItemSerialize.$(item));
    }

    public Tellraw item(String json)
    {
        return onHover("show_item", json);
    }

    public Tellraw link(String url)
    {
        return onClick("open_url", url);
    }

    public Tellraw openurl(String url)
    {
        return onClick("open_url", url);
    }

    public void send(CommandSender sender)
    {
        String json = toJsonString();
        if (((sender instanceof Player)) && (json.getBytes().length < 32000))
        {
            IChatBaseComponent comp = ChatSerializer.a(json);
            PacketPlayOutChat packet = new PacketPlayOutChat(comp);
            ((CraftPlayer) sender).getHandle().playerConnection.sendPacket(packet);
        }
        else
        {
            sender.sendMessage(toOldMessageFormat());
        }
    }

    public Tellraw sug_tip(String command, String... tip)
    {
        return suggest(command).tip(tip);
    }

    public Tellraw suggest(String command)
    {
        return onClick("suggest_command", command);
    }

    public Tellraw text(String text)
    {
        latest().text = text;
        return this;
    }

    public Tellraw then(String text)
    {
        return then(new MessagePart(text));
    }

    public Tellraw then(String name, ItemStack item)
    {
        return then(name).item(ItemSerialize.$(item));
    }

    public Tellraw then(String text, Object... objects)
    {
        return then(new MessagePart(String.format(text, objects)));
    }

    public Tellraw tip(List<String> texts)
    {
        if (texts.isEmpty())
        {
            return this;
        }
        StringBuilder text = new StringBuilder();
        for (String t : texts)
        {
            text.append(t).append("\n");
        }
        return tip(text.toString().substring(0, text.length() - 1));
    }

    public Tellraw tip(String text)
    {
        return onHover("show_text", text);
    }

    public Tellraw tip(String... texts)
    {
        return tip(Arrays.asList(texts));
    }

    public String toJsonString()
    {
        if (cache == null)
        {
            StringBuilder msg = new StringBuilder();
            msg.append("[\"\"");
            for (MessagePart messagePart : messageParts)
            {
                msg.append(",");
                messagePart.writeJson(msg);
            }
            msg.append("]");
            cache = msg.toString();
        }
        return cache;
    }

    public Tellraw setMessageParts(List<MessagePart> messageParts)
    {
        this.messageParts = new ArrayList<MessagePart>(messageParts);
        return this;
    }

    @Override public Tellraw clone() throws CloneNotSupportedException
    {
        return ((Tellraw) super.clone()).setMessageParts(messageParts);
    }

    public String toOldMessageFormat()
    {
        StringBuilder result = new StringBuilder();
        for (MessagePart part : messageParts)
        {
            result.append(part.text);
        }
        return result.toString();
    }

    private MessagePart latest()
    {
        return messageParts.get(messageParts.size() - 1);
    }

    private Tellraw onClick(String name, String data)
    {
        MessagePart latest = latest();
        latest.clickActionName = name;
        latest.clickActionData = data;
        return this;
    }

    private Tellraw onHover(String name, String data)
    {
        MessagePart latest = latest();
        latest.hoverActionName = name;
        latest.hoverActionData = data;
        return this;
    }

    private Tellraw then(MessagePart part)
    {
        MessagePart last = latest();
        if (!last.hasText())
        {
            last.text = part.text;
        }
        else
        {
            messageParts.add(part);
        }
        cache = null;
        return this;
    }
}
