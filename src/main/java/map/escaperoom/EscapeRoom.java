package map.escaperoom;

import cmplus.deb.DebAPI;
import map.escaperoom.dungeon.EntityRespawnZombie;
import oneline.api.RenderAPI;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid ="PuzzleMap", name = "Puzzle Map")
public class EscapeRoom {

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
        RenderAPI.registerRender(EntityRoomBlock.class);
        RenderAPI.registerRender(EntityRoomBlockButton.class);
        RenderAPI.registerRender(EntityRoomBlockArrow.class);
        RenderAPI.registerRender(EntityRoomWindEntity.EntityPuzzleWindBlock.class);
        RenderAPI.registerRender(EntityRoomWindEntity.class);
        RenderAPI.registerRender(EntityRoomDoor.class);
        RenderAPI.registerRender(EntityRespawnZombie.class);
        RenderAPI.registerRender(EntityRoomMonster.class);
        RenderAPI.registerRender(EntityRoomPathCreeper.class);
    }

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent e){
        MinecraftForge.EVENT_BUS.register(new PuEvent());
    }

    @Mod.EventHandler
    public void server(FMLServerStartingEvent e){
        e.registerServerCommand(new CommandRoom());
        e.registerServerCommand(new CommandRoomm());
    }
}
