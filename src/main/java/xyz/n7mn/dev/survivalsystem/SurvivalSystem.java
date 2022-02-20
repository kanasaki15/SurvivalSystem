package xyz.n7mn.dev.survivalsystem;

import de.freesoccerhdx.advancedworldcreatorapi.AdvancedWorldCreator;
import de.freesoccerhdx.advancedworldcreatorapi.EnvironmentBuilder;
import de.freesoccerhdx.advancedworldcreatorapi.GeneratorConfiguration;
import de.freesoccerhdx.advancedworldcreatorapi.biome.*;
import de.freesoccerhdx.advancedworldcreatorapi.biomeprovider.BiomeProviderMultiNoise;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.n7mn.dev.survivalsystem.commands.DebugCommand;
import xyz.n7mn.dev.survivalsystem.commands.GraveCommand;
import xyz.n7mn.dev.survivalsystem.commands.ReloadCommand;
import xyz.n7mn.dev.survivalsystem.commands.VanishCommand;
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

        getCommand("vanish").setExecutor(new VanishCommand());
        getCommand("reloadConfig").setExecutor(new ReloadCommand());
        getCommand("grave").setExecutor(new GraveCommand());
        getCommand("debug").setExecutor(new DebugCommand());

        //de.freesoccerhdx.advancedworldcreatorapi.AdvancedWorldCreatorAPI;


        BiomeCreator biomeCreator = new BiomeCreator(this.getName().toLowerCase(),"under_world");

        biomeCreator.setWaterColor(new java.awt.Color(76, 255, 255));
        biomeCreator.setGrassColor(new java.awt.Color(100,200,160));
        biomeCreator.setFoliageColor(new java.awt.Color(252, 168, 215));
        biomeCreator.setSkyColor(new java.awt.Color(30, 150, 170));
        biomeCreator.setWaterFogColor(new java.awt.Color(255, 255, 255));
        biomeCreator.addBiomeCarver(BiomeFeatureType.AIR, BiomeCarvers.CAVE);

        biomeCreator.addBiomeFeature(BiomeDecorationType.FLUID_SPRINGS, BiomeDecoration.SPRING_DELTA);

        biomeCreator.addBiomeFeature(BiomeDecorationType.TOP_LAYER_MODIFICATION, BiomeDecoration.FLOWER_FOREST_FLOWERS);

        BiomeProviderMultiNoise.Builder builder = new BiomeProviderMultiNoise.Builder();

        builder.addBiome(biomeCreator.createBiome(true), new BiomeProviderMultiNoise.NoiseData(1f,1f,1f,1,1f,1f,1f));

        AdvancedBiomeProvider biomeprovider = builder.create();

        AdvancedWorldCreator creator = new AdvancedWorldCreator("under_world");
        creator.seed(123456789);

        GeneratorConfiguration.NoiseGeneration noiseGeneration = new GeneratorConfiguration.NoiseGeneration();

        GeneratorConfiguration generatorConfig = new GeneratorConfiguration();
        generatorConfig.getNoiseGeneration().setMinY(-128);
        generatorConfig.getNoiseGeneration().setHeight(256+128);

        biomeCreator.addBiomeFeature(BiomeDecorationType.RAW_GENERATION, BiomeDecoration.ORE_COPPER_LARGE);

        //generatorConfig.setOreVeinsEnabled(true);
        generatorConfig.getNoiseGeneration().setIslandNoiseOverride(true);

        noiseGeneration.setTopSlideSettings(new GeneratorConfiguration.SliderGeneration(1,1,1));
        noiseGeneration.setBottomSlideSettings(new GeneratorConfiguration.SliderGeneration(1,1,1));

        generatorConfig.setNoiseGeneration(noiseGeneration);

        creator.setGeneratorConfiguration(generatorConfig);


        creator.setEnvironmentBuilder(new EnvironmentBuilder());
        creator.setAdvancedBiomeProvider(biomeprovider);
        Bukkit.getScheduler().runTask(this, () -> creator.createWorld());

        SurvivalInstance.INSTANCE.init();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        SurvivalInstance.INSTANCE.getTimer().stop();
    }
}
