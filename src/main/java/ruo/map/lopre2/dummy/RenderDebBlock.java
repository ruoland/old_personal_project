package ruo.map.lopre2.dummy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

public class RenderDebBlock extends RenderLiving<EntityDebBlock> {

	public RenderDebBlock(ModelBase modelbaseIn, float shadowsizeIn) {
		super(Minecraft.getMinecraft().getRenderManager(), modelbaseIn, shadowsizeIn);
	}

	protected static ResourceLocation loc;
	int count = 0;

	@Override
	protected void renderModel(EntityDebBlock entitylivingbaseIn, float limbSwing, float limbSwingAmount,
			float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
		GlStateManager.pushMatrix();
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.color(1, 1, 1, 0.7F);
		super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch,
				scaleFactor);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1F);
		GlStateManager.disableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityDebBlock entity) {
		if (loc == null) {
			IBlockState block = Blocks.STONE.getDefaultState();
			BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
			loc = new ResourceLocation("minecraft:textures/" + blockrendererdispatcher.getBlockModelShapes()
					.getTexture(block).getIconName().replace("minecraft:", "") + ".png");
		}
		return loc;
	}

}
