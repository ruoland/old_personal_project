package ruo.asdfrpg;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import ruo.asdfrpg.skill.*;
import ruo.cmplus.util.MouseHelper10;
import ruo.minigame.action.ActionEffect;

public class CommandSkill extends CommandBase {
    @Override
    public String getCommandName() {
        return "skill";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        System.out.println(((EntityPlayer)sender).getDisplayNameString());
        if(args[0].equalsIgnoreCase("open")){
            SkillHelper.registerSkill((EntityPlayer)sender, Skills.BLOCK_GRAB);
            SkillHelper.registerSkill((EntityPlayer)sender, Skills.FLY);
            SkillHelper.registerSkill((EntityPlayer)sender, Skills.DOUBLE_JUMP);
            ActionEffect.doubleJump(true);
            Minecraft.getMinecraft().currentScreen = new GuiAsdfSkill();
            return;
        }
        Skill skill = SkillHelper.getSkill(args[0]);
        PlayerSkill playerSkill = SkillHelper.getPlayerSkill(((EntityPlayer)sender));
        SkillStack skillStack = playerSkill.getSkill(skill);
        if(skillStack != null)
            skillStack.onEffect();
        else
            playerSkill.registerSkill(skill);
    }
}
