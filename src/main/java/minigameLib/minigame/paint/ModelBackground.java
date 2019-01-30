


package minigameLib.minigame.paint;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelBackground extends ModelBase
{
  //fields
    ModelRenderer Shape1;
  
  public ModelBackground()
  {
	  textureWidth = 16;
	  textureHeight = 23;
      Shape1 = new ModelRenderer(this, -1, 0);
      Shape1.addBox(0F, 0F, 0F, 16, 22, 1);
      Shape1.setRotationPoint(-8F, 0F, 0F);
      Shape1.setTextureSize(16, 23);
      Shape1.setTextureOffset(-1, 0);
      Shape1.mirror = true;
  }
  
  @Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale) {
	  	Shape1.render(scale);
	  	GlStateManager.pushMatrix();
	  	GlStateManager.translate(0, 0, -1);
	  	GlStateManager.rotate(180, 0, 1, 0);
	  	GlStateManager.translate(0, 0, 1);
	  	GlStateManager.translate(0, 0, -2.1);
	  	GlStateManager.scale(10, 10, 10);
	  	Shape1.render(scale);
	  	GlStateManager.popMatrix();
		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
	}

}
