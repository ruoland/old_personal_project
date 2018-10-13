package ruo.hardcore;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.util.Random;

//@Mod(modid = "RuoHardCore", name = "HARD CORE")
public class HardCore {

    @Mod.EventHandler
    public void init(FMLInitializationEvent e){
        MinecraftForge.EVENT_BUS.register(new HardCoreEvent());
    }

    public static int rand(int bound){
        Random random = new Random();
        return random.nextBoolean() ? random.nextInt(bound) : -random.nextInt(bound);
    }
}
