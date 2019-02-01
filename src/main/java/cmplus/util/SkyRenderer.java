package cmplus.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;

public class SkyRenderer extends IRenderHandler {
	public ResourceLocation MOON_PHASES_TEXTURES = new ResourceLocation("textures/environment/moon_phases.png");
	public ResourceLocation SUN_TEXTURES = new ResourceLocation("textures/environment/sun.png");
	public ResourceLocation CLOUDS_TEXTURES = new ResourceLocation("textures/environment/clouds.png");
	public ResourceLocation END_SKY_TEXTURES = new ResourceLocation("textures/environment/end_sky.png");

	private net.minecraft.client.renderer.vertex.VertexBuffer starVBO;
	private net.minecraft.client.renderer.vertex.VertexBuffer skyVBO;
	private net.minecraft.client.renderer.vertex.VertexBuffer sky2VBO;
	/** The star GL Call list */
	private int starGLCallList = -1;
	/** OpenGL sky list */
	private int glSkyList = -1;
	/** OpenGL sky list 2 */
	private int glSkyList2 = -1;
	public Minecraft mc;
	private VertexFormat vertexBufferFormat = null;

	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		
		if (this.mc == null) {
			this.mc = mc;
        	if(sky2VBO == null)
			try{
				Field field = ReflectionHelper.findField(RenderGlobal.class, "vertexBufferFormat");
				vertexBufferFormat = (VertexFormat) field.get(mc.renderGlobal);
				
				Method method =ReflectionHelper.findMethod(RenderGlobal.class, mc.renderGlobal, new String[]{"generateStars"});
				method.invoke(mc.renderGlobal);
				
				Method method2 =ReflectionHelper.findMethod(RenderGlobal.class, mc.renderGlobal, new String[]{"generateSky"});
				method2.invoke(mc.renderGlobal);
				
				Method method3 =ReflectionHelper.findMethod(RenderGlobal.class, mc.renderGlobal, new String[]{"generateSky2"});
				method3.invoke(mc.renderGlobal);
				
				Field field2 = ReflectionHelper.findField(RenderGlobal.class, "starVBO");
				starVBO = (net.minecraft.client.renderer.vertex.VertexBuffer) field2.get(mc.renderGlobal);
				Field field3 = ReflectionHelper.findField(RenderGlobal.class, "skyVBO");
				skyVBO = (net.minecraft.client.renderer.vertex.VertexBuffer) field3.get(mc.renderGlobal);
				Field field4 = ReflectionHelper.findField(RenderGlobal.class, "sky2VBO");
				sky2VBO = (net.minecraft.client.renderer.vertex.VertexBuffer) field4.get(mc.renderGlobal);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		if (world.provider.getDimensionType().getId() == 1) {
			this.renderSkyEnd();
		} else if (world.provider.isSurfaceWorld()) {
			GlStateManager.disableTexture2D();
			Vec3d vec3d = world.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
			float f = (float) vec3d.xCoord;
			float f1 = (float) vec3d.yCoord;
			float f2 = (float) vec3d.zCoord;
			int pass = 2;
			if (pass != 2) {
				float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
				float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
				float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
				f = f3;
				f1 = f4;
				f2 = f5;
			}

			GlStateManager.color(f, f1, f2);
			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer vertexbuffer = tessellator.getBuffer();
			GlStateManager.depthMask(false);
			GlStateManager.enableFog();
			GlStateManager.color(f, f1, f2);

			if (OpenGlHelper.useVbo()) {
				if(skyVBO == null) {
					try{
						Field field = ReflectionHelper.findField(RenderGlobal.class, "vertexBufferFormat");
						vertexBufferFormat = (VertexFormat) field.get(mc.renderGlobal);
						
						Method method =ReflectionHelper.findMethod(RenderGlobal.class, mc.renderGlobal, new String[]{"generateStars"});
						method.invoke(mc.renderGlobal);
						
						Method method2 =ReflectionHelper.findMethod(RenderGlobal.class, mc.renderGlobal, new String[]{"generateSky"});
						method2.invoke(mc.renderGlobal);
						
						Method method3 =ReflectionHelper.findMethod(RenderGlobal.class, mc.renderGlobal, new String[]{"generateSky2"});
						method3.invoke(mc.renderGlobal);
						
						Field field2 = ReflectionHelper.findField(RenderGlobal.class, "starVBO");
						starVBO = (net.minecraft.client.renderer.vertex.VertexBuffer) field2.get(mc.renderGlobal);
						Field field3 = ReflectionHelper.findField(RenderGlobal.class, "skyVBO");
						skyVBO = (net.minecraft.client.renderer.vertex.VertexBuffer) field3.get(mc.renderGlobal);
						Field field4 = ReflectionHelper.findField(RenderGlobal.class, "sky2VBO");
						sky2VBO = (net.minecraft.client.renderer.vertex.VertexBuffer) field4.get(mc.renderGlobal);
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
				this.skyVBO.bindBuffer();
				GlStateManager.glEnableClientState(32884);
				GlStateManager.glVertexPointer(3, 5126, 12, 0);
				this.skyVBO.drawArrays(7);
				this.skyVBO.unbindBuffer();
				GlStateManager.glDisableClientState(32884);
			} else {
				GlStateManager.callList(this.glSkyList);
			}

			GlStateManager.disableFog();
			GlStateManager.disableAlpha();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
					GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
					GlStateManager.DestFactor.ZERO);
			RenderHelper.disableStandardItemLighting();
			float[] afloat = world.provider
					.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);

			if (afloat != null) {
				GlStateManager.disableTexture2D();
				GlStateManager.shadeModel(7425);
				GlStateManager.pushMatrix();
				GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(
						MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F,
						0.0F, 0.0F, 1.0F);
				GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
				float f6 = afloat[0];
				float f7 = afloat[1];
				float f8 = afloat[2];

				if (pass != 2) {
					float f9 = (f6 * 30.0F + f7 * 59.0F + f8 * 11.0F) / 100.0F;
					float f10 = (f6 * 30.0F + f7 * 70.0F) / 100.0F;
					float f11 = (f6 * 30.0F + f8 * 70.0F) / 100.0F;
					f6 = f9;
					f7 = f10;
					f8 = f11;
				}

				vertexbuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
				vertexbuffer.pos(0.0D, 100.0D, 0.0D).color(f6, f7, f8, afloat[3]).endVertex();
				int j = 16;

				for (int l = 0; l <= 16; ++l) {
					float f21 = (float) l * ((float) Math.PI * 2F) / 16.0F;
					float f12 = MathHelper.sin(f21);
					float f13 = MathHelper.cos(f21);
					vertexbuffer
							.pos((double) (f12 * 120.0F), (double) (f13 * 120.0F), (double) (-f13 * 40.0F * afloat[3]))
							.color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
				}

				tessellator.draw();
				GlStateManager.popMatrix();
				GlStateManager.shadeModel(7424);
			}

			GlStateManager.enableTexture2D();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE,
					GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.pushMatrix();
			float f16 = 1.0F - world.getRainStrength(partialTicks);
			GlStateManager.color(1.0F, 1.0F, 1.0F, f16);
			GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
			float f17 = 30.0F;
			if (SUN_TEXTURES != null) {
				mc.renderEngine.bindTexture(SUN_TEXTURES);
				vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
				vertexbuffer.pos((double) (-f17), 100.0D, (double) (-f17)).tex(0.0D, 0.0D).endVertex();
				vertexbuffer.pos((double) f17, 100.0D, (double) (-f17)).tex(1.0D, 0.0D).endVertex();
				vertexbuffer.pos((double) f17, 100.0D, (double) f17).tex(1.0D, 1.0D).endVertex();
				vertexbuffer.pos((double) (-f17), 100.0D, (double) f17).tex(0.0D, 1.0D).endVertex();
				tessellator.draw();
			}
			f17 = 20.0F;
			if (MOON_PHASES_TEXTURES != null) {
				mc.renderEngine.bindTexture(MOON_PHASES_TEXTURES);
				int i = world.getMoonPhase();
				int k = i % 4;
				int i1 = i / 4 % 2;
				float f22 = (float) (k + 0) / 4.0F;
				float f23 = (float) (i1 + 0) / 2.0F;
				float f24 = (float) (k + 1) / 4.0F;
				float f14 = (float) (i1 + 1) / 2.0F;
				vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
				vertexbuffer.pos((double) (-f17), -100.0D, (double) f17).tex((double) f24, (double) f14).endVertex();
				vertexbuffer.pos((double) f17, -100.0D, (double) f17).tex((double) f22, (double) f14).endVertex();
				vertexbuffer.pos((double) f17, -100.0D, (double) (-f17)).tex((double) f22, (double) f23).endVertex();
				vertexbuffer.pos((double) (-f17), -100.0D, (double) (-f17)).tex((double) f24, (double) f23).endVertex();
				tessellator.draw();
			}
			GlStateManager.disableTexture2D();
			float f15 = world.getStarBrightness(partialTicks) * f16;

			if (f15 > 0.0F) {
				GlStateManager.color(f15, f15, f15, f15);

				if (OpenGlHelper.useVbo()) {
		        	if(starVBO == null)

					try{
						Field field = ReflectionHelper.findField(RenderGlobal.class, "vertexBufferFormat");
						vertexBufferFormat = (VertexFormat) field.get(mc.renderGlobal);
						
						Method method =ReflectionHelper.findMethod(RenderGlobal.class, mc.renderGlobal, new String[]{"generateStars"});
						method.invoke(mc.renderGlobal);
						
						Method method2 =ReflectionHelper.findMethod(RenderGlobal.class, mc.renderGlobal, new String[]{"generateSky"});
						method2.invoke(mc.renderGlobal);
						
						Method method3 =ReflectionHelper.findMethod(RenderGlobal.class, mc.renderGlobal, new String[]{"generateSky2"});
						method3.invoke(mc.renderGlobal);
						
						Field field2 = ReflectionHelper.findField(RenderGlobal.class, "starVBO");
						starVBO = (net.minecraft.client.renderer.vertex.VertexBuffer) field2.get(mc.renderGlobal);
						Field field3 = ReflectionHelper.findField(RenderGlobal.class, "skyVBO");
						skyVBO = (net.minecraft.client.renderer.vertex.VertexBuffer) field3.get(mc.renderGlobal);
						Field field4 = ReflectionHelper.findField(RenderGlobal.class, "sky2VBO");
						sky2VBO = (net.minecraft.client.renderer.vertex.VertexBuffer) field4.get(mc.renderGlobal);
					}
					catch(Exception e){
						e.printStackTrace();
					}
					this.starVBO.bindBuffer();
					GlStateManager.glEnableClientState(32884);
					GlStateManager.glVertexPointer(3, 5126, 12, 0);
					this.starVBO.drawArrays(7);
					this.starVBO.unbindBuffer();
					GlStateManager.glDisableClientState(32884);
				} else {
					GlStateManager.callList(this.starGLCallList);
				}
			}

			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableBlend();
			GlStateManager.enableAlpha();
			GlStateManager.enableFog();
			GlStateManager.popMatrix();
			GlStateManager.disableTexture2D();
			GlStateManager.color(0.0F, 0.0F, 0.0F);
			double d0 = this.mc.thePlayer.getPositionEyes(partialTicks).yCoord - world.getHorizon();

			if (d0 < 0.0D) {
				GlStateManager.pushMatrix();
				GlStateManager.translate(0.0F, 12.0F, 0.0F);

				if (OpenGlHelper.useVbo()) {
					if(sky2VBO == null)
					try{
						Field field = ReflectionHelper.findField(RenderGlobal.class, "vertexBufferFormat");
						vertexBufferFormat = (VertexFormat) field.get(mc.renderGlobal);
						
						Method method =ReflectionHelper.findMethod(RenderGlobal.class, mc.renderGlobal, new String[]{"generateStars"});
						method.invoke(mc.renderGlobal);
						
						Method method2 =ReflectionHelper.findMethod(RenderGlobal.class, mc.renderGlobal, new String[]{"generateSky"});
						method2.invoke(mc.renderGlobal);
						
						Method method3 =ReflectionHelper.findMethod(RenderGlobal.class, mc.renderGlobal, new String[]{"generateSky2"});
						method3.invoke(mc.renderGlobal);
						
						Field field2 = ReflectionHelper.findField(RenderGlobal.class, "starVBO");
						starVBO = (net.minecraft.client.renderer.vertex.VertexBuffer) field2.get(mc.renderGlobal);
						Field field3 = ReflectionHelper.findField(RenderGlobal.class, "skyVBO");
						skyVBO = (net.minecraft.client.renderer.vertex.VertexBuffer) field3.get(mc.renderGlobal);
						Field field4 = ReflectionHelper.findField(RenderGlobal.class, "sky2VBO");
						sky2VBO = (net.minecraft.client.renderer.vertex.VertexBuffer) field4.get(mc.renderGlobal);
					}
					catch(Exception e){
						e.printStackTrace();
					}
					this.sky2VBO.bindBuffer();
					GlStateManager.glEnableClientState(32884);
					GlStateManager.glVertexPointer(3, 5126, 12, 0);
					this.sky2VBO.drawArrays(7);
					this.sky2VBO.unbindBuffer();
					GlStateManager.glDisableClientState(32884);
				} else {
					GlStateManager.callList(this.glSkyList2);
				}

				GlStateManager.popMatrix();
				float f18 = 1.0F;
				float f19 = -((float) (d0 + 65.0D));
				float f20 = -1.0F;
				vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
				vertexbuffer.pos(-1.0D, (double) f19, 1.0D).color(0, 0, 0, 255).endVertex();
				vertexbuffer.pos(1.0D, (double) f19, 1.0D).color(0, 0, 0, 255).endVertex();
				vertexbuffer.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
				vertexbuffer.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
				vertexbuffer.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
				vertexbuffer.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
				vertexbuffer.pos(1.0D, (double) f19, -1.0D).color(0, 0, 0, 255).endVertex();
				vertexbuffer.pos(-1.0D, (double) f19, -1.0D).color(0, 0, 0, 255).endVertex();
				vertexbuffer.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
				vertexbuffer.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
				vertexbuffer.pos(1.0D, (double) f19, 1.0D).color(0, 0, 0, 255).endVertex();
				vertexbuffer.pos(1.0D, (double) f19, -1.0D).color(0, 0, 0, 255).endVertex();
				vertexbuffer.pos(-1.0D, (double) f19, -1.0D).color(0, 0, 0, 255).endVertex();
				vertexbuffer.pos(-1.0D, (double) f19, 1.0D).color(0, 0, 0, 255).endVertex();
				vertexbuffer.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
				vertexbuffer.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
				vertexbuffer.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
				vertexbuffer.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
				vertexbuffer.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
				vertexbuffer.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
				tessellator.draw();
			}

			if (world.provider.isSkyColored()) {
				GlStateManager.color(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F);
			} else {
				GlStateManager.color(f, f1, f2);
			}

			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, -((float) (d0 - 16.0D)), 0.0F);
			GlStateManager.callList(this.glSkyList2);
			GlStateManager.popMatrix();
			GlStateManager.enableTexture2D();
			GlStateManager.depthMask(true);
		}

	}
	   private void generateSky2()
	    {
	        Tessellator tessellator = Tessellator.getInstance();
	        VertexBuffer vertexbuffer = tessellator.getBuffer();

	        if (this.sky2VBO != null)
	        {
	            this.sky2VBO.deleteGlBuffers();
	        }

	        if (this.glSkyList2 >= 0)
	        {
	            GLAllocation.deleteDisplayLists(this.glSkyList2);
	            this.glSkyList2 = -1;
	        }

	        if (OpenGlHelper.useVbo())
	        {
	        	if(sky2VBO == null)
				try{
					Field field = ReflectionHelper.findField(RenderGlobal.class, "vertexBufferFormat");
					vertexBufferFormat = (VertexFormat) field.get(mc.renderGlobal);
					
					Method method =ReflectionHelper.findMethod(RenderGlobal.class, mc.renderGlobal, new String[]{"generateStars"});
					method.invoke(mc.renderGlobal);
					
					Method method2 =ReflectionHelper.findMethod(RenderGlobal.class, mc.renderGlobal, new String[]{"generateSky"});
					method2.invoke(mc.renderGlobal);
					
					Method method3 =ReflectionHelper.findMethod(RenderGlobal.class, mc.renderGlobal, new String[]{"generateSky2"});
					method3.invoke(mc.renderGlobal);
					
					Field field2 = ReflectionHelper.findField(RenderGlobal.class, "starVBO");
					starVBO = (net.minecraft.client.renderer.vertex.VertexBuffer) field2.get(mc.renderGlobal);
					Field field3 = ReflectionHelper.findField(RenderGlobal.class, "skyVBO");
					skyVBO = (net.minecraft.client.renderer.vertex.VertexBuffer) field3.get(mc.renderGlobal);
					Field field4 = ReflectionHelper.findField(RenderGlobal.class, "sky2VBO");
					sky2VBO = (net.minecraft.client.renderer.vertex.VertexBuffer) field4.get(mc.renderGlobal);
				}
				catch(Exception e){
					e.printStackTrace();
				}
	            this.sky2VBO = new net.minecraft.client.renderer.vertex.VertexBuffer(this.vertexBufferFormat);
	            this.renderSky(vertexbuffer, -16.0F, true);
	            vertexbuffer.finishDrawing();
	            vertexbuffer.reset();
	            this.sky2VBO.bufferData(vertexbuffer.getByteBuffer());
	        }
	        else
	        {
	            this.glSkyList2 = GLAllocation.generateDisplayLists(1);
	            GlStateManager.glNewList(this.glSkyList2, 4864);
	            this.renderSky(vertexbuffer, -16.0F, true);
	            tessellator.draw();
	            GlStateManager.glEndList();
	        }
	    }
	   private void renderSky(VertexBuffer worldRendererIn, float posY, boolean reverseX)
	    {
	        int i = 64;
	        int j = 6;
	        worldRendererIn.begin(7, DefaultVertexFormats.POSITION);

	        for (int k = -384; k <= 384; k += 64)
	        {
	            for (int l = -384; l <= 384; l += 64)
	            {
	                float f = (float)k;
	                float f1 = (float)(k + 64);

	                if (reverseX)
	                {
	                    f1 = (float)k;
	                    f = (float)(k + 64);
	                }

	                worldRendererIn.pos((double)f, (double)posY, (double)l).endVertex();
	                worldRendererIn.pos((double)f1, (double)posY, (double)l).endVertex();
	                worldRendererIn.pos((double)f1, (double)posY, (double)(l + 64)).endVertex();
	                worldRendererIn.pos((double)f, (double)posY, (double)(l + 64)).endVertex();
	            }
	        }
	    }
	    private void generateSky()
	    {
	        Tessellator tessellator = Tessellator.getInstance();
	        VertexBuffer vertexbuffer = tessellator.getBuffer();

	        if (this.skyVBO != null)
	        {
	            this.skyVBO.deleteGlBuffers();
	        }

	        if (this.glSkyList >= 0)
	        {
	            GLAllocation.deleteDisplayLists(this.glSkyList);
	            this.glSkyList = -1;
	        }

	        if (OpenGlHelper.useVbo())
	        {
	            this.skyVBO = new net.minecraft.client.renderer.vertex.VertexBuffer(this.vertexBufferFormat);
	            this.renderSky(vertexbuffer, 16.0F, false);
	            vertexbuffer.finishDrawing();
	            vertexbuffer.reset();
	            this.skyVBO.bufferData(vertexbuffer.getByteBuffer());
	        }
	        else
	        {
	            this.glSkyList = GLAllocation.generateDisplayLists(1);
	            GlStateManager.glNewList(this.glSkyList, 4864);
	            this.renderSky(vertexbuffer, 16.0F, false);
	            tessellator.draw();
	            GlStateManager.glEndList();
	        }
	    }

