package ruo.cmplus.cm.v18.customgui;

import net.minecraft.client.Minecraft;
import ruo.cmplus.cm.v18.function.VAR;
import ruo.minigame.api.RenderAPI;

public class GuiString {
	private Minecraft mc = Minecraft.getMinecraft();
	public String str, x, y;
	int id;
	public boolean visible = true;
	public GuiString(int id, String str, String x, String y) {
		this.id = id;
		this.str = str;
		this.x = x;
		this.y = y;
	}
	
	public void renderString(){
		if(visible){
			RenderAPI.drawString(str, (int) VAR.getDouble(x), (int) VAR.getDouble(y), 0xFFFFFF);
		}
	}
	public boolean mousePressed(Minecraft p_146116_1_, int p_146116_2_, int p_146116_3_) {
		return p_146116_2_ >= (int) VAR.getDouble(x) && p_146116_3_ >= (int) VAR.getDouble(y)
				&& p_146116_2_ < (int) VAR.getDouble(x) + mc.fontRendererObj.getStringWidth(str)
				&& p_146116_3_ < (int) VAR.getDouble(y) + 9;
	}
}
