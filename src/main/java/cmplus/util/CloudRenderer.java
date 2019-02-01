package cmplus.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.IRenderHandler;

public class CloudRenderer extends IRenderHandler {
	public ResourceLocation MOON_PHASES_TEXTURES = new ResourceLocation("textures/environment/moon_phases.png");
	public ResourceLocation SUN_TEXTURES = new ResourceLocation("textures/environment/sun.png");
	public ResourceLocation CLOUDS_TEXTURES = new ResourceLocation("textures/environment/clouds.png");
	public ResourceLocation END_SKY_TEXTURES = new ResourceLocation("textures/environment/end_sky.png");
	public Minecraft mc;
	public World world;
	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		
		this.world = world;
		int pass = mc.gameSettings.clouds;
		if (this.mc == null) {
			this.mc = mc;
		}
		if (world.provider.isSurfaceWorld()) {
			if (mc.gameSettings.shouldRenderClouds() == 2) {
				if (Sky.cloudRed == 0 && Sky.cloudBlue == 0 && Sky.cloudGreen == 0) {
					this.renderClouds2(partialTicks, pass);
					return;
				} else
					this.renderCloudsFancy(partialTicks, pass);
			} else {
				GlStateManager.disableCull();
				Entity entity = mc.getRenderViewEntity();
				float f = (float) (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks);
				int i = 32;
				int j = 8;
				Tessellator tessellator = Tessellator.getInstance();
				VertexBuffer vertexbuffer = tessellator.getBuffer();
				mc.renderEngine.bindTexture(CLOUDS_TEXTURES);
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
						GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
						GlStateManager.DestFactor.ZERO);
				Vec3d vec3d = world.getCloudColour(partialTicks);
				float f1 = (float) vec3d.xCoord;
				float f2 = (float) vec3d.yCoord;
				float f3 = (float) vec3d.zCoord;

				if (pass != 2) {
					float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
					float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
					float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
					f1 = f4;
					f2 = f5;
					f3 = f6;
				}

				float f10 = 4.8828125E-4F;
				double d2 = (double) (partialTicks);
				double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks
						+ d2 * 0.029999999329447746D;
				double d1 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks;
				int k = MathHelper.floor_double(d0 / 2048.0D);
				int l = MathHelper.floor_double(d1 / 2048.0D);
				d0 = d0 - (double) (k * 2048);
				d1 = d1 - (double) (l * 2048);
				float f7 = Sky.cloudHeight - f + 0.33F;
				float f8 = (float) (d0 * 4.8828125E-4D);
				float f9 = (float) (d1 * 4.8828125E-4D);
				vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);

				for (int i1 = -256; i1 < 256; i1 += 32) {
					for (int j1 = -256; j1 < 256; j1 += 32) {
						vertexbuffer.pos((double) (i1 + 0), (double) f7, (double) (j1 + 32))
								.tex((double) ((float) (i1 + 0) * 4.8828125E-4F + f8),
										(double) ((float) (j1 + 32) * 4.8828125E-4F + f9))
								.color(f1, f2, f3, 0.8F).endVertex();
						vertexbuffer.pos((double) (i1 + 32), (double) f7, (double) (j1 + 32))
								.tex((double) ((float) (i1 + 32) * 4.8828125E-4F + f8),
										(double) ((float) (j1 + 32) * 4.8828125E-4F + f9))
								.color(f1, f2, f3, 0.8F).endVertex();
						vertexbuffer.pos((double) (i1 + 32), (double) f7, (double) (j1 + 0))
								.tex((double) ((float) (i1 + 32) * 4.8828125E-4F + f8),
										(double) ((float) (j1 + 0) * 4.8828125E-4F + f9))
								.color(f1, f2, f3, 0.8F).endVertex();
						vertexbuffer.pos((double) (i1 + 0), (double) f7, (double) (j1 + 0))
								.tex((double) ((float) (i1 + 0) * 4.8828125E-4F + f8),
										(double) ((float) (j1 + 0) * 4.8828125E-4F + f9))
								.color(f1, f2, f3, 0.8F).endVertex();
					}
				}

