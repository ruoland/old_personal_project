package ruo.yout;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ruo.asdfrpg.skill.PotionFly;
import ruo.yout.mojaelab.LabEvent;

import java.util.ArrayList;
import java.util.HashMap;

@Mod(modid =  "MoJaeYoutube")
public class Mojae {
    public static HashMap<String, String> monterAttack = new HashMap<>();//왼쪽에 있는 몬스터는 오른쪽에 몬스터를 공격함
    public static boolean dog_pan, skelreeper, arrow_reeper, arrow_riding;
    public static int arrow_count= 1;
    @SidedProxy(clientSide = "ruo.yout.ClientProxy", serverSide = "ruo.yout.CommonProxy")
    public static CommonProxy proxy;
    public static final PotionLock lockPotion = new PotionLock(false, 0);
    public static final PotionGod godPotion = new PotionGod(false, 0);
    public static final Item itemLock = new ItemLock();
    public static final Item itemRiding = new ItemRiding();
    public static final Item itemGod = new ItemGod();
    public static final Item itemOneShot = new ItemOneShot();
    @Mod.EventHandler
    public  void init(FMLInitializationEvent e){
        MinecraftForge.EVENT_BUS.register(new MoJaeEvent());
        MinecraftForge.EVENT_BUS.register(new LabEvent());

        Potion.REGISTRY.register(30, new ResourceLocation("lock"), lockPotion);
        Potion.REGISTRY.register(31, new ResourceLocation("godPotion"), godPotion);
        proxy.init();;
    }
    @Mod.EventHandler
    public  void init(FMLPreInitializationEvent e){

        GameRegistry.register(itemLock.setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setRegistryName("lock").setUnlocalizedName("lock"));
        GameRegistry.register(itemRiding.setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setRegistryName("riding").setUnlocalizedName("riding"));
        GameRegistry.register(itemGod.setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setRegistryName("god").setUnlocalizedName("god"));
        GameRegistry.register(itemOneShot.setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setRegistryName("oneshot").setUnlocalizedName("oneshot"));
        proxy.preinit();
    }
    @Mod.EventHandler
    public  void init(FMLPostInitializationEvent e){

    }
    @Mod.EventHandler
    public  void init(FMLServerStartingEvent e){
        e.registerServerCommand(new CommandMoJae());
    }
}
