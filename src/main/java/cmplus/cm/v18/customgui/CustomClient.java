package cmplus.cm.v18.customgui;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import scala.collection.mutable.HashMap;

import java.io.File;

public class CustomClient {
	public static Configuration config;
	public static Configuration configGui;

	private static String configFile;
	public static HashMap<String, Configuration> configG = new HashMap<String, Configuration>();

	public static void preInit(FMLPreInitializationEvent event){
		configFile = event.getSuggestedConfigurationFile().getAbsolutePath().replace("\\CommandPlus.cfg", "");
		System.out.println(configFile.replace("\\CommandPlus.cfg", ""));
		//위 메세지 결과물 C:\Users\oprond\Desktop\intelliJ\run\config
    	configGui = new Configuration(new File(event.getSuggestedConfigurationFile().getAbsolutePath()), "CustomGUI");
    	configGui.load();
    	config = new Configuration(event.getSuggestedConfigurationFile());
    	config.load();

    	config.save();
	}
	
	public static Configuration addConfig(String name){
    	configGui = new Configuration(new File(configFile, name+".cfg"));
    	configGui.load();
    	configG.put(name, configGui);

		return configGui;
	}
	public static Configuration getConfig(String key){
		return configG.get(key).get();
	}
}
	