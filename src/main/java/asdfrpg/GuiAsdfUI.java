package ruo.asdfrpg;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import ruo.asdfrpg.skill.system.SkillHelper;
import ruo.cmplus.deb.DebAPI;
import ruo.minigame.api.RenderAPI;

import java.io.IOException;

public class GuiAsdfUI extends GuiScreen {
    static DebAPI back = DebAPI.createDebAPI("back", 4.6, 213, 0);//체력 뒷배경 z값이랑 슬롯 Z값이랑 공유함
    static DebAPI health = DebAPI.createDebAPI("health", 5.8, 215.3, 7.8);//체력 z 값이랑 배고픔 z 값이랑 서로 공유함
    static DebAPI food = DebAPI.createDebAPI("food", 5.8, 222.8, 0);
    static DebAPI exp = DebAPI.createDebAPI("exp", 0, 235, 10);
    static DebAPI hotbar = DebAPI.createDebAPI("hot", 142,25.5, 9.8);
    static DebAPI uiback = DebAPI.createDebAPI("ui", 0, 207.4, 40);
    static DebAPI slot = DebAPI.createDebAPI("slot", 69, 209.9, 23);
    private DebAPI cur;
    @Override
    public void initGui() {
        super.initGui();
        cur = DebAPI.get();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        //this.drawDefaultBackground();

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        RenderAPI.drawTextureZ("asdfrpg:uibackground.png", uiback.x, uiback.y, 100,  width, 40);
        RenderAPI.drawTexture("asdfrpg:backgroundbar.png", back.x, back.y, player.getMaxHealth() * 3, slot.z);
        RenderAPI.drawTexture("asdfrpg:healthbar.png", health.x, health.y, player.getHealth() * 3 - 2, health.z);
        RenderAPI.drawTexture("asdfrpg:foodbar.png", food.x, food.y, (player.getFoodStats().getFoodLevel()) * 3 - 2, health.z);
        RenderAPI.drawTexture("asdfrpg:expbar.png", exp.x, exp.y, (SkillHelper.getPlayerSkill().getPlayerExp()) * 10, exp.z);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0,0,101);
        fontRendererObj.drawString("체력 ",(int) (health.x - 20), (int) health.y,0xFFFFFF);
        fontRendererObj.drawString("배고픔 ",(int) food.x - 20, (int) food.y,0xFFFFFF);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
            GlStateManager.pushMatrix();
            RenderAPI.drawTextureZ("asdfrpg:backgroundbar.png", slot.x+(i * 20), slot.y, 101, 19 ,slot.z);
            GlStateManager.popMatrix();
            RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
            itemRender.zLevel = 200.0F;
            net.minecraft.client.gui.FontRenderer font = null;
            if (stack != null) font = stack.getItem().getFontRenderer(stack);
            if (font == null) font = mc.fontRendererObj;
            if (stack != null) {
                RenderHelper.enableGUIStandardItemLighting();
                itemRender.renderItemAndEffectIntoGUI(stack, (int) (width / 2 - hotbar.x) + (i * 20), (int) (height - hotbar.y));
                itemRender.renderItemOverlayIntoGUI(font, stack, (int) (width / 2 - hotbar.x) + (i * 20), (int) (height - hotbar.y), null);
                RenderHelper.disableStandardItemLighting();
            }
            itemRender.zLevel = 0.0F;
        }
        GlStateManager.popMatrix();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (DebAPI.activeName != null && DebAPI.debAPI.containsKey(DebAPI.activeName)) {
            double speed = 0.1;
            if(Keyboard.isKeyDown(Keyboard.KEY_Z)){
                speed+=0.1;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                cur.x+=speed;
                cur.println();
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                cur.x-=speed;
                cur.println();

            }
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                    cur.z += speed;
                    cur.println();

                }
                if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                    cur.z -= speed;
                    cur.println();

                }
            }else {
                if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                    cur.y -= speed;
                    cur.println();
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                    cur.y += speed;
                    cur.println();
                }
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        if (DebAPI.activeName != null && DebAPI.debAPI.containsKey(DebAPI.activeName)) {
            DebAPI.get().x = mouseX;
            DebAPI.get().y = mouseY;
            System.out.println(DebAPI.activeName + " - " + mouseX + " - " + mouseY);
        }
    }
}
