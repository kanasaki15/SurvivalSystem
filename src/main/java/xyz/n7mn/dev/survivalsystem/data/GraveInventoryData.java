package xyz.n7mn.dev.survivalsystem.data;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Getter
public class GraveInventoryData {
    private Timestamp timestamp;
    private final World world;
    private final String playerName;
    private final UUID UUID;
    private final Location location;
    private final List<ItemStack> itemStackList;
    private final UUID armorStandUUID;

    public GraveInventoryData(String world, String playerName, UUID uuid, Location location, List<ItemStack> itemStackList, UUID armorStandUUID) {
        this.world = Bukkit.getWorld(world);
        this.playerName = playerName;
        this.UUID = uuid;
        this.location = location;
        this.itemStackList = itemStackList;
        this.armorStandUUID = armorStandUUID;
    }

    public GraveInventoryData(Timestamp timestamp, String world, String playerName, UUID uuid, Location location, List<ItemStack> itemStackList, UUID armorStandUUID) {
        this.timestamp = timestamp;
        this.world = Bukkit.getWorld(world);
        this.playerName = playerName;
        this.UUID = uuid;
        this.location = location;
        this.itemStackList = itemStackList;
        this.armorStandUUID = armorStandUUID;
    }
}
