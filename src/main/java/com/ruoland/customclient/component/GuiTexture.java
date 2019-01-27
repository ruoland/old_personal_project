package com.ruoland.customclient.component;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import ruo.minigame.api.RenderAPI;

import java.io.File;

public class GuiTexture implements IGuiComponent {

    public int x, y, z, width, height, id;
    public float alpha = 1F;
    public boolean visible = true;
    public ResourceLocation resourceLocation, dynamicLocation;

    public GuiTexture(int id, String texture, int x, int y, int width, int height) {
        this(id, new ResourceLocation(texture), x, y, width, height);
    }
    public GuiTexture(int id) {
        this.id = id;
    }
    public GuiTexture(int id, ResourceLocation texture, int x, int y, int width, int height) {
        this.id = id;
        this.resourceLocation = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        if (texture.toString().startsWith("https://") || texture.toString().startsWith("http://")) {
            File f = new File("resourcepacks/CustomClient/assets/customclient/textures/gui/");
            f.mkdirs();
            //resourceLocation = (URLDownload.download("CustomClient", texture.toString(),
            //	"resourcepacks/CustomClient/assets/customclient/textures/gui/"));
        }
    }

    public void renderTexture() {
        if (visible) {
            if (dynamicLocation != null)
                RenderAPI.drawTextureZ(dynamicLocation, alpha, this.x, this.y, z, this.width, this.height);
            else
                RenderAPI.drawTextureZ(resourceLocation, alpha, x, y, z, width, height);
        }
    }

    public boolean mousePressed(int p_146116_2_, int p_146116_3_) {
        return p_146116_2_ >= x && p_146116_3_ >= this.y && p_146116_2_ < this.x + this.width && p_146116_3_ < this.y + this.height;
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("texture", resourceLocation.toString());
        compound.setInteger("xPosition", x);
        compound.setInteger("yPosition", y);
        compound.setInteger("z", z);
        compound.setInteger("Width", width);
        compound.setInteger("Height", height);
        compound.setBoolean("Visible", visible);
        compound.setFloat("alpha", alpha);
        return compound;
    }
    public NBTTagCompound deserializeNBT(NBTTagCompound compound) {
        resourceLocation = new ResourceLocation(compound.getString("texture"));
        x = compound.getInteger("xPosition");
        y = compound.getInteger("yPosition");
        z = compound.getInteger("z");
        width = compound.getInteger("Width");
        height = compound.getInteger("Height");
        visible = compound.getBoolean("Visible");
        if(compound.hasKey("alpha")) {
            alpha = compound.getFloat("alpha");
        }
        return compound;
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
        return visible;
    }

    @Override
    public ResourceLocation getTexture() {
        return resourceLocation;
    }

    @Override
    public void setTexture(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
