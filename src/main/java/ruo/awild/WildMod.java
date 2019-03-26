package ruo.awild;

import cmplus.deb.DebAPI;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Biomes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

//@Mod(modid =  "WildPlus", name =  "WildPlus")
public class WildMod {

    @SidedProxy(serverSide = "ruo.awild.CommonProxy", clientSide = "ruo.awild.ClientProxy")
    public static CommonProxy proxy;
    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new WildEvent());
        EntityRegistry.addSpawn(EntityHorse.class, 10, 1, 5, EnumCreatureType.MONSTER, Biomes.PLAINS, Biomes.SAVANNA);
        DebAPI.registerEntity(this, "EnderBlock", EntityEnderBlock.class);
        DebAPI.registerEntity(this, "WildZombie", EntityWildZombie.class);

        DebAPI.registerEntity(this, "WildHorse", EntityWildHorse.class);
        DebAPI.registerEntity(this, "WildEnderman", EntityWildEnderman.class);
    }

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent e) {
    }

    @Mod.EventHandler
    public void init(FMLPostInitializationEvent e) {

    }
}
