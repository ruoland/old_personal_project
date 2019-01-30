package customclient.component;

import customclient.GuiCustomBase;
import customclient.RenderAPI;
import customclient.URLDownload;
import customclient.button.ButtonBucket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.io.File;

public class GuiCusButton extends GuiButton implements IGuiComponent {
    public boolean canEdit = true, displayStringVisible = true;
    public ButtonBucket buttonBucket;
    public float alpha = 1F;
    public boolean isLock;
    public ResourceLocation dynamicLocation;
    public ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");

    public GuiCusButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiCusButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        this(buttonId, x, y, widthIn, heightIn, buttonText, true);
    }

    public GuiCusButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, boolean canEdit) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.canEdit = canEdit;
        visible = canEdit;
        if (!canEdit)
            alpha = 0.5F;
        buttonBucket = new ButtonBucket(this);
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        GuiCustomBase customBase = (GuiCustomBase) mc.currentScreen;
        if (alpha > 1F)
            alpha = 0.99F;
        if (buttonTextures.toString().startsWith("https://") || buttonTextures.toString().startsWith("http://")) {
            File f = new File("resourcepacks/CustomClient/assets/customclient/textures/gui/");
            f.mkdirs();
            buttonTextures = new ResourceLocation("customclient:textures/gui/" + URLDownload.download("CustomClient", buttonTextures.toString(),
                    "resourcepacks/CustomClient/assets/customclient/textures/gui/"));
        }

        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            if (!buttonTextures.toString().equals("minecraft:textures/gui/widgets.png")) {
                if (dynamicLocation != null)
                    RenderAPI.drawTexture(dynamicLocation.toString(), alpha, this.xPosition, this.yPosition, this.width, this.height);
                else
                    RenderAPI.drawTexture(buttonTextures.toString(), alpha, this.xPosition, this.yPosition, this.width, this.height);
            } else {
                this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + i * 20, this.width / 2, this.height);
                this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
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
            if (displayStringVisible)
                this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
        }
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return this.enabled && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    public NBTTagCompound serializeNBT(GuiScreen menuRealNew) {
        int i2 = menuRealNew.height / 4 + 48;
        NBTTagCompound compound = new NBTTagCompound();
        if (displayString.equalsIgnoreCase(""))
            displayString = "Language";
        compound.setString("Button", displayString);
        compound.setString("Texture", buttonTextures.toString());
        compound.setInteger("xPosition", xPosition);
        compound.setInteger("yPosition", yPosition);
        compound.setInteger("Width", width);
        compound.setInteger("Height", height);
        compound.setBoolean("Visible", visible);
        compound.setBoolean("displayStringVisible", displayStringVisible);
        compound.setFloat("alpha", alpha);
        compound.setBoolean("isLock", isLock);
        compound.setString("function", buttonBucket.getAllScript());
        System.out.println(buttonBucket.getAllScript());
        return compound;
    }

    public void deserializeNBT(NBTTagCompound compound) {
        if (compound.hasKey("Button")) {
            displayString = compound.getString("Button");
            buttonTextures = new ResourceLocation(compound.getString("Texture"));
            xPosition = compound.getInteger("xPosition");
            yPosition = compound.getInteger("yPosition");
            width = compound.getInteger("Width");
            height = compound.getInteger("Height");
            visible = compound.getBoolean("Visible");
            if (compound.hasKey("alpha"))
                alpha = compound.getFloat("alpha");
            if (compound.hasKey("displayStringVisible"))
                displayStringVisible = compound.getBoolean("displayStringVisible");
            isLock = compound.getBoolean("isLock");
            if (compound.hasKey("function")) {
                System.out.println("[커스텀 버튼]"+displayString+" NBT에서 스크립트 불러오는 중");
                buttonBucket.read(compound.getString("function"));
                System.out.println("[커스텀 버튼]"+displayString+" NBT에서 스크립트 로딩이 끝남");
            }
        }

    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        this.enabled = visible;
    }

    @Override
    public int getX() {
        return xPosition;
    }

    @Override
    public int getY() {
        return yPosition;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public boolean isLock() {
        return isLock;
    }

    @Override
    public void setLock(boolean lock) {
        isLock = lock;
    }

    @Override
    public ResourceLocation getTexture() {
        return buttonTextures;
    }

    @Override
    public void setTexture(ResourceLocation resourceLocation) {
        this.buttonTextures = resourceLocation;
    }

}
