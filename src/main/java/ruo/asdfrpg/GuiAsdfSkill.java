package ruo.asdfrpg;

import net.minecraft.client.gui.GuiScreen;
import ruo.asdfrpg.skill.SkillHelper;
import ruo.asdfrpg.skill.Skill;
import ruo.minigame.api.RenderAPI;

public class GuiAsdfSkill extends GuiScreen
{

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        for(Skill skill : SkillHelper.getSkillList()) {
            RenderAPI.drawTexture("asdfrpg:first.png", skill.lineX(), skill.lineY(), 32, 32);
        }

    }
}
