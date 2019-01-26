package com.ruoland.customclient.component;

import com.ruoland.customclient.GuiCustomBase;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiCusTextField extends GuiTextField {
    public GuiCusTextField(GuiCustomBase customBase, int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
        super(componentId, fontrendererObj, x, y, par5Width, par6Height);
        customBase.fieldList.add(this);
        this.setMaxStringLength(10000);
    }

    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
        setEnabled(isVisible);
    }

    @Override
    public void drawTextBox() {
        super.drawTextBox();
    }
}
