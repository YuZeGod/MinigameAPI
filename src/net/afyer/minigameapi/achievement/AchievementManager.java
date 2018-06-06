package net.afyer.minigameapi.achievement;

import net.afyer.minigameapi.MinigameAPI;
import net.afyer.minigameapi.achievement.event.PlayerAwardAchievementEvent;
import net.afyer.minigameapi.database.KeyValue;
import net.afyer.minigameapi.tellraw.Tellraw;
import net.afyer.minigameapi.util.ItemBuilder;
import net.afyer.minigameapi.util.PageInventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AchievementManager
{
    public static final String TABLENAME = "achievements";
    public static final KeyValue KV = new KeyValue("uuid", "VARCHAR(36) PRIMARY KEY");
    private static final List<Achievement> ACHIEVEMENTS = new ArrayList<Achievement>();
    private static final Map<OfflinePlayer, List<Achievement>> CACHES = new HashMap<OfflinePlayer, List<Achievement>>();
    private static final int[] SLOT = new int[] { 10, 11, 12, 13, 14, 15, 16, 19, 20, 21,
            22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34 };

    public static void openInventory(Player player, int page)
    {
        if (page > getMaxPages())
        {
            page = getMaxPages();
        }
        if (page < 1)
        {
            page = 1;
        }
        PageInventoryHolder holder = new PageInventoryHolder(page);
        Inventory inv = Bukkit.createInventory(holder, 54, "§7成就");
        holder.setInventory(inv);
        int i = 0;
        int from = SLOT.length * (page - 1) + 1;
        int to = SLOT.length * page;
        Achievement[] achievements = ACHIEVEMENTS.toArray(new Achievement[0]);
        for (int h = from; h <= to; h++)
        {
            if ((i > SLOT.length) || (h >= achievements.length))
            {
                break;
            }
            Achievement achievement = achievements[h - 1];
            if (achievement != null)
            {
                boolean unlocked = hasAchievement(player, achievement.getId());
                List<String> lore = new ArrayList<String>();
                for (String line : achievement.getDescription())
                {
                    lore.add("§f" + ChatColor.stripColor(line));
                }
                lore.add(" ");
                lore.add("§7点数: §e" + achievement.getPoints());
                lore.add(" ");
                lore.add(unlocked ? "§a已解锁" : "§c未解锁");
                inv.setItem(SLOT[i],
                        new ItemBuilder(unlocked ? Material.DIAMOND : Material.COAL,
                                achievement.getPoints()).setDisplayName(
                                "§c" + ChatColor.stripColor(achievement.getName()))
                                .setLore(lore.toArray(new String[0])).build());
            }
            i += 1;
        }
        if (getMaxPages() > 1)
        {
            if (page == 1)
            {
                inv.setItem(53, new ItemBuilder(Material.ARROW).setDisplayName("§a下一页")
                        .setLore("§e页序号: " + (page + 1)).build());
            }
            else if ((page > 1) && (page < getMaxPages()))
            {
                inv.setItem(45, new ItemBuilder(Material.ARROW).setDisplayName("§a上一页")
                        .setLore("§e页序号: " + (page - 1)).build());
                inv.setItem(53, new ItemBuilder(Material.ARROW).setDisplayName("§a下一页")
                        .setLore("§e页序号: " + (page + 1)).build());
            }
            else if (page == getMaxPages())
            {
                inv.setItem(45, new ItemBuilder(Material.ARROW).setDisplayName("§a上一页")
                        .setLore("§e页序号: " + (page - 1)).build());
            }
        }
        DecimalFormat df = new DecimalFormat("#");
        inv.setItem(49, new ItemBuilder(Material.GOLD_NUGGET).setDisplayName("§a成就")
                .setLore("§8" + MinigameAPI.getInstance().getGame().getDisplayName(),
                        "§7已解锁: §b" + fetchAchievements(player).size() + "§7/§b"
                                + ACHIEVEMENTS.size() + " §8(" + df
                                .format(fetchAchievements(player).size() / ACHIEVEMENTS
                                        .size()) + "%)",
                        "§7点数: §e" + getPoints(player) + "§7/§e" + getTotalPoints()
                                + " §8(" + df.format(getPoints(player) / getTotalPoints())
                                + "%)").build());
        player.openInventory(inv);
    }

    private static int getMaxPages()
    {
        int i = ACHIEVEMENTS.size();
        if (i % SLOT.length == 0)
        {
            return i / SLOT.length;
        }
        double j = i / SLOT.length;
        int h = (int) Math.floor(j * 100) / 100;
        return h + 1;
    }

    public static boolean rewardAchievement(Player player, int id)
    {
        if (getById(id) == null)
        {
            return false;
        }
        Achievement achievement = getById(id);
        Bukkit.getPluginManager().callEvent(
                new PlayerAwardAchievementEvent(player.getPlayer(), achievement));
        List<String> tip = new ArrayList<String>();
        tip.add("§a" + achievement.getName());
        tip.addAll(achievement.getDescription());
        tip.add(" ");
        tip.add("§7点数: §e" + achievement.getPoints());
        Tellraw.create("§a§kI§r§a>>    已解锁成就: ").then("§6" + achievement.getName())
                .tip(tip).then("    §a<<§kI").send(player.getPlayer());
        List<Achievement> list = fetchAchievements(player);
        list.add(achievement);
        saveAchievements(player, list);
        fetchAchievements(player);
        return false;
    }

    public static List<Achievement> fetchAchievements(OfflinePlayer player)
    {
        if (MinigameAPI.getInstance().getGame() == null)
        {
            return new ArrayList<Achievement>();
        }
        else if (CACHES.containsKey(player))
        {
            return CACHES.get(player);
        }
        String gameName = MinigameAPI.getInstance().getGame().getName();
        List<Achievement> list = new ArrayList<Achievement>();
        if (!MinigameAPI.getInstance().getDataBase().isValueExists(TABLENAME, KV,
                new KeyValue("uuid", player.getUniqueId().toString())))
        {
            MinigameAPI.getInstance().getDataBase().dbInsert(TABLENAME,
                    new KeyValue("uuid", player.getUniqueId().toString()));
        }
        for (KeyValue kv : MinigameAPI.getInstance().getDataBase()
                .dbSelect(TABLENAME, new KeyValue(gameName, "VARCHAR"),
                        new KeyValue("uuid", player.getUniqueId().toString())))
        {
            if (kv.getString(gameName).equals(""))
            {
                return new ArrayList<Achievement>();
            }
            for (String s : kv.getString(gameName).split("#"))
            {
                if (s.equals(""))
                {
                    continue;
                }
                list.add(getById(Integer.parseInt(s)));
            }
            break;
        }
        CACHES.put(player, list);
        return list;
    }

    public static int saveAchievements(OfflinePlayer player,
            List<Achievement> achievements)
    {
        String line = "";
        for (Achievement achieve : achievements)
        {
            line = line + achieve.getId() + "#";
        }
        CACHES.put(player, achievements);
        return MinigameAPI.getInstance().getDataBase().dbUpdate(TABLENAME,
                new KeyValue("uuid", player.getUniqueId().toString())
                        .add(MinigameAPI.getInstance().getGame().getName(), line),
                new KeyValue("uuid", player.getUniqueId().toString()));
    }

    public static boolean hasAchievement(OfflinePlayer player, int id)
    {
        List<Achievement> list = CACHES.getOrDefault(player, fetchAchievements(player));
        for (Achievement achieve : list)
        {
            if (achieve.getId() == id)
            {
                return true;
            }
        }
        return false;
    }

    public static int getPoints(OfflinePlayer player)
    {
        List<Achievement> list = CACHES.getOrDefault(player, fetchAchievements(player));
        int points = 0;
        for (Achievement achievement : list)
        {
            points += achievement.getPoints();
        }
        return points;
    }

    public static int getTotalPoints()
    {
        int points = 0;
        for (Achievement achievement : ACHIEVEMENTS)
        {
            points += achievement.getPoints();
        }
        return points;
    }

    public static Achievement getById(int id)
    {
        for (Achievement achievement : ACHIEVEMENTS)
        {
            if (achievement.getId() == id)
            {
                return achievement;
            }
        }
        return null;
    }

    public static void register(Achievement achievement)
    {
        ACHIEVEMENTS.add(achievement);
    }

    public static List<Achievement> getAchievements()
    {
        return ACHIEVEMENTS;
    }
}
