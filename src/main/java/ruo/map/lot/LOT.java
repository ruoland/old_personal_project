package ruo.map.lot;

import api.player.model.ModelPlayerAPI;
import api.player.render.RenderPlayerAPI;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.deb.DebAPI;
import ruo.map.lot.block.BlockClock;
import ruo.map.lot.block.TileClock;
import ruo.map.lot.block.TileClockRenderer;
import ruo.map.lot.dungeon.EntityWind;
import ruo.map.lot.dungeon.area.CommandArea;
import ruo.map.lot.dungeon.arrow.*;
import ruo.map.lot.switch2.BlockSwitch;
import ruo.map.lot.switch2.TileSwitch;
import ruo.map.lot.switch2.TileSwitchRenderer;
import ruo.map.lot.tool.BlockEnderShot;
import ruo.map.lot.tool.EntityEnderShot;
import ruo.map.lot.tool.ItemEnderShot;

//@Mod(modid = "LOT")
public class LOT {

    public static final Block switch2 = new BlockSwitch().setRegistryName("switch").setUnlocalizedName("lotswitch").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

    //enderShotBlock은 엔더샷으로 텔레포트 가능한 블럭임
    public static final Block enderShotBlock = new BlockEnderShot().setRegistryName("endershotblock").setUnlocalizedName("endershotblock").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final Item enderShotIem = new ItemEnderShot().setRegistryName("endershotitem").setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setUnlocalizedName("endershotitem");

    public static final Block time = new BlockClock().setRegistryName("time").setUnlocalizedName("lottime").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final Block arrowspawn = new BlockArrowSpawn(Material.ANVIL).setRegistryName("arrowspawn").setUnlocalizedName("arrowspawn").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final Item lotbow = new ItemLOTBow().setRegistryName("bow").setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setUnlocalizedName("lotbow");

    @SidedProxy(serverSide = "ruo.map.lot.CommonProxy", clientSide = "ruo.map.lot.ClientProxy")
    public static CommonProxy proxy;
    public LOT() {
        try {
            Class.forName("api.player.server.ServerPlayerAPI");
            RenderPlayerAPI.register("LOT", LOTRenderPlayer.class);
            ModelPlayerAPI.register("LOT", LOTModelPlayer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @EventHandler
    public void init(FMLServerStartingEvent e) {
        e.registerServerCommand(new CommandDoor());
        e.registerServerCommand(new CommandWind());
        e.registerServerCommand(new CommandLot());
        e.registerServerCommand(new CommandArea());

    }

    @EventHandler
    public void init(FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new LOTEvent());
    }

    @EventHandler
    public void init(FMLPostInitializationEvent e) {
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {

        DebAPI.registerEntity(this, "WINDFALLBLOCK2", EntityFallBlock.class);
        DebAPI.registerEntity(this, "WIND", EntityWind.class);
        DebAPI.registerEntity(this, "WALLBLOCK", EntityWallBlock.class);
        DebAPI.registerEntity(this, "DOORBLOCK", EntityDoorBlock.class);
        DebAPI.registerEntity(this, "DUNARROW-NO-EGG-", EntityDunArrow.class);
        DebAPI.registerEntity(this, "lotender", EntityEnderShot.class);
        proxy.init();;

        DebAPI.registerBlock(enderShotBlock);
        GameRegistry.register(enderShotIem);
        GameRegistry.register(lotbow);
        DebAPI.createJson(lotbow, Items.BOW);
        DebAPI.createJson(enderShotIem, Items.ENDER_PEARL);
        DebAPI.createJson(enderShotBlock, Blocks.STONE);
        DebAPI.createJson(switch2, Blocks.STONE);
        DebAPI.createJson(time, Blocks.STONE);
        DebAPI.createJson(arrowspawn, Blocks.STONE);


    }
}
