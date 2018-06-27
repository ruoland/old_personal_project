package ruo.map.platformer.chapter1;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

/**
 * 이 모델은 실험용 모델
 */
public class RenderMagmaFalling extends RenderLiving{

	public RenderMagmaFalling(ModelBase modelbaseIn, float shadowsizeIn) {
		super(Minecraft.getMinecraft().getRenderManager(), modelbaseIn, shadowsizeIn);
	}

	protected static ResourceLocation loc;
	int count =0;
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		if(loc == null){
			IBlockState block = Blocks.STONE.getDefaultState();
			BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
			loc = new ResourceLocation("minecraft:textures/"+blockrendererdispatcher.getBlockModelShapes().getTexture(block).getIconName().replace("minecraft:", "")+".png");
		}
		return loc;
	}

}

