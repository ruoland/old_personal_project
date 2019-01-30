package com.ruoland.customclient;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;

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

    public static void drawTextureZ(ResourceLocation texture, float alpha, double x, double y, double z, double width,
                                    double height) {
        drawTexture(texture.toString(), 1, 1, 1, alpha, x, y, z, width, height, true);
    }

    public static void drawTextureZ(ResourceLocation texture, double x, double y, double z, double width,
                                    double height) {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
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



}
