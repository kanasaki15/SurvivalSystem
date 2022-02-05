package xyz.n7mn.dev.survivalsystem.data;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;
import xyz.n7mn.dev.survivalsystem.cache.GraveCache;
import xyz.n7mn.dev.survivalsystem.cache.serializable.ItemStackSerializable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class GraveInventoryData {
    private final Timestamp timestamp;
    private final World world;
    private final String playerName;
    private final UUID UUID;
    private final ItemStackSerializable itemStack;
    private final UUID armorStandUUID;

    private final List<ItemStack> itemStackList = new ArrayList<>();

    private final boolean active;

    public GraveInventoryData(Timestamp timestamp, String world, String playerName, UUID uuid, ItemStackSerializable itemStackSerializable, UUID armorStandUUID) {
        this.timestamp = timestamp;
        this.world = Bukkit.getWorld(world);
        this.playerName = playerName;
        this.UUID = uuid;
        this.itemStack = itemStackSerializable;
        this.armorStandUUID = armorStandUUID;
        this.active = true;
    }

    public GraveInventoryData(Timestamp timestamp, String world, String playerName, UUID uuid, ItemStackSerializable itemStackSerializable, UUID armorStandUUID, boolean active) {
        this.timestamp = timestamp;
        this.world = Bukkit.getWorld(world);
        this.playerName = playerName;
        this.UUID = uuid;
        this.itemStack = itemStackSerializable;
        this.armorStandUUID = armorStandUUID;
        this.active = active;
    }


    //SerializableをItemStackに変換する
    public List<ItemStack> translateSerializable() {
        if (itemStackList.isEmpty() && !itemStack.getSerializable().isEmpty()) {
            itemStack.getSerializable().forEach(serializable -> itemStackList.add(ItemStack.deserialize(serializable)));
        }
        return itemStackList;
    }

    public void remove(final boolean active) {
        SurvivalInstance.INSTANCE.getConnection().getGraveTable().updateActive(this, active);

        GraveCache.graveCache.keySet().removeIf(uuid -> uuid.equals(armorStandUUID));
    }

    public void remove() {
        remove(true);
    }
}