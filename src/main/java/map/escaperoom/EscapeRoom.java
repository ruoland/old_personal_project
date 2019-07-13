package map.escaperoom;

import cmplus.deb.DebAPI;
import map.escaperoom.block.BlockBarrier;
import map.escaperoom.nouse.EntityRoomDoor;
import map.escaperoom.nouse.EntityRoomEnderman;
import map.escaperoom.nouse.EntityRoomPathCreeper;
import map.escaperoom.nouse.EntityRoomWindEntity;
import map.escaperoom.nouse.dungeon.EntityRespawnZombie;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid ="PuzzleMap", name = "Puzzle Map")
public class EscapeRoom {
    public static boolean isRedMode;
    @SidedProxy(clientSide =  "map.escaperoom.EscapeClientProxy", serverSide = "map.escaperoom.EscapeServerProxy")
    public static EscapeServerProxy proxy;
    public static Block blockBarrier = new BlockBarrier(Material.ANVIL).setRegistryName("PuzzleMap", "puzzlebarrier").setUnlocalizedName("barrier").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static Item itemRB = new ItemRB().setUnlocalizedName("itemrb").setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setRegistryName("PuzzleMap", "itemrb");
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
        DebAPI.registerEntity(this, "PuzzleEnderman", EntityRoomEnderman.class);
        DebAPI.registerEntity(this, "PuzzleBlockShooter", EntityRoomBlockShooter.class);

        DebAPI.registerEntity(this, "PuzzleMonsterTower", EntityRoomMonsterTower.class);
        DebAPI.registerEntity(this, "PuzzleMovingBlock", EntityRoomMovingBlock.class);
        DebAPI.registerEntity(this, "PuzzleRedBlue", EntityRoomRedBlue.class);
        DebAPI.registerEntity(this, "PuzzleJumper", EntityRoomJumper.class);

        proxy.init();

    }

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent e){
        DebAPI.registerBlock(blockBarrier);
        GameRegistry.register(itemRB);
        MinecraftForge.EVENT_BUS.register(new PuEvent());
        proxy.preInit();;
    }

    @Mod.EventHandler
    public void server(FMLServerStartingEvent e){
        e.registerServerCommand(new CommandRoom());
        e.registerServerCommand(new CommandRoomm());
    }
}
