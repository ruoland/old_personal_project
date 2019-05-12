package gooditem;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ruo.yout.Mojae;

@Mod(modid = "GoodItem")
public class GoodItem {
    public static final Item itemForbidden = new ItemForbidden().setRegistryName("GoodItem", "forbidden").setUnlocalizedName("forbidden").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final Item itemMagnet = new ItemMagnet().setRegistryName("GoodItem", "magnet").setUnlocalizedName("magnet").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final Item itemKill = new ItemKill().setRegistryName("GoodItem", "kill").setUnlocalizedName("kill").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final Item itemWarning = new ItemWarning().setRegistryName("GoodItem", "warning").setUnlocalizedName("warning").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final Item itemCall = new ItemCall().setRegistryName("GoodItem", "call").setUnlocalizedName("call").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

    @SidedProxy(clientSide = "gooditem.GIClientProxy", serverSide = "gooditem.GIServerProxy")
    public static GIServerProxy proxy;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        GameRegistry.register(itemForbidden);
        GameRegistry.register(itemMagnet);
        GameRegistry.register(itemKill);
        GameRegistry.register(itemWarning);
        GameRegistry.register(itemCall);
        GameRegistry.addRecipe(new ItemStack(itemForbidden), "###", "#$#", "###",  '#', Blocks.REDSTONE_BLOCK, '$', Items.DIAMOND);
        GameRegistry.addRecipe(new ItemStack(itemMagnet), "###", "#$#", "###",  '#', Items.IRON_INGOT, '$', Items.DIAMOND);
        GameRegistry.addRecipe(new ItemStack(itemWarning), "###", "#$#", "###",  '#',Items.REDSTONE, '$', Items.COMPASS);
        GameRegistry.addRecipe(new ItemStack(itemKill), "###", "###", "###",  '#', Blocks.BEDROCK);

        proxy.preInit();

    }
}
