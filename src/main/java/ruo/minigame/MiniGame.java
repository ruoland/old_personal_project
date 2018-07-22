package ruo.minigame;

import api.player.client.ClientPlayerAPI;
import api.player.render.RenderPlayerAPI;
import api.player.server.ServerPlayerAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
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
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.deb.CommandClassLoader;
import ruo.cmplus.deb.DebAPI;
import ruo.minigame.action.ActionEffect;
import ruo.minigame.android.CommandCall;
import ruo.minigame.android.CommandNotification;
import ruo.minigame.command.*;
import ruo.minigame.effect.TickRegister;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.minigame.bomber.BomberEvent;
import ruo.minigame.minigame.elytra.ElytraEvent;
import ruo.minigame.minigame.minerun.EntityMineRunCreeper;
import ruo.minigame.minigame.minerun.MineRun;
import ruo.minigame.action.ActionData;
import ruo.minigame.action.ActionEvent;
import ruo.minigame.minigame.bomber.Bomber;
import ruo.minigame.minigame.bomber.EntityBomb;
import ruo.minigame.minigame.elytra.Elytra;
import ruo.minigame.minigame.elytra.EntityElytraItem;
import ruo.minigame.minigame.elytra.EntityFlyingWeen;
import ruo.minigame.minigame.elytra.miniween.*;
import ruo.minigame.minigame.minerun.MineRunEvent;
import ruo.minigame.minigame.scroll.Scroll;
import ruo.minigame.minigame.scroll.ScrollEvent;

import java.awt.event.PaintEvent;


@Mod(modid = "MiniGame", name = "MiniGame")
public class MiniGame {
    public static SimpleNetworkWrapper network;
    public static KeyBinding grab = new KeyBinding("액션-", Keyboard.KEY_R, "카카카테고리");
    public static final StringCooldownTracker cooldownTracker = new StringCooldownTracker();

    @SidedProxy(clientSide = "ruo.minigame.ClientProxy", serverSide = "ruo.minigame.CommonProxy")
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
    public Configuration minigameConfig;
    public MiniGame() {
        try {
            Class.forName("api.player.server.ServerPlayerAPI");
            ServerPlayerAPI.register("MiniGame", MiniGameServerPlayer.class);
            ClientPlayerAPI.register("MiniGame", MiniGameClientPlayer.class);
            RenderPlayerAPI.register("MiniGame", MiniGameRenderPlayer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @EventHandler
    public void init(FMLPreInitializationEvent e) {
        minigameConfig = new Configuration(e.getSuggestedConfigurationFile());
        minigameConfig.load();
        minigameConfig.save();

        proxy.pre(e);
        network();
        ActionEffect.load();
        MiniGame.minerun = new MineRun();
        MiniGame.scroll = new Scroll();
        MiniGame.bomber = new Bomber();
        MiniGame.elytra = new Elytra();
        //ClientPlayerAPI.register("MiniGame", LoopPlayer.class);
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        //스크롤 메이커용
        //DebAPI.registerEntity(this, "ScrollMouse", EntityScrollMouse.class);
        //마인런 게임용
        DebAPI.registerEntity(this, "MRCreeper", EntityMineRunCreeper.class);
        DebAPI.registerEntity(this, "Test", EntityElytraTest.class);

        //엘리트라 슈팅 게임용
        DebAPI.registerEntity(this, "NO-EGG-ElytraWeen", EntityElytraWeenCore.class);
        DebAPI.registerEntity(this, "NO-EGG-ElytraBossWeen", EntityFlyingWeen.class);
        DebAPI.registerEntity(this, "NO-EGG-ElytraItem", EntityElytraItem.class);
        DebAPI.registerEntity(this, "NO-EGG-ElytraTNT", EntityElytraWeenTNT.class);
        DebAPI.registerEntity(this, "NO-EGG-ElytraFallingWeen", EntityElytraWeenUP.class);
        DebAPI.registerEntity(this, "NO-EGG-ElytraPumpkin", EntityElytraPumpkin.class);
        DebAPI.registerEntity(this, "NO-EGG-ElytraBullet", EntityElytraBullet.class);
        DebAPI.registerEntity(this, "NO-EGG-ElytraWeenFire", EntityElytraPumpkinFire.class);

        //폭탄게임용
        EntityRegistry.registerModEntity(EntityBomb.class, "bomb", 174, this, 80, 3, false);
        GameRegistry.register(bomber.bombItem.setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setRegistryName("tntmini").setUnlocalizedName("tntmini"));
        reg(bomber.bombItem);

        DebAPI.registerEntity(this, "NO-EGG-FakePlayer", EntityFakePlayer.class);
        DebAPI.registerEntity(this, "NO-EGG-DefaultNPC", EntityDefaultNPC.class);
        DebAPI.registerEvent(new ActionEvent());
        DebAPI.registerEvent(new TickRegister());
        DebAPI.registerEvent(new MiniGameEvent());
        DebAPI.registerEvent(mineRunEvent = new MineRunEvent());
        DebAPI.registerEvent(scrollEvent = new ScrollEvent());
        DebAPI.registerEvent(bomberEvent = new BomberEvent());
        DebAPI.registerEvent(elytraEvent = new ElytraEvent());
        ClientRegistry.registerKeyBinding(grab);

        ClientCommandHandler.instance.registerCommand(new CommandMg());
    }

    @EventHandler
    public void init(FMLServerStartingEvent e) {
        e.registerServerCommand(new CommandMonologue());
        e.registerServerCommand(new CommandClassLoader());
        e.registerServerCommand(new CommandMgs());

        e.registerServerCommand(new CommandScroll());
        e.registerServerCommand(new CommandBomber());
        e.registerServerCommand(new CommandElytra());
        //e.registerCommand(new CommandMonologue());
        e.registerServerCommand(new CommandMineRun());
        e.registerServerCommand(new CommandNotification());
        e.registerServerCommand(new CommandCall());

    }

    @EventHandler
    public void init(FMLServerStoppedEvent e) {
        ActionEffect.save();
        minigameConfig.save();
    }


    public static void reg(Item block) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
                .register(block, 0, new ModelResourceLocation("minigame" + ":" + block.getUnlocalizedName().substring(5), "inventory"));
    }

    public void network() {
        network = NetworkRegistry.INSTANCE.newSimpleChannel("MiniGame");
    }

}
