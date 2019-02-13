package map.puzzle;

import cmplus.deb.DebAPI;
import map.puzzle.dungeon.EntityRespawnZombie;
import minigameLib.api.RenderAPI;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid ="PuzzleMap", name = "Puzzle Map")
public class Puzzle {

    @Mod.EventHandler
    public void init(FMLInitializationEvent e){
        DebAPI.registerEntity(this, "PuzzleBlock", EntityPuzzleBlock.class);

        DebAPI.registerEntity(this, "PuzzleBlockButton", EntityPuzzleBlockButton.class);
        DebAPI.registerEntity(this, "PuzzleBlockArrow", EntityPuzzleBlockArrow.class);
        DebAPI.registerEntity(this, "PuzzleWindBlock", EntityPuzzleWindEntity.EntityPuzzleWindBlock.class);
        DebAPI.registerEntity(this, "PuzzleWindPlayer", EntityPuzzleWindEntity.class);
        DebAPI.registerEntity(this, "PuzzleDoor", EntityPuzzleDoor.class);
        DebAPI.registerEntity(this, "PuzzleMonster", EntityPuzzleMonster.class);
        DebAPI.registerEntity(this, "PuzzlePathFinder", EntityPuzzlePathCreeper.class);
        DebAPI.registerEntity(this, "RespawnZombie", EntityRespawnZombie.class);
        RenderAPI.registerRender(EntityPuzzleBlock.class);
        RenderAPI.registerRender(EntityPuzzleBlockButton.class);
        RenderAPI.registerRender(EntityPuzzleBlockArrow.class);
        RenderAPI.registerRender(EntityPuzzleWindEntity.EntityPuzzleWindBlock.class);
        RenderAPI.registerRender(EntityPuzzleWindEntity.class);
        RenderAPI.registerRender(EntityPuzzleDoor.class);
        RenderAPI.registerRender(EntityRespawnZombie.class);
        RenderAPI.registerRender(EntityPuzzleMonster.class);
        RenderAPI.registerRender(EntityPuzzlePathCreeper.class);
    }

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent e){
        MinecraftForge.EVENT_BUS.register(new PuEvent());
    }

    @Mod.EventHandler
    public void server(FMLServerStartingEvent e){
        e.registerServerCommand(new CommandPuzzle());
        e.registerServerCommand(new CommandPuzzlem());
    }
}
