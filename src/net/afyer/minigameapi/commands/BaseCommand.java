package net.afyer.minigameapi.commands;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseCommand
{
    private String name;
    private String permission;
    private String[] aliases;

    public BaseCommand(String name)
    {
        this(name, new String[0]);
    }

    public BaseCommand(String name, String... aliases)
    {
        this.name = name;
        this.aliases = aliases;
    }

    public String getName()
    {
        return name;
    }

    public String getPermission()
    {
        return permission;
    }

    public void setPermission(String permission)
    {
        this.permission = permission;
    }

    public final boolean hasPermission(CommandSender sender)
    {
        if (permission == null)
        {
            return true;
        }
        return sender.hasPermission(permission);
    }

    public abstract String getPossibleArguments();

    public abstract int getMinimumArguments();

    public abstract void execute(CommandSender sender, String label, String[] args)
            throws CommandException;

    public abstract boolean isOnlyPlayerExecutable();

    public final boolean isValidTrigger(String name)
    {
        if (this.name.equalsIgnoreCase(name))
        {
            return true;
        }
        if (aliases != null)
        {
            for (String alias : aliases)
            {
                if (alias.equalsIgnoreCase(name))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> getCommandList()
    {
        List<String> cmds = new ArrayList<String>();
        cmds.add(name);
        cmds.addAll(Arrays.asList(aliases));
        return cmds;
    }
}
