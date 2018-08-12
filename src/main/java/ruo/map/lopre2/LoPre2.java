package ruo.map.lopre2;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.deb.DebAPI;
import ruo.map.lopre2.dummy.EntityDummyGuardLoop;
import ruo.map.lopre2.jump2.*;
import ruo.minigame.api.WorldAPI;

@Mod(modid = "LoopPre2", name = "LoopPre2")
public class LoPre2 {
    //점프맵 2 코드
    public static KeyBinding grab = new KeyBinding("액션", Keyboard.KEY_R, "카카카테고리");
    public static Item itemCopy = new ItemCopy().setUnlocalizedName("copy").setRegistryName("looppre2:copy").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

    public static Item itemSpanner = new ItemSpanner().setUnlocalizedName("spanner").setRegistryName("looppre2:spanner").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static Item itemBlockMove = new ItemBlockMove().setUnlocalizedName("blockmove").setRegistryName("looppre2:blockmove").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static KeyBinding blockSetKey = new KeyBinding("BLOCKSET", Keyboard.KEY_K, "LP2");
    @Instance("LoopPre2")
    public static LoPre2 instance;

    @EventHandler
    public void init(FMLPreInitializationEvent e) {
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        //더미로간 클래스
        //        DebAPI.registerEntity(this, "LoopUpDownWaterBlock", EntityLoopUpDownWaterBlock.class);
        //        DebAPI.registerEntity(this, "DebBlock", EntityDebBlock.class, new RenderDebBlock(new ModelDebBlock(), 0.5F));
        //DebAPI.registerEntity(this, "VELOCITY-LoopUpDownBlock", EntityLoopUpDownMoveBlock.class);
        //   DebAPI.registerEntity(this, "shipBlock", EntityShipBlock.class);
        //        DebAPI.registerEntity(this, "shipBlockPart", EntityShipBlockPart.class);
        //        DebAPI.registerEntity(this, "RopeBlock", EntityRopeBlock.class);
        //        DebAPI.registerEntity(this, "RopeBlock2", EntityRopeBlock2.class);
        //        DebAPI.registerEntity(this, "buildMove", EntityBuildBlockMove.class);
        //        DebAPI.registerEntity(this, "BeltBlock", EntityBeltBlock.class);
        //DebAPI.registerEntity(this, "ridingBlock", EntityRidingBlock.class);
        //점프맵 2 코드
        JumpEvent2.food = Boolean.valueOf(DebAPI.getWorldProperties("JumpMap").getProperty("food", "false"));
        ClientRegistry.registerKeyBinding(blockSetKey);
        ClientRegistry.registerKeyBinding(grab);
        DebAPI.registerEvent(new JumpEvent2());
        DebAPI.registerEntity(this, "TeleportBlock", EntityTeleportBlock.class);
        DebAPI.registerEntity(this, "LavaUpDownBlock", EntityMagmaBlock.class);
        DebAPI.registerEntity(this, "LoopGuard", EntityDummyGuardLoop.class);
        DebAPI.registerEntity(this, "BigBlockInvisible", EntityBigInvisibleBlock.class);
        DebAPI.registerEntity(this, "BigBlockMovejump", EntityBigBlockMove.class);
        DebAPI.registerEntity(this, "SmallBlockJump", EntitySmallBlock.class);
        DebAPI.registerEntity(this, "BigBlockjump", EntityBigBlock.class);
        DebAPI.registerEntity(this, "KnockbackBlock", EntityKnockbackBlock.class);
        DebAPI.registerEntity(this, "JumpSpider", EntityJumpSpider.class, new RenderJumpSpider());
        DebAPI.registerEntity(this, "VELOCITY-lavablock", EntityLavaBlock.class);
        DebAPI.registerEntity(this, "VELOCITY-loopFallingBlock", EntityFallingBlock.class);
        DebAPI.registerEntity(this, "VELOCITY-LoopMoveBlock", EntityMoveBlock.class);
        DebAPI.registerEntity(this, "VELOCITY-LoopWaterBlock", EntityWaterFlowBlock.class);
        DebAPI.registerEntity(this, "VELOCITY-loopdownblock", EntityWaterBlockCreator.class);
        DebAPI.registerEntity(this, "VELOCITY-NO-EGG-PreBlock", EntityPreBlock.class);
        //DebAPI.registerEntity(this, "LoopMoveBlockTest", EntityMoveBlockTest.class);


        DebAPI.registerEntity(this, "BuildBlock", EntityBuildBlock.class);
        DebAPI.registerEntity(this, "InvisibleBlock", EntityInvisibleBlock.class);
        GameRegistry.register(itemSpanner);
        GameRegistry.register(itemBlockMove);
        GameRegistry.register(itemCopy);
        DebAPI.createJson(itemSpanner, Items.NETHER_STAR);

        DebAPI.createJson(itemSpanner, Items.NETHER_STAR);
        DebAPI.createJson(itemBlockMove, Items.NETHER_STAR);
        DebAPI.registerEvent(new LooPre2Event());
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
        return worldName.equalsIgnoreCase("JumpMap") || worldName.equalsIgnoreCase("JumpMap2");
    }
}
