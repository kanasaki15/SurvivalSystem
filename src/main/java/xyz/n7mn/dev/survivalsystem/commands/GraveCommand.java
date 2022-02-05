package xyz.n7mn.dev.survivalsystem.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.n7mn.dev.survivalsystem.gui.grave.GraveGUI;
import xyz.n7mn.dev.survivalsystem.gui.base.GUIData;
import xyz.n7mn.dev.survivalsystem.util.MessageManager;

public class GraveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                Inventory inventory = new GraveGUI().createPage(new GUIData(player.getUniqueId(), player.getName()), 1);

                player.openInventory(inventory);
            } else {
                @Nullable OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[0]);

                if (target != null) {
                    Inventory inventory = new GraveGUI().createPage(new GUIData(target.getUniqueId(), target.getName()), 1);

                    player.openInventory(inventory);
                } else {
                    player.sendMessage("エラーが発生しました！");
                }
            }
        } else {
            sender.sendMessage(MessageManager.getString("CANNOT-EXECUTE-FROM-CONSOLE"));
        }
        return true;
    }
}