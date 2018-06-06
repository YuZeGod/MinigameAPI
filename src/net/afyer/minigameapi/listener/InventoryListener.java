package net.afyer.minigameapi.listener;

import net.afyer.minigameapi.MinigameAPI;
import net.afyer.minigameapi.achievement.AchievementManager;
import net.afyer.minigameapi.game.AGame;
import net.afyer.minigameapi.spectator.SpectatorManager;
import net.afyer.minigameapi.spectator.SpectatorSettings;
import net.afyer.minigameapi.spectator.SpectatorSettings.Option;
import net.afyer.minigameapi.util.PageInventoryHolder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InventoryListener extends BaseListener
{
    private static AGame game = MinigameAPI.getInstance().getGame();

    public InventoryListener(MinigameAPI plugin)
    {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e)
    {
        if (!(e.getWhoClicked() instanceof Player))
        {
            return;
        }
        ItemStack item = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();
        if (SpectatorManager.isSpectator(player))
        {
            e.setCancelled(true);
            if (item == null)
            {
                return;
            }
            else if (item.getType() == Material.AIR)
            {
                return;
            }
            if (e.getInventory().getTitle().equals("§7旁观者设置"))
            {
                SpectatorSettings settings = SpectatorSettings.get(player);
                switch (e.getRawSlot())
                {
                    case 11:
                        if (settings.getSpeed() == 0)
                        {
                            if (player.hasPotionEffect(PotionEffectType.SPEED))
                            {
                                player.removePotionEffect(PotionEffectType.SPEED);
                            }
                            return;
                        }
                        if (player.hasPotionEffect(PotionEffectType.SPEED))
                        {
                            player.removePotionEffect(PotionEffectType.SPEED);
                        }
                        player.sendMessage("§c你不再有任何速度效果！");
                        settings.setSpeed(0);
                        player.closeInventory();
                        break;
                    case 12:
                        if (settings.getSpeed() == 1)
                        {
                            if (player.hasPotionEffect(PotionEffectType.SPEED))
                            {
                                player.removePotionEffect(PotionEffectType.SPEED);
                            }
                            player.addPotionEffect(
                                    new PotionEffect(PotionEffectType.SPEED,
                                            Integer.MAX_VALUE, 1));
                            return;
                        }
                        if (player.hasPotionEffect(PotionEffectType.SPEED))
                        {
                            player.removePotionEffect(PotionEffectType.SPEED);
                        }
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
                                Integer.MAX_VALUE, 1));
                        player.sendMessage("§a你获得了 速度 I 效果！");
                        settings.setSpeed(1);
                        player.closeInventory();
                        break;
                    case 13:
                        if (settings.getSpeed() == 2)
                        {
                            if (player.hasPotionEffect(PotionEffectType.SPEED))
                            {
                                player.removePotionEffect(PotionEffectType.SPEED);
                            }
                            player.addPotionEffect(
                                    new PotionEffect(PotionEffectType.SPEED,
                                            Integer.MAX_VALUE, 2));
                            return;
                        }
                        if (player.hasPotionEffect(PotionEffectType.SPEED))
                        {
                            player.removePotionEffect(PotionEffectType.SPEED);
                        }
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
                                Integer.MAX_VALUE, 2));
                        player.sendMessage("§a你获得了 速度 II 效果！");
                        settings.setSpeed(2);
                        player.closeInventory();
                        break;
                    case 14:
                        if (settings.getSpeed() == 3)
                        {
                            if (player.hasPotionEffect(PotionEffectType.SPEED))
                            {
                                player.removePotionEffect(PotionEffectType.SPEED);
                            }
                            player.addPotionEffect(
                                    new PotionEffect(PotionEffectType.SPEED,
                                            Integer.MAX_VALUE, 3));
                            return;
                        }
                        if (player.hasPotionEffect(PotionEffectType.SPEED))
                        {
                            player.removePotionEffect(PotionEffectType.SPEED);
                        }
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
                                Integer.MAX_VALUE, 3));
                        player.sendMessage("§a你获得了 速度 III 效果！");
                        settings.setSpeed(3);
                        player.closeInventory();
                        break;
                    case 15:
                        if (settings.getSpeed() == 4)
                        {
                            if (player.hasPotionEffect(PotionEffectType.SPEED))
                            {
                                player.removePotionEffect(PotionEffectType.SPEED);
                            }
                            player.addPotionEffect(
                                    new PotionEffect(PotionEffectType.SPEED,
                                            Integer.MAX_VALUE, 4));
                            return;
                        }
                        if (player.hasPotionEffect(PotionEffectType.SPEED))
                        {
                            player.removePotionEffect(PotionEffectType.SPEED);
                        }
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
                                Integer.MAX_VALUE, 4));
                        player.sendMessage("§a你获得了 速度 IV 效果！");
                        settings.setSpeed(4);
                        player.closeInventory();
                        break;
                    case 20:
                        if (settings.getOption(Option.AUTOTP))
                        {
                            settings.setOption(Option.AUTOTP, false);
                            player.sendMessage("§c你不再被自动传送到目标位置！");
                            player.closeInventory();
                            return;
                        }
                        else
                        {
                            settings.setOption(Option.AUTOTP, true);
                            player.sendMessage("§a你开启了自动传送功能！");
                            player.closeInventory();
                        }
                        break;
                    case 21:
                        if (settings.getOption(Option.NIGHTVISION))
                        {
                            if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION))
                            {
                                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                            }
                            settings.setOption(Option.NIGHTVISION, false);
                            player.sendMessage("§c你不再有夜视效果了！");
                            player.closeInventory();
                            return;
                        }
                        else
                        {
                            if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION))
                            {
                                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                            }
                            player.addPotionEffect(
                                    new PotionEffect(PotionEffectType.NIGHT_VISION,
                                            Integer.MAX_VALUE, 1));
                            settings.setOption(Option.NIGHTVISION, true);
                            player.sendMessage("§a你现在拥有了夜视！");
                            player.closeInventory();
                        }
                        break;
                    case 22:
                        if (settings.getOption(Option.FIRSTPERSON))
                        {
                            settings.setOption(Option.FIRSTPERSON, false);
                            player.sendMessage("§c你将默认使用第三人称模式！");
                            player.closeInventory();
                            return;
                        }
                        else
                        {
                            settings.setOption(Option.FIRSTPERSON, true);
                            player.sendMessage("§a当你用你的指南针现在一个玩家后，你会被自动传送到他那里！");
                            player.closeInventory();
                        }
                        break;
                    case 23:
                        if (settings.getOption(Option.HIDEOTHER))
                        {
                            settings.setOption(Option.HIDEOTHER, false);
                            player.sendMessage("§a你现在可以看见其他旁观者了！");
                            player.closeInventory();
                            return;
                        }
                        else
                        {
                            settings.setOption(Option.HIDEOTHER, true);
                            player.sendMessage("§c你不会再看到其他的旁观者！");
                            player.closeInventory();
                        }
                        break;
                    case 24:
                        if (settings.getOption(Option.FLY))
                        {
                            settings.setOption(Option.FLY, false);
                            player.sendMessage("§a你现在能停止飞行！");
                            player.closeInventory();
                            return;
                        }
                        else
                        {
                            settings.setOption(Option.FLY, true);
                            player.sendMessage("§a你现在不能停止飞行！");
                            player.closeInventory();
                        }
                        break;
                }
            }
            switch (item.getType())
            {
                case COMPASS:
                    game.openSpectatorInventory(player);
                    break;
                case DIODE:
                    player.closeInventory();
                    SpectatorManager.openSettingsInventory(player);
                    break;
                case BED:
                    player.sendMessage("§c§l返回大厅功能正在完善中！");
                    break;
                default:
                    break;
            }
        }
        else if (game.isWaiting())
        {
            e.setCancelled(true);
            if (e.getInventory().getTitle().equals("§7成就"))
            {
                if (item == null)
                {
                    return;
                }
                else if (item.getType() == Material.AIR)
                {
                    return;
                }
                else if (item.getType() == Material.ARROW)
                {
                    String action = ChatColor
                            .stripColor(item.getItemMeta().getDisplayName());
                    if ((e.getInventory().getHolder() != null) && (e.getInventory()
                            .getHolder() instanceof PageInventoryHolder))
                    {
                        PageInventoryHolder holder = (PageInventoryHolder) e
                                .getInventory().getHolder();
                        player.closeInventory();
                        switch (action)
                        {
                            case "上一页":
                                AchievementManager
                                        .openInventory(player, holder.getPage() - 1);
                                break;
                            case "下一页":
                                AchievementManager
                                        .openInventory(player, holder.getPage() + 1);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            if (item == null)
            {
                return;
            }
            else if (item.getType() == Material.AIR)
            {
                return;
            }
            else if (item.getType() == Material.BED)
            {
                player.sendMessage("§c§l返回大厅功能正在完善中！");
                return;
            }
        }
        getPlugin().getEventHandler().onInventoryClick(e);
    }
}
