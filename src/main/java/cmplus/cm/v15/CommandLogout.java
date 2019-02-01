package cmplus.cm.v15;

import cmplus.util.CommandTool;
import minigameLib.effect.AbstractTick;
import minigameLib.effect.TickRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;

import java.lang.reflect.Field;

public class CommandLogout extends CommandBase{

	@Override
	public String getCommandName() {
		
		return "out";
	}
	@Override
	public int getRequiredPermissionLevel() {
	
		return 2;
	}
	@Override
	public String getCommandUsage(ICommandSender sender) {
		
		return "commandPlus.logout.help";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		CommandTool t = new CommandTool(getCommandName());
		if(t.length(args)){
			return;
		}
		TickRegister.register(new AbstractTick(Type.CLIENT, 1, false) {
			
			@Override
			public void run(Type type) {
				final StringBuffer b = new StringBuffer();
				for(String s : args)
				b.append(s+" ");
				try {
					Field mc = Minecraft.class.getDeclaredField("isGamePaused");
					mc.setAccessible(true);
					mc.set(Minecraft.getMinecraft(), true);
					Minecraft.getMinecraft().theWorld.sendQuittingDisconnectingPacket();
					Minecraft.getMinecraft().loadWorld((WorldClient)null);
					GuiDisconnected dis = new GuiDisconnected(new GuiMainMenu(), b.toString(), new TextComponentString(b.toString()));
					Minecraft.getMinecraft().displayGuiScreen(dis);
				} catch (Exception e) {
					
					e.printStackTrace();
				} 
			}
		});
	}

}
