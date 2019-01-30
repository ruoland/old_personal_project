package ruo.asdfrpg;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ruo.asdfrpg.camp.BlockCampFire;
import ruo.asdfrpg.cook.CookedRecipe;
import ruo.asdfrpg.cook.CookedRecipeHelper;
import ruo.asdfrpg.npc.EntityTrader;
import ruo.asdfrpg.npc.monster.EntityRPGGolem;
import ruo.asdfrpg.npc.monster.EntityRPGWolf;
import ruo.asdfrpg.skill.entity.EntityLight;
import ruo.asdfrpg.skill.entity.EntitySkillBlock;
import ruo.asdfrpg.skill.entity.EntityThrowBlock;
import ruo.asdfrpg.skill.potion.PotionFly;
import ruo.asdfrpg.skill.potion.PotionIronBody;
import ruo.asdfrpg.skill.system.Skills;
import ruo.cmplus.deb.DebAPI;

@Mod(modid = "asdfrpg", dependencies = "required-after:DynamicLights")
public class AsdfRPG {
    public static final PotionIronBody ironBodyPotion = new PotionIronBody(false, 0);

    public static final PotionFly flyPotion = new PotionFly(false, 0);
    public static final Item villageReturn = new ItemRespawn().setCreativeTab(CreativeTabs.COMBAT).setUnlocalizedName("villagereturn").setRegistryName("asdfrpg:villagereturn").setMaxStackSize(1);
    public static final Item respawn = new ItemRespawn().setCreativeTab(CreativeTabs.COMBAT).setUnlocalizedName("respawn").setRegistryName("asdfrpg:respawn").setMaxStackSize(1);
    public static final Block campFire = new BlockCampFire().setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setUnlocalizedName("campfire").setRegistryName("asdfrpg:campfire");
    public static final CreativeTabs SKILL_TABS = new CreativeTabs("asdfrpg.skilltabs.name") {
        @Override
        public Item getTabIconItem() {
            return Items.BOOK;
        }
    };
    public static final SimpleNetworkWrapper ASDF_RPG = NetworkRegistry.INSTANCE.newSimpleChannel("asdfrpg");

    @SidedProxy(serverSide = "ruo.asdfrpg.CommonProxy", clientSide = "ruo.asdfrpg.ClientProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        DebAPI.registerEntity(this, "NO-EGG-SkillBlock", EntitySkillBlock.class);
        DebAPI.registerEntity(this, "NO-EGG-ThrowBlock", EntityThrowBlock.class);
        DebAPI.registerEntity(this, "NO-EGG-Light", EntityLight.class);
        DebAPI.registerEntity(this, "RPGWolf", EntityRPGWolf.class);
        DebAPI.registerEntity(this, "TraderR", EntityTrader.class);
        DebAPI.registerEntity(this, "RPGGolem", EntityRPGGolem.class);
        proxy.init();
        GameRegistry.register(villageReturn);
        GameRegistry.register(respawn);
        MinecraftForge.EVENT_BUS.register(new AsdfEvent());
        MinecraftForge.EVENT_BUS.register(new SkillEvent());
        CookedRecipeHelper.registerRecipe(new CookedRecipe(new ItemStack(Items.COOKED_BEEF), new ItemStack(Items.BEEF)));
        CookedRecipeHelper.registerRecipe(new CookedRecipe(new ItemStack(Items.COOKED_PORKCHOP),  new ItemStack(Items.PORKCHOP)));
        CookedRecipeHelper.registerRecipe(new CookedRecipe(new ItemStack(Items.COOKED_RABBIT),  new ItemStack(Items.RABBIT)));
        CookedRecipeHelper.registerRecipe(new CookedRecipe(new ItemStack(Items.COOKED_FISH),  new ItemStack(Items.FISH)));
        CookedRecipeHelper.registerRecipe(new CookedRecipe(new ItemStack(Items.COOKED_CHICKEN),  new ItemStack(Items.CHICKEN)));
        CookedRecipeHelper.registerRecipe(new CookedRecipe(new ItemStack(Items.COOKED_CHICKEN),  new ItemStack(Items.CHICKEN)));
        Skills.register();
        Potion.REGISTRY.register(28, new ResourceLocation("fly"), flyPotion);
        Potion.REGISTRY.register(29, new ResourceLocation("iron_body"), ironBodyPotion);

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

