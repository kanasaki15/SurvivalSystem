package xyz.n7mn.dev.survivalsystem;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.n7mn.dev.survivalsystem.commands.DebugCommand;
import xyz.n7mn.dev.survivalsystem.commands.GraveCommand;
import xyz.n7mn.dev.survivalsystem.commands.ReloadCommand;
import xyz.n7mn.dev.survivalsystem.commands.VanishCommand;
import xyz.n7mn.dev.survivalsystem.customblockdata.CustomBlockData;
import xyz.n7mn.dev.survivalsystem.event.EventListener;
import xyz.n7mn.dev.survivalsystem.util.MessageManager;
import xyz.n7mn.dev.survivalsystem.util.PlayerDataUtil;

public final class SurvivalSystem extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        MessageManager.init();

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerDataUtil.putPlayerData(player);
        }

        getServer().getPluginManager().registerEvents(new EventListener(), this);
        CustomBlockData.registerListener(this);

        getCommand("vanish").setExecutor(new VanishCommand());
        getCommand("reloadConfig").setExecutor(new ReloadCommand());
        getCommand("grave").setExecutor(new GraveCommand());
        getCommand("debug").setExecutor(new DebugCommand());

        SurvivalInstance.INSTANCE.init();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        SurvivalInstance.INSTANCE.getTimer().stop();
    }
}
