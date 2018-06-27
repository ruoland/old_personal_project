package ruo.minigame.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import ruo.minigame.MiniGame;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.fakeplayer.FakePlayerHelper;
import ruo.minigame.minigame.elytra.Elytra;

public class CommandElytra extends CommandBase{

	@Override
	public String getCommandName() {
		
		return "elytra";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		
		return "";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		EntityPlayer player = (EntityPlayer) sender;
		if(args[0].equalsIgnoreCase("ween")){
			Elytra.flyingWeen.setHealth(Float.valueOf(args[1]));
			return;
		}

		if(args[0].equalsIgnoreCase("fake")){
			FakePlayerHelper.fakePlayer.setHealth(Float.valueOf(args[1]));
			return;
		}

		if(args[0].equalsIgnoreCase("respawn")){
			WorldAPI.teleport(MiniGame.elytra.playerSpawnX, MiniGame.elytra.playerSpawnY, MiniGame.elytra.playerSpawnZ);
			FakePlayerHelper.spawnFakePlayer(true).setPosition(MiniGame.elytra.playerSpawnX, MiniGame.elytra.playerSpawnY, MiniGame.elytra.playerSpawnZ);
			FakePlayerHelper.fakePlayer.setCustomNameTag("RESPAWN");
            WorldAPI.teleport(MiniGame.elytra.playerSpawnX, MiniGame.elytra.playerSpawnY+5, MiniGame.elytra.playerSpawnZ);
        }
		else if(args[0].equals("start"))
			MiniGame.elytra.start(args[1]);
		else
			MiniGame.elytra.end();
		
	}

}
