package ruo.cmplus.deb;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import ruo.cmplus.util.CommandPlusBase;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.TickRegister;

import java.lang.reflect.Method;

public class CommandDeb extends CommandPlusBase {

	public static boolean cmdeb, debText, debKey = false, debFunction = false, debVar = false, debMove = false;
	public static String str, funcName;//funcName

	@Override
	public void execute(MinecraftServer server, final ICommandSender sender, String[] args) {
		try {


			try {
				if (args.length == 0) {
					sender.getEntityWorld().getWorldInfo().setDifficulty(EnumDifficulty.PEACEFUL);
					TickRegister.register(new AbstractTick(20, false) {

						@Override
						public void run(Type type) {
							sender.getEntityWorld().getWorldInfo().setDifficulty(EnumDifficulty.EASY);
						}
					});
					return;
				}
				if (args[0].equals("re")) {
					RClassLoader classLoader = new RClassLoader();
					classLoader.findAllFile(null);
				}
				if (args[0].equals("my")) {
					RClassLoader classLoader = new RClassLoader();
					Object p = classLoader.preload(args[1], args[2]);
				}
				if (args[0].equals("for")) {
					RClassLoader classLoader = new RClassLoader();
					Object p = classLoader.preload(args[1], args[1]);
					Minecraft.getMinecraft().displayGuiScreen((GuiScreen) p);
				}
				if (args[0].equals("cmdeb")) {
					cmdeb = parseBoolean(args[1]);
				}
				if (args[0].equals("text")) {
					debText = parseBoolean(args[1]);
				}
				if (args[0].equals("move")) {
					debMove = parseBoolean(args[1]);
				}
				if (args[0].equals("var")) {
					debVar = parseBoolean(args[1]);
				}
				if (args[0].equals("key")) {
					debKey = parseBoolean(args[1]);
				}
				if (args[0].equals("function") || args[0].equals("func")) {
					debFunction = parseBoolean(args[1]);
					if (args.length > 1) {
						funcName = args[2];
					} else
						funcName = null;
				}
				if (args[0].equalsIgnoreCase("PlDonEvent") || args[0].equalsIgnoreCase("p")) {
					if (args.length > 3) {
						Class clas = Class.forName("ruo.map.looppre.text.PlDonEventRenewal" + args[2]);
						if (!args[1].equals("start") && !args[1].equals("toTheDaePiSo")) {
							Method deb = clas.getMethod("debspawn", double.class, double.class, double.class);
							deb.invoke(null, sender.getPosition().getX(), sender.getPosition().getY(), sender.getPosition().getZ());
						}
						Method me = clas.getMethod(args[1]);
						me.invoke(null);
						return;
					}
					RClassLoader classLoader = new RClassLoader();
					Object p = classLoader.preload("ruo.map.looppre.text.PlDonEventRenewal" + args[2], "ruo.map.looppre.text.PlDonEventRenewal" + args[2]);
					if (t.findString(args, 2, "false").equals("true")) {//true로 인자가 넘어오면 상위클래스에서 메서드를 찾음
						if (!args[1].equals("start") && !args[1].equals("toTheDaePiSo")) {
							System.out.println("인자1이 스타트가 아니므로 엔피씨 소환-- 인자1" + args[1] + args[0] + args[2]);
							Method deb = p.getClass().getSuperclass().getMethod("debspawn", double.class, double.class, double.class);
							deb.invoke(p, sender.getPosition().getX(), sender.getPosition().getY(), sender.getPosition().getZ());
						}
						Method debug = p.getClass().getSuperclass().getMethod(args[1]);
						debug.invoke(p);
						System.out.println(p.getClass().getSuperclass().getName());
					} else {
						if (!args[1].equals("start") && !args[1].equals("toTheDaePiSo")) {
							System.out.println("인자1이 스타트가 아니므로 엔피씨 소환-- 인자1" + args[1] + args[0] + args[2]);
							Method deb = p.getClass().getMethod("debspawn", double.class, double.class, double.class);
							deb.invoke(p, sender.getPosition().getX(), sender.getPosition().getY(), sender.getPosition().getZ());
						}
						Method debug = p.getClass().getMethod(args[1]);
						debug.invoke(p);
						System.out.println(p.getClass().getName());
					}

					str = p.getClass().getName();
					return;

				}

			} catch (Exception e) {
				System.out.println("엑스펙션");
				e.printStackTrace();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/*
	 * 
	 * if(!sender.getEntityWorld().isRemote && args[0].equalsIgnoreCase("Season2") || args[0].equalsIgnoreCase("s")){
				Method debug = ReflectionHelper.findMethod(Season2.class, null, args);
				Season2.debspawn(sender.getPosition().getX(), sender.getPosition().getY(), sender.getPosition().getZ());
				debug.invoke(null);
				return;
			}
	 */
}
