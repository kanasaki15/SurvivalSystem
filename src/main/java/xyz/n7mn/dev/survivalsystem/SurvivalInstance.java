package xyz.n7mn.dev.survivalsystem;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.survivalsystem.advancement.Advancement;
import xyz.n7mn.dev.survivalsystem.cache.GraveCache;
import xyz.n7mn.dev.survivalsystem.customcraft.CustomCraft;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemDataUtils;
import xyz.n7mn.dev.survivalsystem.customenchant.CustomEnchant;
import xyz.n7mn.dev.survivalsystem.gui.GUIManager;
import xyz.n7mn.dev.survivalsystem.itemchecker.InventoryItemChecker;
import xyz.n7mn.dev.survivalsystem.sql.SQLConnection;
import xyz.n7mn.dev.survivalsystem.timer.Timer;

import java.util.Objects;


@Getter
public enum SurvivalInstance {

    INSTANCE;

    @NotNull
    private final Plugin plugin = Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("SurvivalSystem"));

    private final Timer timer = new Timer();

    private final SQLConnection connection = new SQLConnection();

    private final Advancement advancement = new Advancement();

    private final CustomCraft customCraft = new CustomCraft();

    private final CustomEnchant customEnchant = new CustomEnchant();

    private final GUIManager guiManager = new GUIManager();

    private final InventoryItemChecker itemChecker = new InventoryItemChecker();

    public void init() {
        itemChecker.init();

        timer.start();

        connection.setUseSQL(plugin.getConfig().getBoolean("useSQL"));
        connection.init();

        GraveCache.init();
        ItemDataUtils.init();

        advancement.init();

        customCraft.init();
        customEnchant.init();

        guiManager.init();

        SurvivalInstance.INSTANCE.getPlugin().saveResource("dungeons/mine-entrance-1.schem", false);
        SurvivalInstance.INSTANCE.getPlugin().saveResource("dungeons/mine-road-first-1-1", false);
        SurvivalInstance.INSTANCE.getPlugin().saveResource("dungeons/mine-road-first-1-2", false);
        SurvivalInstance.INSTANCE.getPlugin().saveResource("dungeons/mine-road-first-2-1", false);
        SurvivalInstance.INSTANCE.getPlugin().saveResource("dungeons/mine-road-first-2-2", false);
        SurvivalInstance.INSTANCE.getPlugin().saveResource("dungeons/mine-road-first-3-1", false);
        SurvivalInstance.INSTANCE.getPlugin().saveResource("dungeons/mine-road-first-3-2", false);
    }
}