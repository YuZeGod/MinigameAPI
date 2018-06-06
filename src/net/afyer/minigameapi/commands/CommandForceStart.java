package net.afyer.minigameapi.commands;

import net.afyer.minigameapi.MinigameAPI;
import net.afyer.minigameapi.game.AGame;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

public class CommandForceStart extends BaseCommand
{

    public CommandForceStart()
    {
        super("forcestart");
        setPermission("MinigameAPI.admin");
    }

    @Override public String getPossibleArguments()
    {
        return "";
    }

    @Override public int getMinimumArguments()
    {
        return 0;
    }

    @Override public void execute(CommandSender sender, String label, String[] args)
            throws CommandException
    {
        AGame game = MinigameAPI.getInstance().getGame();
        if (game == null)
        {
            sender.sendMessage("§c§l未获取到游戏对象,无法强制开启游戏！");
            return;
        }
        else
        {
            game.forceStart();
        }
    }

    @Override public boolean isOnlyPlayerExecutable()
    {
        return false;
    }
}
