package ruo.minigame;

import api.player.render.RenderPlayerAPI;
import api.player.render.RenderPlayerBase;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import ruo.minigame.api.Direction;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;

public class MiniGameRenderPlayer extends RenderPlayerBase {

	public MiniGameRenderPlayer(RenderPlayerAPI playerAPI) {
		super(playerAPI);
	}

	@Override
	public void renderModel(AbstractClientPlayer paramAbstractClientPlayer, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(getX(MiniGame.mineRunEvent.lineLR < 0 ? Direction.RIGHT : Direction.LEFT),
				0, getZ(MiniGame.mineRunEvent.lineLR < 0 ? Direction.RIGHT : Direction.LEFT));
		super.renderModel(paramAbstractClientPlayer, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
		GlStateManager.popMatrix();
	}

	public double getX(Direction direction) {
		return EntityAPI.getX(WorldAPI.getPlayer(), direction, MiniGame.mineRunEvent.absLR() * 2, false);
	}

	public double getZ(Direction direction) {
		return EntityAPI.getZ(WorldAPI.getPlayer(), direction, MiniGame.mineRunEvent.absLR() * 2, false);
	}
}

