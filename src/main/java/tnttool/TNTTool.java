package tnttool;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = "tnttool", name = "TNT Tool")
public class TNTTool {
    public static Configuration tnttoolConfig;
    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(new TNTEvent());

    }
    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event){
        tnttoolConfig = new Configuration(event.getSuggestedConfigurationFile());
        tnttoolConfig.load();
        tnttoolConfig.save();
    }
    @Mod.EventHandler
    public void init(FMLServerStartingEvent event){
        event.registerServerCommand(new CommandTNT());
    }
}
