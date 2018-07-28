package ruo.asdfrpg;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import ruo.asdfrpg.skill.SkillHelper;
import ruo.asdfrpg.skill.Skill;
import ruo.minigame.api.RenderAPI;

public class GuiAsdfSkill extends GuiScreen
{

    public GuiAsdfSkill(){
        mc = Minecraft.getMinecraft();
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        for(Skill skill : SkillHelper.getSkillList()) {
            RenderAPI.drawTexture("asdfrpg:"+skill.getUnlocalizedName().toLowerCase()+".png", skill.lineX(), skill.lineY(), 32, 32);
            if(!SkillHelper.getPlayerSkill(mc.thePlayer.getUniqueID()).isRegister(skill)){
                RenderAPI.drawTexture("asdfrpg:skill.png", skill.lineX(), skill.lineY(), 32, 32);
            }
        }

    }
}
