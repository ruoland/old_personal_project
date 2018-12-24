package com.ruoland.customclient;


import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.util.HashMap;

@Mod(modid = CustomClient.modid, name = "CustomClientR", version = CustomClient.version)
public class CustomClient {
    public static final String modid = "CustomClientR";
    public static final String version = "2.2";
    public static Configuration config;
    public static Configuration configGui;

    private static String configFile;

    @Mod.EventHandler
    public void asdf(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    public void asdf(FMLPreInitializationEvent event) {
        configFile = event.getSuggestedConfigurationFile().getAbsolutePath().replace("\\CustomClientR.cfg", "");
        System.out.println(configFile.replace("\\CustomClientR.cfg", ""));

        configGui = new Configuration(new File(event.getSuggestedConfigurationFile().getAbsolutePath()), "CustomGUI");
        configGui.load();
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        config.addCustomCategoryComment("Mods", "");

        config.get("Mods", "Version", version).set(this.version);
        config.save();
        MinecraftForge.EVENT_BUS.register(new CustomClientEvent());
    }
}
	