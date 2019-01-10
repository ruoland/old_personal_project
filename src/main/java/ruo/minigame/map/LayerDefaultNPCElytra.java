package ruo.minigame.map;

import net.minecraft.client.model.ModelElytra;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ruo.minigame.api.RenderAPI;
import ruo.minigame.api.WorldAPI;

@SideOnly(Side.CLIENT)
public class LayerDefaultNPCElytra implements LayerRenderer<EntityDefaultNPC> {
    private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("textures/entity/elytra.png");
    private final RenderDefaultNPC renderNPC;
    private final ModelElytra modelElytra = new ModelElytra();

    public LayerDefaultNPCElytra(RenderDefaultNPC renderNPC) {
        this.renderNPC = renderNPC;
    }

    public void doRenderLayer(EntityDefaultNPC entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (WorldAPI.equalsItem(entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.CHEST), Items.ELYTRA) || entitylivingbaseIn.isElytra() || entitylivingbaseIn.isElytraFlying()) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();

            this.renderNPC.bindTexture(TEXTURE_ELYTRA);
            GlStateManager.pushMatrix();
            GlStateManager.rotate(entitylivingbaseIn.getRotateX(), 1, 0, 0);
            GlStateManager.rotate(entitylivingbaseIn.getRotateY(), 0, 1, 0);
            GlStateManager.rotate(entitylivingbaseIn.getRotateZ(), 0, 0, 1);
            GlStateManager.translate(entitylivingbaseIn.getTraX(), entitylivingbaseIn.getTraY(), entitylivingbaseIn.getTraZ());
            GlStateManager.scale(entitylivingbaseIn.getScaleX(), entitylivingbaseIn.getScaleY(), entitylivingbaseIn.getScaleZ());
            GlStateManager.translate(0.0F, 0.0F, 0.125F);
            this.modelElytra.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entitylivingbaseIn);
            this.modelElytra.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.popMatrix();
            GlStateManager.disableBlend();
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}