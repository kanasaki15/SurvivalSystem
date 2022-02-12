package xyz.n7mn.dev.survivalsystem.advancement;

import lombok.Getter;
import net.roxeez.advancement.AdvancementManager;
import org.bukkit.Bukkit;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;
import xyz.n7mn.dev.survivalsystem.SurvivalSystem;
import xyz.n7mn.dev.survivalsystem.advancement.base.reward.AdvancementRewardManager;
import xyz.n7mn.dev.survivalsystem.advancement.data.*;
import xyz.n7mn.dev.survivalsystem.itemchecker.ItemChecker;

import static org.bukkit.Bukkit.getServer;

@Getter
public class Advancement {

    private AdvancementManager manager;
    private final AdvancementRewardManager rewardManager = new AdvancementRewardManager();
    private final ItemChecker itemChecker = new ItemChecker();

    public void init() {
        manager = new AdvancementManager(SurvivalInstance.INSTANCE.getPlugin());

        //register advancement
        manager.register(new EnterServerAdvancement());

        manager.register(new GlassBottleAdvancement());
        manager.register(new GreatHoneyAdvancement());

        manager.register(new CustomCraftOpenAdvancement());
        manager.register(new CustomCraftCreateAdvancement());

        rewardManager.register(EnterServerAdvancement.ID, new EnterServerAdvancement());
        rewardManager.register(CustomCraftCreateAdvancement.ID, new CustomCraftCreateAdvancement());

        itemChecker.init();

        itemChecker.register(new GlassBottleAdvancement());

        //サーバーが起動してからじゃないとヌルポ発生する
        Bukkit.getScheduler().runTask(SurvivalInstance.INSTANCE.getPlugin(), () -> manager.createAll(true));
    }
}