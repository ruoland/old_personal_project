package cmplus.cm.v18.customgui;

import cmplus.cm.v18.function.Function;
import olib.api.DrawTexture;
import olib.api.RenderAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiCustom extends GuiScreen {
	public GuiCustom(String name) {
		new CustomGUITool(name);
		Minecraft.getMinecraft().mouseHelper.ungrabMouseCursor();
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		CustomGUITool.instance.drawScreen(p_73863_1_, p_73863_2_);
	}

	@Override
	protected void mouseClickMove(int p_146273_1_, int p_146273_2_, int p_146273_3_, long p_146273_4_) {
		super.mouseClickMove(p_146273_1_, p_146273_2_, p_146273_3_, p_146273_4_);
		CustomGUITool.instance.mouseClickMove(p_146273_1_, p_146273_2_, p_146273_3_, p_146273_4_);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		CustomGUITool.instance.mouseClicked(mouseX, mouseY, mouseButton);
		if (!CustomGUITool.instance.isEditMode()) {
			Function function = Function.addFunction(CustomGUITool.instance.getName() + "-마우스클릭", "@클릭X", "" + mouseX, "@클릭Y",
					"" + mouseY, "@클릭", "" + mouseButton);
			function(function);
			function.runScript();
		}
	}

	public void initGui() {
		CustomGUITool.instance.initGui(width, height);
		if (!CustomGUITool.instance.isEditMode()) {
			Function function = Function.addFunction("GUI", CustomGUITool.instance.getName() + "-켜짐");
			function(function);
			function.runScript();
		}
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) throws IOException {
		super.keyTyped(p_73869_1_, p_73869_2_);
		CustomGUITool.instance.keyTyped(p_73869_1_, p_73869_2_);
		if (!CustomGUITool.instance.isEditMode()) {
			Function function = Function.addFunction(CustomGUITool.instance.getName() + "-키입력", "@키코드", "" + p_73869_2_);
			function(function);
			function.runScript();
		}
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) throws IOException {
		super.actionPerformed(p_146284_1_);
		GuiCusButton button = (GuiCusButton) p_146284_1_;
		CustomGUITool.instance.actionPerformed(button);
		if (!CustomGUITool.instance.isEditMode() && (button.command != null) && !button.command.equals("")) {
			Function function = Function.addFunction(button.command, "@아이디", "" + p_146284_1_.id, "@이름",
					p_146284_1_.displayString, "@버튼X", "" + p_146284_1_.xPosition, "@버튼Y", "" + p_146284_1_.yPosition);
			function(function);
			function.runScript();
		}
	}

	public List<GuiButton> getButton() {
		return this.buttonList;
	}

	public List<GuiTexture> getTexture() {
		return CustomGUITool.instance.textureList;
	}

	public List<GuiString> getString() {
		return CustomGUITool.instance.stringList;
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
	}

	/**
	 * 펑션에서 다른 버튼과 이미지를 다룰 수 있게 하기 위해서
	 */
	public void function(Function function) {
		for (GuiButton button : getButton()) {
			function.replace.put("@버튼" + button.id + "이름", button.displayString);
			function.replace.put("@버튼" + button.id + "X", "" + button.xPosition);
			function.replace.put("@버튼" + button.id + "Y", "" + button.yPosition);
			function.replace.put("@버튼" + button.id + "넓이", "" + button.width);
			function.replace.put("@버튼" + button.id + "높이", "" + button.height);
		}
		for (GuiString button : getString()) {
			function.replace.put("@문자" + button.id + "이름", button.str);
			function.replace.put("@문자" + button.id + "X", "" + button.x);
			function.replace.put("@문자" + button.id + "Y", "" + button.y);

		}
		for (GuiTexture button : getTexture()) {
			function.replace.put("@이미지" + button.id + "이름", "" + button.id);
			function.replace.put("@이미지" + button.id + "X", "" + button.x);
			function.replace.put("@이미지" + button.id + "Y", "" + button.y);
			function.replace.put("@이미지" + button.id + "넓이", "" + button.width);
			function.replace.put("@이미지" + button.id + "높이", "" + button.height);

		}
	}

	public static class CustomTextField extends GuiTextField {
		static int id = 0;
		public static ArrayList<GuiTextField> fieldList = new ArrayList<GuiTextField>();
		public static ArrayList<GuiTextField> textureFieldList = new ArrayList<GuiTextField>();
		public static ArrayList<GuiTextField> strFieldList = new ArrayList<GuiTextField>();

		public CustomTextField(int x, int y, int par5Width, int par6Height) {
			super(id++, Minecraft.getMinecraft().fontRendererObj, x, y, par5Width, par6Height);
			this.setMaxStringLength(6000);
		}

		public CustomTextField but() {
			this.fieldList.add(this);
			return this;
		}

		public CustomTextField tex() {
			this.textureFieldList.add(this);
			return this;
		}

		public GuiTextField str() {
			strFieldList.add(this);
			return this;
		}

		public static void key(char p_73869_1_, int p_73869_2_) {
			for (GuiTextField f : CustomTextField.fieldList) {
				f.textboxKeyTyped(p_73869_1_, p_73869_2_);
			}
			for (GuiTextField f : CustomTextField.textureFieldList) {
				f.textboxKeyTyped(p_73869_1_, p_73869_2_);
			}
			for (GuiTextField f : strFieldList) {
				f.textboxKeyTyped(p_73869_1_, p_73869_2_);
			}
		}

		public static boolean mouse(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
			boolean isFocused = false;
			for (GuiTextField f : CustomTextField.fieldList) {
				f.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
				if (f.isFocused())
					isFocused = true;
			}

			for (GuiTextField f : textureFieldList) {
				f.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
				if (f.isFocused())
					isFocused = true;
			}
			for (GuiTextField f : strFieldList) {
				f.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
				if (f.isFocused())
					isFocused = true;
			}
			return isFocused;
		}

		public static void renderTextbox() {
			for (GuiTextField f : CustomTextField.fieldList) {
				GL11.glPushMatrix();
				GL11.glTranslatef(0, 0, 2);
				f.drawTextBox();
				GL11.glPopMatrix();
			}
			for (GuiTextField f : CustomTextField.textureFieldList) {
				GL11.glPushMatrix();
				GL11.glTranslatef(0, 0, 2);
				f.drawTextBox();
				GL11.glPopMatrix();
			}
			for (GuiTextField f : CustomTextField.strFieldList) {
				GL11.glPushMatrix();
				GL11.glTranslatef(0, 0, 2);
				f.drawTextBox();
				GL11.glPopMatrix();
			}
		}
	}

	public static class GuiCusButton extends GuiButton {
		public ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
		public String command;

		public GuiCusButton(int p_i1021_1_, int p_i1021_2_, int p_i1021_3_, int p_i1021_4_, int p_i1021_5_,
				String p_i1021_6_, String command) {
			super(p_i1021_1_, p_i1021_2_, p_i1021_3_, p_i1021_4_, p_i1021_5_, p_i1021_6_);
			this.command = command;
		}

		public GuiCusButton(int p_i1020_1_, int p_i1020_2_, int p_i1020_3_, String p_i1020_4_) {
			this(p_i1020_1_, p_i1020_2_, p_i1020_3_, 200, 20, p_i1020_4_, null);
		}

		/**
		 * Draws this button to the screen.
		 */
		private Minecraft mc = Minecraft.getMinecraft();
		float updateCounter;

		public void drawButton(Minecraft mc, int mouseX, int mouseY) {
			if (buttonTextures.toString().startsWith("https://") || buttonTextures.toString().startsWith("http://")) {
				File f = new File("resourcepacks/CustomClient/assets/customclient/textures/gui/");
				f.mkdirs();
				buttonTextures = new ResourceLocation(CMResourcePack.imageDownload(buttonTextures.toString()));
			}
			if (this.visible) {
				FontRenderer fontrenderer = mc.fontRendererObj;
				mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition
						&& mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
				int i = this.getHoverState(this.hovered);
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
						GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
						GlStateManager.DestFactor.ZERO);
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
						GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
				if (buttonTextures.toString().equals("minecraft:textures/gui/title/minecraft.png")) {
					Tessellator tessellator = Tessellator.getInstance();
					short short1 = 274;
					int k1 = width / 2 - short1 / 2;
					byte b0 = (byte) (height / 2 - 30);
					this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/title/minecraft.png"));
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

					if ((double) updateCounter < 1.0E-4D) {
						drawTexturedModalRect(k1 + 0, b0 + 0, 0, 0, 99, 44);
						drawTexturedModalRect(k1 + 99, b0 + 0, 129, 0, 27, 44);
						drawTexturedModalRect( k1 + 99 + 26, b0 + 0, 126, 0, 3, 44);
						drawTexturedModalRect( k1 + 99 + 26 + 3, b0 + 0, 99, 0, 26, 44);
						drawTexturedModalRect(k1 + 155, b0 + 0, 0, 45, 155, 44);
					} else {
						drawTexturedModalRect(k1 + 0, b0 + 0, 0, 0, 155, 44);
						drawTexturedModalRect( k1 + 155, b0 + 0, 0, 45, 155, 44);
					}
				} else if (!buttonTextures.toString().equals("minecraft:textures/gui/widgets.png")) {
					RenderAPI.drawTexture(new DrawTexture.Builder().setTexture(buttonTextures).setXY( this.xPosition, this.yPosition).setSize(this.width,
							this.height).build());

				} else {
					this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + i * 20, this.width / 2,
							this.height);
					this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2,
							46 + i * 20, this.width / 2, this.height);
				}
				this.mouseDragged(mc, mouseX, mouseY);
				int j = 14737632;

				if (packedFGColour != 0) {
					j = packedFGColour;
				} else if (!this.enabled) {
					j = 10526880;
				} else if (this.hovered) {
					j = 16777120;
				}

				this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2,
						this.yPosition + (this.height - 8) / 2, j);
			}
		}
	}
}
