package ruo.minigame;

import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public void pre(FMLPreInitializationEvent event) {

		//DebAPI.registerEvent(new TextEvent());
	}
	
	@Override
	public void post(FMLPostInitializationEvent event){
	
	}	
}
