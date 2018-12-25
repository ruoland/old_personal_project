package com.ruoland.customclient;

import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiCustomUI extends GuiCustomBase {
    private String a;
    public GuiCustomUI(String name, String ui) {
        super(name);
        customTool.menuData.backgroundImage = "";
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
            CustomClientEvent.expX = mouseX + -132;
            CustomClientEvent.expY = mouseY + -188;
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
