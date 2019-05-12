package grab;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

//@Mod(modid = "GrabCreeper")
public class GrabCreeper {

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new GrabEvent());
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    }

}
