package cmplus.cm.v18.customgui;

import cmplus.cm.v18.function.VAR;
import olib.api.RenderAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiTexture {

	public String width, height, x, y;
	public int id;
	public boolean visible = true;
	public ResourceLocation resourceLocation;
	public GuiTexture(int id, String l, String x, String y, String width, String height) {
		this.id = id;
		this.resourceLocation = new ResourceLocation(l);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		if (l.toString().startsWith("https://") || l.toString().startsWith("http://")) {
			resourceLocation = new ResourceLocation(CMResourcePack.imageDownload(l.toString()));
		}
	}
	public GuiTexture(int id, ResourceLocation l, String x, String y, String width, String height) {
		this(id, l.toString(), x,y,width,height);
	}
	public void renderTexture(){
		if(visible){
			GlStateManager.pushMatrix();
			RenderAPI.drawTexture(resourceLocation, 1.0F, VAR.getDouble(x), VAR.getDouble(y), VAR.getDouble(width), VAR.getDouble(height));
			GlStateManager.popMatrix();
		}
	}

	public boolean mousePressed(Minecraft p_146116_1_, int p_146116_2_, int p_146116_3_) {
		return p_146116_2_ >= VAR.getDouble(x) && p_146116_3_ >= VAR.getDouble(y)
				&& p_146116_2_ < VAR.getDouble(x) + VAR.getDouble(width)
				&& p_146116_3_ < VAR.getDouble(y) + VAR.getDouble(height);
	}
}
