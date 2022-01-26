package xyz.n7mn.dev.survivalsystem.customcraft;

import org.bukkit.Bukkit;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftAbstract;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftData;
import xyz.n7mn.dev.survivalsystem.customcraft.craft.CustomCraftTest1;
import xyz.n7mn.dev.survivalsystem.customcraft.craft.CustomCraftTest2;

import java.util.HashMap;

public class CustomCraft {

    private HashMap<String, CustomCraftAbstract> craftAbstractHashMap = new HashMap<>();

    public boolean init() {
        craftAbstractHashMap.put("test-1", new CustomCraftTest2());
        craftAbstractHashMap.put("test-2", new CustomCraftTest1());

        boolean active = craftAbstractHashMap.get("test-1").create().equals(craftAbstractHashMap.get("test-2").create());

        Bukkit.broadcastMessage(String.valueOf(active));

        return active;
    }
}