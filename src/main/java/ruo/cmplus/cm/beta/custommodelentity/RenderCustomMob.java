package ruo.cmplus.cm.beta.custommodelentity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

/**
 * 이 모델은 실험용 모델
 */
public class RenderCustomMob extends RenderLiving<EntityCustomMob> {
	private static HashMap<Block, ResourceLocation> gr = new HashMap<>();
	public RenderCustomMob(ModelBase modelbaseIn, float shadowsizeIn) {
		super(Minecraft.getMinecraft().getRenderManager(), modelbaseIn, shadowsizeIn);
	}

	protected static ResourceLocation loc;
	int count = 0;

	@Override
	public void doRender(EntityCustomMob entity, double x, double y, double z, float entityYaw, float partialTicks) {
		EntityCustomMob f = (EntityCustomMob) entity;
		if(f.getBlockList(f.getName()) == null){
			entity.setDead();
			return;
		}
		super.doRender(entity, x, y, z, entityYaw, partialTicks);

	}

	@Override
	protected void renderModel(EntityCustomMob entitylivingbaseIn, float limbSwing, float limbSwingAmount,
			float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
		super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch,
				scaleFactor);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCustomMob entity) {
		if (loc == null) {
			IBlockState block = Blocks.STONE.getDefaultState();
			BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
			loc = new ResourceLocation("minecraft:textures/" + blockrendererdispatcher.getBlockModelShapes()
					.getTexture(block).getIconName().replace("minecraft:", "") + ".png");
		}
		return loc;
	}

	protected ResourceLocation getBlockTexture(Block block) {
		if (!gr.containsKey(block)) {
			BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
			loc = new ResourceLocation("minecraft:textures/" + blockrendererdispatcher.getBlockModelShapes()
					.getTexture(block.getDefaultState()).getIconName().replace("minecraft:", "") + ".png");
			gr.put(block, loc);
		}
		return loc;
	}
}
