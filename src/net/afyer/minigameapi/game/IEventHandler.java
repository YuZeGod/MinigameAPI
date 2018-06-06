package net.afyer.minigameapi.game;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public interface IEventHandler
{
    public void onPlayerJoin(PlayerJoinEvent e);

    public void onPlayerQuit(PlayerQuitEvent e);

    public void onPlayerDeath(PlayerDeathEvent e);

    public void onPlayerMove(PlayerMoveEvent e);

    public void onAsyncPlayerChat(AsyncPlayerChatEvent e);

    public void onPlayerDropItem(PlayerDropItemEvent e);

    public void onPlayerPickupItem(PlayerPickupItemEvent e);

    public void onPlayerInteract(PlayerInteractEvent e);

    public void onPlayerInteractEntity(PlayerInteractEntityEvent e);

    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e);

    public void onFoodLevelChange(FoodLevelChangeEvent e);

    public void onBlockBreak(BlockBreakEvent e);

    public void onBlockPlace(BlockPlaceEvent e);

    public void onEntityExplode(EntityExplodeEvent e);

    public void onEntityTarget(EntityTargetEvent e);

    public void onEntityDamage(EntityDamageEvent e);

    public void onInventoryClick(InventoryClickEvent e);
}
