package xyz.n7mn.dev.survivalsystem.advancement.data;

import net.roxeez.advancement.Advancement;
import net.roxeez.advancement.AdvancementCreator;
import net.roxeez.advancement.Context;
import net.roxeez.advancement.trigger.TriggerType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public class GlassBottleAdvancement implements AdvancementCreator {

    public static final String ID = "glass_bottle";

    @Override
    public @NotNull Advancement create(@NotNull Context context) {
        Advancement advancement = new Advancement(context.getPlugin(), ID);

        advancement.setParent(new NamespacedKey(context.getPlugin(), EnterServerAdvancement.ID));

        advancement.setDisplay(show -> {
            show.setTitle("ガラス瓶を入手する");
            show.setDescription("ガラス瓶を入手してみよう");
            show.setIcon(Material.GLASS_BOTTLE);
        });

        advancement.addCriteria("glass_bottle", TriggerType.INVENTORY_CHANGED, trigger -> trigger.hasItemMatching(item -> item.setType(Material.GLASS_BOTTLE)));

        return advancement;
    }
}
