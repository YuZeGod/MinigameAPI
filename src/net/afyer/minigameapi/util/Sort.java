package net.afyer.minigameapi.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Sort
{
    public static void sort(List<Kill> kills)
    {
        Collections.sort(kills, new Comparator<Kill>()
        {
            @Override public int compare(Kill o1, Kill o2)
            {
                return o2.getKills() - o1.getKills();
            }
        });
    }

    public static void main(String[] args)
    {
        List<Kill> kills = new ArrayList<Kill>();
        kills.add(new Kill("YuZeGod", 13));
        kills.add(new Kill("_Aning", 13));
        kills.add(new Kill("duserxiao", 11));
        kills.add(new Kill("South_Town", 12));
        kills.add(new Kill("chengzi", 6));
        kills.add(new Kill("mcard", 5));
        sort(kills);
        for (Kill kill : kills)
        {
            System.out.println(kill.getName() + " : " + kill.getKills());
        }
    }
}
