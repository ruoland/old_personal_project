package ruo.map.lot;

import api.player.render.RenderPlayerAPI;
import api.player.render.RenderPlayerBase;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import ruo.map.lot.old.LOTEffect;

public class LOTRenderPlayer extends RenderPlayerBase{

	public LOTRenderPlayer(RenderPlayerAPI renderPlayerAPI) {
		super(renderPlayerAPI);
	}

	@Override
	public void renderModel(AbstractClientPlayer paramAbstractClientPlayer, float paramFloat1, float paramFloat2,
			float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6) {
		if (LOTEffect.isPlayerWallMode) {
			GlStateManager.pushMatrix();
			BlockPos pos = LOTEffect.pos;
			if (pos.getX() > 0) {
				GlStateManager.translate(0, 0, 0.9);
				GlStateManager.scale(1, 1, 0.1);
			}
			if (pos.getX() < 0) {
				GlStateManager.scale(1, 1, 0.1);
				GlStateManager.translate(0, 0, -0.9);

			}
			if (pos.getZ() > 0) {
				GlStateManager.scale(0.1, 1, 1);
				GlStateManager.translate(0.9, 0, 0);
			}
			if (pos.getZ() < 0) {
				GlStateManager.scale(0.1, 1, 1);
				GlStateManager.translate(-0.9, 0, 0);
			}
			super.renderModel(paramAbstractClientPlayer, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5,
					paramFloat6);
			GlStateManager.popMatrix();
		}else
			super.renderModel(paramAbstractClientPlayer, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5,
					paramFloat6);

	}
}
