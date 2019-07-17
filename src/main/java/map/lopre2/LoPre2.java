package map.lopre2;

import cmplus.deb.DebAPI;
import map.lopre2.nouse.EntityMagmaBlock;
import map.lopre2.nouse.EntitySmallBlock;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import olib.api.WorldAPI;
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
import map.lopre2.jump1.*;
import map.lopre2.jump2.*;
import map.lopre2.jump3.EntityLavaSpawnBlock;
import org.lwjgl.input.Keyboard;

@Mod(modid = "LoopPre2", name = "LoopPre2")
public class LoPre2 {
    public static Achievement achievementOneClear = new Achievement("achievement.oneclear", "oneclear", 0, 0, Items.FIREWORK_CHARGE, null);
    public static Achievement achievementNoDie1 = new Achievement("achievement.nodie1", "nodie1", 0, 1, new ItemStack(Blocks.SKULL), LoPre2.achievementNoGameMode1);
    public static Achievement achievementNoGameMode1 = new Achievement("achievement.nogamemode1", "nogamemode1", 0, 2, Items.GOLDEN_APPLE, null);
    public static Achievement achievementApple = new Achievement("achievement.apple", "apple", 0, 3, new ItemStack(Items.GOLDEN_APPLE), LoPre2.achievementNoGameMode1);
    public static Achievement achievementHidePath3 = new Achievement("achievement.hidepath3", "hidepath3", 0, 4, Items.GOLDEN_APPLE, null);
    public static Achievement achievementHidePath1 = new Achievement("achievement.hidepath1", "hidepath1", 0, 6, Items.FEATHER, null);

    public static Achievement achievementTwoClear = new Achievement("achievement.twoclear", "twoclear", 1, 0, Items.FIREWORKS, null);
    public static Achievement achievementNoDie2 = new Achievement("achievement.nodie2", "nodie2", 1, 1, new ItemStack(Blocks.SKULL), LoPre2.achievementNoGameMode2);
    public static Achievement achievementNoGameMode2 = new Achievement("achievement.nogamemode2", "nogamemode2", 1, 2, Items.GOLDEN_APPLE, null);
    public static Achievement achievementHidePath2 = new Achievement("achievement.hidepath2", "hidepath2", 1, 6, Items.FEATHER, null);
    public static KeyBinding KEY_ADD = new KeyBinding("복사 거리를 늘립니다.", Keyboard.KEY_ADD, "모쿠르");
    public static KeyBinding KEY_ADD_2 = new KeyBinding("복사 거리를 늘립니다.(+와 = 같이 있는 키보드용)", Keyboard.KEY_EQUALS, "모쿠르");

    public static KeyBinding KEY_MINUS = new KeyBinding("복사 거리를 줄입니다.", Keyboard.KEY_MINUS, "모쿠르");
    public static final CreativeTabs MO_BLOCK = new CreativeTabs("모쿠르") {
        @Override
        public Item getTabIconItem() {
            return LoPre2.itemSpanner;
        }
    };
    //점프맵 2 코드
    public static Item itemCopy = new ItemCopy().setCreativeTab(MO_BLOCK);
    public static Item itemDifficulty = new ItemDifficulty().setCreativeTab(MO_BLOCK);
    public static Item itemSpanner = new ItemSpanner().setCreativeTab(MO_BLOCK);
    public static Item itemBlockMove = new ItemBlockMove().setCreativeTab(MO_BLOCK);
    @Instance("LoopPre2")
    public static LoPre2 instance;

    @SidedProxy(serverSide = "map.lopre2.JumpCommonProxy", clientSide = "map.lopre2.JumpClientProxy")
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
        ClientRegistry.registerKeyBinding(KEY_ADD);
        ClientRegistry.registerKeyBinding(KEY_ADD_2);
        ClientRegistry.registerKeyBinding(KEY_MINUS);
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
                achievementTwoClear, achievementOneClear, achievementNoGameMode1, achievementNoDie1, achievementNoGameMode2, achievementNoDie2, achievementApple, achievementHidePath1, achievementHidePath2
        }));
        //점프맵 2 코드
        MinecraftForge.EVENT_BUS.register(new JumpEvent2());
        MinecraftForge.EVENT_BUS.register(new LooPre2Event());
        MinecraftForge.EVENT_BUS.register(new LooPreThreeEvent());
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
            MinecraftForge.EVENT_BUS.register(new LooPreClientEvent());


        DebAPI.registerEntity(this, "TeleportBlock", EntityTeleportBlock.class);
        DebAPI.registerEntity(this, "BigBlockInvisible", EntityBigInvisibleBlock.class);
        DebAPI.registerEntity(this, "BigBlockMovejump", EntityBigBlockMove.class);
        DebAPI.registerEntity(this, "BigBlockjump", EntityBigBlock.class);
        DebAPI.registerEntity(this, "KnockbackBlock", EntityKnockbackBlock.class);
        DebAPI.registerEntity(this, "VELOCITY-lavablock", EntityLavaBlock.class);
        DebAPI.registerEntity(this, "VELOCITY-loopFallingBlock", EntityLoopFallingBlock.class);
        DebAPI.registerEntity(this, "VELOCITY-LoopMoveBlock", EntityMoveBlock.class);
        DebAPI.registerEntity(this, "NO-EGG-VELOCITY-LoopWaterBlock", EntityWaterFlowBlock.class);
        DebAPI.registerEntity(this, "VELOCITY-loopdownblock", EntityWaterBlockCreator.class);
        DebAPI.registerEntity(this, "VELOCITY-NO-EGG-PreBlock", EntityPreBlock.class);
        DebAPI.registerEntity(this, "BuildBlock", EntityBuildBlock.class);
        DebAPI.registerEntity(this, "InvisibleBlock", EntityInvisibleBlock.class);

        proxy.init();
        //DebAPI.registerEntity(this, "LavaUpDownBlock", EntityMagmaBlock.class);
        //DebAPI.registerEntity(this, "LoopMoveBlockTest", EntityMoveBlockTest.class);
        //DebAPI.registerEntity(this, "JumpSpider", EntityJumpSpider.class, new RenderJumpSpider());
        //DebAPI.registerEntity(this, "SmallBlockJump", EntitySmallBlock.class);
        //DebAPI.registerEntity(this, "LavaSpawnBlock", EntityLavaSpawnBlock.class);

    }

    @EventHandler
    public void init(FMLPostInitializationEvent e) {

    }

    public static int compare(double a, double b) {
        a = Math.round(a * 1000) / 1000.0;
        b = Math.round(b * 1000) / 1000.0;
        if (a < b)
            return -1;
        else if (a > b)
            return 1;
        else if ( a== b)
            return 0;
        else return -10;

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

    public static boolean checkJumpThree() {
        String worldName = WorldAPI.getCurrentWorldName();
        return worldName.equalsIgnoreCase("JumpThree");
    }
}
