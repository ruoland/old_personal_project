package cmplus.cm.beta.customnpc;

import cmplus.util.CommandPlusBase;
import olib.api.RenderAPI;
import olib.api.WorldAPI;
import olib.map.EntityDefaultNPC;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

import javax.annotation.Nullable;
import java.util.List;

public class CommandNPCModel extends CommandPlusBase {

	public boolean args(String args, String...arg){
		
		for(String ar : arg){
			if(ar.equalsIgnoreCase(args)){
				return true;
			}
		}
		return false;
	}
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		
		if (args(args[1], "스윙", "손흔들기", "swing")) {
			EntityDefaultNPC.getNPC(args[0]).swingArm();
		}
		if (args[1].equalsIgnoreCase("model")) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			boolean flag = false;
			try {
				ITextComponent itextcomponent = getChatComponentFromNthArg(sender, args, 2);
				nbttagcompound = JsonToNBT.getTagFromJson(itextcomponent.getUnformattedText());
				nbttagcompound.setString("id", args[2]);
				Entity entity = AnvilChunkLoader.readWorldEntityPos(nbttagcompound, WorldAPI.getWorld(), 0, 0, 0, true);
				EntityDefaultNPC.getNPC(args[0]).setCustomModel(RenderAPI.getEntityModel(entity));
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
	}
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
    	return getListOfStringsMatchingLastWord(args, "swing", "getLeftarm", "rightarm", "leftleg", "rightleg", "왼팔", "오른팔", "왼다리", "오른다리", "스윙");
    }

}
