package ruo.cmplus.cm;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import ruo.cmplus.util.CommandPlusBase;

import javax.annotation.Nullable;
import java.util.List;

public class CommandSound extends CommandPlusBase{

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		super.execute(server, sender, args);
		GameSettings mc = Minecraft.getMinecraft().gameSettings;
		double volume = 0;
		for(String str : args){
			if(t.isValueOf(str))
				volume = parseDouble(str, 0, 100);
		}
		volume = (volume < 10) ? Double.valueOf("0.0"+(int)volume) : volume < 100 ?  Double.valueOf("0."+(int)volume) : 1.0D;
		SoundCategory f = SoundCategory.valueOf(args[0].toUpperCase());
		Minecraft.getMinecraft().gameSettings.setSoundLevel(f, (float) volume);
		t.addSettingMessage(f.name(), mc.getSoundLevel(f));
	}
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
    	if(args.length == 1){
    		return getListOfStringsMatchingLastWord(args, SoundCategory.getSoundCategoryNames());
    	}
    	else if(args.length == 2){
    		return getListOfStringsMatchingLastWord(args, "sub", "set", "add");
    	}
    	return super.getTabCompletionOptions(server, sender, args, pos);
    }
	
}
