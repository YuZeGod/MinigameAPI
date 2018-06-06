package net.afyer.minigameapi.commands;

import net.afyer.minigameapi.MinigameAPI;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler implements CommandExecutor, TabCompleter
{
    private List<BaseCommand> commands = new ArrayList<BaseCommand>();

    public CommandHandler()
    {
        registerCommand(MinigameAPI.getInstance(), new CommandForceStart());
    }

    @Override public boolean onCommand(CommandSender sender, Command cmd, String label,
            String[] args)
    {
        for (BaseCommand command : commands)
        {
            if (command.isValidTrigger(cmd.getName()))
            {
                if (!command.hasPermission(sender))
                {
                    sender.sendMessage("§c你没有此命令的权限！");
                    return true;
                }
                if (command.isOnlyPlayerExecutable() && !(sender instanceof Player))
                {
                    sender.sendMessage("§c控制台无法使用此命令！");
                    return true;
                }
                if (args.length >= command.getMinimumArguments())
                {
                    try
                    {
                        command.execute(sender, label, args);
                        return true;
                    }
                    catch (CommandException e)
                    {
                    }
                }
                else
                {
                    sender.sendMessage("§c错误的参数: /" + command.getName() + command
                            .getPossibleArguments());
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label,
            String[] args)
    {
        return null;
    }

    public void registerCommand(JavaPlugin plugin, BaseCommand command)
    {
        commands.add(command);
        plugin.getCommand(command.getName()).setExecutor(this);
        plugin.getCommand(command.getName()).setTabCompleter(this);
    }
}
