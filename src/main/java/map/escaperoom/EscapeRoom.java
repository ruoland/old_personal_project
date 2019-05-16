package map.escaperoom;

import cmplus.deb.DebAPI;
import map.escaperoom.dungeon.EntityRespawnZombie;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid ="PuzzleMap", name = "Puzzle Map")
public class EscapeRoom {

    @SidedProxy(clientSide =  "map.escaperoom.EscapeClientProxy", serverSide = "map.escaperoom.EscapeServerProxy")
    public static EscapeServerProxy proxy;
    public static Block blockJumper = new BlockJumper(Material.ANVIL).setRegistryName("PuzzleMap", "jumper").setUnlocalizedName("jumper").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static Block blockForward = new BlockForward(Material.ANVIL).setRegistryName("PuzzleMap", "forward").setUnlocalizedName("forward").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static Block blockWhatBlock = new BlockForward(Material.ANVIL).setRegistryName("PuzzleMap", "whatblock").setUnlocalizedName("whatBlock").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static Block blockBarrier = new BlockBarrier(Material.ANVIL).setRegistryName("PuzzleMap", "barrier").setUnlocalizedName("barrier").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

    @Mod.EventHandler
    public void init(FMLInitializationEvent e){
        DebAPI.registerEntity(this, "PuzzleBlock", EntityRoomBlock.class);
        DebAPI.registerEntity(this, "PuzzleBlockButton", EntityRoomBlockButton.class);
        DebAPI.registerEntity(this, "PuzzleBlockArrow", EntityRoomBlockArrow.class);
        DebAPI.registerEntity(this, "PuzzleWindBlock", EntityRoomWindEntity.EntityPuzzleWindBlock.class);
        DebAPI.registerEntity(this, "PuzzleWindPlayer", EntityRoomWindEntity.class);
        DebAPI.registerEntity(this, "PuzzleDoor", EntityRoomDoor.class);
        DebAPI.registerEntity(this, "PuzzleMonster", EntityRoomMonster.class);
        DebAPI.registerEntity(this, "PuzzlePathFinder", EntityRoomPathCreeper.class);
        DebAPI.registerEntity(this, "PuzzleMoveZombie", EntityRoomMoveZombie.class);
        DebAPI.registerEntity(this, "RespawnZombie", EntityRespawnZombie.class);
        DebAPI.registerEntity(this, "PuzzleFallingBlock", EntityRoomFallingBlock.class);
        DebAPI.registerEntity(this, "PuzzleJumpMap", EntityRoomBlockJumpMap.class);

        proxy.init();
    }

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent e){
        DebAPI.registerBlock(blockJumper);
        DebAPI.registerBlock(blockForward);
        DebAPI.registerBlock(blockWhatBlock);
        DebAPI.registerBlock(blockBarrier);
        MinecraftForge.EVENT_BUS.register(new PuEvent());
        proxy.preInit();;
    }

    @Mod.EventHandler
    public void server(FMLServerStartingEvent e){
        e.registerServerCommand(new CommandRoom());
        e.registerServerCommand(new CommandRoomm());
    }
}
