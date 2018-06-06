package net.afyer.minigameapi.spectator;

import net.afyer.minigameapi.MinigameAPI;
import net.afyer.minigameapi.database.DataBase;
import net.afyer.minigameapi.database.KeyValue;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;

public class SpectatorSettings
{
    public static final String TABLENAME = "spectatorsettings";
    public static final KeyValue KV = new KeyValue("uuid", "VARCHAR(36) PRIMARY KEY")
            .add("speed", "INTEGER").add("speed", "INTEGER").add("autoTp", "INTEGER")
            .add("nightVision", "INTEGER").add("firstPerson", "INTEGER")
            .add("hideOther", "INTEGER").add("fly", "INTEGER");
    private static final Map<OfflinePlayer, SpectatorSettings> CACHES = new HashMap<OfflinePlayer, SpectatorSettings>();
    private static DataBase database = MinigameAPI.getInstance().getDataBase();
    private final OfflinePlayer player;
    private int speed;
    private boolean autoTp;
    private boolean nightVision;
    private boolean firstPerson;
    private boolean hideOther;
    private boolean fly;

    public SpectatorSettings(OfflinePlayer player)
    {
        this.player = player;
        if (!database.isValueExists(TABLENAME, KV,
                new KeyValue("uuid", player.getUniqueId().toString())))
        {
            database.dbInsert(TABLENAME,
                    new KeyValue("uuid", player.getUniqueId().toString()).add("speed", 0)
                            .add("autoTp", 0).add("nightVision", 0).add("firstPerson", 1)
                            .add("hideOther", 0).add("fly", 0));
        }
        load();
    }

    public static SpectatorSettings get(OfflinePlayer player)
    {
        SpectatorSettings s = null;
        if (CACHES.containsKey(player))
        {
            s = CACHES.get(player);
        }
        else
        {
            s = new SpectatorSettings(player);
            CACHES.put(player, s);
        }
        return CACHES.get(player);
    }

    public void load()
    {
        for (KeyValue kv : database.dbSelect(TABLENAME, KV,
                new KeyValue("uuid", player.getUniqueId().toString())))
        {
            speed = Integer.parseInt(kv.getString("speed"));
            autoTp = Integer.parseInt(kv.getString("autoTp")) == 1;
            nightVision = Integer.parseInt(kv.getString("nightVision")) == 1;
            firstPerson = Integer.parseInt(kv.getString("firstPerson")) == 1;
            hideOther = Integer.parseInt(kv.getString("hideOther")) == 1;
            fly = Integer.parseInt(kv.getString("fly")) == 1;
        }
    }

    public int getSpeed()
    {
        return speed;
    }

    public void setSpeed(int key)
    {
        if ((key < 0) || (key > 4))
        {
            return;
        }
        speed = key;
        database.dbUpdate(TABLENAME, new KeyValue("speed", key),
                new KeyValue("uuid", player.getUniqueId().toString()));
    }

    public boolean getOption(Option o)
    {
        switch (o)
        {
            case AUTOTP:
                return autoTp;
            case NIGHTVISION:
                return nightVision;
            case FIRSTPERSON:
                return firstPerson;
            case HIDEOTHER:
                return hideOther;
            case FLY:
                return fly;
        }
        return false;
    }

    public void setOption(Option o, boolean key)
    {
        if (getOption(o) && key)
        {
            return;
        }
        switch (o)
        {
            case AUTOTP:
                autoTp = key;
                database.dbUpdate(TABLENAME, new KeyValue("autoTp", key ? 1 : 0),
                        new KeyValue("uuid", player.getUniqueId().toString()));
                break;
            case NIGHTVISION:
                nightVision = key;
                database.dbUpdate(TABLENAME, new KeyValue("nightVision", key ? 1 : 0),
                        new KeyValue("uuid", player.getUniqueId().toString()));
                break;
            case FIRSTPERSON:
                firstPerson = key;
                database.dbUpdate(TABLENAME, new KeyValue("firstPerson", key ? 1 : 0),
                        new KeyValue("uuid", player.getUniqueId().toString()));
                break;
            case HIDEOTHER:
                hideOther = key;
                database.dbUpdate(TABLENAME, new KeyValue("hideOther", key ? 1 : 0),
                        new KeyValue("uuid", player.getUniqueId().toString()));
                break;
            case FLY:
                fly = key;
                database.dbUpdate(TABLENAME, new KeyValue("fly", key ? 1 : 0),
                        new KeyValue("uuid", player.getUniqueId().toString()));
                break;
        }
    }

    public OfflinePlayer getPlayer()
    {
        return player;
    }

    public enum Option
    {
        AUTOTP, NIGHTVISION, FIRSTPERSON, HIDEOTHER, FLY;
    }
}
