package minigameLib.map;

import minigameLib.api.RenderAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import map.lopre2.jump1.EntityBuildBlock;

import static minigameLib.map.TypeModel.*;

@SideOnly(Side.CLIENT)
public class RenderDefaultNPC<T extends EntityDefaultNPC> extends RenderLiving<EntityDefaultNPC> {
    private ResourceLocation DEFAULT_RES_LOC;

    protected float scale;

    public RenderDefaultNPC(ModelBase modelBipedIn, ResourceLocation r) {
        super(Minecraft.getMinecraft().getRenderManager(), modelBipedIn, 0);
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerDefaultNPCElytra(this));
        this.addLayer(new LayerEye(this));
        //this.addLayer(new LayerDefaultNPCEye(this));
        DEFAULT_RES_LOC = r;
    }

    /*
     */
    @Override
    protected void renderModel(EntityDefaultNPC npc, float limbSwing, float limbSwingAmount, float ageInTicks,
                               float netHeadYaw, float headPitch, float scaleFactor) {
        if (!npc.isInvisible()) {
            if (npc instanceof EntityBuildBlock) {
                EntityBuildBlock block = (EntityBuildBlock) npc;
                if (block.blockList.size() == 0) {
                    return;
                }
                GlStateManager.pushMatrix();
                modelRender(npc);
                RenderAPI.renderBlock(block.blockPosList, block.blockList, npc);
                GlStateManager.popMatrix();
                return;
            }

            if (npc.getModel() == BLOCK) {
                boolean flag = !npc.isInvisible() || this.renderOutlines;
                boolean flag1 = !flag && !npc.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer);

                if (flag || flag1) {
                    this.setRenderOutlines(false);
                    GlStateManager.pushMatrix();
                    if (!this.bindEntityTexture(npc)) {
                        GlStateManager.popMatrix();
                        return;
                    }

                    if (flag1) {
                        GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
                    }
                    GlStateManager.translate(0,1,0);

                    modelRender(npc);
                    RenderAPI.renderBlock(npc.getCurrentStack(), npc);

                    if (flag1) {
                        GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
                    }
                    GlStateManager.popMatrix();
                }
                return;
            }

            if (npc.getModel() == SHAPE_BLOCK) {
                GlStateManager.pushMatrix();
                GlStateManager.enableAlpha();
                GlStateManager.enableBlend();
                modelRender(npc);
                if(npc instanceof EntityDefaultBlock) {
                    EntityDefaultBlock entitylivingbaseIn = (EntityDefaultBlock) npc;
                    for (EntityDefaultBlock.BlockData blockData : entitylivingbaseIn.getBlockList()) {
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(blockData.getX(), blockData.getY(), blockData.getZ());
                        this.bindTexture(blockData.getTexture());
                        this.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
                        GlStateManager.popMatrix();
                    }
                }

                this.bindTexture(npc.getTexture());
                this.mainModel.render(npc, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                return;
            }

            GlStateManager.pushMatrix();
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            if (npc.isChild()) {
                GlStateManager.translate(0, 0.7, 0);
                GlStateManager.scale(0.7, 0.7, 0.6);
            } else {
                modelRender(npc);
            }
            super.renderModel(npc, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            GlStateManager.color(0, 0, 0, 1F);
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public void modelRender(EntityDefaultNPC npc){
        Rotations translation = npc.getTraXYZ();
        Rotations rotations = npc.getRotationXYZ();
        Rotations scale = npc.getScaleXYZ();
        Rotations rgb = npc.getRGBColor();
        GlStateManager.rotate(rotations.getX(), 1, 0, 0);
        GlStateManager.rotate(rotations.getY(), 0, 1, 0);
        GlStateManager.rotate(rotations.getZ(), 0, 0, 1);
        GlStateManager.translate(translation.getX(), translation.getY(), translation.getZ());
        GlStateManager.scale(scale.getX(), scale.getY(), scale.getZ());
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(rgb.getX(), rgb.getY(), rgb.getZ(), npc.getTransparency());
    }
    @Override
    public void doRender(EntityDefaultNPC entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.mainModel.isChild = entity.isChild();
        if (entity instanceof EntityDefaultBlock && entity.getModel() == BLOCK && !(mainModel instanceof minigameLib.map.ModelDefaultBlock)) {
            mainModel = new ModelDefaultBlock();
            DEFAULT_RES_LOC = entity.getTexture();
        }
        if (entity.getModel() == TypeModel.NPC && !(mainModel instanceof ModelDefaultNPC)) {
            mainModel = new ModelDefaultNPC();
            DEFAULT_RES_LOC = new ResourceLocation("textures/entity/steve.png");
        }
        if ((entity.getModel() == TypeModel.PIG_ZOMBIE && !(mainModel instanceof ModelZombie))) {
            mainModel = new ModelZombie();
            DEFAULT_RES_LOC = new ResourceLocation("textures/entity/zombie_pigman.png");
        }
        if ((entity.getModel() == TypeModel.ZOMBIE && !(mainModel instanceof ModelZombie))) {
            mainModel = new ModelZombie();
            DEFAULT_RES_LOC = new ResourceLocation("textures/entity/zombie/zombie.png");
        }
        if (entity.getModel() == TypeModel.CREEPER && !(mainModel instanceof ModelCreeper)) {
            mainModel = new ModelCreeper();
            DEFAULT_RES_LOC = new ResourceLocation("textures/entity/creeper/creeper.png");
        }
        if (entity.getModel() == TypeModel.ENDERMAN && !(mainModel instanceof ModelEnderman)) {
            mainModel = new ModelEnderman(0);
            DEFAULT_RES_LOC = new ResourceLocation("textures/entity/enderman/enderman.png");
        }
        if (entity.getModel() == TypeModel.WITCH && !(mainModel instanceof ModelWitch)) {
            mainModel = new ModelWitch(0);
            DEFAULT_RES_LOC = new ResourceLocation("textures/entity/witch.png");
        }
        if (entity.getModel() == TypeModel.SNOWMAN && !(mainModel instanceof ModelSnowMan)) {
            mainModel = new ModelSnowMan();
            DEFAULT_RES_LOC = new ResourceLocation("textures/entity/snowman.png");
        }
        if (entity.getModel() == TypeModel.SILVERFISH && !(mainModel instanceof ModelSilverfish)) {
            mainModel = new ModelSilverfish();
            DEFAULT_RES_LOC = new ResourceLocation("textures/entity/silverfish.png");
        }
        if (entity.getModel() == TypeModel.COW && !(mainModel instanceof ModelCow)) {
            mainModel = new ModelCow();
            DEFAULT_RES_LOC = new ResourceLocation("textures/entity/cow/cow.png");
        }
        if (entity.getModel() == TypeModel.CHICKEN && !(mainModel instanceof ModelChicken)) {
            mainModel = new ModelChicken();
            DEFAULT_RES_LOC = new ResourceLocation("textures/entity/chicken.png");
        }
        if (entity.getModel() == TypeModel.PIG && !(mainModel instanceof ModelPig)) {
            mainModel = new ModelPig();
            DEFAULT_RES_LOC = new ResourceLocation("textures/entity/pig/pig.png");
        }
        if (entity.getModel() == TypeModel.CART && !(mainModel instanceof ModelMinecart)) {
            mainModel = new ModelMinecart();
            DEFAULT_RES_LOC = new ResourceLocation("textures/entity/minecart.png");
        }
        if(entity.getModel() == BLOCK && !DEFAULT_RES_LOC.equals(RenderAPI.getBlockTexture(entity.getCurrentBlock()))){
            DEFAULT_RES_LOC = RenderAPI.getBlockTexture(entity.getCurrentBlock());
        }
        if(entity.getModel() == SHAPE_BLOCK && !DEFAULT_RES_LOC.equals(RenderAPI.getBlockTexture(entity.getCurrentBlock()))){
            DEFAULT_RES_LOC = RenderAPI.getBlockTexture(entity.getCurrentBlock());
            mainModel = new ModelDefaultBlock();
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected void rotateCorpse(EntityDefaultNPC entityLiving2, float p_77043_2_, float p_77043_3_, float partialTicks) {

        if(mainModel instanceof ModelBiped){
            ModelBiped modelBiped = (ModelBiped) mainModel;
            Rotations leftArm = entityLiving2.getRotations(EnumModel.LEFT_ARM);
            Rotations rightArm = entityLiving2.getRotations(EnumModel.RIGHT_ARM);
            Rotations leftLeg = entityLiving2.getRotations(EnumModel.LEFT_LEG);
            Rotations rightLeg = entityLiving2.getRotations(EnumModel.RIGHT_LEG);
            modelBiped.bipedLeftArm.rotateAngleX = leftArm.getX();
            modelBiped.bipedLeftArm.rotateAngleY = leftArm.getY();
            modelBiped.bipedLeftArm.rotateAngleZ = leftArm.getZ();
            modelBiped.bipedRightArm.rotateAngleX = rightArm.getX();
            modelBiped.bipedRightArm.rotateAngleY = rightArm.getY();
            modelBiped.bipedRightArm.rotateAngleZ = rightArm.getZ();
            modelBiped.bipedLeftLeg.rotateAngleX = leftLeg.getX();
            modelBiped.bipedLeftLeg.rotateAngleY = leftLeg.getY();
            modelBiped.bipedLeftLeg.rotateAngleZ = leftLeg.getZ();
            modelBiped.bipedRightLeg.rotateAngleX = rightLeg.getX();
            modelBiped.bipedRightLeg.rotateAngleY = rightLeg.getY();
            modelBiped.bipedRightLeg.rotateAngleZ = rightLeg.getZ();
        }
        if (entityLiving2.isSleep()) {
            GlStateManager.rotate(entityLiving2.getSleepRotate(), 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(90, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0, 0.3, 0);
        } else if (entityLiving2.isElytra() || entityLiving2.isElytraFlying()) {
            if (entityLiving2.isElytra())
                entityLiving2.setElytra(true);
            super.rotateCorpse(entityLiving2, p_77043_2_, p_77043_3_, partialTicks);
            float f = (float) entityLiving2.getTicksElytraFlying() + partialTicks;
            float f1 = MathHelper.clamp_float(f * f / 100.0F, 0.0F, 1.0F);
            GlStateManager.rotate(f1 * (-90.0F - entityLiving2.rotationPitch), 1.0F, 0.0F, 0.0F);
            Vec3d vec3d = entityLiving2.getLook(partialTicks);
            double d0 = entityLiving2.motionX * entityLiving2.motionX + entityLiving2.motionZ * entityLiving2.motionZ;
            double d1 = vec3d.xCoord * vec3d.xCoord + vec3d.zCoord * vec3d.zCoord;
            if (d0 > 0.0D && d1 > 0.0D) {
                double d2 = (entityLiving2.motionX * vec3d.xCoord + entityLiving2.motionZ * vec3d.zCoord)
                        / (Math.sqrt(d0) * Math.sqrt(d1));
                double d3 = entityLiving2.motionX * vec3d.zCoord - entityLiving2.motionZ * vec3d.xCoord;
                GlStateManager.rotate((float) (Math.signum(d3) * Math.acos(d2)) * 180.0F / (float) Math.PI, 0.0F, 1.0F, 0.0F);
            }
        } else {
            super.rotateCorpse(entityLiving2, p_77043_2_, p_77043_3_, partialTicks);
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless
     * you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityDefaultNPC entity) {
        EntityDefaultNPC entityLiving = (EntityDefaultNPC) entity;
        if(entity.getModel() == CREEPER){

        }
        if ((entity.getModel() == TypeModel.ZOMBIE) || entity.getModel() == CREEPER || entity.getModel() == ENDERMAN) {
            entityLiving.setTexture("minecraft:");
        }
        if (!entityLiving.getTexture().toString().equalsIgnoreCase("minecraft:"))
            return entityLiving.getTexture();
        else
            return DEFAULT_RES_LOC;
    }

    public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.0F, 0.1875F, 0.0F);
    }
}