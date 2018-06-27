package ruo.cmplus.cm;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import ruo.cmplus.cm.v18.customgui.CMResourcePack;
import ruo.cmplus.util.CommandPlusBase;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.CMEffect;

import java.util.List;

public class CommandDrawtexture extends CommandPlusBase{
	private Minecraft mc = Minecraft.getMinecraft();

	@Override
	public void execute(MinecraftServer server, ICommandSender p_71515_1_, String[] p_71515_2_) {
		String texture = p_71515_2_[0];
		if(p_71515_2_[0].startsWith("https://") || p_71515_2_[0].startsWith("http://")){
			texture = "textures/gui/"+CMResourcePack.imageDownload(texture);
		}else {
			if(texture.indexOf(":") == -1){
				texture = WorldAPI.getCurrentWorldName()+":"+texture;
			}
		}
		if(p_71515_2_.length == 3)
			CMEffect.getEffect().drawImage(texture, 0,0,mc.displayWidth,mc.displayHeight,Integer.parseInt(p_71515_2_[1]) * 20);
		else
			CMEffect.getEffect().drawImage(texture, Integer.parseInt(p_71515_2_[1]), Integer.parseInt(p_71515_2_[2]), Integer.parseInt(p_71515_2_[3]), Integer.parseInt(p_71515_2_[4]),  Integer.parseInt(p_71515_2_[5]) * 20);
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
