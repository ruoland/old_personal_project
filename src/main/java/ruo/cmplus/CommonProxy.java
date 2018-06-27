package ruo.cmplus;


import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void pre(FMLPreInitializationEvent event) {

    }

    public void config(FMLPreInitializationEvent event) {

    }

    public void post(FMLPostInitializationEvent event) {
        //DebAPI.registerEntity(this, "MonsterA", EntityCustomMob.class, new RenderCustomMob(new ModelCustomMob(), 1.0F));

    }
}