	private void renderSkyEnd() {
		GlStateManager.disableFog();
		GlStateManager.disableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.depthMask(false);
		mc.renderEngine.bindTexture(END_SKY_TEXTURES);
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();

		for (int i = 0; i < 6; ++i) {
			GlStateManager.pushMatrix();

			if (i == 1) {
				GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
			}

			if (i == 2) {
				GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
			}

			if (i == 3) {
				GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
			}

			if (i == 4) {
				GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
			}

			if (i == 5) {
				GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
			}

			vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
			vertexbuffer.pos(-100.0D, -100.0D, -100.0D).tex(0.0D, 0.0D).color(40, 40, 40, 255).endVertex();
			vertexbuffer.pos(-100.0D, -100.0D, 100.0D).tex(0.0D, 16.0D).color(40, 40, 40, 255).endVertex();
			vertexbuffer.pos(100.0D, -100.0D, 100.0D).tex(16.0D, 16.0D).color(40, 40, 40, 255).endVertex();
			vertexbuffer.pos(100.0D, -100.0D, -100.0D).tex(16.0D, 0.0D).color(40, 40, 40, 255).endVertex();
			tessellator.draw();
			GlStateManager.popMatrix();
		}

		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GlStateManager.enableAlpha();
	}

	public void generateStars() {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();

		if (this.starVBO != null) {
			this.starVBO.deleteGlBuffers();
		}

		if (this.starGLCallList >= 0) {
			GLAllocation.deleteDisplayLists(this.starGLCallList);
			this.starGLCallList = -1;
		}

		if (OpenGlHelper.useVbo()) {
			this.starVBO = new net.minecraft.client.renderer.vertex.VertexBuffer(vertexBufferFormat);
			this.renderStars(vertexbuffer);
			vertexbuffer.finishDrawing();
			vertexbuffer.reset();
			this.starVBO.bufferData(vertexbuffer.getByteBuffer());
		} else {
			this.starGLCallList = GLAllocation.generateDisplayLists(1);
			GlStateManager.pushMatrix();
			GlStateManager.glNewList(this.starGLCallList, 4864);
			this.renderStars(vertexbuffer);
			tessellator.draw();
			GlStateManager.glEndList();
			GlStateManager.popMatrix();
		}
	}
	private void renderStars(VertexBuffer worldRendererIn)
    {
        Random random = new Random(10842L);
        worldRendererIn.begin(7, DefaultVertexFormats.POSITION);

        for (int i = 0; i < Sky.starCount; ++i)
        {
            double d0 = (double)(random.nextFloat() * 2.0F - 1.0F);
            double d1 = (double)(random.nextFloat() * 2.0F - 1.0F);
            double d2 = (double)(random.nextFloat() * 2.0F - 1.0F);
            double d3 = (double)(0.15F + random.nextFloat() * 0.1F);
            double d4 = d0 * d0 + d1 * d1 + d2 * d2;

            if (d4 < 1.0D && d4 > 0.01D)
            {
                d4 = 1.0D / Math.sqrt(d4);
                d0 = d0 * d4;
                d1 = d1 * d4;
                d2 = d2 * d4;
                double d5 = d0 * 100.0D;
                double d6 = d1 * 100.0D;
                double d7 = d2 * 100.0D;
                double d8 = Math.atan2(d0, d2);
                double d9 = Math.sin(d8);
                double d10 = Math.cos(d8);
                double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
                double d12 = Math.sin(d11);
                double d13 = Math.cos(d11);
                double d14 = random.nextDouble() * Math.PI * 2.0D;
                double d15 = Math.sin(d14);
                double d16 = Math.cos(d14);

                for (int j = 0; j < 4; ++j)
                {
                    double d17 = 0.0D;
                    double d18 = (double)((j & 2) - 1) * d3;
                    double d19 = (double)((j + 1 & 2) - 1) * d3;
                    double d20 = 0.0D;
                    double d21 = d18 * d16 - d19 * d15;
                    double d22 = d19 * d16 + d18 * d15;
                    double d23 = d21 * d12 + 0.0D * d13;
                    double d24 = 0.0D * d12 - d21 * d13;
                    double d25 = d24 * d9 - d22 * d10;
                    double d26 = d22 * d9 + d24 * d10;
                    worldRendererIn.pos(d5 + d25, d6 + d23, d7 + d26).endVertex();
                }
            }
        }
    }


}
