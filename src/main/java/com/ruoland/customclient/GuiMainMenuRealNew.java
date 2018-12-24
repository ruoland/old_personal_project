package com.ruoland.customclient;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import net.montoyo.mcef.remote.Resource;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import ruo.minigame.api.RenderAPI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GuiMainMenuRealNew extends GuiScreen {
    private static final ResourceLocation SPLASH_TEXTS = new ResourceLocation("texts/splashes.txt");
    private static final Random RANDOM = new Random();
    private static final ResourceLocation defaultResource = new ResourceLocation("textures/gui/title/background/panorama_0.png");

    String splashText;

    public GuiMainMenuRealNew() {
        this.splashText = "missingno";
        IResource iresource = null;

        try {
            List<String> list = Lists.<String>newArrayList();
            iresource = Minecraft.getMinecraft().getResourceManager().getResource(SPLASH_TEXTS);
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(iresource.getInputStream(), Charsets.UTF_8));
            String s;

            while ((s = bufferedreader.readLine()) != null) {
                s = s.trim();

                if (!s.isEmpty()) {
                    list.add(s);
                }
            }

            if (!list.isEmpty()) {
                while (true) {
                    this.splashText = list.get(RANDOM.nextInt(list.size()));

                    if (this.splashText.hashCode() != 125780783) {
                        break;
                    }
                }
            }
        } catch (IOException exc) {

        } finally {
            IOUtils.closeQuietly((Closeable) iresource);
        }
    }

    private GuiButton realmsButton, modButton;

    public void initGui() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24) {
            this.splashText = "Merry X-mas!";
        } else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1) {
            this.splashText = "Happy new year!";
        } else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31) {
            this.splashText = "OOoooOOOoooo! Spooky!";
        }

        int i = 104;
        int j = this.height / 4 + 48;
        this.buttonList.add(new GuiCusButton(1, this.width / 2 - 100, j, I18n.format("menu.singleplayer", new Object[0])));
        this.buttonList.add(new GuiCusButton(2, this.width / 2 - 100, j + 24 * 1, I18n.format("menu.multiplayer", new Object[0])));
        this.realmsButton = this.addButton(new GuiCusButton(14, this.width / 2 + 2, j + 24 * 2, 98, 20, I18n.format("menu.online", new Object[0]).replace("Minecraft", "").trim()));
        this.buttonList.add(modButton = new GuiCusButton(6, this.width / 2 - 100, j + 24 * 2, 98, 20, I18n.format("fml.menu.mods")));

        this.buttonList.add(new GuiCusButton(0, this.width / 2 - 100, i + 84, 98, 20, I18n.format("menu.options", new Object[0])));
        this.buttonList.add(new GuiCusButton(4, this.width / 2 + 2, i + 84, 98, 20, I18n.format("menu.quit", new Object[0])));
        this.buttonList.add(new GuiButtonLang(5, this.width / 2 - 124, i + 72 + 12));
        CustomTool.instance.initGui();
    }


    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 200) {
            if (button.displayString.equals("false"))
                button.displayString = "true";
            else if (button.displayString.equals("true"))
                button.displayString = "false";
        }
        if (button.id == 10) {
            CustomTool customTool = CustomTool.instance;
            File file = (CustomTool.fileChooser());
            if (file != null) {
                FileUtils.copyFile(file.getAbsoluteFile(), new File("./resourcepacks/CustomClient/assets/customclient/textures/gui/" + file.getName()));
                ResourceLocation chooser = RenderAPI.getDynamicTexture(file.getName(), file);
                String name = "customclient:textures/gui/" + file.getName();
                if (CustomTool.instance.selectButton != -1) {
                    CustomTool.instance.setDynamicButtonField(chooser, name);
                } else if (customTool.isBackgroundEdit()) {
                    CustomTool.instance.setDynamicBackgroundImage(chooser.toString(), name);
                } else {
                    CustomTool.instance.setDynamicTextureField(RenderAPI.getDynamicTexture(file.getName(), file), name);
                }
            }
        }
        if (button.id == 51) {
            CustomTool.instance.addTexture();
        }
        if (CustomTool.instance.isEditMode()) {
            return;
        }
        if (button.id == 50) {
            return;
        }
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }

        if (button.id == 5) {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }

        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiWorldSelection(this));
        }
        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if (button.id == 4) {
            CustomTool.closeBrowser();
            this.mc.shutdown();
        }
        if (button.id == 6) {
            this.mc.displayGuiScreen(new net.minecraftforge.fml.client.GuiModList(this));
        }
    }

    public List<GuiButton> getButton() {
        return this.buttonList;
    }

    public String getSplashText() {
        return splashText;
    }

    int delay = 0;

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        String texture = CustomTool.instance.backgroundImage;

        if (texture.startsWith("http") || texture.startsWith("Http") || texture.startsWith("www.youtube") || texture.startsWith("youtube.com")) {
            if (texture.indexOf("youtube") != -1) {
                CustomTool.instance.drawBrowser(texture, width, height);
            } else {
                CustomTool.instance.backgroundImage = (texture);
                CustomTool.instance.drawBrowser(texture, width, height);
            }
        } else {
            RenderAPI.drawTextureZ(texture, 0, 0, -100, mc.displayWidth / 2, mc.displayHeight / 2);
            RenderAPI.drawTextureZ(texture, 0, 0, -100, mc.displayWidth / 2, mc.displayHeight / 2);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) (this.width / 2 + 90), 70.0F, 0.0F);
        GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
        float f = 1.8F - MathHelper.abs(MathHelper.sin((float) (Minecraft.getSystemTime() % 1000L) / 1000.0F * ((float) Math.PI * 2F)) * 0.1F);
        f = f * 100.0F / (float) (this.fontRendererObj.getStringWidth(this.splashText) + 32);
        GlStateManager.scale(f, f, f);
        this.drawCenteredString(this.fontRendererObj, this.splashText, 0, -8, -256);
        GlStateManager.popMatrix();
        if (CustomTool.instance.isGradient()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, -100);
            this.drawGradientRect(0, 0, width, height, -2130706433, 16777215);
            this.drawGradientRect(0, 0, width, height, 0, Integer.MIN_VALUE);
            GlStateManager.popMatrix();
        }
        this.drawString(this.fontRendererObj, "Copyright Mojang AB. Do not distribute!", this.width - this.fontRendererObj.getStringWidth("Copyright Mojang AB. Do not distribute!") - 2, this.height - 10, -1);
        CustomTool.instance.drawScreen(width, height);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) throws IOException {
        super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        CustomTool.instance.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
    }

    @Override
    protected void mouseClickMove(int p_146273_1_, int p_146273_2_, int p_146273_3_, long p_146273_4_) {
        super.mouseClickMove(p_146273_1_, p_146273_2_, p_146273_3_, p_146273_4_);
        CustomTool.instance.mouseClickMove(p_146273_1_, p_146273_2_, p_146273_3_, p_146273_4_);
        drawTexturedModalRect(p_146273_3_, p_146273_3_, p_146273_3_, p_146273_3_, p_146273_3_, p_146273_3_);
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char p_73869_1_, int p_73869_2_) throws IOException {
        super.keyTyped(p_73869_1_, p_73869_2_);
        CustomTool.instance.keyTyped(p_73869_1_, p_73869_2_);
        System.out.println("키누름");
    }

    @Override
    public void onGuiClosed() {
        CustomTool.instance.configsave();
        CustomTool.closeBrowser();
        super.onGuiClosed();
    }
}
