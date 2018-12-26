package com.ruoland.customclient;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import ruo.minigame.api.RenderAPI;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class GuiMainMenuRealNew extends GuiCustomBase {
    private static final ResourceLocation SPLASH_TEXTS = new ResourceLocation("texts/splashes.txt");
    private static final Random RANDOM = new Random();

    String splashText;

    public GuiMainMenuRealNew(String name) {
        super(name);
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
        customTool.initGui(this);

    }


    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 6) {
            this.mc.displayGuiScreen(new net.minecraftforge.fml.client.GuiModList(this));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        customTool.keyTyped(typedChar, keyCode);
        System.out.println("키누름");
    }

    public String getSplashText() {
        return splashText;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        this.drawString(this.fontRendererObj, "Copyright Mojang AB. Do not distribute!", this.width - this.fontRendererObj.getStringWidth("Copyright Mojang AB. Do not distribute!") - 2, this.height - 10, -1);

        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) (this.width / 2 + 90), 70.0F, 0.0F);
        GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
        float f = 1.8F - MathHelper.abs(MathHelper.sin((float) (Minecraft.getSystemTime() % 1000L) / 1000.0F * ((float) Math.PI * 2F)) * 0.1F);
        f = f * 100.0F / (float) (this.fontRendererObj.getStringWidth(this.splashText) + 32);
        GlStateManager.scale(f, f, f);
        this.drawCenteredString(this.fontRendererObj, this.splashText, 0, -8, -256);
        GlStateManager.popMatrix();
    }


}
