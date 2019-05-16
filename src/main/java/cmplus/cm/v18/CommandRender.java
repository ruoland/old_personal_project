package cmplus.cm.v18;

import cmplus.util.CommandPlusBase;
import olib.api.WorldAPI;
import olib.effect.AbstractTick;
import olib.effect.CMEffect;
import olib.effect.TickRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;

import java.util.HashMap;

public class CommandRender extends CommandPlusBase {

	private HashMap<String, AbstractTick> list = new HashMap<String, AbstractTick>();

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		if (args[0].equals("darkscreen")) {
			
			if(args[1].equals("plus"))
			CMEffect.getEffect().darkScreen(Integer.valueOf(args[2]) * 20, new AbstractTick() {
				@Override
				public void run(Type type) {
					WorldAPI.command(t.findString(args, 3, ""));
				}
			});
			if(args[1].equals("minus"))
				CMEffect.getEffect().darkScreen2(Integer.valueOf(args[2]) * 20, 0, new AbstractTick() {
					@Override
					public void run(Type type) {
						WorldAPI.command(t.findString(args, 3, ""));
					}
				});
		}
		if (args[0].equalsIgnoreCase("drawtext")) {
			String name = args[1].replace("/n/", " ");
			if(list.get(name) != null) {
				AbstractTick absTick = list.get(name);
				absTick.stopTick();
				list.remove(args[1]);
			}
			list.put(name, new AbstractTick(Type.RENDER, 1, true) {
				@Override
				public boolean stopCondition() {
					return WorldAPI.getWorld() == null;
				}
				@Override
				public void run(Type type) {
					GlStateManager.pushMatrix();
					GlStateManager.translate(0, 0, t.findFloat(args, 7, 0));
					GlStateManager.scale(t.findFloat(args, 4, 1), t.findFloat(args, 5, 1), t.findFloat(args, 6, 1));
					Minecraft.getMinecraft().fontRendererObj.drawString(name, t.findInteger(args, 2, 0),
							t.findInteger(args, 3, 0), 0xFFFFFF);
					GlStateManager.popMatrix();
				}
			});
			TickRegister.register(list.get(name));
		}
		if (args[0].equalsIgnoreCase("removetext")) {
			AbstractTick absTick = list.get(args[1].replace("/n/", " "));
			absTick.stopTick();
			list.remove(args[1]);
		}

	}
}
