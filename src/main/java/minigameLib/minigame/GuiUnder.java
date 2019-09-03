package minigameLib.minigame;

import net.minecraft.client.Minecraft;
import olib.api.DrawTexture;
import olib.api.RenderAPI;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiUnder extends GuiScreen {
    public GuiUnder(){
        this.mc = Minecraft.getMinecraft();
    }
    private int playerX = 10, playerY = 220;
    private int textBoxX = 10, textBoxY = 120, textBoxWidth = 406, textBoxHeight = 88;
    private boolean attackMode;
    private int select;


    public void selectButton(){
        if (select == 0) {
            attackMode = true;
        }
        if (select == 1) {
            attackMode = true;
        }
        if (select == 2) {
            attackMode = true;
        }
    }

    private static final ResourceLocation location = new ResourceLocation("scroll:textures/darkscreen.png");
    private StringBuffer currentText = new StringBuffer(), monsterName = new StringBuffer("");
    private StringBuffer renderText = new StringBuffer("");
    private int messageTick = 10;
    private boolean updateMessage;
    private int stickX, stickY;
    private int renderTick = 20;//렌더틱. 몬스터를 천천히 투명하게 만들 때 쓰임
    private float mobTextureAlpha = 1F, mobHealth = 20;
    private boolean isDead = false;

    public void updateMessage(String monsterName, String um){
        renderText = new StringBuffer("");
        updateMessage = true;
        currentText.append(monsterName).append(um);
        messageTick = 10;
    }
    public void setMonster(EntityLivingBase base){
        updateMessage(base.getCustomNameTag(), "가 나타났다.");
        mobHealth = base.getHealth();
        monsterName = new StringBuffer(base.getCustomNameTag());
    }

    public void resetBox(){

    }

    public void setDead(){
        isDead = true;
        textBoxWidth = 406;
        textBoxHeight = 88;
        textBoxX = 10;
        textBoxY = 120;
        updateMessage(monsterName.toString(), "는(은) 사라졌다.");
    }


    public void playerAttack(){
        stickX = playerX + 10;
        stickY = playerY + 5;
        attackMode = true;
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        this.mc.getTextureManager().bindTexture(location);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        vertexbuffer.pos(0.0D, (double) this.height, 0.0D).tex(0.0D, (double) (float) this.height / 32.0F)
                .color(0, 0, 0, 255).endVertex();
        vertexbuffer.pos((double) this.width, (double) this.height, 0.0D)
                .tex((double) ((float) this.width / 32.0F), (double) ((float) this.height / 32.0F + 0F))
                .color(0, 0, 0, 255).endVertex();
        vertexbuffer.pos((double) this.width, 0.0D, 0.0D).tex((double) ((float) this.width / 32.0F), 0D)
                .color(0, 0, 0, 255).endVertex();
        vertexbuffer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0D).color(64, 64, 64, 255).endVertex();
        tessellator.draw();
        if (updateMessage) {
            if (messageTick != -1)
                messageTick--;
            if (messageTick == 0) {
                messageTick = 10;
                renderText.append(currentText.substring(renderText.length(), renderText.length() + 1));
                if(currentText.length() == renderText.length())
                    updateMessage = false;
            }

        }
        GlStateManager.pushMatrix();
        GlStateManager.scale(2, 2, 2);
        RenderAPI.drawString(renderText.toString(), 13, 65, 0xFFFFFF);
        GlStateManager.popMatrix();
        //RenderAPI.drawModel(CREEPER_TEXTURES, new ModelCreeper(), 220, 30, 0, 180, 0.9F, 1);
        if (attackMode) {
            if (textBoxWidth <= 162) {
                if (Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP) && playerY > 128) {
                    playerY -= 2;
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN) && playerY < 180) {
                    playerY += 2;
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT) && playerX > 164) {
                    playerX -= 2;
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && playerX < 258) {
                    playerX += 2;
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_Z) || Keyboard.isKeyDown(Keyboard.KEY_X)) {
                    playerAttack();
                }
            } else if (!isDead) {
                if (playerX < 210) {
                    playerX += 10;
                }
                if (playerY > 152) {
                    playerY -= 8;
                }
            }
            if(!isDead) {
                if (textBoxX < 162) {
                    textBoxX += 4.000;
                }

                if (textBoxWidth > 130) {
                    textBoxWidth -= 10;
                }
            }
        }
        if (attackMode) {

            RenderAPI.drawTexture(new DrawTexture.Builder().setTexture("scroll:textures/stick.png").setAlpha(1.0F).setXY(stickX, stickY).setSize(10, 30).build());
            stickY -= 3;
            if (stickX > 187 && stickX < 230 && stickY < 120) {
                System.out.println("attack");
                mobHealth -= 5;
                if (mobHealth <= 0) {
                    setDead();
                }
                attackMode = false;
            }
        }

        if (isDead && (renderTick == 10 || renderTick == 0 || renderTick == 20) && mobTextureAlpha > 0.0F)
            mobTextureAlpha -= 0.1F;
        RenderAPI.drawTexture(new DrawTexture.Builder().setTexture("scroll:textures/textBox.png").setAlpha(1.0F).setXY(textBoxX, textBoxY).setSize(textBoxWidth, textBoxHeight).build());
        RenderAPI.drawTexture(new DrawTexture.Builder().setTexture("scroll:textures/creeper.png").setAlpha(mobTextureAlpha).setXY(202, 20).setSize(30, 100).build());
        RenderAPI.drawTexture(new DrawTexture.Builder().setTexture("scroll:textures/asdf.png").setAlpha(1).setXY(playerX, playerY).setSize(30, 20).build());
        RenderAPI.drawString(mc.thePlayer.getName(), 40, 210, 0xFFFFFF);
        RenderAPI.drawString("LV " + mc.thePlayer.experienceLevel, 80, 210, 0xFFFFFF);
        RenderAPI.drawString("HP" + mc.thePlayer.getHealth(), 200, 210, 0xFFFFFF);
        RenderAPI.drawString(" / " + mc.thePlayer.getMaxHealth(), 220, 210, 0xFFFFFF);
        RenderAPI.drawTexture(new DrawTexture.Builder().setTexture("scroll:textures/attack.png").setAlpha(1).setXY(21 - 3, 215).setSize(50, 30).build());
        RenderAPI.drawTexture(new DrawTexture.Builder().setTexture("scroll:textures/attack.png").setAlpha(1).setXY(131 - 3, 215).setSize(50, 30).build());
        RenderAPI.drawTexture(new DrawTexture.Builder().setTexture("scroll:textures/attack.png").setAlpha(1).setXY(241 - 3, 215).setSize(50, 30).build());
        RenderAPI.drawTexture(new DrawTexture.Builder().setTexture("scroll:textures/attack.png").setAlpha(1).setXY( 351 - 3,215).setSize(50, 30).build());

        if (select != 0)
            RenderAPI.drawTexture(new DrawTexture.Builder().setTexture("scroll:textures/diamond_sword.png").setAlpha(1).setXY( 24 - 3,222).setSize(16, 16).build());

        if (select != 1)
            RenderAPI.drawTexture(new DrawTexture.Builder().setTexture("scroll:textures/diamond_sword.png").setAlpha(1).setXY( 134 - 3,222).setSize(16, 16).build());

        if (select != 2)
            RenderAPI.drawTexture(new DrawTexture.Builder().setTexture("scroll:textures/diamond_sword.png").setAlpha(1).setXY( 244 - 3,222).setSize(16, 16).build());

        if (select != 3)
            RenderAPI.drawTexture(new DrawTexture.Builder().setTexture("scroll:textures/diamond_sword.png").setAlpha(1).setXY( 354 - 3,222).setSize(16, 16).build());

        if (mobTextureAlpha <= 0)
            this.mc.displayGuiScreen((GuiScreen) null);

        renderTick--;
        if (renderTick == 0)
            renderTick = 20;
    }


    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }


    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (attackMode) {
            if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
                //textBoxWidth -= 0.1;
                System.out.println("Boxwidth" + textBoxWidth);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
                //textBoxWidth += 0.1;
                System.out.println("Boxwidth" + textBoxWidth);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
                System.out.println("BoxX" + textBoxX);
                //textBoxX += 1.0;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
                System.out.println("BoxX" + textBoxX);
                //textBoxX -= 0.1;
            }
        }
        if (!attackMode) {
            if (Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && select != 4) {
                select++;
                playerX += 110;
                System.out.println("Select:" + select);

            }
            if (Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT) && select != 0) {
                select--;
                playerX -= 110;
                System.out.println("Select:" + select);
            }
            if (select >= 4) {
                select = 0;
                playerX = 10;
            }
            if (select <= -1) {
                select = 3;
                playerX = 340;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
            selectButton();
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {

        super.actionPerformed(button);
    }

}
