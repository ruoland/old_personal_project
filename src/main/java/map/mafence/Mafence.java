package map.mafence;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

//@Mod(modid = "Mafence")
public class Mafence {
    public static boolean MAFENCE_START = false;//타워 디펜스가 시작된 경우 TRUE가 됨
    @SubscribeEvent
    public void init(FMLInitializationEvent e){


    }


    @SubscribeEvent
    public void preInit(FMLPreInitializationEvent e){

    }

    @SubscribeEvent
    public void postInit(FMLPostInitializationEvent e){

    }
}
