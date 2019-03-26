package map.lot.dungeon.arrow;

import oneline.api.RenderAPI;
import oneline.api.WorldAPI;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class TileArrowSpawnRenderer<T> extends TileEntitySpecialRenderer<TileArrowSpawn> {
	ItemStack stack = new ItemStack(Blocks.STONE);
	@Override
	public void renderTileEntityAt(TileArrowSpawn te, double x, double y, double z, float partialTicks, int destroyStage) {
		super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
		GlStateManager.pushMatrix();
		RenderAPI.renderBlock(stack, WorldAPI.getPlayer());
		GlStateManager.popMatrix();
	}
}
