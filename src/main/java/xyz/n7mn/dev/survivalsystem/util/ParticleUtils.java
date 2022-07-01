package xyz.n7mn.dev.survivalsystem.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ParticleUtils {

    public void summonOutlineParticle(Player player, World world, BoundingBox box, Particle type) {
        Bukkit.getScheduler().runTaskAsynchronously(SurvivalInstance.INSTANCE.getPlugin(), () -> {
            //normal block bounding box
            final double distX = (box.getMaxX() - box.getMinX()) / 4;
            final double distY = (box.getMaxY() - box.getMinY()) / 4;
            final double distZ = (box.getMaxZ() - box.getMinZ()) / 4;

            for (double x = box.getMinX(); x <= box.getMaxX(); x += distX) {
                for (double y = box.getMinY(); y <= box.getMaxY(); y += distY) {
                    for (double z = box.getMinZ(); z <= box.getMaxZ(); z += distZ) {
                        final Location location = new Location(world, x, y, z);

                        if (contains(location, box) > 1) summon(location, type, player);
                    }
                }
            }
        });
    }

    public void summonOutlineParticle(Player player, World world, Location location1, Location location2, Particle type) {
        summonOutlineParticle(player, world, new BoundingBox(location1.getX(), location1.getY(), location1.getZ(), location2.getX(), location2.getY(), location2.getZ()), type);
    }

    public List<Location> getSphereParticleList(Location location, final double size) {
        final List<Location> locations = new ArrayList<>();

        final double value = Math.PI / 10 / size;

        for (double pi = 0; pi <= Math.PI; pi += value) {
            final double sin = Math.sin(pi);
            final double y = size * Math.cos(pi);

            for (double theta = 0; theta < Math.PI * 2; theta += value) {
                final double x = size * Math.cos(theta) * sin;
                final double z = size * Math.sin(theta) * sin;
                location.add(x, y, z);

                locations.add(location.clone());

                location.subtract(x, y, z);
            }
        }
        return locations;
    }

    public void summonSphereParticle(Player player, Location location, final double size, Particle type) {
        Bukkit.getScheduler().runTaskAsynchronously(SurvivalInstance.INSTANCE.getPlugin(), () -> {
            List<Location> particleList = getSphereParticleList(location, size);

            particleList.forEach(particle -> summon(particle, type, player));
        });
    }

    public List<Location> getTowerParticleList(Location location, final double size, final double y) {
        final List<Location> locations = new ArrayList<>();

        final double value = Math.PI / 100 / size;

        for (double pi = 0; pi <= Math.PI * 2; pi += value) {
            final double x = size * Math.cos(pi);
            final double z = size * Math.sin(pi);

            location.add(x, y, z);

            locations.add(location.clone());

            location.subtract(x, 0, z);
        }

        return locations;
    }


    public void summonTowerParticle(Player player, Location location, final double repeat, final double size, final double y) {
        List<Location> locations = getTowerParticleList(location, size, y);

        final double toY = Math.abs(locations.get(locations.size() - 1).getY() - locations.get(0).getY());

        //limits
        for (int i = 0; i < repeat; i++) {

            final double additionalY = i * toY;

            for (Location loc : locations) {
                ParticleUtils.summon(loc.clone().add(0, additionalY, 0), Particle.FLAME, player);
            }
        }
    }

    public int contains(Location location, BoundingBox box) {
        int total = 0;

        if (location.getX() == box.getMinX() || location.getX() == box.getMaxX()) total++;
        if (location.getY() == box.getMinY() || location.getY() == box.getMaxY()) total++;
        if (location.getZ() == box.getMinZ() || location.getZ() == box.getMaxZ()) total++;

        return total;
    }

    public void summon(Location location, Particle type, Player player) {
        location.getWorld().spawnParticle(type, player == null ? null : List.of(player), player, location.getX(), location.getY(), location.getZ(), 1, 0, 0, 0, 0, null);
        //location.getWorld().spawnParticle(type, Arrays.asList(player), player, location.getX(), location.getY(), location.getZ(), 1, 0, 0, 0,0);
    }
}
