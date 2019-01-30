package com.ruoland.customclient;


import com.google.common.collect.Lists;
import com.ruoland.customclient.beta.CommandDrawTexture;
import com.ruoland.customclient.beta.CommandDrawYoutube;
import com.ruoland.customclient.beta.CommandGui;
import com.ruoland.customclient.beta.CommandUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiResourcePackList;
import net.minecraft.client.resources.ResourcePackListEntry;
import net.minecraft.client.resources.ResourcePackListEntryFound;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.List;

//@Mod(modid = CustomClient.modid, name = "CustomClientR", version = CustomClient.version)
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
        //ResourceHelper.init();
        Minecraft mc = Minecraft.getMinecraft();
        mc.gameSettings.resourcePacks.add("CustomClient");
        mc.gameSettings.saveOptions();

    }
}
	