package net.afyer.minigameapi.achievement;

import java.util.List;

public class Achievement
{
    private int id;
    private int points;
    private String name;
    private List<String> description;

    public Achievement(int id, int points, String name, List<String> description)
    {
        this.id = id;
        this.points = points;
        this.name = name;
        this.description = description;
    }

    public int getId()
    {
        return id;
    }

    public int getPoints()
    {
        return points;
    }

    public String getName()
    {
        return name;
    }

    public List<String> getDescription()
    {
        return description;
    }
}
