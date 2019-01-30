package minigameLib.command;

import minigameLib.MiniGame;
import minigameLib.api.WorldAPI;
import minigameLib.fakeplayer.FakePlayerHelper;
import minigameLib.minigame.minerun.MineRun;
import minigameLib.minigame.minerun.MineRunEvent;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandMineRun extends CommandBase {

	@Override
	public String getCommandName() {
		return "minerun";

	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		
		return "";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length > 0){
			if(args[0].equals("fake")){
				FakePlayerHelper.spawnFakePlayer(false);
				return;
			}
			if(args[0].equals("dead")){
				FakePlayerHelper.setFakeDead();
				return;
			}
			if(args[0].equals("left")){
				EntityPlayer player = (EntityPlayer) sender;
				WorldAPI.teleport(player.posX, player.posY, player.posZ, player.getHorizontalFacing().getHorizontalAngle()+90, 70);
				MiniGame.minerun.start(sender, args);
				return;
			}
			if(args[0].equals("right")){
				EntityPlayer player = (EntityPlayer) sender;
				WorldAPI.teleport(player.posX, player.posY, player.posZ, player.getHorizontalFacing().getHorizontalAngle()-90, 70);
				MiniGame.minerun.start(sender, args);
				return;
			}
			if(args[0].equals("lava")){
				for(int i = 0; i < MineRun.removeLavaState.size();i++){
					sender.getEntityWorld().setBlockState(MineRun.removeLavaPos.get(i), MineRun.removeLavaState.get(i));
				}
				return;
			}
			if(args[0].equals("respawn")){
				MineRunEvent.respawnTime = 61;
				WorldAPI.command("/minerun lava");
				return;
			}
		}
		if(args.length > 0 && (args[0].equalsIgnoreCase("end") || args[0].equalsIgnoreCase("stop")))
			MiniGame.minerun.end();
		else
			MiniGame.minerun.start(sender, args);
	}

}