package ruo.minigame.map;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerEye implements LayerRenderer<EntityDefaultNPC> {
    private final RenderDefaultNPC renderDefaultNPC;
    private final ModelTest modelTest = new ModelTest();
    private final ModelDefaultNPC modelNPC;

    public LayerEye(RenderDefaultNPC renderDefaultNPC) {
        this.renderDefaultNPC = renderDefaultNPC;
        this.modelNPC = (ModelDefaultNPC) renderDefaultNPC.getMainModel();
    }

    public void doRenderLayer(EntityDefaultNPC entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!entitylivingbaseIn.isInvisible() && entitylivingbaseIn.getModel() == TypeModel.NPC) {

            // setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entitylivingbaseIn);
            this.renderDefaultNPC.bindTexture(this.renderDefaultNPC.getEntityTexture(entitylivingbaseIn));
            float f = entitylivingbaseIn.prevRotationYaw + (entitylivingbaseIn.rotationYaw - entitylivingbaseIn.prevRotationYaw) * partialTicks - (entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks);
            float f1 = entitylivingbaseIn.prevRotationPitch + (entitylivingbaseIn.rotationPitch - entitylivingbaseIn.prevRotationPitch) * partialTicks;
            GlStateManager.pushMatrix();
            GlStateManager.color(100,100,100,1);
            GlStateManager.rotate(entitylivingbaseIn.rotationPitch, 1, 0, 0);
            GlStateManager.rotate(f, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(f1, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-f1, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-f, 0.0F, 1.0F, 0.0F);
            modelTest.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 0.0625F);
            GlStateManager.popMatrix();
        }
    }

    public boolean shouldCombineTextures() {
        return true;
    }
}