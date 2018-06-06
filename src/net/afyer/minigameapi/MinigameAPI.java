package net.afyer.minigameapi;

import net.afyer.minigameapi.achievement.AchievementManager;
import net.afyer.minigameapi.commands.CommandHandler;
import net.afyer.minigameapi.config.FileConfig;
import net.afyer.minigameapi.database.DataBase;
import net.afyer.minigameapi.database.KeyValue;
import net.afyer.minigameapi.game.AGame;
import net.afyer.minigameapi.game.GameTimer;
import net.afyer.minigameapi.game.IEventHandler;
import net.afyer.minigameapi.listener.*;
import net.afyer.minigameapi.spectator.SpectatorManager;
import net.afyer.minigameapi.spectator.SpectatorSettings;
import net.afyer.minigameapi.spectator.SpectatorTarget;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MinigameAPI extends JavaPlugin
{
    private static MinigameAPI instance;
    private FileConfig config;
    private AGame game;
    private IEventHandler eventHandler;
    private GameTimer gameTimer;
    private CommandHandler commandHandler;
    private DataBase database;

    public static MinigameAPI getInstance()
    {
        return instance;
    }

    @Override public void onEnable()
    {
        instance = this;
        config = new FileConfig(this);
        commandHandler = new CommandHandler();
        database = DataBase.create(getConfig().getConfigurationSection("database"));
        database.createTables(SpectatorSettings.TABLENAME, SpectatorSettings.KV, null);
        database.createTables(AchievementManager.TABLENAME, AchievementManager.KV, null);
    }

    @Override public void onDisable()
    {
        if (game != null && game.isStarted())
        {
            game.onStop();
        }
        database.close();
    }

    @Override public FileConfig getConfig()
    {
        return config;
    }

    public DataBase getDataBase()
    {
        return database;
    }

    public AGame getGame()
    {
        return game;
    }

    public void setupGame(AGame game)
    {
        if (this.game != null)
        {
            return;
        }
        this.game = game;
        gameTimer = new GameTimer();
        gameTimer.setTime(game.getWaitSeconds());
        Bukkit.getScheduler().runTaskTimer(this, gameTimer, 0L, 20L);
        registerListener();
        Bukkit.getScheduler().runTaskTimer(this, new Runnable()
        {
            @Override public void run()
            {
                for (Player p : Bukkit.getOnlinePlayers())
                {
                    if ((SpectatorManager.isSpectator(p)) && (
                            SpectatorManager.getTarget(p) != null))
                    {
                        SpectatorTarget target = SpectatorManager.getTarget(p);
                        target.sendTip();
                        target.autoTp();
                    }
                    for (Player player : Bukkit.getOnlinePlayers())
                    {
                        if (p.equals(player))
                        {
                            return;
                        }
                        else if ((!SpectatorManager.isSpectator(p)) && (SpectatorManager
                                .isSpectator(player)))
                        {
                            p.hidePlayer(player);
                        }
                        else if ((!SpectatorManager.isSpectator(p)) && (!SpectatorManager
                                .isSpectator(player)))
                        {
                            p.showPlayer(player);
                        }
                        else if ((SpectatorManager.isSpectator(p)) && (SpectatorManager
                                .isSpectator(player)) && (SpectatorSettings.get(p)
                                .getOption(SpectatorSettings.Option.HIDEOTHER)))
                        {
                            p.showPlayer(player);
                        }
                        else if ((SpectatorManager.isSpectator(p)) && (SpectatorManager
                                .isSpectator(player)) && (!SpectatorSettings.get(p)
                                .getOption(SpectatorSettings.Option.HIDEOTHER)))
                        {
                            p.hidePlayer(player);
                        }
                    }
                }
            }
        }, 20L, 10L);
        if (!database.isFieldExists(AchievementManager.TABLENAME,
                new KeyValue(game.getName(), "VARCHAR")))
        {
            try
            {
                database.getDataBaseCore().executeUpdate(
                        "ALTER TABLE " + AchievementManager.TABLENAME + " ADD " + game
                                .getName() + " VARCHAR(999);");
            }
            catch (SQLException e)
            {
            }
        }
    }

    public IEventHandler getEventHandler()
    {
        return eventHandler;
    }

    public void setEventHandler(IEventHandler eventHandler)
    {
        if (this.eventHandler != null)
        {
            return;
        }
        this.eventHandler = eventHandler;
    }

    public GameTimer getGameTimer()
    {
        return gameTimer;
    }

    public CommandHandler getCommandHandler()
    {
        return commandHandler;
    }

    public List<Player> getIngamePlayers()
    {
        List<Player> players = new ArrayList<Player>();
        players.addAll(Bukkit.getOnlinePlayers());
        players.removeAll(SpectatorManager.getOnlineSpectators());
        return players;
    }

    private void registerListener()
    {
        new PlayerListener(this);
        new BlockListener(this);
        new EntityListener(this);
        new InventoryListener(this);
        GameMessageListener listener = new GameMessageListener();
        getServer().getMessenger()
                .registerIncomingPluginChannel(this, "BungeeCord", listener);
        getServer().getMessenger()
                .registerIncomingPluginChannel(this, "MinigameAPI", listener);
    }
}
