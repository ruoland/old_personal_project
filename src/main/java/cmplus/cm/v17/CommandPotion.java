package cmplus.cm.v17;

import cmplus.util.CommandPlusBase;
import olib.effect.ENEffect;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionType;
import net.minecraft.server.MinecraftServer;

public class CommandPotion extends CommandPlusBase {

	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		double[] xyz = getPos(sender,0, args);
		try {
			ENEffect.spawnPotion((EntityLivingBase) sender, xyz[0], xyz[1], xyz[2], (PotionType) PotionTypes.class.getField(args[3]).get(null));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
