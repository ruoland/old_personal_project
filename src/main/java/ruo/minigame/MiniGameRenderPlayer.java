package ruo.minigame;

import api.player.render.RenderPlayerAPI;
import api.player.render.RenderPlayerBase;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import ruo.minigame.api.Direction;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.minigame.minerun.MineRun;

public class MiniGameRenderPlayer extends RenderPlayerBase {

	public MiniGameRenderPlayer(RenderPlayerAPI playerAPI) {
		super(playerAPI);
	}

	@Override
	public void renderModel(AbstractClientPlayer paramAbstractClientPlayer, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6) {
		GlStateManager.pushMatrix();
		if(MiniGame.minerun.isStart()) {
			EnumFacing facing = paramAbstractClientPlayer.getHorizontalFacing();

			if (facing == EnumFacing.NORTH) {
				GlStateManager.translate(getX(MiniGame.mineRunEvent.lineLR > 0 ? Direction.RIGHT : Direction.LEFT),
						0, getZ(MiniGame.mineRunEvent.lineLR > 0 ? Direction.RIGHT : Direction.LEFT));
			} else if (facing == EnumFacing.SOUTH) {
				GlStateManager.translate(getX(MiniGame.mineRunEvent.lineLR < 0 ? Direction.RIGHT : Direction.LEFT),
						0, getZ(MiniGame.mineRunEvent.lineLR < 0 ? Direction.RIGHT : Direction.LEFT));
			} else if (facing == EnumFacing.EAST) {
				GlStateManager.translate(getZ(MiniGame.mineRunEvent.lineLR > 0 ? Direction.RIGHT : Direction.LEFT),
						0, getX(MiniGame.mineRunEvent.lineLR > 0 ? Direction.RIGHT : Direction.LEFT));
			} else if (facing == EnumFacing.WEST) {
				GlStateManager.translate(getZ(MiniGame.mineRunEvent.lineLR < 0 ? Direction.RIGHT : Direction.LEFT),
						0, getX(MiniGame.mineRunEvent.lineLR < 0 ? Direction.RIGHT : Direction.LEFT));
			}
		}
		super.renderModel(paramAbstractClientPlayer, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
		GlStateManager.popMatrix();
	}

	@Override
	public void doRenderShadowAndFire(AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2) {
		//super.doRenderShadowAndFire(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);
	}

	@Override
	public void transformHeldFull3DItemLayer() {
		//super.transformHeldFull3DItemLayer();
	}

	@Override
	public void doRender(AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2) {
		super.doRender(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);
	}

	public double getX(Direction direction) {
		return MineRun.playerPosHelper.getX( direction, MiniGame.mineRunEvent.absLR() * 2, false);
	}

	public double getZ(Direction direction) {
		return  MineRun.playerPosHelper.getZ(direction, MiniGame.mineRunEvent.absLR() * 2, false);
	}
}

