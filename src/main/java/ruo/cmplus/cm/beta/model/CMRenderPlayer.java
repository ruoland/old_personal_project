package ruo.cmplus.cm.beta.model;

import api.player.render.RenderPlayerAPI;
import api.player.render.RenderPlayerBase;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import ruo.cmplus.CMManager;

public class CMRenderPlayer extends RenderPlayerBase {

	public CMRenderPlayer(RenderPlayerAPI renderPlayerAPI) {
		super(renderPlayerAPI);
	}

	@Override
	public void doRender(AbstractClientPlayer paramAbstractClientPlayer, double x, double y, double z, float yaw,
			float ti) {
		super.doRender(paramAbstractClientPlayer, x, y, z, yaw, ti);
	}

	@Override
	public void rotateCorpse(AbstractClientPlayer paramAbstractClientPlayer, float paramFloat1, float paramFloat2,
			float paramFloat3) {
		if (CMManager.isSleep()) {
			EnumFacing enumfacing = CMManager.getSleepFacing();
			float rotate = 0;

			if(enumfacing == EnumFacing.SOUTH) {
				rotate = 90F;
			}
			if(enumfacing == EnumFacing.NORTH) {
				rotate = 270F;
			}
			if(enumfacing == EnumFacing.WEST) {
				rotate = 0;
			}
			if(enumfacing == EnumFacing.EAST) {
				rotate = 180F;
			}
			GlStateManager.rotate(rotate, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(90, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(270, 0.0F, 1.0F, 0.0F);//이 값을 0으로 하면 엎드린채 잔다
		}
		else
			super.rotateCorpse(paramAbstractClientPlayer, paramFloat1, paramFloat2, paramFloat3);

	}
}
