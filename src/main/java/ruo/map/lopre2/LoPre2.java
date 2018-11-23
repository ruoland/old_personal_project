package ruo.map.lopre2;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import ruo.cmplus.deb.DebAPI;
import ruo.map.lopre2.jump1.*;
import ruo.map.lopre2.jump2.*;
import ruo.map.lopre2.jump3.EntityLavaSpawnBlock;
import ruo.minigame.api.WorldAPI;

@Mod(modid = "LoopPre2", name = "LoopPre2")
public class LoPre2 {
    public static Achievement achievementOneClear = new Achievement("achievement.oneclear", "oneclear", 0, 0, Items.FIREWORK_CHARGE, null);
    public static Achievement achievementNoDie1 = new Achievement("achievement.nodie1", "nodie1", 0, 1, new ItemStack(Blocks.SKULL), LoPre2.achievementNoGameMode1);
    public static Achievement achievementNoGameMode1 = new Achievement("achievement.nogamemode1", "nogamemode1", 0, 2, Items.GOLDEN_APPLE, null);
    public static Achievement achievementApple = new Achievement("achievement.apple", "apple", 0, 3, new ItemStack(Items.GOLDEN_APPLE), LoPre2.achievementNoGameMode1);
    public static Achievement achievementPoorBlock = new Achievement("achievement.poorblock", "poorblock", 0, 4, Items.GOLDEN_APPLE, null);
    public static Achievement achievementHidePath1= new Achievement("achievement.hidepath1", "hidepath1", 0, 6, Items.FEATHER, null);

    public static Achievement achievementTwoClear = new Achievement("achievement.twoclear", "twoclear", 1, 0, Items.FIREWORKS, null);
    public static Achievement achievementNoDie2 = new Achievement("achievement.nodie2", "nodie2", 1, 1, new ItemStack(Blocks.SKULL), LoPre2.achievementNoGameMode2);
    public static Achievement achievementNoGameMode2 = new Achievement("achievement.nogamemode2", "nogamemode2", 1, 2, Items.GOLDEN_APPLE, null);
    public static Achievement achievementHidePath2= new Achievement("achievement.hidepath2", "hidepath2", 1, 6, Items.FEATHER, null);


    //점프맵 2 코드
    public static Item itemCopy = new ItemCopy().setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static Item itemDifficulty = new ItemDifficulty().setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static Item itemSpanner = new ItemSpanner().setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static Item itemBlockMove = new ItemBlockMove().setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    @Instance("LoopPre2")
    public static LoPre2 instance;

    @SidedProxy(serverSide = "ruo.map.lopre2.JumpCommonProxy", clientSide = "ruo.map.lopre2.JumpClientProxy")
    public static JumpCommonProxy proxy;
    @EventHandler
    public void init(FMLPreInitializationEvent e) {
        GameRegistry.register(itemSpanner.setUnlocalizedName("spanner").setRegistryName("looppre2:spanner"));
        GameRegistry.register(itemBlockMove.setUnlocalizedName("blockmove").setRegistryName("looppre2:blockmove"));
        GameRegistry.register(itemCopy.setUnlocalizedName("copy").setRegistryName("looppre2:copy"));
        GameRegistry.register(itemDifficulty.setUnlocalizedName("difficulty").setRegistryName("looppre2:difficulty"));
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {

        achievementApple.registerStat();
        achievementNoDie1.registerStat();
        achievementNoGameMode1.registerStat();
        achievementNoDie2.registerStat();
        achievementNoGameMode2.registerStat();
        achievementOneClear.registerStat();
        achievementTwoClear.registerStat();
        achievementHidePath1.registerStat();
        achievementHidePath2.registerStat();
        AchievementPage.registerAchievementPage(new AchievementPage("모드 점프맵 도전과제", new Achievement[]{
                achievementHidePath1, achievementHidePath2,
                achievementTwoClear, achievementOneClear, achievementNoGameMode1,achievementNoDie1,achievementNoGameMode2,achievementNoDie2,achievementApple, achievementHidePath1, achievementHidePath2
        }));
        //점프맵 2 코드
        MinecraftForge.EVENT_BUS.register(new JumpEvent2());
        MinecraftForge.EVENT_BUS.register(new LooPre2Event());
        MinecraftForge.EVENT_BUS.register(new LooPreThreeEvent());
        if(FMLCommonHandler.instance().getSide() == Side.CLIENT)
        MinecraftForge.EVENT_BUS.register(new LooPreClientEvent());

        DebAPI.registerEntity(this, "LavaSpawnBlock", EntityLavaSpawnBlock.class);

        DebAPI.registerEntity(this, "TeleportBlock", EntityTeleportBlock.class);
        DebAPI.registerEntity(this, "LavaUpDownBlock", EntityMagmaBlock.class);
        DebAPI.registerEntity(this, "BigBlockInvisible", EntityBigInvisibleBlock.class);
        DebAPI.registerEntity(this, "BigBlockMovejump", EntityBigBlockMove.class);
        DebAPI.registerEntity(this, "SmallBlockJump", EntitySmallBlock.class);
        DebAPI.registerEntity(this, "BigBlockjump", EntityBigBlock.class);
        DebAPI.registerEntity(this, "KnockbackBlock", EntityKnockbackBlock.class);
        //DebAPI.registerEntity(this, "JumpSpider", EntityJumpSpider.class, new RenderJumpSpider());
        DebAPI.registerEntity(this, "VELOCITY-lavablock", EntityLavaBlock.class);
        DebAPI.registerEntity(this, "VELOCITY-loopFallingBlock", EntityFallingBlock.class);
        DebAPI.registerEntity(this, "VELOCITY-LoopMoveBlock", EntityMoveBlock.class);
        DebAPI.registerEntity(this, "VELOCITY-LoopWaterBlock", EntityWaterFlowBlock.class);
        DebAPI.registerEntity(this, "VELOCITY-loopdownblock", EntityWaterBlockCreator.class);
        DebAPI.registerEntity(this, "VELOCITY-NO-EGG-PreBlock", EntityPreBlock.class);
        //DebAPI.registerEntity(this, "LoopMoveBlockTest", EntityMoveBlockTest.class);
        DebAPI.registerEntity(this, "BuildBlock", EntityBuildBlock.class);
        DebAPI.registerEntity(this, "InvisibleBlock", EntityInvisibleBlock.class);
        proxy.init();
    }

    @EventHandler
    public void init(FMLPostInitializationEvent e) {

    }

    @EventHandler
    public void init(FMLServerStartingEvent e) {
        e.registerServerCommand(new CommandJB());
    }

    public static boolean checkWorld() {
        String worldName = WorldAPI.getCurrentWorldName();
        return worldName.equalsIgnoreCase("JumpMap")
                || worldName.equalsIgnoreCase("JumpMap Sea2")
                || worldName.equalsIgnoreCase("JumpThree");
    }
}
