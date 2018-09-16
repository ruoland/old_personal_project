package ruo.map.tycoon.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class TileBreadCallRenderer<T> extends TileEntitySpecialRenderer<TileBreadCall> {
	@Override
	public void renderTileEntityAt(TileBreadCall te, double x, double y, double z, float partialTicks, int destroyStage) {
		super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.5, 0, 0.5);
		renderLivingLabel(""+te.getBreadCount(),x,y,z, 50);
		GlStateManager.popMatrix();
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.5, 0.3, 0.5);
		renderLivingLabel("남은 시간:"+te.breadCallSecond,x,y,z, 50);
		GlStateManager.popMatrix();
	}
	protected void renderLivingLabel(String str, double x, double y, double z, int maxDistance)
    {
        double d0 = Minecraft.getMinecraft().getRenderManager().renderViewEntity.getDistance(x, y, z);
        if (d0 <= (double)(maxDistance * maxDistance))
        {
            float f = Minecraft.getMinecraft().getRenderManager().playerViewY;
            float f1 = Minecraft.getMinecraft().getRenderManager().playerViewX;
            float f2 = 1 + 0.5F - (0.0F);
            int i = "deadmau5".equals(str) ? -10 : 0;
            EntityRenderer.drawNameplate(this.getFontRendererFromRenderManager(), str, (float)x, (float)y + f2, (float)z, i, f, f1, false, false);
        }
    }
    public FontRenderer getFontRendererFromRenderManager()
    {
        return Minecraft.getMinecraft().getRenderManager().getFontRenderer();
    }
}
