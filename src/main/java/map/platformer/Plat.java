package map.platformer;

import cmplus.deb.DebAPI;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import map.platformer.chapter1.EntityBabyCow;
import map.platformer.chapter1.EntityBossHorse;
import map.platformer.chapter1.EntityCowBumo;
import map.platformer.chapter1.EntityPlatBlock;
import map.platformer.chapter2.EntityChickenShopKeeper;


//@Mod(modid = "plat")
public class Plat {
    public static Item platCoinSpawn = new ItemCoinSpawn().setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setUnlocalizedName("platCoinSpawn").setRegistryName("platCoinSpawn");
    public static Item platCoin = new ItemCoin().setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setUnlocalizedName("platcoin").setRegistryName("platcoin");
    public static Item chickenRiding = new ItemChickenRiding().setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setUnlocalizedName("platChicken").setRegistryName("platChicken");

    @SidedProxy(serverSide = "map.platformer.CommonProxy", clientSide = "map.platformer.ClientProxy")
    public static CommonProxy proxy;
    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {

        MinecraftForge.EVENT_BUS.register(new PlatEvent());
        GameRegistry.register(platCoin);
        GameRegistry.register(chickenRiding);
        GameRegistry.register(platCoinSpawn);
        ModelLoader.setCustomModelResourceLocation(platCoin, 0, new ModelResourceLocation("plat:platcoin", "inventory"));
        DebAPI.registerEntity(this, "platHookShot", EntityHookShot.class);
        DebAPI.registerEntity(this, "platnpc", EntityNPC.class);
        DebAPI.registerEntity(this, "platc1BossHorse", EntityBossHorse.class);
        DebAPI.registerEntity(this, "platc1BabyCow", EntityBabyCow.class);
        DebAPI.registerEntity(this, "platc2ChickenShop", EntityChickenShopKeeper.class);
        DebAPI.registerEntity(this, "platCoin", EntityCoin.class);
        DebAPI.registerEntity(this, "platPlatBlock", EntityPlatBlock.class);
        DebAPI.registerEntity(this, "platCowBumo", EntityCowBumo.class);
        proxy.init();
        DebAPI.createJson(platCoinSpawn, Items.CHICKEN);
        DebAPI.createJson(chickenRiding, Items.COOKED_CHICKEN);
        DebAPI.createJson(platCoin, Items.NETHER_STAR);
    }

    @Mod.EventHandler
    public void preInit(FMLServerStartingEvent e) {
        e.registerServerCommand(new CommandGiveCoin());
        e.registerServerCommand(new CommandStage());

    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    }
}
