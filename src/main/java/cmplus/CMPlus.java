package cmplus;

import cmplus.cm.CommandMultiCommand;
import cmplus.cm.CommandTimer;
import cmplus.cm.beta.CommandItem;
import cmplus.cm.beta.CommandPlayer;
import cmplus.cm.beta.custommodelentity.CommandCustomEntity;
import cmplus.cm.shortco.CommandCm;
import cmplus.cm.shortco.CommandFunc;
import cmplus.cm.v16.CommandTimeSpeed;
import cmplus.cm.v17.CommandPosCommand;
import cmplus.cm.v17.CommandPosition;
import cmplus.cm.v17.key.KeyEvent;
import cmplus.cm.v18.*;
import cmplus.cm.v18.customgui.CustomClient;
import cmplus.cm.v18.function.*;
import cmplus.deb.CommandDeb;
import cmplus.deb.DebAPI;
import cmplus.deb.DebEvent;
import cmplus.test.CMMessageHandler;
import cmplus.test.CMPacketCommand;
import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import ruo.yout.MoJaeEvent;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Mod(modid = "CommandPlus", name = "CommandPlus", version = "1.8")
public class CMPlus {
    @SidedProxy(clientSide = "cmplus.ClientProxy", serverSide = "cmplus.CommonProxy")
    public static CommonProxy proxy;

    public CMPlus() {

    }

    @Instance("CommandPlus")
    public static CMPlus instance;
    public static Configuration cmPlusConfig;//

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("CommandPlus");

    @EventHandler
    public void init(FMLPreInitializationEvent event) {
        cmPlusConfig = new Configuration(event.getSuggestedConfigurationFile());
        cmPlusConfig.load();
        cmPlusConfig.save();
        proxy.pre(event);
        MinecraftForge.EVENT_BUS.register(new DebEvent());
        MinecraftForge.EVENT_BUS.register(new CMPlusEvent());
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            CustomClient.preInit(event);
            MinecraftForge.EVENT_BUS.register(new FunctionEvent());
            MinecraftForge.EVENT_BUS.register(new CMPlusCameraEvent());
            MinecraftForge.EVENT_BUS.register(new KeyEvent());
            MinecraftForge.EVENT_BUS.register(new CMPlusClientEvent());
        }
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
        Map<String, Property> keyvalue = cmPlusConfig.getCategory("customcommand").getValues();
        for (String com : keyvalue.keySet()) {
            ClientCommandHandler.instance.registerCommand(new CommandCustom.CustomCommand(com, keyvalue.get(com).getString()));
        }

    }

    @EventHandler
    public void stopServer(FMLServerStoppedEvent event) {
        cmPlusConfig.save();
        DebAPI.saveWorldConfig();
    }

    @EventHandler
    public void startServer(FMLServerStartingEvent event) {
        readCustomCommand();
        event.registerServerCommand(new CommandTimeSpeed());
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
        event.registerServerCommand(new CommandCustomEntity());
        event.registerServerCommand(new CommandOpenFolder());
        event.registerServerCommand(new CommandEvent());
        event.registerServerCommand(new CommandMultiCommand());
        event.registerServerCommand(new CommandTimer());
        event.registerServerCommand(new CommandPosCommand());
        event.registerServerCommand(new CommandPosition());
        event.registerServerCommand(new CommandIF());
        event.registerServerCommand(new CommandWhile());
        event.registerServerCommand(new CommandFor());
        event.registerServerCommand(new CommandFunction());
        event.registerServerCommand(new CommandVar());
        event.registerServerCommand(new CommandMath());
        event.registerServerCommand(new CommandSend());

        //여러가지 이유로 멀티에서 사용불가능인 명령어들
        //개발중 버그 안잡음 멀티에선 NPC 사용 불가능 타임스피드는..
        //event.registerServerCommand(new CommandPotion());
        //event.registerServerCommand(new CommandEntity());
        //event.registerServerCommand(new CommandTimeSpeed());
       // event.registerServerCommand(new CommandNPCModel());
        //event.registerServerCommand(new CommandNPC());
        //event.registerServerCommand(new CommandText());
    }
    public static ArrayList<String> commandLogList = new ArrayList<>();
    public static void saveCommandLog(String worldName){
        LocalDateTime currentDate = LocalDateTime.now();
        StringBuffer date = new StringBuffer("log-");
        date.append(currentDate.getYear()).append("-").append(currentDate.getMonthValue()).append("-").append(currentDate.getDayOfMonth()).append("-").append(worldName).append(".txt");
        File dir = new File("./commandLog/");
        dir.mkdir();
        File commandLog = new File(dir+"/"+date.toString());
        try {
            commandLog.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(commandLog));
            for(String com : commandLogList){
                writer.write(com);
                writer.newLine();
            }
            writer.close();;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void autoRegister() {
        String jar = CMPlus.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
        String pac = CMPlus.class.getPackage().getName().replace(".", "/");
        try {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(jar));
            ZipEntry entry = null;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory() || !entry.getName().startsWith("Command") || !entry.getName().endsWith(".class")) {
                    continue;
                }
                Class cla = Class.forName(entry.getName());
                Object obj = cla.newInstance();
                if (obj instanceof ICommand) {
                    System.out.println(((ICommand) obj).getCommandName());
                }
            }
            zis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
