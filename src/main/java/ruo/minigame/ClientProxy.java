package ruo.minigame;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ruo.cmplus.deb.DebAPI;
import ruo.minigame.android.CommandCall;
import ruo.minigame.android.CommandNotification;
import ruo.minigame.command.CommandBomber;
import ruo.minigame.command.CommandElytra;
import ruo.minigame.command.CommandMineRun;
import ruo.minigame.command.CommandScroll;
import ruo.minigame.effect.TickRegister;
import ruo.minigame.minigame.minerun.MineRun;
import ruo.minigame.minigame.bomber.BombEvent;
import ruo.minigame.minigame.bomber.Bomber;
import ruo.minigame.minigame.elytra.Elytra;
import ruo.minigame.minigame.elytra.ElytraEvent;
import ruo.minigame.minigame.scroll.Scroll;
import ruo.minigame.text.TextEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public void pre(FMLPreInitializationEvent event) {
		MiniGame.minerun = new MineRun();
		MiniGame.scroll = new Scroll();
		MiniGame.bomber = new Bomber();
		MiniGame.elytra = new Elytra();

		ClientCommandHandler.instance.registerCommand(new CommandScroll());
		ClientCommandHandler.instance.registerCommand(new CommandBomber());
		ClientCommandHandler.instance.registerCommand(new CommandElytra());
		//ClientCommandHandler.instance.registerCommand(new CommandMonologue());
		ClientCommandHandler.instance.registerCommand(new CommandMineRun());
		ClientCommandHandler.instance.registerCommand(new CommandNotification());
		ClientCommandHandler.instance.registerCommand(new CommandCall());

		DebAPI.registerEvent(new TickRegister());
		DebAPI.registerEvent(new MiniGameEvent());
		DebAPI.registerEvent(new BombEvent());
		DebAPI.registerEvent(new ElytraEvent());
		DebAPI.registerEvent(new TextEvent());
	}
	
	@Override
	public void post(FMLPostInitializationEvent event){
	
	}	
}
