package xyz.n7mn.dev.survivalsystem.itemchecker;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.n7mn.dev.survivalsystem.advancement.base.check.ItemCheck;
import xyz.n7mn.dev.survivalsystem.customcraft.item.ResistanceRing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InventoryItemChecker {

    private final List<ItemCheck> itemChecks = new ArrayList<>();

    public void init() {
        register(new ResistanceRing());
    }

    public void register(ItemCheck itemCheck) {
        itemChecks.add(itemCheck);
    }

    public void forEach(Player player, ItemStack itemStack) {
        if (itemStack != null) itemChecks.forEach(itemCheck -> itemCheck.item(player, itemStack));
    }


    public void tasks(Player player) {
        Arrays.stream(player.getInventory().getContents()).forEach(item -> forEach(player, item));
    }
}