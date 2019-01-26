package com.ruoland.customclient.component;

import net.minecraft.util.ResourceLocation;

public interface IGuiComponent
{
    public void setVisible(boolean visible);
    public void setTexture(ResourceLocation resourceLocation);
    public int getX();
    public int getY();
    public int getWidth();
    public int getHeight();
    public int getID();
    public boolean isVisible();
    public ResourceLocation getTexture();

}
