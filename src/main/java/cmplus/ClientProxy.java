package cmplus;

import cmplus.cm.*;
import cmplus.cm.v15.CommandGameOver;
import cmplus.cm.v15.CommandLogout;
import cmplus.cm.v16.CommandDisplay;
import cmplus.cm.v16.CommandMouseCancel;
import cmplus.cm.v16.CommandSky;
import cmplus.cm.v16.CommandTimeTeleport;
import cmplus.cm.v17.CommandClip;
import cmplus.cm.v17.CommandMonologue;
import cmplus.cm.v17.CommandOpen;
import cmplus.cm.v17.key.CommandKey;
import cmplus.cm.v18.CommandGrab;
import cmplus.cm.v18.customgui.CommandGUI;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
