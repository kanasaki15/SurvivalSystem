package xyz.n7mn.dev.survivalsystem.advancement.data;

import net.roxeez.advancement.Advancement;
import net.roxeez.advancement.AdvancementCreator;
import net.roxeez.advancement.Context;
import net.roxeez.advancement.trigger.TriggerType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.survivalsystem.util.MessageUtil;

public class GreatHoneyAdvancement implements AdvancementCreator {

    public static final String ID = "great_honey";

    @Override
    public @NotNull Advancement create(@NotNull Context context) {
        Advancement advancement = new Advancement(context.getPlugin(), ID);

        advancement.setParent(new NamespacedKey(context.getPlugin(), GlassBottleAdvancement.ID));

        advancement.setDisplay(show -> {
            show.setTitle("上質なはちみつを飲む");
            show.setDescription(MessageUtil.replaceMessage("はちみつを手に入れるときに%chance%%の確率で手に入るらしい", "%chance%|"  + MessageUtil.replaceFromConfig("HONEY-RARE-ITEM-CHANCE")));
            show.setIcon(Material.HONEY_BOTTLE);
        });

        advancement.addCriteria("grant", TriggerType.IMPOSSIBLE, trigger -> {
            //何もなし
        });

        return advancement;
    }
}
