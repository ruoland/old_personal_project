package ruo.what;

import cmplus.deb.DebAPI;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.Random;

@Mod(modid = "What", name = "asdf")
public class What {
    private static Random rand = new Random();
    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        DebAPI.registerEntity(this, "TeleportCreeper", EntityTeleportCreeper.class);
        DebAPI.registerEntity(this, "EnderBlock", EntityEnderBlock.class);

        //DebAPI.registerEntity(this, "SpiderJockey", EntitySpiderJockey.class);
        Biome[] biomes = BiomeProvider.allowedBiomes.toArray(new Biome[BiomeProvider.allowedBiomes.size()]);
        EntityRegistry.addSpawn(EntityGhast.class, 1, 1, 2, EnumCreatureType.MONSTER, biomes);
        EntityRegistry.addSpawn(EntityCreeper.class, 30, 1, 2, EnumCreatureType.CREATURE, biomes);
        EntityRegistry.addSpawn(EntityZombie.class, 20, 10, 20, EnumCreatureType.CREATURE, biomes);

        MinecraftForge.EVENT_BUS.register(new WhatEvent());

    }

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event){

    }
    public static int rand(int round) {

        if(round <= 0)
            return 0;
        return rand.nextBoolean() ? -rand.nextInt(round) : rand.nextInt(round);
    }

}
