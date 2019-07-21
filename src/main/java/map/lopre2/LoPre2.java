package map.lopre2;

import cmplus.deb.DebAPI;
import map.lopre2.jump3.EntityLavaInvisible;
import map.lopre2.nouse.EntityMagmaBlock;
import map.lopre2.nouse.EntitySmallBlock;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import olib.api.NBTAPI;
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
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import map.lopre2.jump1.*;
import map.lopre2.jump2.*;
import map.lopre2.jump3.EntityLavaSpawnBlock;
import olib.effect.AbstractTick;
import olib.effect.TickRegister;
import org.lwjgl.input.Keyboard;

@Mod(modid = "LoopPre2", name = "LoopPre2")
public class LoPre2 {
    public static NBTAPI nbtapi = new NBTAPI("./mokur");
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
        DebAPI.registerEntity(this, "LavaInvisible", EntityLavaInvisible.class);

        proxy.init();
        //DebAPI.registerEntity(this, "LavaUpDownBlock", EntityMagmaBlock.class);
        //DebAPI.registerEntity(this, "LoopMoveBlockTest", EntityMoveBlockTest.class);
        //DebAPI.registerEntity(this, "JumpSpider", EntityJumpSpider.class, new RenderJumpSpider());
        //DebAPI.registerEntity(this, "SmallBlockJump", EntitySmallBlock.class);
        //DebAPI.registerEntity(this, "LavaSpawnBlock", EntityLavaSpawnBlock.class);

    }

    public static void worldload() {
        LoPre2.nbtapi.readNBT();
        TickRegister.register(new AbstractTick("ln", TickEvent.Type.SERVER, 1, true) {
            @Override
            public void run(TickEvent.Type type) {
                CommandJB.lavaTick = absRunCount == 200 ? absRunCount = 0 : absRunCount;
            }
        });

    }

    public static void worldUnload() {
        LoPre2.nbtapi.saveNBT();
        if (TickRegister.isAbsTickRun("ln"))
            TickRegister.remove("ln");
        CommandJB.lavaTick = LoPre2.nbtapi.getNBT().getInteger("lavaNumber");

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
        else if (a == b)
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
