package xyz.n7mn.dev.survivalsystem.commands;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.survivalsystem.commands.populator.GrassPopulator;
import xyz.n7mn.dev.survivalsystem.util.FastNoiseLite;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChunkProvider extends ChunkGenerator {
    private final FastNoiseLite terrainNoise = new FastNoiseLite();
    private final FastNoiseLite detailNoise = new FastNoiseLite();
    private final FastNoiseLite caveNoise = new FastNoiseLite();
    private final FastNoiseLite caveNoiseY = new FastNoiseLite();

    private final FastNoiseLite bedrockNoise = new FastNoiseLite();

    public ChunkProvider() {
        caveNoiseY.SetFrequency(0.00945F);
        caveNoise.SetFrequency(0.005f);
        //caveNoise.SetFractalType(FastNoiseLite.FractalType.FBm);

        bedrockNoise.SetFrequency(0.65F);
    }

    @Override
    public void generateNoise(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkGenerator.ChunkData chunkData) {
        for (int x = 0; x < 16; x++) {
            for (int y = worldInfo.getMinHeight(); y < Math.min(worldInfo.getMaxHeight(), 160); y++) {
                for (int z = 0; z < 16; z++) {
                    boolean passed = false;

                    final float noise = caveNoise.GetNoise(x + chunkX * 16, y, z + chunkZ * 16);

                    final float noiseY = caveNoiseY.GetNoise(x + chunkX * 16,  z + chunkZ * 16);

                    //80 is lower of y // == maxY
                    final float currentY = 80 + (noiseY * 60);

                    final float surface = Math.abs(y - currentY);
                    //make cave? TODO: USE FUNCTION... (NOT STABLE)
                    final double function = noiseY * 2;

                    if (currentY > y && noise > function) {// && noise > function) {
                        if (1 > surface) {
                            chunkData.setBlock(x, y, z, Material.GRASS_BLOCK);
                        } else if (5.5 > surface) {
                            chunkData.setBlock(x, y, z, Material.DIRT);
                        } else if (Math.min(worldInfo.getMinHeight() + 3, worldInfo.getMaxHeight()) > y && (y == worldInfo.getMinHeight() || bedrockNoise.GetNoise(x + chunkX * 16, y + z + chunkZ * 16) > -0.1F)) {
                            chunkData.setBlock(x, y, z, Material.BEDROCK);
                        } else {
                            chunkData.setBlock(x, y, z, Material.STONE);
                        }

                        passed = true;
                    }

                    if (!passed && Math.min(worldInfo.getMinHeight() + 3, worldInfo.getMaxHeight()) > y && (y == worldInfo.getMinHeight() || bedrockNoise.GetNoise(x + chunkX * 16, y + z + chunkZ * 16) > -0.1F)) {
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
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return new ArrayList<>() {{
            add(new GrassPopulator());
        }};
    }
}

