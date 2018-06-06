package net.afyer.minigameapi.cache;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

public class PlayerData
{
    private final OfflinePlayer player;
    private final UUID uuid;
    private final float xp;
    private final int food;
    private final int level;
    private final Location loc;
    private final GameMode mode;
    private final double health;
    private final int fireticks;
    private final float flyspeed;
    private final float walkspeed;
    private final boolean isFlying;
    private final float fallDistance;
    private final ItemStack[] armour;
    private final ItemStack[] inventory;
    private final Collection<PotionEffect> effects;

    public PlayerData(OfflinePlayer player)
    {
        this.player = player;
        uuid = player.getUniqueId();
        xp = player.getPlayer().getExp();
        level = player.getPlayer().getLevel();
        loc = player.getPlayer().getLocation();
        health = player.getPlayer().getHealth();
        mode = player.getPlayer().getGameMode();
        food = player.getPlayer().getFoodLevel();
        isFlying = player.getPlayer().getAllowFlight();
        flyspeed = player.getPlayer().getFlySpeed();
        fireticks = player.getPlayer().getFireTicks();
        walkspeed = player.getPlayer().getWalkSpeed();
        fallDistance = player.getPlayer().getFallDistance();
        effects = player.getPlayer().getActivePotionEffects();
        inventory = player.getPlayer().getInventory().getContents();
        armour = player.getPlayer().getInventory().getArmorContents();
    }

    public OfflinePlayer getPlayer()
    {
        return player;
    }

    public UUID getUUID()
    {
        return uuid;
    }

    public void restore()
    {
        player.getPlayer().teleport(loc);
        player.getPlayer().setExp(xp);
        player.getPlayer().setLevel(level);
        player.getPlayer().setGameMode(mode);
        player.getPlayer().setHealth(health);
        player.getPlayer().setFoodLevel(food);
        if (isFlying)
        {
            player.getPlayer().setAllowFlight(true);
            player.getPlayer().setFlying(true);
        }
        else
        {
            player.getPlayer().setAllowFlight(true);
            player.getPlayer().setFlying(false);
            player.getPlayer().setAllowFlight(false);
        }
        player.getPlayer().setFlySpeed(flyspeed);
        player.getPlayer().setFireTicks(fireticks);
        player.getPlayer().setWalkSpeed(walkspeed);
        player.getPlayer().setFallDistance(fallDistance);
        player.getPlayer().getInventory().setArmorContents(armour);
        player.getPlayer().getInventory().setContents(inventory);
        PotionEffect effect = null;
        Iterator it = null;
        for (it = player.getPlayer().getActivePotionEffects().iterator(); it.hasNext(); )
        {
            effect = (PotionEffect) it.next();
            player.getPlayer().removePotionEffect(effect.getType());
        }
        for (it = effects.iterator(); it.hasNext(); )
        {
            effect = (PotionEffect) it.next();
            player.getPlayer().addPotionEffect(effect);
        }
        player.getPlayer().updateInventory();
    }
}