				tessellator.draw();
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.disableBlend();
				GlStateManager.enableCull();
			}
		}
	}

	private void renderCloudsFancy2(float partialTicks, int pass) {
		GlStateManager.disableCull();
		Entity entity = this.mc.getRenderViewEntity();
		float f = (float) (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks);
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		float f1 = 12.0F;
		float f2 = 4.0F;
		double d0 = (double) (partialTicks);
		double d1 = (entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks
				+ d0 * 0.029999999329447746D) / 12.0D;
		double d2 = (entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks) / 12.0D
				+ 0.33000001311302185D;
		float f3 = Sky.cloudHeight - f + 0.33F;
		int i = MathHelper.floor_double(d1 / 2048.0D);
		int j = MathHelper.floor_double(d2 / 2048.0D);
		d1 = d1 - (double) (i * 2048);
		d2 = d2 - (double) (j * 2048);
		mc.renderEngine.bindTexture(CLOUDS_TEXTURES);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
		Vec3d vec3d = world.getCloudColour(partialTicks);
		float f4 = (float) vec3d.xCoord;
		float f5 = (float) vec3d.yCoord;
		float f6 = (float) vec3d.zCoord;

		if (pass != 2) {
			float f7 = (f4 * 30.0F + f5 * 59.0F + f6 * 11.0F) / 100.0F;
			float f8 = (f4 * 30.0F + f5 * 70.0F) / 100.0F;
			float f9 = (f4 * 30.0F + f6 * 70.0F) / 100.0F;
			f4 = f7;
			f5 = f8;
			f6 = f9;
		}

		float f26 = f4 * 0.9F;
		float f27 = f5 * 0.9F;
		float f28 = f6 * 0.9F;
		float f10 = f4 * 0.7F;
		float f11 = f5 * 0.7F;
		float f12 = f6 * 0.7F;
		float f13 = f4 * 0.8F;
		float f14 = f5 * 0.8F;
		float f15 = f6 * 0.8F;
		float f16 = 0.00390625F;
		float f17 = (float) MathHelper.floor_double(d1) * 0.00390625F;
		float f18 = (float) MathHelper.floor_double(d2) * 0.00390625F;
		float f19 = (float) (d1 - (double) MathHelper.floor_double(d1));
		float f20 = (float) (d2 - (double) MathHelper.floor_double(d2));
		int k = 8;
		int l = 4;
		float f21 = 9.765625E-4F;
		GlStateManager.scale(Sky.cloudX, Sky.cloudY, Sky.cloudZ);
		for (int i1 = 0; i1 < 2; ++i1) {
			if (i1 == 0) {
				GlStateManager.colorMask(false, false, false, false);
			} else {
				switch (pass) {
				case 0:
					GlStateManager.colorMask(false, true, true, true);
					break;
				case 1:
					GlStateManager.colorMask(true, false, false, true);
					break;
				case 2:
					GlStateManager.colorMask(true, true, true, true);
				}
			}

			for (int j1 = -3; j1 <= 4; ++j1) {
				for (int k1 = -3; k1 <= 4; ++k1) {
					vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
					float f22 = (float) (j1 * 8);
					float f23 = (float) (k1 * 8);
					float f24 = f22 - f19;
					float f25 = f23 - f20;

					if (f3 > -5.0F) {
						vertexbuffer.pos((double) (f24 + 0.0F), (double) (f3 + 0.0F), (double) (f25 + 8.0F))
								.tex((double) ((f22 + 0.0F) * 0.00390625F + f17),
										(double) ((f23 + 8.0F) * 0.00390625F + f18))
								.color(f10, f11, f12, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
						vertexbuffer.pos((double) (f24 + 8.0F), (double) (f3 + 0.0F), (double) (f25 + 8.0F))
								.tex((double) ((f22 + 8.0F) * 0.00390625F + f17),
										(double) ((f23 + 8.0F) * 0.00390625F + f18))
								.color(f10, f11, f12, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
						vertexbuffer.pos((double) (f24 + 8.0F), (double) (f3 + 0.0F), (double) (f25 + 0.0F))
								.tex((double) ((f22 + 8.0F) * 0.00390625F + f17),
										(double) ((f23 + 0.0F) * 0.00390625F + f18))
								.color(f10, f11, f12, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
						vertexbuffer.pos((double) (f24 + 0.0F), (double) (f3 + 0.0F), (double) (f25 + 0.0F))
								.tex((double) ((f22 + 0.0F) * 0.00390625F + f17),
										(double) ((f23 + 0.0F) * 0.00390625F + f18))
								.color(f10, f11, f12, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
					}

					if (f3 <= 5.0F) {
						vertexbuffer
								.pos((double) (f24 + 0.0F), (double) (f3 + 4.0F - 9.765625E-4F), (double) (f25 + 8.0F))
								.tex((double) ((f22 + 0.0F) * 0.00390625F + f17),
										(double) ((f23 + 8.0F) * 0.00390625F + f18))
								.color(f4, f5, f6, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
						vertexbuffer
								.pos((double) (f24 + 8.0F), (double) (f3 + 4.0F - 9.765625E-4F), (double) (f25 + 8.0F))
								.tex((double) ((f22 + 8.0F) * 0.00390625F + f17),
										(double) ((f23 + 8.0F) * 0.00390625F + f18))
								.color(f4, f5, f6, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
						vertexbuffer
								.pos((double) (f24 + 8.0F), (double) (f3 + 4.0F - 9.765625E-4F), (double) (f25 + 0.0F))
								.tex((double) ((f22 + 8.0F) * 0.00390625F + f17),
										(double) ((f23 + 0.0F) * 0.00390625F + f18))
								.color(f4, f5, f6, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
						vertexbuffer
								.pos((double) (f24 + 0.0F), (double) (f3 + 4.0F - 9.765625E-4F), (double) (f25 + 0.0F))
								.tex((double) ((f22 + 0.0F) * 0.00390625F + f17),
										(double) ((f23 + 0.0F) * 0.00390625F + f18))
								.color(f4, f5, f6, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
					}

					if (j1 > -1) {
						for (int l1 = 0; l1 < 8; ++l1) {
							vertexbuffer
									.pos((double) (f24 + (float) l1 + 0.0F), (double) (f3 + 0.0F),
											(double) (f25 + 8.0F))
									.tex((double) ((f22 + (float) l1 + 0.5F) * 0.00390625F + f17),
											(double) ((f23 + 8.0F) * 0.00390625F + f18))
									.color(f26, f27, f28, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
							vertexbuffer
									.pos((double) (f24 + (float) l1 + 0.0F), (double) (f3 + 4.0F),
											(double) (f25 + 8.0F))
									.tex((double) ((f22 + (float) l1 + 0.5F) * 0.00390625F + f17),
											(double) ((f23 + 8.0F) * 0.00390625F + f18))
									.color(f26, f27, f28, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
							vertexbuffer
									.pos((double) (f24 + (float) l1 + 0.0F), (double) (f3 + 4.0F),
											(double) (f25 + 0.0F))
									.tex((double) ((f22 + (float) l1 + 0.5F) * 0.00390625F + f17),
											(double) ((f23 + 0.0F) * 0.00390625F + f18))
									.color(f26, f27, f28, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
							vertexbuffer
									.pos((double) (f24 + (float) l1 + 0.0F), (double) (f3 + 0.0F),
											(double) (f25 + 0.0F))
									.tex((double) ((f22 + (float) l1 + 0.5F) * 0.00390625F + f17),
											(double) ((f23 + 0.0F) * 0.00390625F + f18))
									.color(f26, f27, f28, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
						}
					}

					if (j1 <= 1) {
						for (int i2 = 0; i2 < 8; ++i2) {
							vertexbuffer
									.pos((double) (f24 + (float) i2 + 1.0F - 9.765625E-4F), (double) (f3 + 0.0F),
											(double) (f25 + 8.0F))
									.tex((double) ((f22 + (float) i2 + 0.5F) * 0.00390625F + f17),
											(double) ((f23 + 8.0F) * 0.00390625F + f18))
									.color(f26, f27, f28, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
							vertexbuffer
									.pos((double) (f24 + (float) i2 + 1.0F - 9.765625E-4F), (double) (f3 + 4.0F),
											(double) (f25 + 8.0F))
									.tex((double) ((f22 + (float) i2 + 0.5F) * 0.00390625F + f17),
											(double) ((f23 + 8.0F) * 0.00390625F + f18))
									.color(f26, f27, f28, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
							vertexbuffer
									.pos((double) (f24 + (float) i2 + 1.0F - 9.765625E-4F), (double) (f3 + 4.0F),
											(double) (f25 + 0.0F))
									.tex((double) ((f22 + (float) i2 + 0.5F) * 0.00390625F + f17),
											(double) ((f23 + 0.0F) * 0.00390625F + f18))
									.color(f26, f27, f28, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
							vertexbuffer
									.pos((double) (f24 + (float) i2 + 1.0F - 9.765625E-4F), (double) (f3 + 0.0F),
											(double) (f25 + 0.0F))
									.tex((double) ((f22 + (float) i2 + 0.5F) * 0.00390625F + f17),
											(double) ((f23 + 0.0F) * 0.00390625F + f18))
									.color(f26, f27, f28, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
						}
					}

					if (k1 > -1) {
						for (int j2 = 0; j2 < 8; ++j2) {
							vertexbuffer
									.pos((double) (f24 + 0.0F), (double) (f3 + 4.0F),
											(double) (f25 + (float) j2 + 0.0F))
									.tex((double) ((f22 + 0.0F) * 0.00390625F + f17),
											(double) ((f23 + (float) j2 + 0.5F) * 0.00390625F + f18))
									.color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
							vertexbuffer
									.pos((double) (f24 + 8.0F), (double) (f3 + 4.0F),
											(double) (f25 + (float) j2 + 0.0F))
									.tex((double) ((f22 + 8.0F) * 0.00390625F + f17),
											(double) ((f23 + (float) j2 + 0.5F) * 0.00390625F + f18))
									.color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
							vertexbuffer
									.pos((double) (f24 + 8.0F), (double) (f3 + 0.0F),
											(double) (f25 + (float) j2 + 0.0F))
									.tex((double) ((f22 + 8.0F) * 0.00390625F + f17),
											(double) ((f23 + (float) j2 + 0.5F) * 0.00390625F + f18))
									.color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
							vertexbuffer
									.pos((double) (f24 + 0.0F), (double) (f3 + 0.0F),
											(double) (f25 + (float) j2 + 0.0F))
									.tex((double) ((f22 + 0.0F) * 0.00390625F + f17),
											(double) ((f23 + (float) j2 + 0.5F) * 0.00390625F + f18))
									.color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
						}
					}

					if (k1 <= 1) {
						for (int k2 = 0; k2 < 8; ++k2) {
							vertexbuffer
									.pos((double) (f24 + 0.0F), (double) (f3 + 4.0F),
											(double) (f25 + (float) k2 + 1.0F - 9.765625E-4F))
									.tex((double) ((f22 + 0.0F) * 0.00390625F + f17),
											(double) ((f23 + (float) k2 + 0.5F) * 0.00390625F + f18))
									.color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
							vertexbuffer
									.pos((double) (f24 + 8.0F), (double) (f3 + 4.0F),
											(double) (f25 + (float) k2 + 1.0F - 9.765625E-4F))
									.tex((double) ((f22 + 8.0F) * 0.00390625F + f17),
											(double) ((f23 + (float) k2 + 0.5F) * 0.00390625F + f18))
									.color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
							vertexbuffer
									.pos((double) (f24 + 8.0F), (double) (f3 + 0.0F),
											(double) (f25 + (float) k2 + 1.0F - 9.765625E-4F))
									.tex((double) ((f22 + 8.0F) * 0.00390625F + f17),
											(double) ((f23 + (float) k2 + 0.5F) * 0.00390625F + f18))
									.color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
							vertexbuffer
									.pos((double) (f24 + 0.0F), (double) (f3 + 0.0F),
											(double) (f25 + (float) k2 + 1.0F - 9.765625E-4F))
									.tex((double) ((f22 + 0.0F) * 0.00390625F + f17),
											(double) ((f23 + (float) k2 + 0.5F) * 0.00390625F + f18))
									.color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
						}
					}

					tessellator.draw();
				}
			}
		}

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
		GlStateManager.enableCull();
	}

	public void renderClouds2(float partialTicks, int pass) {
		
		if (world.provider.isSurfaceWorld()) {
			if (this.mc.gameSettings.shouldRenderClouds() == 2) {
				this.renderCloudsFancy2(partialTicks, pass);
			} else {
				GlStateManager.disableCull();
				Entity entity = this.mc.getRenderViewEntity();
				float f = (float) (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks);
				int i = 32;
				int j = 8;
				Tessellator tessellator = Tessellator.getInstance();
				VertexBuffer vertexbuffer = tessellator.getBuffer();
				mc.renderEngine.bindTexture(CLOUDS_TEXTURES);
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
						GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
						GlStateManager.DestFactor.ZERO);
				Vec3d vec3d = world.getCloudColour(partialTicks);
				float f1 = (float) vec3d.xCoord;
				float f2 = (float) vec3d.yCoord;
				float f3 = (float) vec3d.zCoord;

				if (pass != 2) {
					float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
					float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
					float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
					f1 = f4;
					f2 = f5;
					f3 = f6;
				}

				float f10 = 4.8828125E-4F;
				double d2 = (double) (partialTicks);
				double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks
						+ d2 * 0.029999999329447746D;
				double d1 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks;
				int k = MathHelper.floor_double(d0 / 2048.0D);
				int l = MathHelper.floor_double(d1 / 2048.0D);
				d0 = d0 - (double) (k * 2048);
				d1 = d1 - (double) (l * 2048);
				float f7 = Sky.cloudHeight - f + 0.33F;
				float f8 = (float) (d0 * 4.8828125E-4D);
				float f9 = (float) (d1 * 4.8828125E-4D);
				vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);

				for (int i1 = -256; i1 < 256; i1 += 32) {
					for (int j1 = -256; j1 < 256; j1 += 32) {
						vertexbuffer.pos((double) (i1 + 0), (double) f7, (double) (j1 + 32))
								.tex((double) ((float) (i1 + 0) * 4.8828125E-4F + f8),
										(double) ((float) (j1 + 32) * 4.8828125E-4F + f9))
								.color(f1, f2, f3, 0.8F).endVertex();
						vertexbuffer.pos((double) (i1 + 32), (double) f7, (double) (j1 + 32))
								.tex((double) ((float) (i1 + 32) * 4.8828125E-4F + f8),
										(double) ((float) (j1 + 32) * 4.8828125E-4F + f9))
								.color(f1, f2, f3, 0.8F).endVertex();
						vertexbuffer.pos((double) (i1 + 32), (double) f7, (double) (j1 + 0))
								.tex((double) ((float) (i1 + 32) * 4.8828125E-4F + f8),
										(double) ((float) (j1 + 0) * 4.8828125E-4F + f9))
								.color(f1, f2, f3, 0.8F).endVertex();
						vertexbuffer.pos((double) (i1 + 0), (double) f7, (double) (j1 + 0))
								.tex((double) ((float) (i1 + 0) * 4.8828125E-4F + f8),
										(double) ((float) (j1 + 0) * 4.8828125E-4F + f9))
								.color(f1, f2, f3, 0.8F).endVertex();
					}
				}

				tessellator.draw();
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.disableBlend();
				GlStateManager.enableCull();
			}
		}
	}

	/**
	 * Checks if the given position is to be rendered with cloud fog
	 */
	public boolean hasCloudFog(double x, double y, double z, float partialTicks) {
		return false;
	}

	private void renderCloudsFancy(float partialTicks, int pass) {
		GlStateManager.disableCull();
		Entity entity = this.mc.getRenderViewEntity();
		float f = (float) (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks);
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		float f1 = 12.0F;
		float f2 = 4.0F;
		double d0 = (double) (partialTicks);
		double d1 = (entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks
				+ d0 * 0.029999999329447746D) / 12.0D;
		double d2 = (entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks) / 12.0D
				+ 0.33000001311302185D;
		float f3 = Sky.cloudHeight - f + 0.33F;
		int i = MathHelper.floor_double(d1 / 2048.0D);
		int j = MathHelper.floor_double(d2 / 2048.0D);
		d1 = d1 - (double) (i * 2048);
		d2 = d2 - (double) (j * 2048);
		mc.renderEngine.bindTexture(CLOUDS_TEXTURES);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
		Vec3d vec3d = world.getCloudColour(partialTicks);
		float f4 = (float) vec3d.xCoord;
		float f5 = (float) vec3d.yCoord;
		float f6 = (float) vec3d.zCoord;

		if (pass != 2) {
			float f7 = (f4 * 30.0F + f5 * 59.0F + f6 * 11.0F) / 100.0F;
			float f8 = (f4 * 30.0F + f5 * 70.0F) / 100.0F;
			float f9 = (f4 * 30.0F + f6 * 70.0F) / 100.0F;
			f4 = f7;
			f5 = f8;
			f6 = f9;
		}

		float f26 = f4 * 0.9F;
		float f27 = f5 * 0.9F;
		float f28 = f6 * 0.9F;
		float f10 = f4 * 0.7F;
		float f11 = f5 * 0.7F;
		float f12 = f6 * 0.7F;
		float f13 = f4 * 0.8F;
		float f14 = f5 * 0.8F;
		float f15 = f6 * 0.8F;
		float f16 = 0.00390625F;
		float f17 = (float) MathHelper.floor_double(d1) * 0.00390625F;
		float f18 = (float) MathHelper.floor_double(d2) * 0.00390625F;
		float f19 = (float) (d1 - (double) MathHelper.floor_double(d1));
		float f20 = (float) (d2 - (double) MathHelper.floor_double(d2));
		int k = 8;
		int l = 4;
		float f21 = 9.765625E-4F;
		GlStateManager.scale(Sky.cloudX, Sky.cloudY, Sky.cloudZ);

		for (int i1 = 0; i1 < 2; ++i1) {
			if (i1 == 0) {
				GlStateManager.colorMask(false, false, false, false);
			} else {
				switch (pass) {
				case 0:
					GlStateManager.colorMask(false, true, true, true);
					break;
				case 1:
					GlStateManager.colorMask(true, false, false, true);
					break;
				case 2:
					GlStateManager.colorMask(true, true, true, true);
				}
			}

			for (int j1 = -3; j1 <= 4; ++j1) {
				for (int k1 = -3; k1 <= 4; ++k1) {
					vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
					float f22 = (float) (j1 * 8);
					float f23 = (float) (k1 * 8);
					float f24 = f22 - f19;
					float f25 = f23 - f20;

					if (f3 > -5.0F) {
						vertexbuffer.pos((double) (f24 + 0.0F), (double) (f3 + 0.0F), (double) (f25 + 8.0F))
								.tex((double) ((f22 + 0.0F) * 0.00390625F + f17),
										(double) ((f23 + 8.0F) * 0.00390625F + f18))
								.color(Sky.cloudRed / 255, Sky.cloudGreen / 255, Sky.cloudBlue / 255, Sky.cloudAlpha)
								.normal(0.0F, -1.0F, 0.0F).endVertex();
						vertexbuffer.pos((double) (f24 + 8.0F), (double) (f3 + 0.0F), (double) (f25 + 8.0F))
								.tex((double) ((f22 + 8.0F) * 0.00390625F + f17),
										(double) ((f23 + 8.0F) * 0.00390625F + f18))
								.color(Sky.cloudRed / 255, Sky.cloudGreen / 255, Sky.cloudBlue / 255, Sky.cloudAlpha)
								.normal(0.0F, -1.0F, 0.0F).endVertex();
						vertexbuffer.pos((double) (f24 + 8.0F), (double) (f3 + 0.0F), (double) (f25 + 0.0F))
								.tex((double) ((f22 + 8.0F) * 0.00390625F + f17),
										(double) ((f23 + 0.0F) * 0.00390625F + f18))
								.color(Sky.cloudRed / 255, Sky.cloudGreen / 255, Sky.cloudBlue / 255, Sky.cloudAlpha)
								.normal(0.0F, -1.0F, 0.0F).endVertex();
						vertexbuffer.pos((double) (f24 + 0.0F), (double) (f3 + 0.0F), (double) (f25 + 0.0F))
								.tex((double) ((f22 + 0.0F) * 0.00390625F + f17),
										(double) ((f23 + 0.0F) * 0.00390625F + f18))
								.color(Sky.cloudRed / 255, Sky.cloudGreen / 255, Sky.cloudBlue / 255, Sky.cloudAlpha)
								.normal(0.0F, -1.0F, 0.0F).endVertex();
					}

					if (f3 <= 5.0F) {
						vertexbuffer
								.pos((double) (f24 + 0.0F), (double) (f3 + 4.0F - 9.765625E-4F), (double) (f25 + 8.0F))
								.tex((double) ((f22 + 0.0F) * 0.00390625F + f17),
										(double) ((f23 + 8.0F) * 0.00390625F + f18))
								.color(Sky.cloudRed / 255, Sky.cloudGreen / 255, Sky.cloudBlue / 255, Sky.cloudAlpha)
								.normal(0.0F, 1.0F, 0.0F).endVertex();
						vertexbuffer
								.pos((double) (f24 + 8.0F), (double) (f3 + 4.0F - 9.765625E-4F), (double) (f25 + 8.0F))
								.tex((double) ((f22 + 8.0F) * 0.00390625F + f17),
										(double) ((f23 + 8.0F) * 0.00390625F + f18))
								.color(Sky.cloudRed / 255, Sky.cloudGreen / 255, Sky.cloudBlue / 255, Sky.cloudAlpha)
								.normal(0.0F, 1.0F, 0.0F).endVertex();
						vertexbuffer
								.pos((double) (f24 + 8.0F), (double) (f3 + 4.0F - 9.765625E-4F), (double) (f25 + 0.0F))
								.tex((double) ((f22 + 8.0F) * 0.00390625F + f17),
										(double) ((f23 + 0.0F) * 0.00390625F + f18))
								.color(Sky.cloudRed / 255, Sky.cloudGreen / 255, Sky.cloudBlue / 255, Sky.cloudAlpha)
								.normal(0.0F, 1.0F, 0.0F).endVertex();
						vertexbuffer
								.pos((double) (f24 + 0.0F), (double) (f3 + 4.0F - 9.765625E-4F), (double) (f25 + 0.0F))
								.tex((double) ((f22 + 0.0F) * 0.00390625F + f17),
										(double) ((f23 + 0.0F) * 0.00390625F + f18))
								.color(Sky.cloudRed / 255, Sky.cloudGreen / 255, Sky.cloudBlue / 255, Sky.cloudAlpha)
								.normal(0.0F, 1.0F, 0.0F).endVertex();
					}

					if (j1 > -1) {
						for (int l1 = 0; l1 < 8; ++l1) {
							vertexbuffer
									.pos((double) (f24 + (float) l1 + 0.0F), (double) (f3 + 0.0F),
											(double) (f25 + 8.0F))
									.tex((double) ((f22 + (float) l1 + 0.5F) * 0.00390625F + f17),
											(double) ((f23 + 8.0F) * 0.00390625F + f18))
									.color(Sky.cloudRed / 255, Sky.cloudGreen / 255, Sky.cloudBlue / 255, Sky.cloudAlpha)
									.normal(-1.0F, 0.0F, 0.0F).endVertex();
							vertexbuffer
									.pos((double) (f24 + (float) l1 + 0.0F), (double) (f3 + 4.0F),
											(double) (f25 + 8.0F))
									.tex((double) ((f22 + (float) l1 + 0.5F) * 0.00390625F + f17),
											(double) ((f23 + 8.0F) * 0.00390625F + f18))
									.color(Sky.cloudRed / 255, Sky.cloudGreen / 255, Sky.cloudBlue / 255, Sky.cloudAlpha)
									.normal(-1.0F, 0.0F, 0.0F).endVertex();
							vertexbuffer
									.pos((double) (f24 + (float) l1 + 0.0F), (double) (f3 + 4.0F),
											(double) (f25 + 0.0F))
									.tex((double) ((f22 + (float) l1 + 0.5F) * 0.00390625F + f17),
											(double) ((f23 + 0.0F) * 0.00390625F + f18))
									.color(Sky.cloudRed / 255, Sky.cloudGreen / 255, Sky.cloudBlue / 255, Sky.cloudAlpha)
									.normal(-1.0F, 0.0F, 0.0F).endVertex();
							vertexbuffer
									.pos((double) (f24 + (float) l1 + 0.0F), (double) (f3 + 0.0F),
											(double) (f25 + 0.0F))
									.tex((double) ((f22 + (float) l1 + 0.5F) * 0.00390625F + f17),
											(double) ((f23 + 0.0F) * 0.00390625F + f18))
									.color(Sky.cloudRed / 255, Sky.cloudGreen / 255, Sky.cloudBlue / 255, Sky.cloudAlpha)
									.normal(-1.0F, 0.0F, 0.0F).endVertex();
						}
					}

					if (j1 <= 1) {
						for (int i2 = 0; i2 < 8; ++i2) {
							vertexbuffer
									.pos((double) (f24 + (float) i2 + 1.0F - 9.765625E-4F), (double) (f3 + 0.0F),
											(double) (f25 + 8.0F))
									.tex((double) ((f22 + (float) i2 + 0.5F) * 0.00390625F + f17),
											(double) ((f23 + 8.0F) * 0.00390625F + f18))
									.color(Sky.cloudRed / 255, Sky.cloudGreen / 255, Sky.cloudBlue / 255, Sky.cloudAlpha)
									.normal(1.0F, 0.0F, 0.0F).endVertex();
							vertexbuffer
									.pos((double) (f24 + (float) i2 + 1.0F - 9.765625E-4F), (double) (f3 + 4.0F),
											(double) (f25 + 8.0F))
									.tex((double) ((f22 + (float) i2 + 0.5F) * 0.00390625F + f17),
											(double) ((f23 + 8.0F) * 0.00390625F + f18))
									.color(Sky.cloudRed / 255, Sky.cloudGreen / 255, Sky.cloudBlue / 255, Sky.cloudAlpha)
									.normal(1.0F, 0.0F, 0.0F).endVertex();
							vertexbuffer
									.pos((double) (f24 + (float) i2 + 1.0F - 9.765625E-4F), (double) (f3 + 4.0F),
											(double) (f25 + 0.0F))
									.tex((double) ((f22 + (float) i2 + 0.5F) * 0.00390625F + f17),
											(double) ((f23 + 0.0F) * 0.00390625F + f18))
									.color(Sky.cloudRed / 255, Sky.cloudGreen / 255, Sky.cloudBlue / 255, Sky.cloudAlpha)
									.normal(1.0F, 0.0F, 0.0F).endVertex();
							vertexbuffer
									.pos((double) (f24 + (float) i2 + 1.0F - 9.765625E-4F), (double) (f3 + 0.0F),
											(double) (f25 + 0.0F))
									.tex((double) ((f22 + (float) i2 + 0.5F) * 0.00390625F + f17),
											(double) ((f23 + 0.0F) * 0.00390625F + f18))
									.color(Sky.cloudRed / 255, Sky.cloudGreen / 255, Sky.cloudBlue / 255, Sky.cloudAlpha)
									.normal(1.0F, 0.0F, 0.0F).endVertex();
						}
					}

					if (k1 > -1) {
						for (int j2 = 0; j2 < 8; ++j2) {
							vertexbuffer
									.pos((double) (f24 + 0.0F), (double) (f3 + 4.0F),
											(double) (f25 + (float) j2 + 0.0F))
									.tex((double) ((f22 + 0.0F) * 0.00390625F + f17),
											(double) ((f23 + (float) j2 + 0.5F) * 0.00390625F + f18))
									.color(Sky.cloudRed / 255, Sky.cloudGreen / 255, Sky.cloudBlue / 255, Sky.cloudAlpha)
									.normal(0.0F, 0.0F, -1.0F).endVertex();
							vertexbuffer
									.pos((double) (f24 + 8.0F), (double) (f3 + 4.0F),
											(double) (f25 + (float) j2 + 0.0F))
									.tex((double) ((f22 + 8.0F) * 0.00390625F + f17),
											(double) ((f23 + (float) j2 + 0.5F) * 0.00390625F + f18))
									.color(Sky.cloudRed / 255, Sky.cloudGreen / 255, Sky.cloudBlue / 255, Sky.cloudAlpha)
									.normal(0.0F, 0.0F, -1.0F).endVertex();
							vertexbuffer
									.pos((double) (f24 + 8.0F), (double) (f3 + 0.0F),
											(double) (f25 + (float) j2 + 0.0F))
									.tex((double) ((f22 + 8.0F) * 0.00390625F + f17),
											(double) ((f23 + (float) j2 + 0.5F) * 0.00390625F + f18))
									.color(Sky.cloudRed / 255, Sky.cloudGreen / 255, Sky.cloudBlue / 255, Sky.cloudAlpha)
									.normal(0.0F, 0.0F, -1.0F).endVertex();
							vertexbuffer
									.pos((double) (f24 + 0.0F), (double) (f3 + 0.0F),
											(double) (f25 + (float) j2 + 0.0F))
									.tex((double) ((f22 + 0.0F) * 0.00390625F + f17),
											(double) ((f23 + (float) j2 + 0.5F) * 0.00390625F + f18))
									.color(Sky.cloudRed / 255, Sky.cloudGreen / 255, Sky.cloudBlue / 255, Sky.cloudAlpha)
									.normal(0.0F, 0.0F, -1.0F).endVertex();
						}
					}

					if (k1 <= 1) {
						for (int k2 = 0; k2 < 8; ++k2) {
							vertexbuffer
									.pos((double) (f24 + 0.0F), (double) (f3 + 4.0F),
											(double) (f25 + (float) k2 + 1.0F - 9.765625E-4F))
									.tex((double) ((f22 + 0.0F) * 0.00390625F + f17),
											(double) ((f23 + (float) k2 + 0.5F) * 0.00390625F + f18))
									.color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
							vertexbuffer
									.pos((double) (f24 + 8.0F), (double) (f3 + 4.0F),
											(double) (f25 + (float) k2 + 1.0F - 9.765625E-4F))
									.tex((double) ((f22 + 8.0F) * 0.00390625F + f17),
											(double) ((f23 + (float) k2 + 0.5F) * 0.00390625F + f18))
									.color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
							vertexbuffer
									.pos((double) (f24 + 8.0F), (double) (f3 + 0.0F),
											(double) (f25 + (float) k2 + 1.0F - 9.765625E-4F))
									.tex((double) ((f22 + 8.0F) * 0.00390625F + f17),
											(double) ((f23 + (float) k2 + 0.5F) * 0.00390625F + f18))
									.color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
							vertexbuffer
									.pos((double) (f24 + 0.0F), (double) (f3 + 0.0F),
											(double) (f25 + (float) k2 + 1.0F - 9.765625E-4F))
									.tex((double) ((f22 + 0.0F) * 0.00390625F + f17),
											(double) ((f23 + (float) k2 + 0.5F) * 0.00390625F + f18))
									.color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
						}
					}

					tessellator.draw();
				}
			}
		}

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
		GlStateManager.enableCull();
	}
}
