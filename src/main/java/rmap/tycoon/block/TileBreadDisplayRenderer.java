package rmap.tycoon.block;

import minigameLib.api.RenderAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;

public class TileBreadDisplayRenderer<T> extends TileEntitySpecialRenderer<TileBreadDisplay> {

	@Override
	public void renderTileEntityAt(TileBreadDisplay te, double x, double y, double z, float partialTicks, int destroyStage) {
		super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
		if (te.getBread() != null) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.5, 0.6, 0.5);
			Minecraft.getMinecraft().getRenderManager().doRenderEntity(te.getBread().getEntityItem(), x,y,z,0,0,false);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.5, 0.3, 0.5);
			renderLivingLabel("빵 이름:"+te.getBread().getDisplayName(),x,y,z, 50);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.5, 0.5, 0.5);
			renderLivingLabel("남은 빵:"+te.getBread().getAmount(),x,y,z, 50);
			GlStateManager.popMatrix();
		}
		if(te.isGlass){
			RenderAPI.render(Blocks.GLASS, x,y,z);
		}
		
	
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
