package xyz.n7mn.dev.survivalsystem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.n7mn.dev.survivalsystem.commands.DebugCommand;
import xyz.n7mn.dev.survivalsystem.commands.GraveCommand;
import xyz.n7mn.dev.survivalsystem.commands.ReloadCommand;
import xyz.n7mn.dev.survivalsystem.commands.VanishCommand;
import xyz.n7mn.dev.survivalsystem.event.EventListener;
import xyz.n7mn.dev.survivalsystem.gui.customcraft.craft.CraftGUI;
import xyz.n7mn.dev.survivalsystem.util.MessageManager;
import xyz.n7mn.dev.survivalsystem.gui.grave.GraveGUI;
import xyz.n7mn.dev.survivalsystem.util.PlayerDataUtil;

import java.security.SecureRandom;
import java.util.List;

public final class SurvivalSystem extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        MessageManager.init();

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                reloadConfig();
                List<String> list = getConfig().getStringList("TipsList");

                int i = new SecureRandom().nextInt(list.size()) - 1;
                if (i >= list.size() || i < 0) {
                    i = list.size() - 1;
                }

                for (Player player : getServer().getOnlinePlayers()) {
                    player.sendMessage(ChatColor.YELLOW + "[ななみ鯖 Tips] " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', list.get(i)));
                }
            }
        };

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerDataUtil.putPlayerData(player);
        }

        long i = 20 * 60;
        runnable.runTaskTimerAsynchronously(this, 0L, i);

        getServer().getPluginManager().registerEvents(new EventListener(), this);
        getServer().getPluginManager().registerEvents(new GraveGUI(), this);
        getServer().getPluginManager().registerEvents(new CraftGUI(), this);

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
