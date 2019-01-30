package com.ruoland.customclient;


import com.ruoland.customclient.component.GuiTexture;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.nio.file.Files;
import java.nio.file.Paths;

public class CustomClientEvent {
    public static int expX, expY;
    public static int healthX, healthY;
    public static int hotbarX,  hotbarY;
    public static int foodX, foodY;

    public static GuiCustomBase uiTool = new GuiCustomBase("./customgui/customUI/default");

    @SubscribeEvent
    public void event(WorldEvent.Load event) {
        if(Files.exists(Paths.get("./customgui/customUI/default.dat"))) {
            NBTTagCompound tagCompound = uiTool.guiData.getNBTAPI().getNBT();
            expX = tagCompound.getInteger("expX");
            expY = tagCompound.getInteger("expX");
            healthX = tagCompound.getInteger("healthX");
            healthY = tagCompound.getInteger("healthY");
            hotbarX = tagCompound.getInteger("hotbarX");
            hotbarY = tagCompound.getInteger("hotbarY");
            foodX = tagCompound.getInteger("foodX");
            foodY = tagCompound.getInteger("foodY");
        }
    }
    @SubscribeEvent
    public void event(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiMainMenu) {
            event.setGui(new GuiMainMenuRealNew("mainmenu"));
        } else {
            uiTool.closeBrowser();
        }
    }

    @SubscribeEvent
    public void event2(RenderGameOverlayEvent.Post event){

        if(event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE ){
            GlStateManager.popMatrix();
        }
        if(event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR){
            GlStateManager.popMatrix();
        }
        if(event.getType() == RenderGameOverlayEvent.ElementType.HEALTH){
            GlStateManager.popMatrix();
        }
        if(event.getType() == RenderGameOverlayEvent.ElementType.FOOD){
            GlStateManager.popMatrix();
        }
        if(event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            if(uiTool != null){
                for (GuiTexture g : uiTool.guiData.textureList) {
                    GL11.glPushMatrix();
                    g.renderTexture();
                    GL11.glPopMatrix();
                }
            }
        }
    }
    @SubscribeEvent
    public void event2(RenderGameOverlayEvent.Pre event){
        event.setPhase(EventPriority.LOWEST);

        if(event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE ){
            GlStateManager.pushMatrix();
            GlStateManager.translate(expX,expY,0);
        }
        if(event.getType() == RenderGameOverlayEvent.ElementType.HEALTH){
            GlStateManager.pushMatrix();
            GlStateManager.translate(healthX,healthY,30);
        }
        if(event.getType() == RenderGameOverlayEvent.ElementType.FOOD){
            GlStateManager.pushMatrix();
            GlStateManager.translate(foodX,foodY,30);
        }
        if(event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR){
            GlStateManager.pushMatrix();
            GlStateManager.translate(hotbarX,hotbarY,30);
        }
    }

}
