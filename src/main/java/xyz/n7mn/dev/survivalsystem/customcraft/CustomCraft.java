package xyz.n7mn.dev.survivalsystem.customcraft;

import lombok.Getter;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftAbstract;
import xyz.n7mn.dev.survivalsystem.customcraft.craft.CustomCraftTest1;

import java.util.HashMap;

@Getter
public class CustomCraft {

    private final HashMap<String, CustomCraftAbstract> craftAbstractHashMap = new HashMap<>();

    public void init() {
        craftAbstractHashMap.put("test-1", new CustomCraftTest1());
    }
}