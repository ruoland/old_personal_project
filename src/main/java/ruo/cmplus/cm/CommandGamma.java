package ruo.cmplus.cm;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings.Options;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import ruo.cmplus.util.CommandPlusBase;

import javax.annotation.Nullable;
import java.util.List;

public class CommandGamma extends CommandPlusBase{

	@Override
	public void execute(MinecraftServer server, ICommandSender p_71515_1_, String[] p_71515_2_) {
		Minecraft mc = Minecraft.getMinecraft();
		float gamma = Float.valueOf(p_71515_2_[1]);
		gamma = t.math(p_71515_2_[0], t.s.gammaSetting, gamma);
		if(gamma < 10 && gamma >= 0) {
			gamma = Float.valueOf("0.0"+(int)gamma);
		}
		if(gamma < 100 && gamma >= 10) {
			gamma = Float.valueOf("0."+(int)gamma);
		}
		if(gamma == 100)
			gamma = 1F;
		if(gamma >= 101) {
			String sub = String.valueOf((int)gamma);
			sub = sub.substring(0, 1);
			gamma = Float.valueOf(sub);
		}
		setOptionF(Options.GAMMA, gamma);
		t.addSettingMessage(t.s.gammaSetting);
	}
	
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos pos) {
		if(args.length == 1)
			return getListOfStringsMatchingLastWord(args, "set", "add", "sub");
		else 
			return super.getTabCompletionOptions(server, sender, args, pos);
	}

	public void setOptionI(Options category, int volume) {
		Minecraft.getMinecraft().gameSettings.setOptionValue(category, volume);
	}

	public void setOptionF(Options category, float volume) {
		Minecraft.getMinecraft().gameSettings.setOptionFloatValue(category, volume);
	}
}
