package xyz.n7mn.dev.survivalsystem.event;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.SecureRandom;

public class DungeonEvent {

    //@EventHandler
    public void onA(ChunkPopulateEvent e) {


        double next = new SecureRandom().nextDouble(100);

        if (next < 0.05) {

            @NotNull Block block = e.getChunk().getBlock(0, e.getChunk().getChunkSnapshot().getHighestBlockYAt(0, 0), 0);

            if (block.getType() != Material.WATER
                    && !block.getBiome().toString().endsWith("RIVER")
                    && !block.getBiome().toString().endsWith("OCEAN")
                    && !block.getBiome().toString().endsWith("SWAMP")
                    && block.getY() > 65) {

                @NotNull Location location = block.getLocation();

                BukkitWorld world = new BukkitWorld(e.getWorld());

                try {
                    File file = Paths.get(SurvivalInstance.INSTANCE.getPlugin().getDataFolder().getPath(), "dungeons", "mine-1-entrance.schem").toFile();

                    FaweAPI.load(file)
                            .paste(world, BlockVector3.at(location.getX(), location.getY(), location.getZ()));

                    e.getChunk().getPersistentDataContainer().set(new NamespacedKey(SurvivalInstance.INSTANCE.getPlugin(), "dungeons"), PersistentDataType.STRING, "mineDungeons");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
