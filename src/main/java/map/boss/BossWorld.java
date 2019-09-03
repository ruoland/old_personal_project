package map.boss;

import cmplus.deb.DebAPI;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "bossroom", name = "BossRoom")
public class BossWorld {

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){

    }

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event){
        DebAPI.registerEntity(this, "BlockBoss", EntityBlockBoss.class);
        DebAPI.registerEntity(this, "LavaBoss", EntityLavaBoss.class);
    }


}
