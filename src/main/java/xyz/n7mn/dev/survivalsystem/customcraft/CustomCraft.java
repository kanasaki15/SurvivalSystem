package xyz.n7mn.dev.survivalsystem.customcraft;

import lombok.Getter;
import org.bukkit.Bukkit;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftAbstract;
import xyz.n7mn.dev.survivalsystem.customcraft.craft.CustomCraftTest1;
import xyz.n7mn.dev.survivalsystem.customcraft.craft.CustomCraftTest2;

import java.util.HashMap;

@Getter
public class CustomCraft {

    private final HashMap<String, CustomCraftAbstract> craftAbstractHashMap = new HashMap<>();

    public void init() {
        craftAbstractHashMap.put("test-1", new CustomCraftTest1());
    }
}