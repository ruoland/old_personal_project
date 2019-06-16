package ruo.what;

import cmplus.deb.DebAPI;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

//  @Mod(modid = "What", name = "asdf")
public class What {

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        DebAPI.registerEntity(this, "TeleportCreeper", EntityTeleportCreeper.class);
        DebAPI.registerEntity(this, "EnderBlock", EntityEnderBlock.class);

        DebAPI.registerEntity(this, "SpiderJockey", EntitySpiderJockey.class);
        Biome[] biomes = BiomeProvider.allowedBiomes.toArray(new Biome[BiomeProvider.allowedBiomes.size()]);
        EntityRegistry.addSpawn(EntityTeleportCreeper.class, 50, 1, 5, EnumCreatureType.MONSTER, biomes);
        EntityRegistry.addSpawn(EntitySpiderJockey.class, 50, 1, 5, EnumCreatureType.MONSTER, biomes);
        MinecraftForge.EVENT_BUS.register(new WhatEvent());

    }

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event){

    }


}
