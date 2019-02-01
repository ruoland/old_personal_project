package cmplus.cm.beta.model;

import api.player.model.ModelPlayerAPI;
import api.player.model.ModelPlayerBase;
import cmplus.CMManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class CMModelPlayer extends ModelPlayerBase {

	public static float roX,roY,roZ, traX, traY, traZ, rightArmZ, leftArmZ, rightArmX, leftArmX, leftLegZ, rightLegZ, rightLegX, leftLegX;

	public CMModelPlayer(ModelPlayerAPI modelPlayerAPI) {
		super(modelPlayerAPI);
	}

	@Override
	public void render(Entity paramEntity, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4,
			float paramFloat5, float paramFloat6) {
		if (CMManager.isSit()) {
			GlStateManager.translate(0, 0.5, 0);
		}
		GlStateManager.translate(traX,0,0);
		GlStateManager.translate(0,traY,0);
		GlStateManager.translate(0,0,traZ);
		GlStateManager.rotate(roX, 1, 0, 0);
		GlStateManager.rotate(roY, 0, 1, 0);
		GlStateManager.rotate(roZ, 0, 0, 1);

		super.render(paramEntity, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
	}

	@Override
	public void setRotationAngles(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4,
			float paramFloat5, float paramFloat6, Entity paramEntity) {
		if(modelPlayer != null && paramEntity != null)
		this.modelPlayer.isRiding = CMManager.isSit() || paramEntity.isRiding();
		super.setRotationAngles(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6,
				paramEntity);

	}

	/**
	 * rightArmZ = rightArmZ == 0 ? modelBiped.bipedRightArm.rotateAngleZ :
	 * rightArmZ; leftArmZ = rightArmZ == 0 ? modelBiped.bipedRightArm.rotateAngleZ
	 * : leftArmZ; leftLegZ = rightArmZ == 0 ? modelBiped.bipedRightArm.rotateAngleZ
	 * : leftLegZ; rightLegZ = rightArmZ == 0 ?
	 * modelBiped.bipedRightArm.rotateAngleZ : rightLegZ; rightArmX = rightArmZ == 0
	 * ? modelBiped.bipedRightArm.rotateAngleZ : rightArmX; leftArmX = rightArmZ ==
	 * 0 ? modelBiped.bipedRightArm.rotateAngleZ : leftArmX; leftLegX = rightArmZ ==
	 * 0 ? modelBiped.bipedRightArm.rotateAngleZ : leftLegX; rightLegX = rightArmZ
	 * == 0 ? modelBiped.bipedRightArm.rotateAngleZ : rightLegX;
	 * modelBiped.bipedRightArm.rotateAngleZ = rightArmZ;
	 * modelBiped.bipedLeftArm.rotateAngleZ = leftArmZ;
	 * modelBiped.bipedLeftLeg.rotateAngleZ = leftLegZ;
	 * modelBiped.bipedRightLeg.rotateAngleZ = rightLegZ;
	 * modelBiped.bipedRightArm.rotateAngleX = rightArmX;
	 * modelBiped.bipedLeftArm.rotateAngleX = leftArmX;
	 * modelBiped.bipedLeftLeg.rotateAngleX = leftLegX;
	 * modelBiped.bipedRightLeg.rotateAngleX = rightLegX;
	 */
}
