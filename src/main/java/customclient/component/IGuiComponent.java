package customclient.component;

import net.minecraft.util.ResourceLocation;

public interface IGuiComponent
{
    public void setVisible(boolean visible);
    public void setTexture(ResourceLocation resourceLocation);
    public int getX();
    public int getY();
    public float getWidth();
    public float getHeight();
    public int getID();
    public boolean isVisible();
    public boolean isLock();
    public void setLock(boolean lock);
    public ResourceLocation getTexture();

}
