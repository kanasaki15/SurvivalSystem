package xyz.n7mn.dev.survivalsystem;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Getter
public enum SurvivalInstance {

    INSTANCE;

    @NotNull
    private final Plugin plugin = Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("SurvivalSystem"));
}