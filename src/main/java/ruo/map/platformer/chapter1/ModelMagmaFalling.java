package ruo.map.platformer.chapter1;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

/**
 * 이 모델은 실험용 모델
 */
public class ModelMagmaFalling extends ModelBase{

	private ModelRenderer block;
	public ModelMagmaFalling() {
		block = new ModelRenderer(this,32,32);
		block.addBox(0,0,0, 16, 16, 16);
	}

	double x = -0.5, z = -0.5;
	float rY, rZ;
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale) {
		EntityMagmaFalling f = (EntityMagmaFalling) entityIn;
		if(f == null) {
			entityIn.setDead();
			return;
		}
		int random =  f.random;
		//render2(entityIn, scale, 1, 0, entityIn.motionY * 90, -entityIn.motionY * 30);
		if(f.getCustomNameTag().equals("-2"))
		{
			render2(entityIn, scale, 0, 0, f.roX, f.roY);
			return;
		}
		if(f.getCustomNameTag().equals("-1") || random == -1)
		{
			render2(entityIn, scale, 0, 0, entityIn.motionY * 30, entityIn.motionY * 30);
		}
		else if(random == 0){
			render2(entityIn, scale, 1, 0, entityIn.motionY * 30, entityIn.motionY * 30);
			render2(entityIn, scale, 3, 0, entityIn.motionY * 50, entityIn.motionY * 70);
			render2(entityIn, scale, -1, 1, entityIn.motionY * 80, entityIn.motionY * 90);
			render2(entityIn, scale, -3, 2, -entityIn.motionY * 10, -entityIn.motionY * 50);
			render2(entityIn, scale, 1, 1, entityIn.motionY * 20, -entityIn.motionY * 80);
			render2(entityIn, scale, 3, 2, -entityIn.motionY * 110, entityIn.motionY * 48);
		}
		else if(random == 1){
			render2(entityIn, scale, 2, 0, -entityIn.motionY * 48, entityIn.motionY * 70);
			render2(entityIn, scale, 4, 0, entityIn.motionY * 110, -entityIn.motionY * 13);
			render2(entityIn, scale, -2, -1, entityIn.motionY * 170, entityIn.motionY * 85);
			render2(entityIn, scale, -4, 0, -entityIn.motionY * 180, -entityIn.motionY * 18);
			render2(entityIn, scale, 2, 0, -entityIn.motionY * 20, entityIn.motionY * 98);
			render2(entityIn, scale, 4, 2, entityIn.motionY * 40, entityIn.motionY * 128);
		}
		else if(random == 2){
			render2(entityIn, scale, 3, 0, entityIn.motionY * 140, entityIn.motionY * 47);
			render2(entityIn, scale, 6, 0, entityIn.motionY * 25, entityIn.motionY * 17);
			render2(entityIn, scale, -3, -2, entityIn.motionY * 19, entityIn.motionY * 35);
			render2(entityIn, scale, -6, 1, -entityIn.motionY * 28, -entityIn.motionY * 58);
			render2(entityIn, scale, 3, 1, entityIn.motionY * 24, -entityIn.motionY * 14);
			render2(entityIn, scale, 6, 2, entityIn.motionY * 25, entityIn.motionY * 57);
		}
		else if(random == 3){
			render2(entityIn, scale, 4, 0, -entityIn.motionY * 17, -entityIn.motionY * 17);
			render2(entityIn, scale, 8, 0, -entityIn.motionY * 25, -entityIn.motionY * 39);
			render2(entityIn, scale, -4, -1, -entityIn.motionY * 81, -entityIn.motionY * 64);
			render2(entityIn, scale, -8, 2, -entityIn.motionY * 90, -entityIn.motionY * 50);
			render2(entityIn, scale, 4, 1, -entityIn.motionY * 17, -entityIn.motionY * 39);
			render2(entityIn, scale, 8, 2, -entityIn.motionY * 59, -entityIn.motionY * 78);

		}
		else if(random == 4){
			render2(entityIn, scale, 7, -2, entityIn.motionY * 28, entityIn.motionY * 92);
			render2(entityIn, scale, 4, 2, entityIn.motionY * 97, entityIn.motionY * 49);
			render2(entityIn, scale, -4, -1, entityIn.motionY * 78, entityIn.motionY * 78);
			render2(entityIn, scale, -2, 2, entityIn.motionY * 54, entityIn.motionY * 78);
			render2(entityIn, scale, 4, 1, entityIn.motionY * 87, entityIn.motionY * 63);
			render2(entityIn, scale, 8, 2, entityIn.motionY * 45, entityIn.motionY * 47);
		}
		else{
			render2(entityIn, scale, 5, 0, -entityIn.motionY * 65, -entityIn.motionY * 75);
			render2(entityIn, scale, 10, 0, -entityIn.motionY * 87, -entityIn.motionY * 65);
			render2(entityIn, scale, -5, -1, entityIn.motionY * 47, entityIn.motionY * 12);
			render2(entityIn, scale, -10, 2, entityIn.motionY * 17, -entityIn.motionY * 25);
			render2(entityIn, scale, 5, 1,-entityIn.motionY * 10, entityIn.motionY * 57);
			render2(entityIn, scale, 10, 2, -entityIn.motionY * 110,entityIn.motionY * 48);
		}
	}

	public void render2(Entity entityIn, float scale, float tX, int tY,double d, double e){
		float randomY = 0, randomX = 0;
		if(entityIn instanceof EntityMagmaFalling){
			EntityMagmaFalling f = (EntityMagmaFalling) entityIn;
			if(f == null) {
				entityIn.setDead();
				return;
			}
			randomY = f.randomY;
			randomX = f.randomX;
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate(tX+randomX, tY+randomY, 0);
		GlStateManager.rotate((float) d, 0, 1, 0);
		GlStateManager.rotate((float) e, 0, 0, 1);
		block.render(scale);
		GlStateManager.popMatrix();
	}
}

