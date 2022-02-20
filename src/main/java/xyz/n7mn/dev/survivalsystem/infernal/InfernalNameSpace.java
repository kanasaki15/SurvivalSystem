package xyz.n7mn.dev.survivalsystem.infernal;

import org.bukkit.NamespacedKey;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;

public class InfernalNameSpace {

    public static void init() {
        quickShotNameSpace = new NamespacedKey(SurvivalInstance.INSTANCE.getPlugin(), "infernal_quick_shot");
    }

    public static NamespacedKey quickShotNameSpace;
}
