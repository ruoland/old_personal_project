package ruo.asdfrpg;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ruo.asdfrpg.camp.BlockCampFire;
import ruo.asdfrpg.camp.TileCampFire;
import ruo.asdfrpg.camp.TileCampFireRenderer;
import ruo.asdfrpg.cook.CookedRecipe;
import ruo.asdfrpg.cook.CookedRecipeHelper;
import ruo.asdfrpg.skill.*;
import ruo.cmplus.deb.DebAPI;

@Mod(modid = "asdfrpg", dependencies = "required-after:DynamicLights")
public class AsdfRPG {
    public static final PotionFly flyPotion = new PotionFly(false, 0);
    public static final Item villageReturn = new ItemRespawn().setCreativeTab(CreativeTabs.COMBAT).setUnlocalizedName("villagereturn").setRegistryName("asdfrpg:villagereturn").setMaxStackSize(1);
    public static final Item respawn = new ItemRespawn().setCreativeTab(CreativeTabs.COMBAT).setUnlocalizedName("respawn").setRegistryName("asdfrpg:respawn").setMaxStackSize(1);
    public static final Block campFire = new BlockCampFire().setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setUnlocalizedName("campfire").setRegistryName("asdfrpg:campfire");
    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        DebAPI.registerTileEntity(campFire, TileCampFire.class, new TileCampFireRenderer());
        DebAPI.registerEntity(this, "NO-EGG-AsdfBlock", EntityAsdfBlock.class);
        DebAPI.registerEntity(this, "NO-EGG-Light", EntityLight.class, new RenderLight(1.0F));

        GameRegistry.register(villageReturn);
        GameRegistry.register(respawn);
        DebAPI.registerEvent(new AsdfEvent());
        CookedRecipeHelper.registerRecipe(new CookedRecipe(new ItemStack(Items.COOKED_BEEF), new ItemStack(Items.BEEF)));
        CookedRecipeHelper.registerRecipe(new CookedRecipe(new ItemStack(Items.COOKED_PORKCHOP),  new ItemStack(Items.PORKCHOP)));
        CookedRecipeHelper.registerRecipe(new CookedRecipe(new ItemStack(Items.COOKED_RABBIT),  new ItemStack(Items.RABBIT)));
        CookedRecipeHelper.registerRecipe(new CookedRecipe(new ItemStack(Items.COOKED_FISH),  new ItemStack(Items.FISH)));
        CookedRecipeHelper.registerRecipe(new CookedRecipe(new ItemStack(Items.COOKED_CHICKEN),  new ItemStack(Items.CHICKEN)));
        CookedRecipeHelper.registerRecipe(new CookedRecipe(new ItemStack(Items.COOKED_CHICKEN),  new ItemStack(Items.CHICKEN)));
        Skills.register();
    }
    @Mod.EventHandler
    public void init(FMLServerStartingEvent e) {
        e.registerServerCommand(new CommandSkill());
    }
    @Mod.EventHandler
    public void init(FMLPreInitializationEvent e) {

    }

    @Mod.EventHandler
    public void init(FMLPostInitializationEvent e) {

    }

}

