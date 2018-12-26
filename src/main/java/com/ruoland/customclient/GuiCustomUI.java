package com.ruoland.customclient;

import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class GuiCustomUI extends GuiCustomBase {
    private String a;
    public GuiCustomUI(String name, String ui) {
        super(name);
        customTool.guiData.backgroundImage = "";
        a = ui;

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void initGui() {
        super.initGui();
        customTool.initGui(this);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        if(a.equalsIgnoreCase("exp")) {
            CustomClientEvent.expX = mouseX + -112;
            CustomClientEvent.expY = mouseY + -208;
        }
        if(a.equalsIgnoreCase("food")){
            CustomClientEvent.foodX = mouseX + -218;
            CustomClientEvent.foodY = mouseY + -203;
        }
        if(a.equalsIgnoreCase("heal")){
            CustomClientEvent.healthX = mouseX + -119;
            CustomClientEvent.healthY = mouseY + -199;
        }
        if(a.equalsIgnoreCase("hot")){
            CustomClientEvent.hotbarX = mouseX + -123;
            CustomClientEvent.hotbarY = mouseY + -216;
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        NBTTagCompound tagCompound = customTool.guiData.getNBTAPI().getNBT();
        tagCompound.setInteger("expX", CustomClientEvent.expX);
        tagCompound.setInteger("expY", CustomClientEvent.expY);
        tagCompound.setInteger("foodX", CustomClientEvent.foodX);
        tagCompound.setInteger("foodY", CustomClientEvent.foodY);
        tagCompound.setInteger("healthX", CustomClientEvent.healthX);
        tagCompound.setInteger("healthY", CustomClientEvent.healthY);
        tagCompound.setInteger("hotbarX", CustomClientEvent.hotbarX);
        tagCompound.setInteger("hotbarY", CustomClientEvent.hotbarY);
        customTool.guiData.getNBTAPI().saveNBT();
        CustomClientEvent.uiTool.guiData.clearTexture();
        CustomClientEvent.uiTool.guiData.readTexture();
    }

    /*

        if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
            syncY--;
            System.out.println("Sync YYY - " + syncY);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
            syncY++;
            System.out.println("Sync YYY - " + syncY);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
            syncX--;
            System.out.println("Sync XXX - " + syncX);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
            syncX++;
            System.out.println("Sync XXX - " + syncX);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_L)) {
            sync++;
            System.out.println(sync );
        }
     */
}
