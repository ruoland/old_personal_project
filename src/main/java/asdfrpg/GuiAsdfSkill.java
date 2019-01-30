package ruo.asdfrpg;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import ruo.asdfrpg.skill.system.PlayerSkill;
import ruo.asdfrpg.skill.system.Skill;
import ruo.asdfrpg.skill.system.SkillHelper;
import ruo.asdfrpg.skill.system.SkillStack;
import ruo.minigame.api.RenderAPI;

import java.io.IOException;

public class GuiAsdfSkill extends GuiScreen {
    private EntityPlayer player;
    private PlayerSkill playerSkill;
    private double max = 20, cur = 20;

    public GuiAsdfSkill(EntityPlayer player) {
        mc = Minecraft.getMinecraft();
        fontRendererObj = mc.fontRendererObj;
        this.player = player;
        playerSkill = SkillHelper.getPlayerSkill(player);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        for (Skill skill : SkillHelper.getSkillList()) {
            RenderAPI.drawTexture("asdfrpg:" + skill.getUnlocalizedName().toLowerCase() + ".png", skill.lineX(), skill.lineY(), 32, 32);
            if (!playerSkill.isRegister(skill)) {
                RenderAPI.drawTexture("asdfrpg:skill.png", skill.lineX(), skill.lineY(), 32, 32);
            } else {
                SkillStack stack = playerSkill.getSkill(skill);
                mc.fontRendererObj.drawString("레벨" + stack.getLevel(), (int) skill.lineX(), (int) skill.lineY() - 10, 0xFFFFFF);
                mc.fontRendererObj.drawString("이름" + stack.getSkill().getLocalizedName(), (int) skill.lineX(), (int) skill.lineY() - 20, 0xFFFFFF);

                RenderAPI.drawTexture("asdfrpg:bar.png", skill.lineX(), skill.lineY(), 32 / (stack.getMaxExp() - stack.getExp()), 8);
            }
        }

    }

    @Override
    public void handleKeyboardInput() throws IOException {
        super.handleKeyboardInput();
        if (Keyboard.isKeyDown(Keyboard.KEY_H))
            max++;
        if (Keyboard.isKeyDown(Keyboard.KEY_N))
            cur--;
        if (Keyboard.isKeyDown(Keyboard.KEY_M))
            cur--;
        System.out.println(max + " - " + cur);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        for (Skill skill : SkillHelper.getSkillList()) {
            SkillStack stack = SkillHelper.getPlayerSkill(mc.thePlayer).getSkill(skill);
            if (skillClick(skill, mouseX, mouseY)) {
                stack.addExp();
                System.out.println(stack.getSkill().getLocalizedName());
                System.out.println("aa " + (32 / (stack.getMaxExp() - stack.getExp())));
                System.out.println(stack.getMaxExp()+" - "+stack.getExp());
            }
        }
    }

    public boolean skillClick(Skill skill, int mouseX, int mouseY) {
        return SkillHelper.getPlayerSkill(mc.thePlayer).isRegister(skill) && mouseX >= skill.lineX() && mouseY >= skill.lineY() && mouseX < skill.lineX() + 32 && mouseY < skill.lineY() + 32;

    }
}
