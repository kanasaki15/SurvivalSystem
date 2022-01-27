package xyz.n7mn.dev.survivalsystem.advancement;

import lombok.Getter;
import net.roxeez.advancement.AdvancementManager;
import org.bukkit.Bukkit;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;
import xyz.n7mn.dev.survivalsystem.advancement.base.AdvancementRewardManager;
import xyz.n7mn.dev.survivalsystem.advancement.data.CustomCraftAdvancement;
import xyz.n7mn.dev.survivalsystem.advancement.data.EnterServerAdvancement;
import xyz.n7mn.dev.survivalsystem.advancement.data.GlassBottleAdvancement;
import xyz.n7mn.dev.survivalsystem.advancement.data.GreatHoneyAdvancement;

@Getter
public class Advancement {

    private AdvancementManager manager;
    private final AdvancementRewardManager rewardManager = new AdvancementRewardManager();

    public void init() {
        manager = new AdvancementManager(SurvivalInstance.INSTANCE.getPlugin());

        //register advancement
        manager.register(new EnterServerAdvancement());

        manager.register(new GlassBottleAdvancement());
        manager.register(new GreatHoneyAdvancement());

        manager.register(new CustomCraftAdvancement());

        rewardManager.register(EnterServerAdvancement.ID, new EnterServerAdvancement());

        //サーバーが起動してからじゃないとヌルポ発生する
        Bukkit.getScheduler().runTask(SurvivalInstance.INSTANCE.getPlugin(), () -> manager.createAll(true));
    }
}
