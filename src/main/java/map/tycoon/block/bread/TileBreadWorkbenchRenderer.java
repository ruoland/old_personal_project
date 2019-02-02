package map.tycoon.block.bread;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class TileBreadWorkbenchRenderer<T> extends TileEntitySpecialRenderer<TileBreadWorkbench> {
    private static final EntityItem wheatItem = new EntityItem(Minecraft.getMinecraft().theWorld, 0, 0, 0, new ItemStack(Items.WHEAT));
    private static final EntityItem wheatDust = new EntityItem(Minecraft.getMinecraft().theWorld, 0, 0, 0, new ItemStack(Items.WHEAT));
    private static final EntityItem wheatComplete = new EntityItem(Minecraft.getMinecraft().theWorld, 0, 0, 0, new ItemStack(Items.WHEAT));

    @Override
    public void renderTileEntityAt(TileBreadWorkbench te, double x, double y, double z, float partialTicks, int destroyStage) {
        super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5, 0, 0.5);
        if (te.getProgress() >= 1 && te.getProgress() < 5)
            Minecraft.getMinecraft().getRenderManager().doRenderEntity(wheatItem, x, y, z, 0, 0, false);
        else if (te.getProgress() > 5)
            Minecraft.getMinecraft().getRenderManager().doRenderEntity(wheatDust, x, y, z, 0, 0, false);
        else if(te.getProgress() > 10)
            Minecraft.getMinecraft().getRenderManager().doRenderEntity(wheatComplete, x, y, z, 0, 0, false);
        GlStateManager.popMatrix();
    }

    public FontRenderer getFontRendererFromRenderManager() {
        return Minecraft.getMinecraft().getRenderManager().getFontRenderer();
    }
}
