package minigameLib;

import api.player.client.ClientPlayerAPI;
import api.player.render.RenderPlayerAPI;
import api.player.server.ServerPlayerAPI;
import cmplus.deb.CommandClassLoader;
import cmplus.deb.DebAPI;
import minigameLib.command.*;
import minigameLib.minigame.minerun.monster.*;
import net.minecraft.item.ItemMonsterPlacer;
import olib.action.ActionEffect;
import olib.android.CommandCall;
import olib.android.CommandNotification;
import olib.fakeplayer.EntityFakePlayer;
import olib.map.EntityDefaultBlock;
import olib.map.EntityDefaultNPC;
import minigameLib.minigame.bomber.Bomber;
import minigameLib.minigame.bomber.BomberEvent;
import minigameLib.minigame.bomber.EntityBomb;
import minigameLib.minigame.elytra.*;
import minigameLib.minigame.elytra.miniween.*;
import minigameLib.minigame.elytra.playerarrow.EntityHomingTNT;
import minigameLib.minigame.elytra.playerarrow.EntityTNTArrow;
import minigameLib.minigame.minerun.*;
import minigameLib.minigame.minerun.invisibleblock.BlockInvisible;
import minigameLib.minigame.minerun.invisibleblock.TileInvisible;
import minigameLib.minigame.scroll.*;
import minigameLib.minigame.starmine.CommandStarMine;
import minigameLib.minigame.starmine.StarMine;
import minigameLib.minigame.starmine.StarMineEvent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import map.lopre2.CommandJumpThree;

import java.util.logging.Logger;

import static net.minecraftforge.fml.relauncher.Side.CLIENT;


@Mod(modid = "MiniGame", name = "MiniGame")
public class MiniGame {
    public static Logger LOG = Logger.getLogger("MINIGAME");
    public static SimpleNetworkWrapper network;
    public static Block blockInvisible = new BlockInvisible(Material.ANVIL);

    @SidedProxy(clientSide = "minigameLib.ClientProxy", serverSide = "minigameLib.CommonProxy")
    public static CommonProxy proxy;

    @Instance("MiniGame")
    public static MiniGame instance;

    public static MineRun minerun;
    public static MineRunEvent mineRunEvent;

    public static Scroll scroll;
    public static ScrollEvent scrollEvent;

    public static Bomber bomber;
    public static BomberEvent bomberEvent;

    public static Elytra elytra;
    public static ElytraEvent elytraEvent;

    public static StarMine starMine;
    public static StarMineEvent starMineEvent;

