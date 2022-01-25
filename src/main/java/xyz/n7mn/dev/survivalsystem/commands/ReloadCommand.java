package xyz.n7mn.dev.survivalsystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;
import xyz.n7mn.dev.survivalsystem.util.MessageManager;
import xyz.n7mn.dev.survivalsystem.util.MessageUtil;

public class ReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        MessageManager.reload();
        SurvivalInstance.INSTANCE.getPlugin().reloadConfig();

        sender.sendMessage(MessageUtil.replaceString("RELOAD-COMMAND"));

        return true;
    }
}