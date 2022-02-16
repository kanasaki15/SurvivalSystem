package xyz.n7mn.dev.survivalsystem.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.survivalsystem.customenchant.CustomEnchantUtils;
import xyz.n7mn.dev.survivalsystem.util.ItemStackUtil;

public class DebugCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            ItemStack itemStack = new ItemStack(Material.DIAMOND_CHESTPLATE);

            CustomEnchantUtils.addCustomEnchant(itemStack, CustomEnchantUtils.RESISTANCE, 5, true);

            ItemStack itemStack1 = new ItemStack(Material.ENCHANTED_BOOK);

            CustomEnchantUtils.addCustomEnchant(itemStack1, CustomEnchantUtils.RESISTANCE, 5, true, true);

            ItemStackUtil.addItem(player, itemStack);
            ItemStackUtil.addItem(player, itemStack1);

            player.sendMessage(String.valueOf(player.getInventory().getItemInMainHand().hasEnchant(CustomEnchantUtils.RESISTANCE)));

            player.getInventory().getItemInMainHand().getEnchantments().forEach(((enchantment, integer) ->
                    player.sendMessage(enchantment.toString() + integer.toString())
            ));
        }

        return true;
    }
}
