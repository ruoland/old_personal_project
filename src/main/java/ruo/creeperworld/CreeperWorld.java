package ruo.creeperworld;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import ruo.asdfwild.EntityFlyingCreeper;
import ruo.asdfwild.EntityMissileCreeper;

import java.util.Random;

@Mod(modid = "CreeperWorld", name = "Creeper World", version = "1.0")
public class CreeperWorld {
    private static Random rand = new Random();
    public static Configuration config;

    @SidedProxy(serverSide = "ruo.creeperworld.CommonProxy", clientSide = "ruo.creeperworld.ClientProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Biome[] biomes = BiomeProvider.allowedBiomes.toArray(new Biome[BiomeProvider.allowedBiomes.size()]);
        EntityRegistry.registerModEntity(EntityTeleportCreeper.class, "TeleportCreeper", 160, this, 80, 3, false, 1211231,130139);
        EntityRegistry.registerModEntity(EntitySpiderJockey.class, "SpiderJockey", 161, this, 80, 3, false, 24424242,424224);
        EntityRegistry.registerModEntity(EntityMiniCreeper.class, "MiniCreeper", 162, this, 80, 3, false, 102012,244224);
        EntityRegistry.addSpawn(EntityMiniCreeper.class, 100, 2,4, EnumCreatureType.MONSTER, biomes);
          proxy.init();
        MinecraftForge.EVENT_BUS.register(new WhatEvent());
    }

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        config.save();
    }

    public static int rand(int round) {

        if (round <= 0)
            return 0;
        return rand.nextBoolean() ? -rand.nextInt(round) : rand.nextInt(round);
    }

}
