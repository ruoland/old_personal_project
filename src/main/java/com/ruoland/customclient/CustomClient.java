package com.ruoland.customclient;


import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.File;

@Mod(modid = CustomClient.modid, name = "CustomClientR", version = CustomClient.version)
public class CustomClient {
    public static final String modid = "CustomClientR";
    public static final String version = "2.2";
    public static Configuration config;
    public static Configuration configGui;

    public static ConfigCategory mainmenuCategory, splashCategory;

    private static String configFile;
    @Mod.EventHandler
    public void asdf(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandGui());
        event.registerServerCommand(new CommandDrawTexture());
        event.registerServerCommand(new CommandDrawYoutube());

        event.registerServerCommand(new CommandUI());
    }
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
        mainmenuCategory = config.getCategory("T");
        splashCategory = config.getCategory("Splash");

        CustomClient.config.get("Splash", "X", Minecraft.getMinecraft().displayWidth / 2 + 90, "기본값: 323");
        CustomClient.config.get("Splash", "Y", 70, "기본값: 70");
        CustomClient.config.get("Splash", "SplashVisible", true, "");
        String backgroundImage = "textures/gui/title/background/panorama_0.png";;
        CustomClient.config.get("T", "Mainmenu", backgroundImage, "기본값:"+backgroundImage);

        config.get("Mods", "Version", version).set(this.version);
        config.save();
        MinecraftForge.EVENT_BUS.register(new CustomClientEvent());
    }
}
	