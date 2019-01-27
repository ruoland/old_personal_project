package com.ruoland.customclient.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import ruo.cmplus.cm.v18.function.VAR;
import ruo.minigame.api.RenderAPI;

public class GuiString implements IGuiComponent {
    public boolean isVisible = true;
    public int x, y, width = 1, height = 1, id;
    public String text;
    private Minecraft mc = Minecraft.getMinecraft();

    public GuiString(String text, int x, int y,int id) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public void renderString() {
        if (isVisible) {
            System.out.println(text);
            GlStateManager.scale(width, height, 0);
            GlStateManager.translate(-width, -height,0);
            RenderAPI.drawString(text, x, y, 0xFFFFFF);
        }
    }

    public boolean mousePressed(Minecraft p_146116_1_, int p_146116_2_, int p_146116_3_) {
        return p_146116_2_ >= x && p_146116_3_ >= (y)
                && p_146116_2_ < x + mc.fontRendererObj.getStringWidth(text)
                && p_146116_3_ < y + 9;
    }


    public NBTTagCompound serializeNBT(GuiScreen menuRealNew) {
        int i2 = menuRealNew.height / 4 + 48;
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("Text", text);
        compound.setInteger("xPosition", x);
        compound.setInteger("yPosition", y);
        compound.setInteger("Width", width);
        compound.setInteger("Height", height);
        compound.setBoolean("Visible", isVisible);
        return compound;
    }

    public void deserializeNBT(NBTTagCompound compound) {
        text = compound.getString("Text");
        x = compound.getInteger("xPosition");
        y = compound.getInteger("yPosition");
        width = compound.getInteger("Width");
        height = compound.getInteger("Height");
        isVisible = compound.getBoolean("Visible");

    }

    @Override
    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    @Override
    public void setTexture(ResourceLocation resourceLocation) {

    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public ResourceLocation getTexture() {
        return null;
    }
}
