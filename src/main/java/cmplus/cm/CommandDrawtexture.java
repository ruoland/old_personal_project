package cmplus.cm;

import cmplus.cm.v18.customgui.CMResourcePack;
import cmplus.util.CommandPlusBase;
import oneline.api.WorldAPI;
import oneline.effect.CMEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class CommandDrawtexture extends CommandPlusBase {
	private Minecraft mc = Minecraft.getMinecraft();

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		String texture = args[0];
		if(args[0].startsWith("https://") || args[0].startsWith("http://")){
			texture = "textures/gui/"+CMResourcePack.imageDownload(texture);
		}else {
			if(texture.indexOf(":") == -1){
				texture = WorldAPI.getCurrentWorldName()+":"+texture;
			}
		}
		if(args.length == 3)
			CMEffect.getEffect().drawImage(texture, 0,0,mc.displayWidth,mc.displayHeight,Integer.parseInt(args[1]) * 20);
		else
			CMEffect.getEffect().drawImage(texture, Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]),  Integer.parseInt(args[5]) * 20);
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos pos) {
		if(args.length == 4) {
			return getListOfStringsMatchingLastWord(args, ""+mc.displayWidth);
		}
		if(args.length == 5) {
			return getListOfStringsMatchingLastWord(args, ""+mc.displayHeight);
		}
		return super.getTabCompletionOptions(server, sender, args, pos);
	}
}
