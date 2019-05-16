package cmplus.cm.v16;

import cmplus.util.CommandPlusBase;
import olib.api.WorldAPI;
import olib.effect.AbstractTick;
import olib.effect.TickRegister;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;

public class CommandTimeTeleport extends CommandPlusBase {

	@Override
	public String getCommandName() {
		return "ttp";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		
		return "commandPlus.ttp.help";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		if(t.length(args)){
			return;
		}		
		EntityPlayerMP player = null;
		try {
			player = this.getPlayer(server, sender, args[0]);
		} catch (PlayerNotFoundException e) {
			
			e.printStackTrace();
		}

		if (args.length == 7) {// /ttp @p X Y Z pitch yaw Tick
			double[] d = WorldAPI.valueOfStr(args[1], args[2], args[3]);
			float yaw = Float.valueOf(args[4]);
			float pitch = Float.valueOf(args[5]);
			double[] playerXYZ = WorldAPI.changePosArray(player);
			float rotationY = player.rotationYaw;
			float rotationP = player.rotationPitch;
			int a = Integer.parseInt(args[6]);
			
			player.connection.setPlayerLocation(d[0], d[1], d[2], yaw, pitch);

			teleport(player, a, playerXYZ[0], playerXYZ[1], playerXYZ[2], rotationY, rotationP);
		
		}

		if (args.length == 5) {// /ttp @p X Y Z Tick
             double[] d = WorldAPI.valueOfStr(args[1], args[2], args[3]);
			double[] playerXYZ = WorldAPI.changePosArray(player);
			float rotationY = player.rotationYaw;
			float rotationP = player.rotationPitch;
			int a = Integer.parseInt(args[4]);
			String tick = (a / 2) + (a % 20) + "00";
			player.connection.setPlayerLocation(d[0], d[1], d[2], player.rotationYaw, player.rotationPitch);

			teleport(player, a, playerXYZ[0], playerXYZ[1], playerXYZ[2], rotationY, rotationP);

		}
		if (args.length == 6) {// /ttp @p X Y Z pitch yaw
			double[] d = WorldAPI.valueOfStr(args[1], args[2], args[3]);
			float yaw = Float.valueOf(args[4]);
			float pitch = Float.valueOf(args[5]);
			player.connection.setPlayerLocation(d[0], d[1], d[2], yaw, pitch);
		}
	}
	
	public void teleport(EntityPlayerMP p, int tick, final double x, final double y, final double z, final float yaw, final float pit){
		TickRegister.register(new AbstractTick(tick * 20, false) {
			@Override
			public void run(Type type) {
				WorldAPI.teleport(x, y, z, yaw, pit);
			}
		});
	}
	
}
