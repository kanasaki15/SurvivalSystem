package xyz.n7mn.dev.survivalsystem.advancement.data;

import net.roxeez.advancement.Advancement;
import net.roxeez.advancement.AdvancementCreator;
import net.roxeez.advancement.Context;
import net.roxeez.advancement.trigger.TriggerType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public class CustomCraftAdvancement implements AdvancementCreator {
    public static final String ID = "custom_craft";

    @Override
    public @NotNull Advancement create(@NotNull Context context) {
        Advancement advancement = new Advancement(context.getPlugin(), ID);

        advancement.setParent(new NamespacedKey(context.getPlugin(), EnterServerAdvancement.ID));

        advancement.setDisplay(show -> {
            show.setTitle("カスタム作業台を開く");
            show.setDescription("作業台にシフトで開く");
            show.setIcon(Material.CRAFTING_TABLE);
        });

        advancement.addCriteria("grant", TriggerType.IMPOSSIBLE, trigger -> {
            //何もなし
        });

        return advancement;
    }
}
