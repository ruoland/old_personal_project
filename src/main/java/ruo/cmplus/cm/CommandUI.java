package ruo.cmplus.cm;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import ruo.cmplus.CMManager;
import ruo.cmplus.util.CommandTool;
import ruo.cmplus.util.Sky;
import ruo.minigame.api.WorldAPI;

import java.util.ArrayList;
import java.util.List;

public class CommandUI extends CommandBase {
	private final String[] s = "PORTAL,CROSSHAIRS,BOSSHEALTH,ARMOR,HEALTH,FOOD,AIR,HOTBAR,EXPERIENCE,TEXT,HEALTHMOUNT,JUMPBAR,CHAT,PLAYER_LIST,DEBUG,HAND"
			.split(",");

	@Override
	public String getCommandName() {
		
		return "ui";
	}

	@Override
	public int getRequiredPermissionLevel() {
		
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		
		return "commandPlus.ui.help";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender p_71515_1_, String[] p_71515_2_) {
		CommandTool t = new CommandTool(getCommandName());
		EntityPlayerMP p = WorldAPI.getPlayerMP();
		if (t.length(p_71515_2_)) {
			return;
		}
		if (p_71515_2_[0].equalsIgnoreCase("reset")) {
			for (String s : s)
				CMManager.setUI(s, true);
			t.addLoMessage("reset");
			return;
		}

		if (p_71515_2_[1].equalsIgnoreCase("true") || p_71515_2_[1].equalsIgnoreCase("on")) {
			if (p_71515_2_[0].equalsIgnoreCase("blocklayer")) {
				Sky.enableBlockLayer();
				return;
			}
			String exp = p_71515_2_[0].equalsIgnoreCase("exp") ? "EXPERIENCE" : p_71515_2_[0].toUpperCase();
			try {
				if (exp.equalsIgnoreCase("hand"))
					exp = "HAND";
				else
					RenderGameOverlayEvent.ElementType.valueOf(exp);
			} catch (java.lang.IllegalArgumentException e) {
				t.addErrorMessage(exp);
				return;
			}
			CMManager.setUI(exp, true);

			t.addSettingMessage(p_71515_2_[0], true);
		}
		if (p_71515_2_[1].equalsIgnoreCase("false") || p_71515_2_[1].equalsIgnoreCase("off")) {
			if (p_71515_2_[0].equalsIgnoreCase("blocklayer")) {
				Sky.disableBlockLayer();
				return;
			}
			String exp = p_71515_2_[0].equalsIgnoreCase("exp") ? "EXPERIENCE" : p_71515_2_[0].toUpperCase();

			try {
				if (exp.equalsIgnoreCase("hand"))
					exp = "HAND";
				else
					RenderGameOverlayEvent.ElementType.valueOf(exp);
			} catch (java.lang.IllegalArgumentException e) {
				t.addErrorMessage(exp);
				return;
			}
			CMManager.setUI(exp, false);

			t.addSettingMessage(p_71515_2_[0], false);
		}
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos pos) {
		if (args.length == 1) {
			ArrayList<String> str = new ArrayList<String>();
			for (ElementType s : RenderGameOverlayEvent.ElementType.values()) {
				str.add(s.name());
			}
			str.add("HAND");
			str.add("BLOCKLAYER");

			return getListOfStringsMatchingLastWord(args, str);
		} else if(args.length == 2)
			return getListOfStringsMatchingLastWord(args, "ON", "OFF", "true", "false");
		return super.getTabCompletionOptions(server, sender, args, pos);
	}
}
