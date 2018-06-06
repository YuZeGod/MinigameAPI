package net.afyer.minigameapi.skill;

import org.bukkit.entity.Player;

public abstract class Skill
{
    private String name;
    private int cooldown;

    public Skill(String name, int cooldown)
    {
        this.name = name;
        this.cooldown = cooldown;
    }

    public abstract void execute(Player p);

    public String getName()
    {
        return name;
    }

    public int getCooldown()
    {
        return cooldown;
    }
}
