package ruo.awild;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Biomes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import ruo.asdf.EntityTeleportCreeper;
import ruo.cmplus.deb.DebAPI;

//@Mod(modid =  "WildPlus", name =  "WildPlus")
public class WildMod {

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new WildEvent());
        EntityRegistry.addSpawn(EntityHorse.class, 10, 1, 5, EnumCreatureType.MONSTER, Biomes.PLAINS, Biomes.SAVANNA);
        DebAPI.registerEntity(this, "EnderBlock", EntityEnderBlock.class);
        DebAPI.registerEntity(this, "WildZombie", EntityWildZombie.class, new RenderZombie(Minecraft.getMinecraft().getRenderManager()));
        DebAPI.registerEntity(this, "WildHorse", EntityWildHorse.class, new RenderHorse(Minecraft.getMinecraft().getRenderManager(), new ModelHorse(), 0.5F));
        DebAPI.registerEntity(this, "WildEnderman", EntityWildEnderman.class, new RenderEnderman(Minecraft.getMinecraft().getRenderManager()));
    }

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent e) {
    }

    @Mod.EventHandler
    public void init(FMLPostInitializationEvent e) {

    }
}
