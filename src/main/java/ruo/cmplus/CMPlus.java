package ruo.cmplus;

import api.player.client.ClientPlayerAPI;
import api.player.model.ModelPlayerAPI;
import api.player.render.RenderPlayerAPI;
import api.player.server.ServerPlayerAPI;
import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import ruo.cmplus.cm.CommandDrawtexture;
import ruo.cmplus.cm.CommandMultiCommand;
import ruo.cmplus.cm.CommandTimer;
import ruo.cmplus.cm.beta.CommandEntity;
import ruo.cmplus.cm.beta.CommandItem;
import ruo.cmplus.cm.beta.CommandPlayer;
import ruo.cmplus.cm.beta.custommodelentity.CommandCustomEntity;
import ruo.cmplus.cm.beta.customnpc.CommandNPC;
import ruo.cmplus.cm.beta.customnpc.CommandNPCModel;
import ruo.cmplus.cm.beta.model.CMModelPlayer;
import ruo.cmplus.cm.beta.model.CMRenderPlayer;
import ruo.cmplus.cm.beta.model.CommandModel;
import ruo.cmplus.cm.shortco.CommandCm;
import ruo.cmplus.cm.shortco.CommandFunc;
import ruo.cmplus.cm.v16.CommandMouseCancel;
import ruo.cmplus.cm.v17.*;
import ruo.cmplus.cm.v17.key.KeyEvent;
import ruo.cmplus.cm.v18.*;
import ruo.cmplus.cm.v18.customgui.CommandGUI;
import ruo.cmplus.cm.v18.customgui.CustomClient;
import ruo.cmplus.cm.v18.function.*;
import ruo.cmplus.deb.CommandDeb;
import ruo.cmplus.deb.DebAPI;
import ruo.cmplus.deb.DebEvent;
import ruo.cmplus.test.CMMessageHandler;
import ruo.cmplus.test.CMPacketCommand;
import ruo.cmplus.util.Sky;

import java.io.FileInputStream;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Mod(modid = "CommandPlus", name = "CommandPlus", version = "1.8")
public class CMPlus {
	@SidedProxy(clientSide = "ruo.cmplus.ClientProxy",serverSide = "cm.cmplus.CommonProxy")
	public static CommonProxy proxy;
	public static boolean isPlayerAPI;
	public CMPlus() {
		try {
			Class.forName("api.player.server.ServerPlayerAPI");
			ServerPlayerAPI.register("CommandPlus", CMServerPlayer.class);
			ClientPlayerAPI.register("CommandPlus", CMClientPlayer.class);
			RenderPlayerAPI.register("CommandPlus", CMRenderPlayer.class);
			ModelPlayerAPI.register("CommandPlus", CMModelPlayer.class);
			isPlayerAPI = true;
		}catch (Exception e){
			System.out.println("없음");
			//e.printStackTrace();
		}
	}
	@Instance("CommandPlus")
	public static CMPlus instance;
	public static Configuration debConfig;//DebAPI 에서 사용됨
	
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("CommandPlus");
	
	@EventHandler
	public void init(FMLPreInitializationEvent event) {
		debConfig = new Configuration(event.getSuggestedConfigurationFile());
		debConfig.load();
		debConfig.save();

		//DebAPI.register(Sky.class);
		//DebAPI.register(CMManager.class);
		VAR.read();
		proxy.pre(event);
		CustomClient.preInit(event);
		DebAPI.registerEvent(new DebEvent());
		DebAPI.registerEvent(new CMPlusEvent());
		DebAPI.registerEvent(new CMPlusCameraEvent());
		DebAPI.registerEvent(new KeyEvent());
		DebAPI.registerEvent(new FunctionEvent());
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		INSTANCE.registerMessage(CMMessageHandler.class, CMPacketCommand.class, 0, Side.CLIENT);
	}
	
	@EventHandler
	public void init(FMLPostInitializationEvent event) {
		proxy.post(event);
	}

	public void readCustomCommand() {
		Map<String, Property> keyvalue = debConfig.getCategory("customcommand").getValues();;
		for(String com : keyvalue.keySet()) {
			ClientCommandHandler.instance.registerCommand(new CommandCustom.CustomCommand(com, keyvalue.get(com).getString()));
		}
		
	}
	@EventHandler
	public void stopServer(FMLServerStoppedEvent event) {
		VAR.save();
		DebAPI.saveObject("Waypoint", CMManager.waypoint);
		DebAPI.saveWorldProperties();
		DebAPI.saveWorldConfig();
	}
	
	@EventHandler
	public void startServer(FMLServerStartingEvent event) {
		readCustomCommand();
		event.registerServerCommand(new CommandCustom());
		event.registerServerCommand(new CommandSpeed());
		event.registerServerCommand(new CommandFlySpeed());
		event.registerServerCommand(new CommandHeal());
		event.registerServerCommand(new CommandFire());
		event.registerServerCommand(new CommandNoRain());
		event.registerServerCommand(new CommandFly());

		event.registerServerCommand(new CommandWaypoint());
		event.registerServerCommand(new CommandItem());
		event.registerServerCommand(new CommandFunc());
		event.registerServerCommand(new CommandDeb());
		event.registerServerCommand(new CommandCm());
		event.registerServerCommand(new CommandRender());
		event.registerServerCommand(new CommandPlayer());
		event.registerServerCommand(new CommandModel());
		event.registerServerCommand(new CommandCustomEntity());
		event.registerServerCommand(new CommandOpenFolder());
		event.registerServerCommand(new CommandEvent());
		event.registerServerCommand(new CommandGUI());

		event.registerServerCommand(new CommandDrawtexture());
		event.registerServerCommand(new CommandMultiCommand());
		event.registerServerCommand(new CommandTimer());
		event.registerServerCommand(new CommandPosCommand());
		event.registerServerCommand(new CommandOpen());
		event.registerServerCommand(new CommandPosition());
		event.registerServerCommand(new CommandIF());
		event.registerServerCommand(new CommandWhile());
		event.registerServerCommand(new CommandFor());
		event.registerServerCommand(new CommandFunction());
		event.registerServerCommand(new CommandVar());
		event.registerServerCommand(new CommandMath());
		event.registerServerCommand(new CommandMouseCancel());

		//여러가지 이유로 멀티에서 사용불가능인 명령어들
		//개발중 버그 안잡음 멀티에선 NPC 사용 불가능 타임스피드는..
		event.registerServerCommand(new CommandPotion());
		event.registerServerCommand(new CommandEntity());
		//event.registerServerCommand(new CommandTimeSpeed());
		event.registerServerCommand(new CommandNPCModel());
		event.registerServerCommand(new CommandNPC());
		event.registerServerCommand(new CommandText());
	}

	public void autoRegister(){
		String jar = CMPlus.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
		String pac = CMPlus.class.getPackage().getName().replace(".", "/");
		try{
			ZipInputStream zis = new ZipInputStream(new FileInputStream(jar));
			ZipEntry entry =null;
			while((entry = zis.getNextEntry()) != null){
				if(entry.isDirectory() || !entry.getName().startsWith("Command") || !entry.getName().endsWith(".class")){
					continue;
				}
				Class cla = Class.forName(entry.getName());
				Object obj = cla.newInstance();
				if(obj instanceof ICommand){
					System.out.println(((ICommand) obj).getCommandName());
				}
			}
			zis.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
