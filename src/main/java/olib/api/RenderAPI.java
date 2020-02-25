package olib.api;

import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.client.renderer.entity.RenderFallingBlock;
import net.minecraft.client.renderer.entity.layers.LayerHeldBlock;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.init.Blocks;
import olib.map.EntityDefaultNPC;
import olib.map.ModelDefaultNPC;
import olib.map.RenderDefaultNPC;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL;

public class RenderAPI {
    private static final ResourceLocation resourceLocation = new ResourceLocation("textures/entity/steve.png");

    public static void registerTileEntity(Block block, Class<? extends TileEntity> tileEntity, TileEntitySpecialRenderer<? super TileEntity> renderer) {
        GameRegistry.register(block);
        GameRegistry.register(new ItemBlock(block), block.getRegistryName());
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0,
                new ModelResourceLocation(block.getRegistryName(), "inventory"));
        GameRegistry.registerTileEntity(tileEntity, block.getRegistryName().toString());
        ClientRegistry.bindTileEntitySpecialRenderer(tileEntity, renderer);
    }

    public static RenderManager getRenderMananger() {
        return Minecraft.getMinecraft().getRenderManager();
    }

    public static void registerRender(Class entity, Render<? extends Entity> r) {
        RenderingRegistry.registerEntityRenderingHandler(entity, r);
    }

    public static void registerRender(Class entity) {
        registerRender(entity, new RenderDefaultNPC(new ModelDefaultNPC(), resourceLocation));

    }

    public static void registerRender(Render<? extends Entity> r, Class entity) {
        registerRender(entity, r);
    }

    private static Minecraft mc = Minecraft.getMinecraft();

    public static ModelBase getEntityModel(Entity entity) {
        Render r = Minecraft.getMinecraft().getRenderManager().getEntityRenderObject(entity);
        Field f;
        try {
            f = r.getClass().getDeclaredField("mainModel");
            f.setAccessible(true);

            return (ModelBase) f.get(r);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResourceLocation getDynamicTexture(String name, File file) {
        try {
            return Minecraft.getMinecraft().renderEngine.getDynamicTextureLocation(name, new DynamicTexture(ImageIO.read(file)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void renderItem(ItemStack stack, int x, int y, boolean stackSize) {
        RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
        itemRender.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = null;
        if (stack != null) font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = Minecraft.getMinecraft().fontRendererObj;
        if (stack != null) {
            RenderHelper.enableGUIStandardItemLighting();
            itemRender.renderItemAndEffectIntoGUI(stack, x, y);
            if (stackSize)
                itemRender.renderItemOverlayIntoGUI(font, stack, x, y, null);
            RenderHelper.disableStandardItemLighting();
        }
        itemRender.zLevel = 0.0F;
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

    private static HashMap<String, DrawTexture> drawTextureMap = new HashMap<>();

    public static void drawTexture(DrawTexture drawTexture) {
        drawTexture(drawTexture.getTexture(), drawTexture.getRed(), drawTexture.getGreen(), drawTexture.getBlue(), drawTexture.getAlpha(), drawTexture.getX(), drawTexture.getY(), drawTexture.getZ(), drawTexture.getWidth(), drawTexture.getHeight(), true);
    }

    public static void drawTexture(ResourceLocation texture, float red, float green, float blue, float alpha, double x, double y, double z, double width, double height,
                                   boolean push) {
        if (push)
            GlStateManager.pushMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
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

    public static void renderBlockA(ArrayList<BlockPos> blockPosList, ArrayList<IBlockState> blockList, EntityLivingBase entitylivingbaseIn) {
        for (int i = 0; i < blockPosList.size(); i++) {
            BlockPos pos = blockPosList.get(i);

            GlStateManager.pushMatrix();

            GlStateManager.rotate(180, 0, 0, 1);
            GlStateManager.translate(pos.getX(), -pos.getY(), pos.getZ());
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            GlStateManager.enableRescaleNormal();
            int i2 = entitylivingbaseIn.getBrightnessForRender(0);
            int j = i2 % 65536;
            int k = i2 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
            if (blockList.get(i).getBlock() instanceof BlockGlass || blockList.get(i).getBlock() instanceof BlockStainedGlass) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
            } else
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            blockrendererdispatcher.renderBlockBrightness(blockList.get(i), 1.0F);
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
        }
    }

    /**
     * 대량의 블럭을 렌더링함
     */
    public static void renderBlock(ArrayList<BlockPos> blockPosList, ArrayList<IBlockState> itemStackList, EntityLivingBase entitylivingbaseIn) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.pushMatrix();
        for (int i = 0; i < blockPosList.size(); i++) {
            BlockPos pos = blockPosList.get(i);
            if (itemStackList.get(i) == null) {
                continue;
            }
            ItemStack itemStack = new ItemStack(itemStackList.get(i).getBlock());
            GlStateManager.pushMatrix();
            GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ());
            renderItem(itemStack, Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(itemStack, entitylivingbaseIn.worldObj, entitylivingbaseIn));
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
