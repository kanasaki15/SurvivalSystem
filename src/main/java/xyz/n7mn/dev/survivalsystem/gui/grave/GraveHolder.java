package xyz.n7mn.dev.survivalsystem.gui.grave;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.survivalsystem.data.GraveInventoryData;
import xyz.n7mn.dev.survivalsystem.gui.base.GUIData;
import xyz.n7mn.dev.survivalsystem.gui.base.GUIItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter @Setter
public class GraveHolder implements InventoryHolder {

    private List<GraveInventoryData> data;
    private int commandID;
    private final Map<Integer, GUIItem> hashMap = new HashMap<>();
    private GUIData guiData;

    private Player basePlayer;

    public GraveHolder(Player player, List<GraveInventoryData> data, int commandID) {
        this.data = data;
        this.commandID = commandID;
    }

    //空
    public GraveHolder(int commandID) {
        this.commandID = commandID;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }

    public void addListener(int chestID, GUIItem guiItem) {
        hashMap.put(chestID, guiItem);
    }

    public GUIData getTargetPlayer() {
        return guiData;
    }
}