package net.afyer.minigameapi.listener;

import net.afyer.minigameapi.MinigameAPI;
import net.afyer.minigameapi.game.AGame;
import net.afyer.minigameapi.spectator.SpectatorManager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.*;

import java.text.NumberFormat;

public class EntityListener extends BaseListener
{
    private static AGame game = MinigameAPI.getInstance().getGame();

    public EntityListener(MinigameAPI plugin)
    {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPotionSplash(PotionSplashEvent e)
    {
        for (LivingEntity entity : e.getAffectedEntities())
        {
            if ((entity instanceof Player) && (SpectatorManager
                    .isSpectator((Player) entity)))
            {
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(EntityExplodeEvent e)
    {
        if (!game.isStarted())
        {
            e.setCancelled(true);
            return;
        }
        getPlugin().getEventHandler().onEntityExplode(e);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityTarget(EntityTargetEvent e)
    {
        if (e.getTarget() instanceof Player)
        {
            Player player = (Player) e.getTarget();
            if (SpectatorManager.isSpectator(player))
            {
                e.setCancelled(true);
                return;
            }
        }
        getPlugin().getEventHandler().onEntityTarget(e);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent e)
    {
        if (!game.isStarted())
        {
            e.setCancelled(true);
            return;
        }
        else if (e.getEntity() instanceof Player)
        {
            Player player = (Player) e.getEntity();
            if (SpectatorManager.isSpectator(player))
            {
                e.setCancelled(true);
                player.setFireTicks(0);
                return;
            }
        }
        if (e instanceof EntityDamageByEntityEvent)
        {
            EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) e;
            if (edbee.getDamager() instanceof Player)
            {
                Player damager = (Player) edbee.getDamager();
                if (SpectatorManager.isSpectator(damager))
                {
                    e.setCancelled(true);
                    damager.setFireTicks(0);
                    return;
                }
            }
            else if (edbee.getDamager() instanceof Projectile)
            {
                Projectile projectile = (Projectile) edbee.getDamager();
                if (projectile.getShooter() instanceof Player)
                {
                    Player shooter = (Player) projectile.getShooter();
                    if (SpectatorManager.isSpectator(shooter))
                    {
                        e.setCancelled(true);
                        return;
                    }
                    else if (projectile.getType() == EntityType.ARROW && e
                            .getEntity() instanceof Player)
                    {
                        Player player = (Player) e.getEntity();
                        double health = player.getHealth();
                        Double damage = Double.valueOf(e.getFinalDamage());
                        if (!player.isDead())
                        {
                            Double realHealth = Double
                                    .valueOf(health - damage.intValue());
                            NumberFormat nf = NumberFormat.getInstance();
                            nf.setMaximumFractionDigits(1);
                            if (realHealth.intValue() > 0)
                            {
                                shooter.sendMessage(
                                        "§e你射中了§7" + player.getName() + "§e剩余§c" + nf
                                                .format(realHealth) + "§e血量！");
                            }
                        }
                    }
                }
            }
        }
        getPlugin().getEventHandler().onEntityDamage(e);
    }
}
