package ruo.test;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = "TestMod")
public class TestMod {

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){

    }

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event){

    }


    @Mod.EventHandler
    public void init(FMLServerStartingEvent event){
        event.registerServerCommand(new CommandTest());
    }
}
