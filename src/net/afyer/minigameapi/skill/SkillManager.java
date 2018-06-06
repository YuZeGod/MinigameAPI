package net.afyer.minigameapi.skill;

import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillManager
{
    private static final List<Skill> SKILLS = new ArrayList<>();
    private static final Map<OfflinePlayer, Long> cooldown = new HashMap<OfflinePlayer, Long>();

    public static Skill getSkillByName(String skillName)
    {
        for (Skill skill : SKILLS)
        {
            if (skill.getName().equals(skillName))
            {
                return skill;
            }
        }
        return null;
    }

    public static boolean isCooldown(OfflinePlayer player)
    {
        if (getCooldown().containsKey(player)
                && System.currentTimeMillis() < getCooldown().get(player))
        {
            return true;
        }
        return false;
    }

    public static void setCooldown(OfflinePlayer player, Skill skill)
    {
        if (!isCooldown(player))
        {
            getCooldown()
                    .put(player, System.currentTimeMillis() + skill.getCooldown() * 1000);
        }
    }

    public static void addSkill(Skill skill)
    {
        SKILLS.add(skill);
    }

    public static List<Skill> getSkills()
    {
        return SKILLS;
    }

    public static Map<OfflinePlayer, Long> getCooldown()
    {
        return cooldown;
    }
}
