package ruo.asdfrpg;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import ruo.asdfrpg.skill.Skill;
import ruo.asdfrpg.skill.SkillBlockGrab;
import ruo.asdfrpg.skill.SkillHelper;

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
        SkillHelper.PlayerSkill playerSkill = SkillHelper.getPlayerSkill(((EntityPlayer)sender).getCustomNameTag());
        Skill skill = playerSkill.getSkill(args[0]);
        if(skill != null)
            skill.onEffect((EntityPlayer) sender);
        else
            playerSkill.addSkill(SkillHelper.getSkill(args[0]));
    }
}
