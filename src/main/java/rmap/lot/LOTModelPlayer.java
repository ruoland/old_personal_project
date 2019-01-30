package rmap.lot;

import api.player.model.ModelPlayerAPI;
import api.player.model.ModelPlayerBase;
import minigameLib.api.RenderAPI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import rmap.lot.old.LOTEffect;

public class LOTModelPlayer extends ModelPlayerBase{

	public LOTModelPlayer(ModelPlayerAPI renderPlayerAPI) {
		super(renderPlayerAPI);
	}
	
	@Override
	public void render(Entity paramEntity, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4,
			float paramFloat5, float paramFloat6) {
		super.render(paramEntity, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
		if(LOTEffect.isPlayerWallMode)
		RenderAPI.renderBlock(LOTEffect.chestItem, (EntityLivingBase) paramEntity);
	}
	@Override
	public void setRotationAngles(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4,
			float paramFloat5, float paramFloat6, Entity paramEntity) {
		//DebAPI raapi = DebAPI.createDebAPI("ra", this.modelBiped.bipedRightArm.rotateAngleX, this.modelBiped.bipedRightArm.rotateAngleY,this.modelBiped.bipedRightArm.rotateAngleZ);
		//DebAPI laapi = DebAPI.createDebAPI("la", this.modelBiped.bipedLeftArm.rotateAngleX, this.modelBiped.bipedLeftArm.rotateAngleY,this.modelBiped.bipedLeftArm.rotateAngleZ);
		super.setRotationAngles(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramEntity);
		if(LOTEffect.isChestItem) {
			this.modelBiped.bipedRightArm.rotateAngleX = 10;
			this.modelBiped.bipedLeftArm.rotateAngleX = 10;

			//this.modelBiped.bipedRightArm.rotateAngleY = raapi.y;
			//this.modelBiped.bipedRightArm.rotateAngleZ = raapi.z;
			//this.modelBiped.bipedLeftArm.rotateAngleY = laapi.y;
			//this.modelBiped.bipedLeftArm.rotateAngleZ = laapi.z;
		}
	}
}
