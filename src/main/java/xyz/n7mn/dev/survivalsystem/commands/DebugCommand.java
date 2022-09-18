package xyz.n7mn.dev.survivalsystem.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.survivalsystem.customenchant.CustomEnchantUtils;
import xyz.n7mn.dev.survivalsystem.infernal.InfernalManager;
import xyz.n7mn.dev.survivalsystem.infernal.InfernalUtils;
import xyz.n7mn.dev.survivalsystem.util.ItemStackUtil;

import java.util.List;

public class DebugCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player player) {
            ItemStack itemStack = new ItemStack(Material.DIAMOND_CHESTPLATE);

            player.sendMessage(Enchantment.DAMAGE_ALL.translationKey());
            player.sendMessage(CustomEnchantUtils.LIFE_STEAL.key().toString());
            player.sendMessage(CustomEnchantUtils.LIFE_STEAL.translationKey());

            player.sendMessage(Component.translatable(CustomEnchantUtils.LIFE_STEAL.translationKey()));
            player.getInventory().getItemInMainHand().getItemMeta().lore()
                            .forEach(player::sendMessage);
            player.sendMessage(player.getInventory().getItemInMainHand().getItemMeta().toString());

            if (Bukkit.getWorld("test") == null) {
                Bukkit.createWorld(new WorldCreator("test").biomeProvider(new BiomeProvider() {
                    @Override
                    public @NotNull Biome getBiome(@NotNull WorldInfo worldInfo, int x, int y, int z) {
                        return worldInfo.vanillaBiomeProvider().getBiome(worldInfo, x, y, z);
                    }

                    @Override
                    public @NotNull List<Biome> getBiomes(@NotNull WorldInfo worldInfo) {
                        return worldInfo.vanillaBiomeProvider().getBiomes(worldInfo);
                    }
                }).generator(new ChunkProvider()));
            }

            player.teleport(new Location(Bukkit.getWorld("test"), 0, 0, 0));
            //CustomEnchantUtils.addCustomEnchant(itemStack, CustomEnchantUtils.RESISTANCE, 5, true);

            ItemStack itemStack1 = new ItemStack(Material.ENCHANTED_BOOK);

            CustomEnchantUtils.addEnchant(itemStack1, CustomEnchantUtils.LIFE_STEAL, 5, true, true);

            ItemStackUtil.addItem(player, itemStack);
            ItemStackUtil.addItem(player, itemStack1);

            //player.sendMessage(String.valueOf(player.getInventory().getItemInMainHand().hasEnchant(CustomEnchantUtils.RESISTANCE)));

            @NotNull Entity skeleton = player.getWorld().spawnEntity(player.getLocation(), EntityType.SKELETON);

            InfernalUtils.addInfernal(skeleton, InfernalManager.Infernal.QUICK_SHOT, 5);

            player.getInventory().getItemInMainHand().getEnchantments().forEach(((enchantment, integer) ->
                    player.sendMessage(enchantment.toString() + integer.toString())
            ));

            player.teleport(Bukkit.getWorld(args[1]).getSpawnLocation());
        }


        return true;
    }
}