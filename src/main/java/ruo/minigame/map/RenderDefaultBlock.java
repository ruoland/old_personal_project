package ruo.minigame.map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderFallingBlock;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import ruo.map.lopre2.EntityBuildBlock;
import ruo.minigame.api.RenderAPI;

@SideOnly(Side.CLIENT)
public class RenderDefaultBlock<T extends EntityDefaultBlock> extends RenderLiving<EntityDefaultBlock> {
    private ResourceLocation DEFAULT_RES_LOC = new ResourceLocation("minecraft:textures/blocks/dirt.png");

    public ModelBiped modelBipedMain;
    protected float scale;
    private double x, y, z;

    public RenderDefaultBlock() {
        super(Minecraft.getMinecraft().getRenderManager(), new ModelDefaultBlock(), 0.1F);
    }

    @Override
    protected void renderModel(EntityDefaultBlock entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        for(EntityDefaultBlock.BlockData blockData : entitylivingbaseIn.getBlockList()){
            GlStateManager.pushMatrix();
            GlStateManager.translate(blockData.getX(), blockData.getY(), blockData.getZ());
            this.bindTexture(blockData.getTexture());
            this.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            GlStateManager.popMatrix();
        }
        this.bindTexture(entitylivingbaseIn.getTexture());
        this.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityDefaultBlock entity) {
        return entity.getTexture();
    }
}