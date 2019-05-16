package cmplus.cm.beta.custommodelentity;

import olib.api.BlockAPI;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

/**
 * 이 모델은 실험용 모델
 */
public class ModelCustomMob extends ModelBase {

	private ModelRenderer block;

	public ModelCustomMob() {
		block = new ModelRenderer(this, 64, 64);
		block.addBox(0, 0, 0, 16, 16, 16);
	}

	private ResourceLocation prevBindTexture;
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale) {
		EntityCustomMob f = (EntityCustomMob) entityIn;
		for (int i =0;i < f.getBlockList(f.getName()).getPosList().size();i++) {
			BlockAPI api = f.getBlockList(f.getName());
			BlockPos firstPos = api.getFirstPos();
			GlStateManager.pushMatrix();
			GlStateManager.translate(firstPos.getX(), firstPos.getY(), firstPos.getZ());
			block.render(scale);
			GlStateManager.popMatrix();
		}
		for (CustomModel api : f.getModel()) {
			for (BlockPos api1 : api.getList().getPosList())
			{
				GlStateManager.pushMatrix();
				GlStateManager.translate(api1.getX(), api1.getY(), api1.getZ());
				block.render(scale);
				GlStateManager.popMatrix();
			}
		}
	}

	public void render2(Entity entityIn, float scale, float tX, int tY, double d, double e) {
		GlStateManager.pushMatrix();
		block.render(scale);
		GlStateManager.popMatrix();
	}
}
