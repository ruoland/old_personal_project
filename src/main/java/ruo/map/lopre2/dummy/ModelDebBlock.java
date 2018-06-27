package ruo.map.lopre2.dummy;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelDebBlock extends ModelBase {

	private ModelRenderer block;

	public ModelDebBlock() {
		block = new ModelRenderer(this, 32, 32);
		block.addBox(0, 0, 0, 16, 16, 16);
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(-0.5, 0.5, -0.5);
		block.render(scale);
		GlStateManager.popMatrix();
	}
}
