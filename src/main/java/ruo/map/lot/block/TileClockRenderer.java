package ruo.map.lot.block;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class TileClockRenderer<T> extends TileEntitySpecialRenderer<TileClock> {
	ruo.map.lot.block.ModelClock clock = new ruo.map.lot.block.ModelClock();
	@Override
	public void renderTileEntityAt(TileClock te, double x, double y, double z, float partialTicks, int destroyStage) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x,y,z);
		clock.render(null,0,0,0,0,0,1);
		GlStateManager.popMatrix();
	}
}
