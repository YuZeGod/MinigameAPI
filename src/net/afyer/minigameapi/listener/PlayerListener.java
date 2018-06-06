package net.afyer.minigameapi.listener;

import net.afyer.minigameapi.MinigameAPI;
import net.afyer.minigameapi.achievement.AchievementManager;
import net.afyer.minigameapi.cache.CacheManager;
import net.afyer.minigameapi.game.AGame;
import net.afyer.minigameapi.game.event.PlayerJoinGameEvent;
import net.afyer.minigameapi.game.event.PlayerQuitGameEvent;
import net.afyer.minigameapi.spectator.SpectatorManager;
import net.afyer.minigameapi.spectator.SpectatorSettings;
import net.afyer.minigameapi.spectator.SpectatorSettings.Option;
import net.afyer.minigameapi.util.ItemBuilder;
import net.afyer.minigameapi.util.TitleUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.inventory.ItemStack;

public class PlayerListener extends BaseListener
{
    private static AGame game = MinigameAPI.getInstance().getGame();

    public PlayerListener(MinigameAPI plugin)
    {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerAdvancementAwarded(PlayerAchievementAwardedEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent e)
    {
        if (game == null)
        {
            e.setResult(Result.KICK_OTHER);
            e.setKickMessage("§c未发现游戏实例,不能进入该服务器!");
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        e.setJoinMessage(null);
        Player p = e.getPlayer();
        AchievementManager.fetchAchievements(p);
        if (game.isWaiting())
        {
            e.setJoinMessage(
                    "§7" + p.getName() + " §e加入了游戏(§b" + MinigameAPI.getInstance()
                            .getIngamePlayers().size() + "§e/§b" + game.getMaxPlayers()
                            + "§e)！");
            if (game.getLobbyLocation() != null)
            {
                e.getPlayer().teleport(game.getLobbyLocation());
            }
            p.setGameMode(GameMode.SURVIVAL);
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);
            p.setMaxHealth(20.0);
            p.setHealth(20.0);
            p.setFireTicks(0);
            p.setLevel(0);
            p.setExp(0);
            p.getInventory().setItem(8,
                    new ItemBuilder(Material.BED).setDisplayName("§c§l返回大厅 §7(右键点击)")
                            .build());
        }
        else if (game.isStarted())
        {
            if (SpectatorManager.isSpectator(p))
            {
                SpectatorManager.leaveSpectator(p);
                SpectatorManager.joinSpectator(p, "§c§l你死了！", "§7你现在是一名旁观");
                if (game.getLobbyLocation() != null)
                {
                    e.getPlayer().teleport(game.getLobbyLocation());
                }
            }
            else if ((game.getCachedPlayers().contains(p.getUniqueId()))
                    && (!SpectatorManager.isSpectator(p)))
            {
                e.setJoinMessage("§a" + e.getPlayer().getName() + " §7重新连接");
                game.onRejoin(p);
            }
        }
        Bukkit.getPluginManager().callEvent(new PlayerJoinGameEvent(game, e.getPlayer()));
        getPlugin().getEventHandler().onPlayerJoin(e);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        e.setQuitMessage(null);
        if (game.isWaiting())
        {
            e.setQuitMessage("§7" + e.getPlayer().getName() + " §e退出了游戏(§b" + (
                    MinigameAPI.getInstance().getIngamePlayers().size() - 1) + "§e/§b"
                    + game.getMaxPlayers() + "§e)！");
        }
        else if (game.isStarted() && !SpectatorManager.isSpectator(e.getPlayer()))
        {
            CacheManager.cache(e.getPlayer());
            e.setQuitMessage("§c" + e.getPlayer().getName() + " §7断开连接");
        }
        Bukkit.getPluginManager().callEvent(new PlayerQuitGameEvent(game, e.getPlayer()));
        getPlugin().getEventHandler().onPlayerQuit(e);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerKick(PlayerKickEvent e)
    {
        e.setLeaveMessage(null);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent e)
    {
        Player player = e.getPlayer();
        if (SpectatorManager.isSpectator(player))
        {
            player.setAllowFlight(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        if (SpectatorManager.isSpectator(e.getEntity()))
        {
            e.setDeathMessage(null);
            e.setDroppedExp(0);
            e.setKeepInventory(true);
            e.setKeepLevel(true);
            SpectatorManager.leaveSpectator(e.getEntity());
            SpectatorManager.joinSpectator(e.getEntity(), null, "§7你现在是一名旁观者");
            if (game.getLobbyLocation() != null)
            {
                e.getEntity().teleport(game.getLobbyLocation());
            }
        }
        getPlugin().getEventHandler().onPlayerDeath(e);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e)
    {
        if (!game.isStarted())
        {
            e.setFormat(ChatColor.translateAlternateColorCodes('&',
                    "&7" + e.getPlayer().getName() + ": " + e.getMessage()));
        }
        else
        {
            if (SpectatorManager.isSpectator(e.getPlayer()))
            {
                e.setFormat(ChatColor.translateAlternateColorCodes('&',
                        "&7" + e.getPlayer().getName() + ": " + e.getMessage()));
            }
        }
        getPlugin().getEventHandler().onAsyncPlayerChat(e);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent e)
    {
        getPlugin().getEventHandler().onPlayerMove(e);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDropItem(PlayerDropItemEvent e)
    {
        if (SpectatorManager.isSpectator(e.getPlayer()) || !game.isStarted())
        {
            e.setCancelled(true);
            return;
        }
        getPlugin().getEventHandler().onPlayerDropItem(e);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPickupItem(PlayerPickupItemEvent e)
    {
        if (SpectatorManager.isSpectator(e.getPlayer()) || !game.isStarted())
        {
            e.setCancelled(true);
            return;
        }
        getPlugin().getEventHandler().onPlayerPickupItem(e);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        Player player = e.getPlayer();
        if (e.getAction() == Action.LEFT_CLICK_AIR
                || e.getAction() == Action.LEFT_CLICK_BLOCK
                || e.getAction() == Action.RIGHT_CLICK_AIR
                || e.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            ItemStack item = player.getItemInHand();
            if (item == null || item.getType() == Material.AIR)
            {
                return;
            }
            else if (item.getType() == Material.BED)
            {
                player.sendMessage("§c§l返回大厅功能正在完善中！");
                return;
            }
            else if (SpectatorManager.isSpectator(player))
            {
                if (item.getType() == Material.COMPASS)
                {
                    if (e.getAction() == Action.RIGHT_CLICK_AIR
                            || e.getAction() == Action.RIGHT_CLICK_BLOCK)
                    {
                        game.openSpectatorInventory(player);
                        return;
                    }
                    else if ((e.getAction() == Action.LEFT_CLICK_AIR
                            || e.getAction() == Action.LEFT_CLICK_BLOCK) && (
                            SpectatorManager.getTarget(player) != null))
                    {
                        SpectatorManager.getTarget(player).tp();
                        return;
                    }
                }
                else if (item.getType() == Material.DIODE)
                {
                    player.closeInventory();
                    SpectatorManager.openSettingsInventory(player);
                    return;
                }
            }
        }
        getPlugin().getEventHandler().onPlayerInteract(e);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e)
    {
        Player player = e.getPlayer();
        if (SpectatorManager.isSpectator(player) || (!game.isStarted()))
        {
            e.setCancelled(true);
            return;
        }
        getPlugin().getEventHandler().onPlayerInteractEntity(e);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e)
    {
        Player player = e.getPlayer();
        if (SpectatorManager.isSpectator(player) || (!game.isStarted()))
        {
            if ((SpectatorManager.isSpectator(player)) && (game.isStarted()) && (e
                    .getRightClicked() instanceof Player) && (SpectatorSettings
                    .get(player).getOption(Option.FIRSTPERSON)))
            {
                e.setCancelled(true);
                TitleUtil.sendFullTitle(player, 0, 20, 0,
                        "&a正在旁观&7" + e.getRightClicked().getName(),
                        "&a点击左键打开菜单  &c按Shift键退出");
                player.setGameMode(GameMode.SPECTATOR);
                player.setSpectatorTarget(e.getRightClicked());
                return;
            }
            e.setCancelled(true);
            return;
        }
        getPlugin().getEventHandler().onPlayerInteractAtEntity(e);
    }

    @SuppressWarnings("deprecation") @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerToggleFlight(PlayerToggleFlightEvent e)
    {
        Player player = e.getPlayer();
        if (SpectatorManager.isSpectator(player) && (game.isStarted())
                && (SpectatorSettings.get(player).getOption(Option.FLY)))
        {
            e.setCancelled(true);
            if (player.isOnGround())
            {
                Location loc = player.getLocation().clone();
                loc.setY(player.getLocation().getY() + 0.1D);
                player.teleport(loc);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e)
    {
        Player player = e.getPlayer();
        if (SpectatorManager.isSpectator(player) && (game.isStarted())
                && (SpectatorSettings.get(player).getOption(Option.FIRSTPERSON)) && (
                player.getGameMode() == GameMode.SPECTATOR))
        {
            TitleUtil.sendFullTitle(player, 0, 20, 0, "&e退出旁观模式", null);
            player.setGameMode(GameMode.ADVENTURE);
            player.setAllowFlight(true);
            player.setFlying(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFoodLevelChange(FoodLevelChangeEvent e)
    {
        if ((e.getEntity() instanceof Player && SpectatorManager
                .isSpectator((Player) e.getEntity()) || !game.isStarted()))
        {
            e.setCancelled(true);
            return;
        }
        getPlugin().getEventHandler().onFoodLevelChange(e);
    }
}