    public MiniGame() {
        try {
            Class.forName("api.player.server.ServerPlayerAPI");
            ServerPlayerAPI.register("MiniGame", MiniGameServerPlayer.class);
            if (FMLCommonHandler.instance().getSide() == CLIENT) {
                ClientPlayerAPI.register("MiniGame", MiniGameClientPlayer.class);
                RenderPlayerAPI.register("MiniGame", MiniGameRenderPlayer.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @EventHandler
    public void init(FMLPreInitializationEvent e) {
        proxy.pre(e);
        network();

        if (FMLCommonHandler.instance().getSide() == CLIENT) {
            MiniGame.minerun = new MineRun();
            MiniGame.scroll = new Scroll();
            MiniGame.bomber = new Bomber();
            MiniGame.elytra = new Elytra();
            MiniGame.starMine = new StarMine();
        }
        //ClientPlayerAPI.register("MiniGame", LoopPlayer.class);

    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        DebAPI.registerTileEntity(blockInvisible.setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setUnlocalizedName("blockInvisible").setRegistryName("minigame:blockInvisible"), TileInvisible.class);
        DebAPI.createJson(new ItemBlock(blockInvisible), "blockInvisible");
        //스크롤 용
        DebAPI.registerEntity(this, "NO-EGG-ScrollCreeper", EntityJumpCreeper.class);
        DebAPI.registerEntity(this, "ScrollSpider", EntityJumpSpider.class);
        DebAPI.registerEntity(this, "NO-EGG-ScrollBoss", EntityScrollBoss.class);
        DebAPI.registerEntity(this, "NO-EGG-ScrollFallingBlock", EntityScrollBossFallingBlock.class);
        DebAPI.registerEntity(this, "NO-EGG-ScrollWarning", EntityScrollBossWarning.class);
        DebAPI.registerEntity(this, "NO-EGG-ScrollShoting", EntityScrollBossShootingBlock.class);
        DebAPI.registerEntity(this, "NO-EGG-ScrollFlyingBlock", EntityJumpFlyingBlock.class);
        DebAPI.registerEntity(this, "NO-EGG-ScrollTNT", EntityJumpTNT.class);
        DebAPI.registerEntity(this, "NO-EGG-ScrollDoubleReset", EntityJumpDoubleReset.class);
        DebAPI.registerEntity(this, "NO-EGG-ScrollFlyingCreeper", EntityJumpFlyingCreeper.class);

        //마인런 게임용
        DebAPI.registerEntity(this, "NO-EGG-MRRunner", EntityMineRunner.class);

        DebAPI.registerEntity(this, "NO-EGG-MRTNTCart", EntityMRTNTCart.class);

        DebAPI.registerEntity(this, "NO-EGG-MRCreeper", EntityMRCreeper.class);
        DebAPI.registerEntity(this, "NO-EGG-MRZombie", EntityMRZombie.class);
        DebAPI.registerEntity(this, "NO-EGG-MREnderman", EntityMREnderman.class);
        DebAPI.registerEntity(this, "NO-EGG-MRMissileCreeper", EntityMRMissileCreeper.class);
        DebAPI.registerEntity(this, "NO-EGG-MRRocketCreeper", EntityMRRocketCreeper.class);
        DebAPI.registerEntity(this, "NO-EGG-MRWalkingZombie", EntityMRWalkingZombie.class);
        DebAPI.registerEntity(this, "NO-EGG-MRWarningBlock", EntityWarningBlock.class);
        DebAPI.registerEntity(this, "NO-EGG-MRTNT", EntityMRTNT.class);
        DebAPI.registerEntity(this, "NO-EGG-MRFallingBlockCreator", EntityMRFallingBlockCreator.class);
        DebAPI.registerEntity(this, "NO-EGG-MRFallingBlock", EntityMRFallingBlock.class);

        //엘리트라 슈팅 게임용
        DebAPI.registerEntity(this, "NO-EGG-ElytraBossWeen", EntityFlyingWeen.class);
        DebAPI.registerEntity(this, "NO-EGG-ElytraBullet", EntityElytraBullet.class);
        DebAPI.registerEntity(this, "NO-EGG-ElytraPumpkin", EntityElytraPumpkin.class);
        DebAPI.registerEntity(this, "NO-EGG-ElytraWeenAttack", EntityElytraPumpkinAttack.class);
        DebAPI.registerEntity(this, "NO-EGG-ElytraWeenFire", EntityElytraPumpkinFire.class);
        DebAPI.registerEntity(this, "NO-EGG-ElytraItem", EntityElytraChest.class);
        DebAPI.registerEntity(this, "NO-EGG-ElytraScore", EntityElytraScore.class);
        DebAPI.registerEntity(this, "NO-EGG-ElytraTNTArrow", EntityTNTArrow.class);
        DebAPI.registerEntity(this, "NO-EGG-ElytraHoamingTNT", EntityHomingTNT.class);
        DebAPI.registerEntity(this, "NO-EGG-ElytraCreeper", EntityElytraCreeper.class);
        DebAPI.registerEntity(this, "NO-EGG-ElytraBoss", EntityElytraBossWeen.class);
        DebAPI.registerEntity(this, "NO-EGG-ElytraBossWeen", EntityElytraBossMini.class);

        //폭탄게임용
        DebAPI.registerEntity(this, "NO-EGG-bomb", EntityBomb.class);
        GameRegistry.register(Bomber.bombItem.setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setRegistryName("tntmini").setUnlocalizedName("tntmini"));
        DebAPI.registerEntity(this, "NO-EGG-FakePlayer", EntityFakePlayer.class);
        DebAPI.registerEntity(this, "VELOCITY-DefaultNPC", EntityDefaultNPC.class);
        DebAPI.registerEntity(this, "NO-EGG-DefaultBlock", EntityDefaultBlock.class);
        proxy.init(e);

        MinecraftForge.EVENT_BUS.register(new MiniGameEvent());
        if (FMLCommonHandler.instance().getSide() == CLIENT) {
            MinecraftForge.EVENT_BUS.register(mineRunEvent = new MineRunEvent());
            MinecraftForge.EVENT_BUS.register(scrollEvent = new ScrollEvent());
            MinecraftForge.EVENT_BUS.register(bomberEvent = new BomberEvent());
            MinecraftForge.EVENT_BUS.register(elytraEvent = new ElytraEvent());
            MinecraftForge.EVENT_BUS.register(new ElytraRenderEvent());
            MinecraftForge.EVENT_BUS.register(starMineEvent = new StarMineEvent());
            ClientCommandHandler.instance.registerCommand(new CommandScroll());
        }
    }

    @EventHandler
    public void init(FMLServerStartingEvent e) {
        e.registerServerCommand(new CommandMonologue());
        e.registerServerCommand(new CommandClassLoader());
        e.registerServerCommand(new CommandMge());
        e.registerServerCommand(new CommandJumpThree());
        //e.registerServerCommand(new CommandScroll());
        e.registerServerCommand(new CommandBomber());
        e.registerServerCommand(new CommandElytra());
        e.registerServerCommand(new CommandStarMine());
        //e.registerCommand(new CommandMonologue());
        e.registerServerCommand(new CommandMineRun());
        e.registerServerCommand(new CommandNotification());
        e.registerServerCommand(new CommandCall());

    }

    @EventHandler
    public void init(FMLServerStoppedEvent e) {
        ActionEffect.save();
    }

    public void network() {
        network = NetworkRegistry.INSTANCE.newSimpleChannel("MiniGame");
    }

}
