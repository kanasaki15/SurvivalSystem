package xyz.n7mn.dev.survivalsystem.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.util.List;

@UtilityClass
public class ParticleUtils {

    public void summonOutlineParticle(Player player, World world, BoundingBox box, Particle type) {
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
    }

    public void summonOutlineParticle(Player player, World world, Location location1, Location location2, Particle type) {
        summonOutlineParticle(player, world, new BoundingBox(location1.getX(), location1.getY(), location1.getZ(), location2.getX(), location2.getY(), location2.getZ()), type);
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
