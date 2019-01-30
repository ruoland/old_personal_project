package rmap.lot.switch2;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;

public class TileSwitchRenderer<T> extends TileEntitySpecialRenderer<TileSwitch> {
	private static final ResourceLocation ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");
	ModelSwitch clock = new ModelSwitch();
	ModelZombie zombie = new ModelZombie();

	@Override
	public void renderTileEntityAt(TileSwitch te, double x, double y, double z, float partialTicks, int destroyStage) {
		if(Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null && Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem() == Items.NAME_TAG)
			renderLivingLabel("명령어:"+te.getCommand(),x+0.5,y,z+0.5, 50);
	}
	protected void renderLivingLabel(String str, double x, double y, double z, int maxDistance)
	{
		double d0 = Minecraft.getMinecraft().getRenderManager().renderViewEntity.getDistance(x, y, z);
		if (d0 <= (double)(maxDistance * maxDistance))
		{
			float f = Minecraft.getMinecraft().getRenderManager().playerViewY;
			float f1 = Minecraft.getMinecraft().getRenderManager().playerViewX;
			float f2 = 1 + 0.5F - (0.0F);
			int i = "deadmau5".equals(str) ? -10 : 0;
			EntityRenderer.drawNameplate(this.getFontRendererFromRenderManager(), str, (float)x, (float)y + f2, (float)z, i, f, f1, false, false);
		}
	}
	public FontRenderer getFontRendererFromRenderManager()
	{
		return Minecraft.getMinecraft().getRenderManager().getFontRenderer();
	}
	@Override
	public void renderTileEntityFast(TileSwitch te, double x, double y, double z, float partialTicks, int destroyStage, VertexBuffer buffer) {
		super.renderTileEntityFast(te, x, y, z, partialTicks, destroyStage, buffer);
	}
}
