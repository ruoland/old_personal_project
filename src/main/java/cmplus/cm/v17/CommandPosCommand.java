package cmplus.cm.v17;

import cmplus.util.CommandPlusBase;
import minigameLib.api.EntityAPI;
import minigameLib.api.WorldAPI;
import minigameLib.effect.AbstractTick;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandPosCommand extends CommandPlusBase {

	@Override
	public String getCommandName() {

		return "posc";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return null;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args[0].equals("reset")) {
			EntityAPI.removeAllPosition();
			;
			return;
		}
		if (!args[0].equals("pos")) {
			EntityPlayer player = (EntityPlayer) sender;
			EntityAPI.position(player.posX, player.posY, player.posZ, new AbstractTick.Position() {
				@Override
				public void runPosition() {
					WorldAPI.command(t.getCommand(args, 1, args.length));
				}
			});
		} else if (args[0].equals("pos")) {
			double[] d = WorldAPI.valueOfStr(args[2], args[3], args[4]);
			EntityAPI.position(d[0], d[1], d[2], 0, new AbstractTick.Position() {
				@Override
				public void runPosition() {
					WorldAPI.command(t.getCommand(args, 5, args.length));
				}
			});
			return;
		}

	}

}
