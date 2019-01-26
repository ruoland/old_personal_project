package com.ruoland.customclient;


import com.ruoland.customclient.beta.CommandDrawTexture;
import com.ruoland.customclient.beta.CommandDrawYoutube;
import com.ruoland.customclient.beta.CommandGui;
import com.ruoland.customclient.beta.CommandUI;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = CustomClient.modid, name = "CustomClientR", version = CustomClient.version)
public class CustomClient {
    public static final String modid = "CustomClientR";
    public static final String version = "2.2";

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
        MinecraftForge.EVENT_BUS.register(new CustomClientEvent());
    }
}
	