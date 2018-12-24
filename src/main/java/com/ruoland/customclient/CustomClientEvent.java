package com.ruoland.customclient;


import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CustomClientEvent {

    @SubscribeEvent
    public void event(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiMainMenu) {
            event.setGui(new GuiMainMenuRealNew());
        } else {
            CustomTool.closeBrowser();
        }
    }
}
