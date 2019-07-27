package olib;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import olib.action.ActionEffect;
import olib.action.ActionEvent;
import olib.effect.TickRegister;

@Mod(modid = "OneLine")

public class OLib {
    @SidedProxy(clientSide = "olib.ClientProxy", serverSide = "olib.CommonProxy")
    public static CommonProxy proxy;

    public static Configuration config;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e){
        config = new Configuration(e.getSuggestedConfigurationFile());
        config.load();
        config.save();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(new ActionEvent());
        MinecraftForge.EVENT_BUS.register(new TickRegister.TickRegisterEvent());
    }

    @Mod.EventHandler
    public void init(FMLServerStoppedEvent e) {
        ActionEffect.save();
    }

}
