package ruo.asdfrpg;

import atomicstryker.dynamiclights.client.DynamicLights;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import ruo.asdfrpg.skill.entity.EntityLightAdapter;
import ruo.asdfrpg.skill.system.*;
import ruo.minigame.action.ActionEffect;

import javax.annotation.Nullable;
import java.util.List;

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
        if (args[0].equalsIgnoreCase("script")) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiAsdfUI());
            return;
        }
        if (args[0].equalsIgnoreCase("light")) {
            DynamicLights.addLightSource(new EntityLightAdapter((EntityPlayer) sender, 15));
            return;
        }
        if (args[0].equalsIgnoreCase("reg")) {
            SkillHelper.registerSkill((EntityPlayer) sender, Skills.valueOf(args[1].toUpperCase()));
            SkillHelper.getPlayerSkill((EntityPlayer) sender).useSkill(Skills.valueOf(args[1].toUpperCase()));
            return;
        }
        if (args[0].equalsIgnoreCase("return")) {
            SkillHelper.registerSkill((EntityPlayer) sender, Skills.VILLAGE_RETURN);
            SkillHelper.getPlayerSkill((EntityPlayer) sender).useSkill(Skills.VILLAGE_RETURN, 1);
            return;
        }
        if (args[0].equalsIgnoreCase("open")) {
            SkillHelper.registerSkill((EntityPlayer) sender, Skills.BLOCK_GRAB);
            SkillHelper.registerSkill((EntityPlayer) sender, Skills.FLY);
            SkillHelper.registerSkill((EntityPlayer) sender, Skills.DOUBLE_JUMP);
            ActionEffect.doubleJump(true);
            Minecraft.getMinecraft().displayGuiScreen(new GuiAsdfSkill((EntityPlayer) sender));
            return;
        }
        Skill skill = SkillHelper.getSkill(args[0]);
        PlayerSkill playerSkill = SkillHelper.getPlayerSkill(((EntityPlayer) sender));
        SkillStack skillStack = playerSkill.getSkill(skill);
        if (skillStack != null)
            skillStack.onEffect(0);
        else
            playerSkill.registerSkill(skill);
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args[0].equalsIgnoreCase("reg"))
            return getListOfStringsMatchingLastWord(args, SkillHelper.getSkillList());
        else {
            return getListOfStringsMatchingLastWord(args, "reg", "return", "open");
        }
    }
//
//
//    private class EntityLightAdapter implements IDynamicLightSource
//    {
//        private EntityPlayer entity;
//        private int level;
//        public EntityLightAdapter(EntityPlayer light, int level)
//        {
//            entity = light;
//            this.level = level;
//            System.out.println(level);
//        }
//
//        @Override
//        public Entity getAttachmentEntity()
//        {
//            return entity;
//        }
//
//        @Override
//        public int getLightLevel()
//        {
//            return level * 2;
//        }
//    }
//}
}
