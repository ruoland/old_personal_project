package ruo.map.tycoon;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import ruo.cmplus.deb.DebAPI;
import ruo.map.tycoon.block.*;
import ruo.map.tycoon.block.shopping.BlockShopping;
import ruo.map.tycoon.block.shopping.TileShopping;
import ruo.map.tycoon.block.shopping.TileShoppingRenderer;
import ruo.map.tycoon.consumer.EntityConsumer;

//@Mod(modid = "BreadTycoon", name="BreadTycooooooooooooooooooooooooooon", version="1.0")
public class TyconMain {
	public static final Block breadcall = new BlockBreadCall().setRegistryName("breadtycoon:breadcall").setUnlocalizedName("breadcall").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	public static final Block breaddisplay = new BlockBreadDisplay().setRegistryName("breadtycoon:blockbread").setUnlocalizedName("blockbread").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	public static final Block shopdisplay = new BlockShopping().setRegistryName("breadtycoon:shopping").setUnlocalizedName("blockshop").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	
	@EventHandler
	public void init(FMLInitializationEvent e){
		DebAPI.registerEvent(new TyconEvent());
		DebAPI.registerEntity(this, "Consumer", EntityConsumer.class);
		DebAPI.registerEntity(this, "PartTimeJob", EntityPartTimeJob.class);
	}
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent e){
		DebAPI.registerTileEntity(shopdisplay, TileShopping.class, new TileShoppingRenderer());
		DebAPI.registerTileEntity(breaddisplay, TileBreadDisplay.class, new TileBreadDisplayRenderer());
		DebAPI.registerTileEntity(breadcall, TileBreadCall.class, new TileBreadCallRenderer());
	}
	@EventHandler
	public void postinit(FMLPostInitializationEvent e){
		
	}
	@EventHandler
	public void postinit(FMLServerStoppedEvent e){
	}
}
