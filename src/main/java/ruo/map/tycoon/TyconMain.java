package ruo.map.tycoon;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ruo.cmplus.deb.DebAPI;
import ruo.map.tycoon.block.*;
import ruo.map.tycoon.block.bread.*;
import ruo.map.tycoon.block.shopping.BlockShopping;
import ruo.map.tycoon.block.shopping.TileShopping;
import ruo.map.tycoon.block.shopping.TileShoppingRenderer;
import ruo.map.tycoon.consumer.EntityConsumer;
import ruo.minigame.api.RenderAPI;

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
		DebAPI.registerTileEntity(breadWorkbench, TileBreadWorkbench.class, new TileBreadWorkbenchRenderer());
		GameRegistry.register(flour);
		DebAPI.createJson(flour, Items.CHICKEN);
		ModelBakery.registerItemVariants(breadFrame,
				new ModelResourceLocation("breadtycoon:frame", "inventory")
				,new ModelResourceLocation("breadtycoon:frame1", "inventory")
				,new ModelResourceLocation("breadtycoon:frame2", "inventory")
				,new ModelResourceLocation("breadtycoon:frame3", "inventory"),
				new ModelResourceLocation("breadtycoon:frame4", "inventory"),
				new ModelResourceLocation("breadtycoon:frame5", "inventory")
		);

		ModelLoader.setCustomMeshDefinition(flour, new ItemFrameMesh());
	}
	@EventHandler
	public void postinit(FMLPostInitializationEvent e){
		
	}
	@EventHandler
	public void postinit(FMLServerStoppedEvent e){
	}
}
