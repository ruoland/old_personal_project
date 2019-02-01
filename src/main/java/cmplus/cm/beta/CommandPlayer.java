package cmplus.cm.beta;

import cmplus.CMManager;
import cmplus.util.CommandPlusBase;
import cmplus.util.CommandTool;
import minigameLib.api.WorldAPI;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class CommandPlayer extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		
		CommandTool t = new CommandTool(getCommandName());
		int mode = 0;
		if(t.argCheck(args[mode], "lock", "락", "잠금"))
		{
			CMManager.setMoveLock(Boolean.valueOf(args[1]));
		}
		if(t.argCheck(args[mode], "elytra", "엘리트라", "겉날개"))
		{
			WorldAPI.getPlayerMP().setElytraFlying();
		}
		if(t.argCheck(args[mode], "fly", "플라이","날기"))
			WorldAPI.getPlayer().capabilities.allowFlying = getTrueFalse(args[mode+1]);
		if(t.argCheck(args[mode], "god", "갓", "무적", "신")){
			WorldAPI.getPlayer().capabilities.disableDamage = getTrueFalse(args[mode+1]);
		}
		if(t.argCheck(args[mode], "reach", "리치")){
			WorldAPI.getPlayerMP().interactionManager.setBlockReachDistance(Float.valueOf(args[mode+1]));
		}
		if(t.argCheck(args[mode], "sleep", "잠")){
			WorldAPI.getPlayer().trySleep(new BlockPos(getBlockPos(sender, mode+1, args)));
		}
		if(t.argCheck(args[mode], "wakeup", "깨우기")){
			WorldAPI.getPlayer().wakeUpPlayer(false, true, true);
		}
		if(t.argCheck(args[mode], "food", "배고픔")){
			WorldAPI.getPlayer().getFoodStats().setFoodLevel(Integer.valueOf(args[mode+1]));
		}
		if(args[mode].equals("damage") || args[mode].equals("공격력"))
			WorldAPI.getPlayer().getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(parseDouble(args[mode+1]));
		if(args[mode].equals("defence") || args[mode].equals("방어력"))
			WorldAPI.getPlayer().getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(parseDouble(args[mode+1]));
		if(args[mode].equals("maxhealth") || args[mode].equals("최대체력"))
			WorldAPI.getPlayer().getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(parseDouble(args[mode+1]));
		if(args[mode].equals("knockback") || args[mode].equals("넉백저항"))
			WorldAPI.getPlayer().getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(parseDouble(args[mode+1]));
		
	}
	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos pos) {
		
		return getListOfStringsMatchingLastWord(args, "fly", "god", "reach", "sleep", "wakeup", "food", "damage", "defence", "maxhealth", "knockback",
				"플라이", "갓", "리치", "잠", "깨우기", "배고픔", "공격력", "방어력", "최대체력", "넉백저항");
	}
}

