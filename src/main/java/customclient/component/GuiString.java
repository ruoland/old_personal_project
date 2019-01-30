package customclient.component;

import customclient.RenderAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class GuiString implements IGuiComponent {
    public boolean isVisible = true, isLock = false;
    public int x, y, id;
    public float width = 1, height = 1;
    public String text;
    private Minecraft mc = Minecraft.getMinecraft();
    public boolean isScale;

    public GuiString(String text, int x, int y, int id) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public void renderString() {
        if (isVisible) {
            if (isScale) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(x, y, 0);
                GlStateManager.scale(width, height, width);
                GlStateManager.translate(-x,-y,0);
                RenderAPI.drawString(text, x, y, 0xFFFFFF);
                GlStateManager.popMatrix();
            } else
                RenderAPI.drawString(text, x, y, 0xFFFFFF);
        }
    }

    public boolean mousePressed(Minecraft p_146116_1_, int p_146116_2_, int p_146116_3_) {
        return p_146116_2_ >= x && p_146116_3_ >= (y)
                && p_146116_2_ < x + mc.fontRendererObj.getStringWidth(text)
                && p_146116_3_ < y + 9;
    }

    public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
        fontRendererIn.drawStringWithShadow(text, (float) (x - fontRendererIn.getStringWidth(text) / 2), (float) y, color);
    }


    public NBTTagCompound serializeNBT(GuiScreen menuRealNew) {
        int i2 = menuRealNew.height / 4 + 48;
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("Text", text);
        compound.setInteger("xPosition", x);
        compound.setInteger("yPosition", y);
        compound.setFloat("Width", width);
        compound.setFloat("Height", height);
        compound.setBoolean("Visible", isVisible);

        compound.setBoolean("isLock", isLock);
        compound.setBoolean("isScale", isScale);
        return compound;
    }

    public void deserializeNBT(NBTTagCompound compound) {
        text = compound.getString("Text");
        x = compound.getInteger("xPosition");
        y = compound.getInteger("yPosition");
        width = compound.getFloat("Width");
        height = compound.getFloat("Height");
        isVisible = compound.getBoolean("Visible");
        isLock = compound.getBoolean("isLock");
        isScale = compound.getBoolean("isScale");
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
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
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
    public boolean isLock() {
        return isLock;
    }

    @Override
    public void setLock(boolean lock) {
        isLock = lock;
    }

    @Override
    public ResourceLocation getTexture() {
        return null;
    }
}
