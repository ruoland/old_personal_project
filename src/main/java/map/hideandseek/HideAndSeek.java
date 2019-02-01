package map.hideandseek;

import cmplus.deb.DebAPI;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

//@Mod(modid = "HAS", name = "Hide And Seek")
public class HideAndSeek {

    @EventHandler
    public void init(FMLInitializationEvent eve){
        DebAPI.registerEntity(this, "guardLoop", EntityGuardLoop.class);

    }
    @EventHandler
    public void init(FMLPostInitializationEvent eve){

    }
    @EventHandler
    public void init(FMLPreInitializationEvent eve){

    }

}
