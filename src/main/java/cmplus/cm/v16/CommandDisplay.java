package cmplus.cm.v16;

import cmplus.util.CommandPlusBase;
import olib.effect.TickTask;
import olib.effect.TickRegister;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import javax.annotation.Nullable;
import java.util.List;

public class CommandDisplay extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, final String[] args) throws CommandException {
		
		TickRegister.register(new TickTask(Type.CLIENT, 1, false) {
			@Override
			public void run(Type type) {
				if(t.argCheck(args[0], "size", "사이즈"))
				{
					TickRegister.register(new TickTask(Type.RENDER, 1, false) {
						
						@Override
						public void run(Type type) {
							try {
								Display.setDisplayMode(new DisplayMode(t.findInteger(args, 1, Display.getWidth()), t.findInteger(args, 2, Display.getHeight())));
							} catch (LWJGLException e) {
								e.printStackTrace();
							}
						}
					});
				}

				if(t.argCheck(args[0], "location", "좌표"))
				{
					Display.setLocation(t.findInteger(args, 1, Display.getX()), t.findInteger(args, 2, Display.getY()));
				}

				if(t.argCheck(args[0], "full", "전체화면"))
				{
					try {
						Display.setFullscreen(Boolean.valueOf(args[1]));
					} catch (LWJGLException e) {
						
						e.printStackTrace();
					}
				}
			}
		});
		
	}

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
    	if(args.length == 2){
			if (args[0].equals("full")) {
				return getListOfStringsMatchingLastWord(args, "true", "false");
			}
    	}
    	if(args.length == 1)
    		return getListOfStringsMatchingLastWord(args, "location", "full", "size");
    	return super.getTabCompletionOptions(server, sender, args, pos);

    }
}
