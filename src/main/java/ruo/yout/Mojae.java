package ruo.yout;

import cmplus.deb.DebAPI;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import olib.api.RenderAPI;
import ruo.yout.command.*;
import ruo.yout.item.*;
import ruo.yout.mojaelab.LabEvent;
import ruo.yout.mojaelab.StatEvent;
import ruo.yout.y.CommandY;
import ruo.yout.y.YEvent;

import java.util.HashMap;

//@Mod(modid =  "Mojae")
public class Mojae {
    public static HashMap<String, Float> damageMap = new HashMap<>();
    public static HashMap<String, Float> killMap = new HashMap<>();

    public static HashMap<String, String> monterAttack = new HashMap<>();//왼쪽에 있는 몬스터는 오른쪽에 몬스터를 공격함
    public static HashMap<String, String> monterAttackRemove = new HashMap<>();//왼쪽에 있는 몬스터는 오른쪽에 몬스터를 공격함
    public static boolean spawnLockMode;//소환되는 몬스터를 전부 잠금

    public static boolean dog_pan, skelreeper, arrowReeper, arrowRiding, canTeamKill = false, wither;
    public static boolean morespawn;
    public static boolean statStart;
    public static int arrow_count= 1, skelDelay = -1;
    @SidedProxy(clientSide = "ruo.yout.MojaeClientProxy", serverSide = "ruo.yout.MojaeCommonProxy")
    public static MojaeCommonProxy proxy;
    public static final PotionLock lockPotion = new PotionLock(false, 0);
    public static final PotionGod godPotion = new PotionGod(false, 0);
    public static final Item itemLock = new ItemLock();
    public static final Item itemRiding = new ItemRiding();
    public static final Item itemGod = new ItemGod();
    public static final Item itemUp = new ItemUp();
    public static final Item itemOneShot = new ItemOneShot();
    public static final Item itemExplosion = new ItemExplosion();
    @Mod.EventHandler
    public  void init(FMLInitializationEvent e){
        MinecraftForge.EVENT_BUS.register(new YEvent());

        MinecraftForge.EVENT_BUS.register(new MoJaeEvent());
        MinecraftForge.EVENT_BUS.register(new LabEvent());
        MinecraftForge.EVENT_BUS.register(new StatEvent());
        Blocks.GLASS.setResistance(1000000);
        Blocks.GLOWSTONE.setResistance(1000000);
        Blocks.GLOWSTONE.setLightLevel(100);
        Potion.REGISTRY.register(30, new ResourceLocation("lock"), lockPotion);
        Potion.REGISTRY.register(31, new ResourceLocation("godPotion"), godPotion);
        DebAPI.registerEntity(this, "MoJaeCreeper", EntityMoJaeCreeper.class);
        DebAPI.registerEntity(this, "MissileCree", EntityMissileCreeperLab.class);
        DebAPI.registerEntity(this, "FlyingCree", EntityFlyingCreeperLab.class);
        DebAPI.registerEntity(this, "VELOCITY-MojaeArrow", EntityMojaeArrow.class);
        RenderAPI.registerRender(EntityFlyingCreeperLab.class);
        RenderAPI.registerRender(EntityMissileCreeperLab.class);
        Blocks.STONE.setResistance(100000);
        proxy.init();;
    }
    @Mod.EventHandler
    public  void init(FMLPreInitializationEvent e){

        GameRegistry.register(itemLock.setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setRegistryName("lock").setUnlocalizedName("lock"));
        GameRegistry.register(itemRiding.setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setRegistryName("riding").setUnlocalizedName("riding"));
        GameRegistry.register(itemGod.setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setRegistryName("god").setUnlocalizedName("god"));
        GameRegistry.register(itemOneShot.setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setRegistryName("oneshot").setUnlocalizedName("oneshot"));
        GameRegistry.register(itemExplosion.setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setRegistryName("explosion").setUnlocalizedName("explosion"));
        GameRegistry.register(itemUp.setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setRegistryName("up").setUnlocalizedName("up"));

        proxy.preinit();
    }
    @Mod.EventHandler
    public  void init(FMLPostInitializationEvent e){

    }
    @Mod.EventHandler
    public  void init(FMLServerStartingEvent e){
        e.registerServerCommand(new CommandY());

        e.registerServerCommand(new CommandAttackDelay());
        e.registerServerCommand(new CommandMoJae());
        e.registerServerCommand(new CommandWorldTeleport());
        e.registerServerCommand(new CommandKillEntity());
        e.registerServerCommand(new CommandLockEntity());
        e.registerServerCommand(new CommandUnlockEntity());
        e.registerServerCommand(new CommandHealthEntity());
        e.registerServerCommand(new CommandTPEntity());
        e.registerServerCommand(new CommandTeamKill());
        e.registerServerCommand(new CommandFindEntity());
        e.registerServerCommand(new CommandMojaeY());
    }

    public static void lockEntity(EntityLiving living){
        living.setCustomNameTag("잠금");
        living.getEntityData().setDouble("LPX", living.posX);
        living.getEntityData().setDouble("LPY", living.posY);
        living.getEntityData().setDouble("LPZ", living.posZ);
        living.addPotionEffect(new PotionEffect(Mojae.lockPotion, 400));
    }
}
