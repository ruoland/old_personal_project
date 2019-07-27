package ruo.what;

import cmplus.deb.DebAPI;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.Random;

@Mod(modid = "CreeperWorld", name = "Creeper World")
public class CreeperWorld {
    private static Random rand = new Random();
    public static Configuration config;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        DebAPI.registerEntity(this, "TeleportCreeper", EntityTeleportCreeper.class);
        DebAPI.registerEntity(this, "SpiderCreeper", EntitySpiderJockey.class);
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
