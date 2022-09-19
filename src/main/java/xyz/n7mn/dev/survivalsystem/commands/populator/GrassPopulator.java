package xyz.n7mn.dev.survivalsystem.commands.populator;

import org.bukkit.Material;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class GrassPopulator extends BlockPopulator {
    @Override
    public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {
        for (int iteration = 0; iteration < 10; iteration++) {
            int x = random.nextInt(16) + chunkX * 16;
            int z = random.nextInt(16) + chunkZ * 16;
            int y = 319;
            while (limitedRegion.getType(x, y, z).isAir() && y > -64) y--;

            //TODO: don't use BlockPopulator..
            if (limitedRegion.getType(x, y, z) == Material.GRASS_BLOCK) {
                limitedRegion.setType(x, y + 1, z, Material.GRASS);
            }
        }
    }
}