package ruo.minigame.api;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.map.RenderDefaultNPC;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL;

public class RenderAPI {

    private static Minecraft mc = Minecraft.getMinecraft();

    public static ResourceLocation getDynamicTexture(String name, File file) {
        try {
            return Minecraft.getMinecraft().renderEngine.getDynamicTextureLocation(name, new DynamicTexture(ImageIO.read(file)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void renderBlock(EntityLivingBase l, ItemStack item) {
        IBakedModel ibakedmodel = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(item, l.worldObj,
                l);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false,
                false);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.7F);
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        GlStateManager.pushMatrix();
        GlStateManager.rotate(90, 1, 0, 0);
        GlStateManager.rotate(90, 0, 1, 0);
        GlStateManager.rotate(90, 0, 0, 1);
        Minecraft.getMinecraft().getRenderItem().renderItem(item, ibakedmodel);
        GlStateManager.cullFace(GlStateManager.CullFace.BACK);
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
    }

    public float x = 0, y = 0.5F, z = 0;

    private void a() {
        if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
            x += 0.01;
            System.out.println("X" + x);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
            y += 0.01;
            System.out.println("Y" + y);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
            z += 0.01;
            System.out.println("Z" + z);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_V)) {
            x -= 0.01;
            System.out.println("X" + x);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
            y -= 0.01;
            System.out.println("Y" + y);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_N)) {
            z -= 0.01;
            System.out.println("Z" + z);
        }

    }

    public static void drawString(String p_73732_2_, int p_73732_3_, int p_73732_4_, int p_73732_5_) {
        mc.fontRendererObj.drawString(p_73732_2_, p_73732_3_, p_73732_4_, p_73732_5_);
    }

    public static void drawModel(ResourceLocation loc, ModelBase modelbase, int x, int y, int z, float rotate,
                                 float size, float alpha) {
        glPushMatrix();
        RenderHelper.enableStandardItemLighting();
        glTranslatef(x, y, z);
        glRotatef(rotate, 0, 1, 0);
        GL11.glScalef(size, size, size);
        glPushMatrix();
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        GL11.glColor4f(255, 255, 255, alpha);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_CULL_FACE);
        glEnable(GL_RESCALE_NORMAL);
        mc.renderEngine.bindTexture(loc);
        for (int i = 0; i < modelbase.boxList.size(); i++) {
            ((ModelRenderer) modelbase.boxList.get(i)).render(4.0F);
        }
        for (int i = 0; i < modelbase.boxList.size(); i++) {
            ((ModelRenderer) modelbase.boxList.get(i)).render(4.0F);
        }
        glDisable(GL_RESCALE_NORMAL);
        glEnable(GL_CULL_FACE);
        glDisable(GL_BLEND);
        glDisable(GL_DEPTH_TEST);
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glPopMatrix();
    }

    private static HashMap<String, ResourceLocation> hash = new HashMap<>();
    private static HashMap<String, DrawTexture> drawTextureMap = new HashMap<>();

    public static DrawTexture drawTexture(String id, String texture, double x, double y, double width, double height) {
        if (!drawTextureMap.containsKey(id)) {
            drawTextureMap.put(id, new DrawTexture(texture, x, y, width, height));
        }
        drawTextureMap.get(id).x = x;
        drawTextureMap.get(id).y = y;
        drawTextureMap.get(id).width = width;
        drawTextureMap.get(id).height = height;

        drawTextureMap.get(id).render();
        return drawTextureMap.get(id);

    }

    public static void drawTexture(String texture, double x, double y, double width, double height) {
        drawTexture(texture, 1, 1, 1, 1, x, y, 1000, width, height, true);
    }

    public static void drawTexture(String texture, float alpha, double x, double y, double width, double height) {
        drawTexture(texture, 1, 1, 1, alpha, x, y, 1000, width, height, true);
    }

    public static void drawTexture(String texture, float alpha, double x, double y, double width, double height, boolean push) {
        drawTexture(texture, 1, 1, 1, alpha, x, y, 1000, width, height, push);
    }

    public static void drawTexture(ResourceLocation texture, float alpha, double x, double y, double width,
                                   double height) {
        drawTexture(texture.toString(), 1, 1, 1, alpha, x, y, 1000, width, height, true);
    }
    public static void drawTextureZ(String texture, double x, double y, double z, double width,
                                   double height) {
        drawTexture(texture, 1, 1, 1, 1, x, y, z, width, height, true);
    }
    public static void drawTexture(String texture, float red, float green, float blue, float alpha, double x, double y, double z, double width, double height,
                                   boolean push) {
        if (!hash.containsKey(texture)) {
            hash.put(texture, new ResourceLocation(texture));
        }
        if (push)
            GlStateManager.pushMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(hash.get(texture));
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(red, green, blue, alpha);
        GlStateManager.translate(0, 0, z);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos((double) x, (double) y + height, 0).tex(0, 1).endVertex();
        vertexbuffer.pos(x + width, y + height, 0).tex(1, 1).endVertex();
        vertexbuffer.pos(x + width, y, 0).tex(1, 0).endVertex();
        vertexbuffer.pos(x, y, 0).tex(0, 0).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        if (push)
            GlStateManager.popMatrix();
    }

    public static class DrawTexture {
        private ResourceLocation location;
        private double x, y, z, width, height;
        private float red, green, blue, alpha;

        public DrawTexture(String texture, double x, double y, double width, double height) {
            this.location = new ResourceLocation(texture);
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public DrawTexture setAlpha(float alpha) {
            this.alpha = alpha;
            return this;
        }

        public DrawTexture setRGB(float red, float green, float blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            return this;
        }

        public DrawTexture setZ(double z) {
            this.z = z;
            return this;
        }

        public void render() {
            Minecraft.getMinecraft().renderEngine.bindTexture(location);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(red, green, blue, alpha);
            GlStateManager.translate(0,0,z);
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer vertexbuffer = tessellator.getBuffer();
            vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
            vertexbuffer.pos((double) x, (double) y + height, 0).tex(0, 1).endVertex();
            vertexbuffer.pos(x + width, y + height, 0).tex(1, 1).endVertex();
            vertexbuffer.pos(x + width, y, 0).tex(1, 0).endVertex();
            vertexbuffer.pos(x, y, 0).tex(0, 0).endVertex();
            tessellator.draw();
            GlStateManager.disableBlend();
        }
    }

    public static void renderBlock2(double x, double y, double z, RenderDefaultNPC defaultNPC, EntityDefaultNPC entity, Block block) {
        World world = entity.worldObj;
        defaultNPC.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.BLOCK);
        IBlockState defaultState = block.getDefaultState();
        BlockPos blockpos = new BlockPos(entity.posX, entity.getEntityBoundingBox().maxY, entity.posZ);
        GlStateManager.translate((float) (x - (double) blockpos.getX() - 0.5D), (float) (y - (double) blockpos.getY()), (float) (z - (double) blockpos.getZ() - 0.5D));
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        blockrendererdispatcher.getBlockModelRenderer().renderModel(world, blockrendererdispatcher.getModelForState(defaultState), defaultState, blockpos, vertexbuffer, false, 0);
        tessellator.draw();

        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }
    public static void renderBlock(ArrayList<BlockPos> blockPosList, ArrayList<ItemStack> itemStackList, EntityLivingBase entitylivingbaseIn) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.pushMatrix();
        for (int i = 0; i < blockPosList.size(); i++) {
            BlockPos pos = blockPosList.get(i);
            if (itemStackList.get(i) == null) {
                continue;
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ());
            renderItem(itemStackList.get(i), Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(itemStackList.get(i), entitylivingbaseIn.worldObj, entitylivingbaseIn));
            //RenderAPI.renderBlock2(x, y, z, this, npc, npc.getCurrentBlock());
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();

    }
    public static void renderBlock(ItemStack itemstack, EntityLivingBase entitylivingbaseIn) {
        IBakedModel ibakedmodel = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(itemstack, entitylivingbaseIn.worldObj, entitylivingbaseIn);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.pushMatrix();
        renderItem(itemstack, ibakedmodel);
        GlStateManager.popMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();

    }

    public static void render(Block block, double x, double y, double z) {
        {
            IBlockState iblockstate = block.getDefaultState();
            if (iblockstate.getRenderType() == EnumBlockRenderType.MODEL) {
                World world = WorldAPI.getWorld();

                if (iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE) {
                    Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                    GlStateManager.pushMatrix();
                    GlStateManager.disableLighting();
                    Tessellator tessellator = Tessellator.getInstance();
                    VertexBuffer vertexbuffer = tessellator.getBuffer();
                    vertexbuffer.begin(7, DefaultVertexFormats.BLOCK);
                    GlStateManager.translate(x, y, z);
                    BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft()
                            .getBlockRendererDispatcher();
                    blockrendererdispatcher.getBlockModelRenderer().renderModel(world,
                            blockrendererdispatcher.getModelForState(iblockstate), iblockstate, new BlockPos(x, y, z),
                            vertexbuffer, false, 0);
                    tessellator.draw();

                    GlStateManager.enableLighting();
                    GlStateManager.popMatrix();
                }
            }
        }
    }

    public static ResourceLocation getBlockTexture(Block block) {
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        return (new ResourceLocation("minecraft:textures/" + blockrendererdispatcher.getBlockModelShapes()
                .getTexture(block.getDefaultState()).getIconName().replace("minecraft:", "") + ".png"));
    }

    public static void drawTexturedModalRect(float alpha, float zLevel, int x, int y, int textureX, int textureY,
                                             int width, int height) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos((double) (x + 0), (double) (y + height), (double) 1)
                .tex((double) ((float) (textureX + 0) * 0.00390625F),
                        (double) ((float) (textureY + height) * 0.00390625F))
                .endVertex();
        vertexbuffer.pos((double) (x + width), (double) (y + height), (double) 1)
                .tex((double) ((float) (textureX + width) * 0.00390625F),
                        (double) ((float) (textureY + height) * 0.00390625F))
                .endVertex();
        vertexbuffer.pos((double) (x + width), (double) (y + 0), (double) 1)
                .tex((double) ((float) (textureX + width) * 0.00390625F),
                        (double) ((float) (textureY + 0) * 0.00390625F))
                .endVertex();
        vertexbuffer.pos((double) (x + 0), (double) (y + 0), (double) 1)
                .tex((double) ((float) (textureX + 0) * 0.00390625F), (double) ((float) (textureY + 0) * 0.00390625F))
                .endVertex();
        tessellator.draw();
    }

    private static void renderItem(ItemStack stack, IBakedModel model) {
        if (stack != null) {
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);
            if (model.isBuiltInRenderer()) {
                GlStateManager.enableRescaleNormal();
                TileEntityItemStackRenderer.instance.renderByItem(stack);
            } else {
                renderModel(model, -1, stack);
            }
        }
    }

    private static void renderModel(IBakedModel model, int color, @Nullable ItemStack stack) {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.ITEM);
        for (EnumFacing enumfacing : EnumFacing.values()) {
            renderQuads(vertexbuffer, model.getQuads(null, enumfacing, 0L), color, stack);
        }
        renderQuads(vertexbuffer, model.getQuads(null, null, 0L), color, stack);
        tessellator.draw();
    }

    private static void renderQuads(VertexBuffer renderer, List<BakedQuad> quads, int color, @Nullable ItemStack stack) {
        boolean flag = color == -1 && stack != null;
        int i = 0;

        for (int j = quads.size(); i < j; ++i) {
            BakedQuad bakedquad = quads.get(i);
            int k = color;

            if (flag && bakedquad.hasTintIndex()) {
                k = k | -16777216;
            }

            net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);
        }
    }

}
