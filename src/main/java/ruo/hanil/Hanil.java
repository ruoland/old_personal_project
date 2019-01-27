package ruo.hanil;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = "Hanil")
public class Hanil {

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){

    }

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(new HanilEvent());
    }

    @Mod.EventHandler
    public void init(FMLServerStartingEvent event){
        event.registerServerCommand(new CommandHan());
    }


}
