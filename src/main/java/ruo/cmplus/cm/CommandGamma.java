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
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		Minecraft mc = Minecraft.getMinecraft();
		float gamma = Float.valueOf(args[1]) / 100F;
		gamma = t.math(args[0], t.getSetting().gammaSetting, gamma);
		setOptionF(Options.GAMMA, gamma);
		t.addSettingMessage(t.getSetting().gammaSetting);
	}
	
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos pos) {
		if(args.length == 1)
			return getListOfStringsMatchingLastWord(args, "set", "add", "sub");
		else 
			return super.getTabCompletionOptions(server, sender, args, pos);
	}

	public void setOptionF(Options category, float volume) {
		Minecraft.getMinecraft().gameSettings.setOptionFloatValue(category, volume);
	}
}
