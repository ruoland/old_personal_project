package ruo.yout;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ruo.asdfrpg.skill.PotionFly;

//@Mod(modid =  "ModoYoutube")
public class MoJaYo {
    public static final PotionLock lockPotion = new PotionLock(false, 0);
    public static final Item itemLock = new ItemLock();
    @Mod.EventHandler
    public  void init(FMLInitializationEvent e){
        MinecraftForge.EVENT_BUS.register(new MoJaeEvent());
        Potion.REGISTRY.register(30, new ResourceLocation("lock"), lockPotion);
        GameRegistry.register(itemLock.setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setRegistryName("lock").setUnlocalizedName("lock"));
    }
    @Mod.EventHandler
    public  void init(FMLPreInitializationEvent e){

    }
    @Mod.EventHandler
    public  void init(FMLPostInitializationEvent e){

    }

}
