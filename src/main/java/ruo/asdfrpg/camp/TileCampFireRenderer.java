package ruo.asdfrpg.camp;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class TileCampFireRenderer extends TileEntitySpecialRenderer<TileCampFire> {
    private ModelCampFire campFire = new ModelCampFire();
    private ResourceLocation resourceLocation = new ResourceLocation("textures/blocks/log_oak");
	@Override
	public void renderTileEntityAt(TileCampFire te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5F, y + 1.5F, z + 0.5F);
        this.bindTexture(resourceLocation);
        //Doing the 3D render
        campFire.render(null, 0,0,0,0,0,0.5F);
        //We close Matrix
        GlStateManager.popMatrix();

    }
    public FontRenderer getFontRendererFromRenderManager()
    {
        return Minecraft.getMinecraft().getRenderManager().getFontRenderer();
    }
}
