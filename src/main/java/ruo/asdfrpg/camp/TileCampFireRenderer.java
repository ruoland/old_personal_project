package ruo.asdfrpg.camp;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class TileCampFireRenderer extends TileEntitySpecialRenderer<TileCampFire> {

	@Override
	public void renderTileEntityAt(TileCampFire te, double x, double y, double z, float partialTicks, int destroyStage) {
		super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
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
