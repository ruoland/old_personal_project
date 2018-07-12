package ruo.minigame.map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelElytra;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ruo.minigame.api.WorldAPI;

@SideOnly(Side.CLIENT)
public class LayerDefaultNPCElytra implements LayerRenderer<EntityDefaultNPC>
{
    /** The basic Elytra texture. */
    private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("textures/entity/elytra.png");
    /** Instance of the player renderer. */
    private final RenderDefaultNPC renderPlayer;
    /** The model used by the Elytra. */
    private final ModelElytra modelElytra = new ModelElytra();

    public LayerDefaultNPCElytra(RenderDefaultNPC renderPlayerIn)
    {
        this.renderPlayer = renderPlayerIn;
    }

    public void doRenderLayer(EntityDefaultNPC entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if(WorldAPI.equalsItem(entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.CHEST), Items.ELYTRA) || entitylivingbaseIn.isElytra() || entitylivingbaseIn.isElytraFlying()){
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();

            if (Minecraft.getMinecraft().thePlayer.hasPlayerInfo() && Minecraft.getMinecraft().thePlayer.getLocationElytra() != null)
            {
                this.renderPlayer.bindTexture(Minecraft.getMinecraft().thePlayer.getLocationElytra());
            }
            else
            {
                this.renderPlayer.bindTexture(TEXTURE_ELYTRA);
            }
            GlStateManager.translate(0.0F, 0.0F, 0.125F);
            this.modelElytra.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entitylivingbaseIn);
            this.modelElytra.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.popMatrix();
        }
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}