package net.afyer.minigameapi.location;

import net.afyer.minigameapi.MinigameAPI;
import net.afyer.minigameapi.config.FileConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationManager
{
    private static LocationManager instance;
    private FileConfig config;

    public LocationManager()
    {
        config = new FileConfig(MinigameAPI.getInstance(), "locations.yml");
    }

    public static LocationManager getInstance()
    {
        if (instance == null)
        {
            instance = new LocationManager();
        }
        return instance;
    }

    public void setBlockLocation(Location location, String path)
    {
        String world = location.getWorld().getName();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        config.set(path, world + "," + x + "," + y + "," + z);
        config.save();
    }

    public void setLocation(Location location, String path)
    {
        String world = location.getWorld().getName();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        double yaw = location.getYaw();
        double pitch = location.getPitch();
        config.set(path, world + "," + x + "," + y + "," + z + "," + yaw + "," + pitch);
        config.save();
    }

    public Location getBlockLocation(String path)
    {
        String locationString = config.getString(path);
        World world = Bukkit.getWorld(locationString.split(",")[0]);
        double x = Double.valueOf(locationString.split(",")[1]);
        double y = Double.valueOf(locationString.split(",")[2]);
        double z = Double.valueOf(locationString.split(",")[3]);
        return new Location(world, x, y, z);
    }

    public Location getLocation(String path)
    {
        if (config.getString(path) != null)
        {
            String locationString = config.getString(path);
            World world = Bukkit.getWorld(locationString.split(",")[0]);
            double x = Double.valueOf(locationString.split(",")[1]);
            double y = Double.valueOf(locationString.split(",")[2]);
            double z = Double.valueOf(locationString.split(",")[3]);
            double yaw = Double.valueOf(locationString.split(",")[4]);
            double pitch = Double.valueOf(locationString.split(",")[5]);
            return new Location(world, x, y, z, (float) yaw, (float) pitch);
        }
        return null;
    }

    public boolean isInsideLocation(Location location, String path)
    {
        Location loc1 = getBlockLocation(path + ".loc1");
        Location loc2 = getBlockLocation(path + ".loc2");

        if (loc1.getWorld() != loc2.getWorld())
        {
            return false;
        }

        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        if (location.getBlockX() >= minX && location.getBlockX() <= maxX
                && location.getBlockY() >= minY && location.getBlockY() <= maxY
                && location.getBlockZ() >= minZ && location.getBlockZ() <= maxZ)
        {
            return true;
        }
        return false;
    }
}
