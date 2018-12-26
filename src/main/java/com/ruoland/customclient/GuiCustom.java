package com.ruoland.customclient;

public class GuiCustom extends GuiCustomBase {
    public GuiCustom(String name) {
        super(name);
        customTool.guiData.backgroundImage = "";
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        super.initGui();
        customTool.initGui(this);
    }


}
