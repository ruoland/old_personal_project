package com.ruoland.customclient;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import ruo.minigame.api.NBTAPI;
import ruo.minigame.api.RenderAPI;

import java.io.File;

public class GuiTexture {

    public int x, y, width, height, id;
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
                RenderAPI.drawTextureZ(dynamicLocation, this.x, this.y, -80, this.width, this.height);
            else
                RenderAPI.drawTextureZ(resourceLocation, x, y, -80, width, height);
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
        compound.setInteger("Width", width);
        compound.setInteger("Height", height);
        compound.setBoolean("Visible", visible);
        return compound;
    }
    public NBTTagCompound deserializeNBT(NBTTagCompound compound) {
        resourceLocation = new ResourceLocation(compound.getString("texture"));
        x = compound.getInteger("xPosition");
        y = compound.getInteger("yPosition");
        width = compound.getInteger("Width");
        height = compound.getInteger("Height");
        visible = compound.getBoolean("Visible");
        return compound;
    }
}
