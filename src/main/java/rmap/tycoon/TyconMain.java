package rmap.tycoon;

import cmplus.deb.DebAPI;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import rmap.tycoon.block.BlockBreadCall;
import rmap.tycoon.block.BlockBreadDisplay;
import rmap.tycoon.block.bread.BlockBreadWorkbench;
import rmap.tycoon.block.bread.ItemFlour;
import rmap.tycoon.block.bread.ItemFrame;
import rmap.tycoon.block.shopping.BlockShopping;
import rmap.tycoon.consumer.EntityConsumer;

//@Mod(modid = "BreadTycoon", name="BreadTycooooooooooooooooooooooooooon", version="1.0")
public class TyconMain {
	public static final CreativeTabs breadTabs = new CreativeTabs(CreativeTabs.getNextID(), "bread") {
		@Override
		public Item getTabIconItem() {
			return Items.BREAD;
		}
	};
	public static final Block breadcall = new BlockBreadCall().setRegistryName("breadtycoon:breadcall").setUnlocalizedName("breadcall").setCreativeTab(breadTabs);
	public static final Block breaddisplay = new BlockBreadDisplay().setRegistryName("breadtycoon:blockbread").setUnlocalizedName("blockbread").setCreativeTab(breadTabs);
	public static final Block shopdisplay = new BlockShopping().setRegistryName("breadtycoon:shopping").setUnlocalizedName("blockshop").setCreativeTab(breadTabs);
	public static final Block breadWorkbench = new BlockBreadWorkbench().setRegistryName("breadtycoon:breadworkbench").setUnlocalizedName("breadworkbench").setCreativeTab(breadTabs);
	public static final Item flour = new ItemFlour().setRegistryName("breadtycoon:flour").setUnlocalizedName("flour").setCreativeTab(breadTabs);
	public static final Item breadFrame = new ItemFrame().setRegistryName("breadtycoon:frame").setUnlocalizedName("frame").setCreativeTab(breadTabs);

	@SidedProxy(serverSide = "ruo.map.tycoon.CommonProxy", clientSide = "ruo.map.tycoon.ClientProxy")
	public static CommonProxy proxy;
	@EventHandler
	public void init(FMLInitializationEvent e){
		MinecraftForge.EVENT_BUS.register(new TyconEvent());
		DebAPI.registerEntity(this, "Consumer", EntityConsumer.class);
		DebAPI.registerEntity(this, "PartTimeJob", EntityPartTimeJob.class);
	}
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent e){
		GameRegistry.register(flour);
		DebAPI.createJson(flour, Items.CHICKEN);
		proxy.init();

	}
	@EventHandler
	public void postinit(FMLPostInitializationEvent e){
		
	}
	@EventHandler
	public void postinit(FMLServerStoppedEvent e){
	}
}
