package ruo.hanil;

import customclient.GuiMainMenuRealNew;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ruo.minigame.api.WorldAPI;

public class HanilEvent {

    @SubscribeEvent
    public void openGui(GuiOpenEvent event){
        if(event.getGui() instanceof GuiMainMenuRealNew){
            GuiMainMenuRealNew mainMenuRealNew = (GuiMainMenuRealNew) event.getGui();
            //mainMenuRealNew.setGuiData(new GuiData(mainMenuRealNew, ""));
        }
    }

    @SubscribeEvent
    public void joinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            String worldName = WorldAPI.getCurrentWorldName();
            if (worldName.equalsIgnoreCase("Nahanil")) {
                event.getEntity().rotationYaw = 7.94F;
                event.getEntity().rotationPitch = -13F;
                Minecraft.getMinecraft().gameSettings.thirdPersonView = 2;
                Minecraft.getMinecraft().displayGuiScreen(new GuiHanLoading());
            }
        }
    }
}
