package cmplus.cm.beta.customnpc;

import cmplus.util.CommandPlusBase;
import minigameLib.map.EntityDefaultNPC;
import minigameLib.map.TypeModel;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.List;

public class CommandNPC extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		
		EntityDefaultNPC npc = EntityDefaultNPC.getNPC(args[0]);
		if(args[0].equals("remove")){
			npc.removeNPC(args[0]);
		}
		if(args[0].equals("func") || args[0].equals("function")){
			EntityCMNPC cmnpc = (EntityCMNPC) EntityDefaultNPC.getNPC(args[0]);
			cmnpc.functionNPC.openFolder();
		}
		if(args[0].equals("create") || args[0].equals("cr")){//npc 이름 create X Y Z 타입 충돌여부 어린이여부
			if(npc != null)
			{
				sender.addChatMessage(new TextComponentString("해당 이름을 가진 엔피씨가 존재합니다. "+npc));
			}
			npc = new EntityDefaultNPC(server.getEntityWorld());
			npc.setCustomNameTag(args[1]);
			double[] pos = t.findPosition(args, 1, sender.getPosition());
            npc.setPosition(pos[0], pos[1], pos[2]);
            npc.setModel(TypeModel.valueOf(t.findString(args, 4, "NPC")));
            npc.setCollision(t.findBoolean(args, 5, false));
            if(npc.getModel() == TypeModel.BLOCK) 
                npc.setBlock(CommandBase.getBlockByText(sender, t.findString(args, 6, "")));
            else
            	npc.setChild(t.findBoolean(args, 6, false));
        	npc.isFly = (t.findBoolean(args, 7, false));

			server.getEntityWorld().spawnEntityInWorld(npc);
			npc.onInitialSpawn(null, null);
		}else
			npc = EntityDefaultNPC.getNPC(args[0]);
	}
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
    	if(args.length == 2){
    	}
    	if(args.length == 5){
    		return getListOfStringsMatchingLastWord(args, "NPC", "BLOCK", "WITCH", "ZOMBIE", "CREEPER", "ENDERMAN");
    	}
    	return super.getTabCompletionOptions(server, sender, args, pos);
    }
}
