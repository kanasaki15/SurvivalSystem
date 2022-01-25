package xyz.n7mn.dev.survivalsystem.cache;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;
import xyz.n7mn.dev.survivalsystem.data.GraveInventoryData;
import xyz.n7mn.dev.survivalsystem.util.MessageUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@UtilityClass
public class GraveCache {
    public static Map<UUID, GraveInventoryData> graveCache = new HashMap<>();

    public void handle() {
        Bukkit.getScheduler().runTask(SurvivalInstance.INSTANCE.getPlugin(), () -> {
            for (GraveInventoryData data : graveCache.values()) {
                Entity entity = data.getWorld().getEntity(data.getArmorStandUUID());
                if (entity != null) {
                    final int time = data.getTime();

                    data.setTime(time - 1);

                    if (time > 0) {
                        entity.setCustomName(MessageUtil.replaceString("GRAVE-NAME", "%name%|" + data.getPlayerName(), "%time%|" + time));
                    } else {
                        data.translateSerializable().forEach(itemStack -> ((Item) entity.getWorld().spawnEntity(entity.getLocation(), EntityType.DROPPED_ITEM)).setItemStack(itemStack));
                        data.remove();

                        entity.remove();
                    }
                }
            }
        });
    }

    public void refundItem(Player player, GraveInventoryData data) {
        data.translateSerializable().forEach(itemStack -> {
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(itemStack);
            } else {
                MessageUtil.sendChat(player, "GRAVE-DROP-ITEM", "%item%|" + WordUtils.capitalizeFully(itemStack.getType().name().toLowerCase().replaceAll("_", " ")));
                ((Item) player.getWorld().spawnEntity(player.getLocation(), EntityType.DROPPED_ITEM)).setItemStack(itemStack);
            }
        });
    }

    public void init() {
        SurvivalInstance.INSTANCE.getConnection().getGraveTable().get(result -> graveCache.putAll(result));
    }
}