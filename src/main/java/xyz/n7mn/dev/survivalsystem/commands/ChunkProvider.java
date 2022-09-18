package xyz.n7mn.dev.survivalsystem.commands;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.survivalsystem.util.FastNoiseLite;

import java.util.Random;

public class ChunkProvider extends ChunkGenerator {
    private final FastNoiseLite terrainNoise = new FastNoiseLite();
    private final FastNoiseLite detailNoise = new FastNoiseLite();
    private final FastNoiseLite caveNoise = new FastNoiseLite();
    private final FastNoiseLite caveNoiseX = new FastNoiseLite();
    private final FastNoiseLite caveNoiseY = new FastNoiseLite();

    private final FastNoiseLite bedrockNoise = new FastNoiseLite();

    public ChunkProvider() {
        terrainNoise.SetFrequency(0.05f);
        detailNoise.SetFrequency(0.15F);

        caveNoiseY.SetFrequency(0.05f);
        caveNoiseX.SetFrequency(0.08f);
        caveNoise.SetFrequency(0.05f);

        bedrockNoise.SetFrequency(0.65F);
    }

    @Override
    public void generateNoise(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkGenerator.ChunkData chunkData) {
        for (int x = 0; x < 16; x++) {
            for (int y = worldInfo.getMinHeight(); y < Math.min(worldInfo.getMaxHeight(), 100); y++) {
                for (int z = 0; z < 16; z++) {
                    final float noise = caveNoise.GetNoise(caveNoiseX.GetNoise(x + chunkX * 16, z + chunkZ * 16) / 20, caveNoiseY.GetNoise(x + chunkX * 16,z + chunkZ * 16) / 10);

                    //allocatedY
                    final float allocatedY = (80 + noise * 150);
                    final float max = (float) (0.5 * Math.pow(Math.abs(y - allocatedY), 2) - 1);

                    // &&

                    if (allocatedY > y && caveNoiseY.GetNoise(x + chunkX * 16, y, z + chunkZ * 16) > Math.min(max, -0.2)) {
                        chunkData.setBlock(x, y, z, Material.STONE);
                    }
                }
            }
        }
    }

    @Override
    public void generateBedrock(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        for (int x = 0; x < 16; x++) {
            for (int y = worldInfo.getMinHeight(); y < Math.min(worldInfo.getMinHeight() + 3, worldInfo.getMaxHeight()); y++) {
                for (int z = 0; z < 16; z++) {
                    //random Bedrock placement....
                    if (y == worldInfo.getMinHeight() || bedrockNoise.GetNoise(x + chunkX * 16, y, z + chunkZ * 16) > -0.1F) {
                        chunkData.setBlock(x, y, z, Material.BEDROCK);
                    }
                }
            }
        }
    }

    @Override
    public void generateCaves(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {

    }

    @Override
    public boolean shouldGenerateCaves() {
        return false;
    }
}

