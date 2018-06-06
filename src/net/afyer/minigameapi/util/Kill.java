package net.afyer.minigameapi.util;

public class Kill
{
    private String name;
    private int kills;

    public Kill(String name, int kills)
    {
        this.name = name;
        this.kills = kills;
    }

    public String getName()
    {
        return name;
    }

    public int getKills()
    {
        return kills;
    }
}
