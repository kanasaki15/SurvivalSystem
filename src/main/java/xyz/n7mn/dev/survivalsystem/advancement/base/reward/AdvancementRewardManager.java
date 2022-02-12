package xyz.n7mn.dev.survivalsystem.advancement.base.reward;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class AdvancementRewardManager {
    private final Map<String, AdvancementReward> rewardList = new HashMap<>();

    public void register(String key, AdvancementReward creator) {
        rewardList.put(key, creator);
    }

    public void execute(Player player, String advancement) {
        AdvancementReward advancementReward = rewardList.get(advancement);
        if (advancementReward != null) {
            advancementReward.reward(player);
        }
    }
}
