package com.ruoland.customclient;


import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GLSync;
import ruo.cmplus.cm.CommandDrawtexture;

public class CustomClientEvent {
    public static int expX, expY;
    public static int healthX, healthY;
    public static int hotbarX,  hotbarY;
    public static int foodX, foodY;

    @SubscribeEvent
    public void event(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiMainMenu) {
            event.setGui(new GuiMainMenuRealNew("메인메뉴"));
        } else {
            CustomTool.closeBrowser();
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
    }
    @SubscribeEvent
    public void event2(RenderGameOverlayEvent.Pre event){
        event.setPhase(EventPriority.LOWEST);
        if(event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
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
