package net.afyer.minigameapi.spectator;

import net.afyer.minigameapi.MinigameAPI;
import net.afyer.minigameapi.spectator.event.SpectatorJoinEvent;
import net.afyer.minigameapi.spectator.event.SpectatorLeaveEvent;
import net.afyer.minigameapi.util.ItemBuilder;
import net.afyer.minigameapi.util.PlayerUtil;
import net.afyer.minigameapi.util.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpectatorManager
{
    private static final List<OfflinePlayer> SPECTATORS = new ArrayList<OfflinePlayer>();
    private static final Map<OfflinePlayer, SpectatorTarget> TARGETS = new HashMap<OfflinePlayer, SpectatorTarget>();
    private static final PotionEffect INVISIBILITY = new PotionEffect(
            PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1);
    private static final PotionEffect NIGHTVISION = new PotionEffect(
            PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1);

    public static void openSettingsInventory(Player p)
    {
        if (!isSpectator(p))
        {
            return;
        }
        SpectatorSettings settings = SpectatorSettings.get(p);
        Inventory inv = Bukkit.createInventory(null, 36, "§7旁观者设置");
        inv.setItem(11, new ItemBuilder(Material.LEATHER_BOOTS).setDisplayName("§a没有速度效果")
                .build());
        inv.setItem(12, new ItemBuilder(Material.CHAINMAIL_BOOTS).setDisplayName("§a速度 I")
                .build());
        inv.setItem(13,
                new ItemBuilder(Material.IRON_BOOTS).setDisplayName("§a速度 II").build());
        inv.setItem(14,
                new ItemBuilder(Material.GOLD_BOOTS).setDisplayName("§a速度 III").build());
        inv.setItem(15, new ItemBuilder(Material.DIAMOND_BOOTS).setDisplayName("§a速度 IV")
                .build());
        if (settings.getOption(SpectatorSettings.Option.AUTOTP))
        {
            inv.setItem(20, new ItemBuilder(Material.COMPASS).setDisplayName("§c停用自动传送")
                    .setLore("§7点击停用自动传送").build());
        }
        else
        {
            inv.setItem(20, new ItemBuilder(Material.COMPASS).setDisplayName("§a启动自动传送")
                    .setLore("§7点击启用自动传送").build());
        }
        if (settings.getOption(SpectatorSettings.Option.NIGHTVISION))
        {
            inv.setItem(21, new ItemBuilder(Material.ENDER_PEARL).setDisplayName("§c停用夜视")
                    .setLore("§7点击停用夜视").build());
        }
        else
        {
            inv.setItem(21,
                    new ItemBuilder(Material.EYE_OF_ENDER).setDisplayName("§a启动夜视")
                            .setLore("§7点击启用夜视").build());
        }
        if (settings.getOption(SpectatorSettings.Option.FIRSTPERSON))
        {
            inv.setItem(22, new ItemBuilder(Material.WATCH).setDisplayName("§c停用第一人称旁观")
                    .setLore("§7点击停用第一人称旁观").build());
        }
        else
        {
            inv.setItem(22, new ItemBuilder(Material.WATCH).setDisplayName("§a启动第一人称旁观")
                    .setLore("§7点击确认使用指南针时", "§7自动沿用第一人称旁观！", "§7你也可以右键点击一位玩家",
                            "§7来启用第一人称旁观").build());
        }
        if (settings.getOption(SpectatorSettings.Option.HIDEOTHER))
        {
            inv.setItem(23,
                    new ItemBuilder(Material.GLOWSTONE_DUST).setDisplayName("§a查看旁观者")
                            .setLore("§7点击以显示其他旁观者").build());
        }
        else
        {
            inv.setItem(23, new ItemBuilder(Material.REDSTONE).setDisplayName("§c隐藏旁观者")
                    .setLore("§7点击来隐藏其他旁观者").build());
        }
        if (settings.getOption(SpectatorSettings.Option.FLY))
        {
            inv.setItem(24, new ItemBuilder(Material.FEATHER).setDisplayName("§c停用持续飞行")
                    .setLore("§7点击停用飞行").build());
        }
        else
        {
            inv.setItem(24, new ItemBuilder(Material.FEATHER).setDisplayName("§a启动持续飞行")
                    .setLore("§7点击启用飞行").build());
        }
        p.openInventory(inv);
    }

    public static void joinSpectator(Player p, String title, String subTitle)
    {
        if (SPECTATORS.contains(p))
        {
            return;
        }
        Bukkit.getPluginManager().callEvent(new SpectatorJoinEvent(p));
        SPECTATORS.add(p);
        if (p.isDead())
        {
            PlayerUtil.respawn(p);
        }
        p.spigot().setCollidesWithEntities(false);
        p.setGameMode(GameMode.ADVENTURE);
        p.setFoodLevel(20);
        p.setMaxHealth(20D);
        p.setHealth(20D);
        p.setFireTicks(0);
        p.setLevel(0);
        p.setExp(0F);
        if (!TARGETS.containsKey(p))
        {
            TARGETS.put(p, new SpectatorTarget(p, null));
        }
        for (Entity entity : p.getNearbyEntities(40D, 40D, 40D))
        {

            Creature livingEntity = null;
            if (entity instanceof Creature)
            {
                livingEntity = (Creature) entity;
                if ((livingEntity.getTarget() != null) && (livingEntity.getTarget()
                        .equals(p)))
                {
                    livingEntity.setTarget(null);
                }
            }
        }
        Bukkit.getScheduler().runTaskLater(MinigameAPI.getInstance(), new Runnable()
        {
            @Override public void run()
            {
                p.getInventory().setArmorContents(null);
                p.getInventory().clear();
                p.getInventory().setItem(0, new ItemBuilder(Material.COMPASS)
                        .setDisplayName("§a§l传送器 §7(右键点击)").build());
                p.getInventory().setItem(4, new ItemBuilder(Material.DIODE)
                        .setDisplayName("§b§l旁观者设置 §7(右键点击)").build());
                p.getInventory().setItem(8,
                        new ItemBuilder(Material.BED).setDisplayName("§c§l返回大厅 §7(右键点击)")
                                .build());
            }
        }, 15L);
        for (PotionEffect effect : p.getActivePotionEffects())
        {
            p.removePotionEffect(effect.getType());
        }
        p.getPlayer().addPotionEffect(INVISIBILITY);
        SpectatorSettings settings = SpectatorSettings.get(p);
        if (settings.getOption(SpectatorSettings.Option.NIGHTVISION))
        {
            if (p.hasPotionEffect(PotionEffectType.NIGHT_VISION))
            {
                p.removePotionEffect(PotionEffectType.NIGHT_VISION);
            }
            p.getPlayer().addPotionEffect(NIGHTVISION);
        }
        p.setAllowFlight(true);
        p.setFlying(true);
        TitleUtil.sendFullTitle(p, 0, 100, 0, title, subTitle);
    }

    public static void leaveSpectator(Player p)
    {
        if (!SPECTATORS.contains(p))
        {
            return;
        }
        Bukkit.getPluginManager().callEvent(new SpectatorLeaveEvent(p));
        SPECTATORS.remove(p);
        if (TARGETS.containsKey(p))
        {
            TARGETS.remove(p);
        }
        p.spigot().setCollidesWithEntities(true);
        p.setFlying(false);
        p.setAllowFlight(false);
        p.setGameMode(GameMode.SURVIVAL);
        p.getInventory().clear();
    }

    public static boolean isSpectator(OfflinePlayer p)
    {
        return SPECTATORS.contains(p);
    }

    public static List<OfflinePlayer> getSpectators()
    {
        return SPECTATORS;
    }

    public static List<Player> getOnlineSpectators()
    {
        List<Player> players = new ArrayList<Player>();
        for (OfflinePlayer player : SPECTATORS)
        {
            if (player.isOnline())
            {
                players.add(player.getPlayer());
            }
        }
        return players;
    }

    public static SpectatorTarget getTarget(OfflinePlayer p)
    {
        return TARGETS.getOrDefault(p, null);
    }
}
