package ruo.cmplus;

import net.minecraft.command.server.CommandOp;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ruo.cmplus.cm.*;
import ruo.cmplus.cm.v15.CommandGameOver;
import ruo.cmplus.cm.v15.CommandLogout;
import ruo.cmplus.cm.v16.CommandDisplay;
import ruo.cmplus.cm.v16.CommandMouseCancel;
import ruo.cmplus.cm.v16.CommandSky;
import ruo.cmplus.cm.v16.CommandTimeTeleport;
import ruo.cmplus.cm.v17.CommandClip;
import ruo.cmplus.cm.v17.CommandMonologue;
import ruo.cmplus.cm.v17.CommandOpen;
import ruo.cmplus.cm.v17.key.CommandKey;
import ruo.cmplus.cm.v18.CommandGrab;
import ruo.cmplus.cm.v18.customgui.CommandGUI;

public class ClientProxy extends CommonProxy {

	@Override
	public void pre(FMLPreInitializationEvent event) {
		clientcommand();
	}
	
	@Override
	public void post(FMLPostInitializationEvent event){
	}
	public void clientcommand() {
		ClientCommandHandler.instance.registerCommand(new CommandCamera());
		ClientCommandHandler.instance.registerCommand(new CommandSky());
		ClientCommandHandler.instance.registerCommand(new CommandDisplay());
		ClientCommandHandler.instance.registerCommand(new CommandGrab());
		ClientCommandHandler.instance.registerCommand(new CommandKey());
		ClientCommandHandler.instance.registerCommand(new CommandShader());
		ClientCommandHandler.instance.registerCommand(new CommandClip());
		ClientCommandHandler.instance.registerCommand(new CommandMonologue());
		ClientCommandHandler.instance.registerCommand(new CommandCloud());
		ClientCommandHandler.instance.registerCommand(new CommandDistance());
		ClientCommandHandler.instance.registerCommand(new CommandGamma());
		ClientCommandHandler.instance.registerCommand(new CommandSound());
		ClientCommandHandler.instance.registerCommand(new CommandShader());
		ClientCommandHandler.instance.registerCommand(new CommandChat());
		ClientCommandHandler.instance.registerCommand(new CommandGUI());
		ClientCommandHandler.instance.registerCommand(new CommandDrawtexture());
		ClientCommandHandler.instance.registerCommand(new CommandMouseCancel());
		ClientCommandHandler.instance.registerCommand(new CommandOpen());

		ClientCommandHandler.instance.registerCommand(new CommandUI());
		ClientCommandHandler.instance.registerCommand(new CommandTimeTeleport());
		ClientCommandHandler.instance.registerCommand(new CommandLogout());
		ClientCommandHandler.instance.registerCommand(new CommandGameOver());
		ClientCommandHandler.instance.registerCommand(new CommandStopsound());
	}
}
